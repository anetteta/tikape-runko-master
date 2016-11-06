package tikape.runko;

import java.util.*;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.TemplateViewRoute;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;


public class Main {

    public static void main(String[] args) throws Exception {
         if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
       
        String jdbcOsoite = System.getenv("DATABASE_URL");  //Herokun tarjoama tietokanta osoite
        
        
         
        
        Database database = new Database(jdbcOsoite);
        AlueDao alueDao = new AlueDao(database); 
        KeskusteluDao keskusteluDao = new KeskusteluDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        nykyinenSijainti alue = new nykyinenSijainti(0);
        nykyinenSijainti keskustelu = new nykyinenSijainti(0);
        nykyinenSijainti viestiSivu = new nykyinenSijainti(0);
        Alue alue2 = new Alue(0, "null");
        
        
        get("/", (req, res) -> {    // Etusivu (1. taso)         
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

       
        post("/lisaaAlue", (Request req, Response res) -> { // Alueen lisäys
            String nimi = req.queryParams("nimi");            
            if (nimi.isEmpty()){
                return "Virhe! Alueella taytyy olla nimi";                
            }else{
                alueDao.lisaaAlue(nimi);
                res.redirect("/");
                return"";  
            }            
        }); 

        get("/alue/:id", (Request req, Response res) -> {
            // Keskustelut alueella (2. taso)
            int numero = (Integer.parseInt(req.params(":id")));
            String nimi = keskusteluDao.getAlueNimi(numero);
            alue2.setNimi(nimi);
            alue.setSijainti(numero);
            
            HashMap map = new HashMap<>();
            List<Keskustelu> keskustelut2 = new ArrayList<>();
            keskustelut2 = keskusteluDao.findAllIn(numero);
            if(!keskustelut2.isEmpty()){
                map.put("keskustelut", keskustelut2);
                return new ModelAndView(map, "keskustelut");
            }else{
                keskustelut2 = keskusteluDao.getAlueNimiListana(numero);
                map.put("keskustelut", keskustelut2);
            }return new ModelAndView(map, "keskustelu2");
         }, new ThymeleafTemplateEngine());       
      
        
        get("/alue/:aid/keskustelu/:id/sivu/:vid", (req, res) -> { // Viestit keskustelussa (3. taso)
            int numero = (Integer.parseInt(req.params(":id")));
            int vid = (Integer.parseInt(req.params(":vid")));
            int aid = (Integer.parseInt(req.params(":aid")));  
            List<Viesti> viestit = new ArrayList<>();
            HashMap map = new HashMap<>();
            alue.setSijainti(aid);
            viestiSivu.setSijainti(vid);
            keskustelu.setSijainti(numero);
            
            boolean totta = false;
            if (vid >= 1)
                totta = true;          
            viestit = viestiDao.findAllIn(numero, vid);
            if (vid == 0){
                vid++;
                viestiSivu.setSijainti(vid);
            }
            if(!viestit.isEmpty() && totta){
                map.put("viestit", viestit);
                return new ModelAndView(map, "viestit");
            }else{
                if (vid != 1){ vid--;}
                viestiSivu.setSijainti(vid);
                List<Keskustelu> viestit2 = keskusteluDao.getAlueNimiListana(1);
                map.put("keskustelut", viestit2);
                return new ModelAndView(map, "viestit2");
            }
        }, new ThymeleafTemplateEngine());
        
        
        post("/lisaaKeskustelu" , (Request req, Response res) -> {  // Keskustelun lisäys 
            String otsikko = req.queryParams("keskustelu_otsikko");
            String nimimerkki = req.queryParams("nimimerkki"); 
            String viesti = req.queryParams("viesti");
            int alueId = alue.getSijainti();
            if (!viesti.isEmpty() && !otsikko.isEmpty() && !nimimerkki.isEmpty()){                          
            int keskusteluId = keskusteluDao.getKeskusteluId();
            keskusteluDao.lisaaKeskustelu(alueId, otsikko);            
            viestiDao.lisaaViesti(alueId, keskusteluId, nimimerkki, viesti);
            res.redirect("/alue/"+alueId);
            return "";           
            }else{
                return "Virhe! Muistithan täyttää kaikki kentät?";
            }
        });
        
        post("/lisaaViesti", (Request req, Response res) -> {  // Viestin lisäys
            String nimimerkki = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");
            if (!viesti.isEmpty() && !nimimerkki.isEmpty()){
            int alueId = alue.getSijainti();
            int keskusteluId = keskustelu.getSijainti();
            viestiDao.lisaaViesti(alueId, keskusteluId, nimimerkki, viesti);
            res.redirect("/alue/"+alueId +"/keskustelu/" + keskusteluId + "/sivu/"+viestiSivu.getSijainti());
            return "";
            }else{
                return "Virhe! Viesti ja/tai nimimerkki puuttuivat.";
            }
        });
        
        post("/seuraavaAlue", (Request req, Response res) -> {  // Viestien selaus, seuraava      
            int alueId = alue.getSijainti();
            int keskusteluId = keskustelu.getSijainti();
            int missa = viestiSivu.getSijainti();
            int missa2 = missa + 1;
            viestiSivu.setSijainti(missa2);                 
            res.redirect("/alue/"+alueId+"/keskustelu/"+keskusteluId+"/sivu/"+viestiSivu.getSijainti());
            return "";
        });
        
        post("/edellinenAlue", (Request req, Response res) -> { // Viestien selaus, edellinen                      
            int alueId = alue.getSijainti();
            int keskusteluId = keskustelu.getSijainti();
            int missa = viestiSivu.getSijainti();
            int missa2 = missa;
            if (missa < 1) {
                missa2 = 1;
            }
            viestiSivu.setSijainti(missa2);                   
            res.redirect("/alue/"+alueId+"/keskustelu/"+keskusteluId+"/sivu/"+viestiSivu.getSijainti());
            return "";
        }); 
    }
}
