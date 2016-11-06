/* TIKAPE keskusutelufoorumi
 * Viesti -luokka: konstruktorit, setterit ja getterit
 * @author Anette
 */

package tikape.runko.domain;

public class Viesti {
    private Integer viesti_id;
    private Integer alue_id;
    private Integer keskustelu_id;
    private String nimimerkki;
    private String p_a;
    private String viesti;    
    private String alueNimi;
    private String otsikko;    

    public Viesti(Integer id, Integer alue, Integer keskustelu, String nimimerkki, String p_a, String viesti, String otsikko, String alueNimi) {
        this.viesti_id = id;
        this.alue_id = alue;
        this.keskustelu_id = keskustelu;
        this.nimimerkki = nimimerkki;
        this.viesti = viesti;
        this.p_a = p_a;
        this.alueNimi = alueNimi;
        this.otsikko = otsikko;
    }
    
    public Viesti(Integer id, Integer alue, Integer keskustelu, String nimimerkki, String viesti) {
        this.viesti_id = id;
        this.alue_id = alue;
        this.keskustelu_id = keskustelu;
        this.nimimerkki = nimimerkki;
        this.viesti = viesti;
        this.p_a = null;
    }
    
    public Integer getId() {
        return viesti_id;
    }
    
    public void setId(Integer id) {
        this.viesti_id = id;
    }
    
    public String getNimimerkki() {
        return nimimerkki;
    }
    
    public void setNimimerkki(String nimimerkki) {
        this.nimimerkki = nimimerkki;
    } 
    
    public String getViesti() {
        return viesti;
    }
    
    public void setViesti(String viesti) {
        this.viesti = viesti;
    }
    
    public String getAika() {
        return p_a;
    }
    
    public void setAika(String p_a) {
        this.p_a = p_a;
    }
    
    public Integer getAlueId() {
        return alue_id;
    }
    
    public void setAlueId(Integer id) {
        this.alue_id = id;
    } 
    
    public Integer getKeskusteluId() {
        return keskustelu_id;
    }
    
    public void setKeskusteluId(Integer id) {
        this.keskustelu_id = id;
    }
    
    public String getAlueNimi(){
        return alueNimi;
    }
    
    public String getOtsikko(){
        return otsikko;
    }     
}
