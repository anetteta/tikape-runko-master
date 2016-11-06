/* TIKAPE keskusutelufoorumi
 * Keskustelu -luokka: konstruktorit, setterit ja getterit
 * @author Anette
 */
package tikape.runko.domain;

public class Keskustelu {

    private Integer keskustelu_id;
    private String otsikko;
    private Integer alue_id;
    private int v_yhteensa;
    private String v_uusin;
    private String alueenNimi;    

    public Keskustelu(Integer id, String otsikko, Integer alue) {
        this.keskustelu_id = id;
        this.otsikko = otsikko;
        this.alue_id = alue;
    }
    
    public Keskustelu(String alueenNimi){
        this.alueenNimi = alueenNimi;
    }
    
    public Keskustelu(Integer id, String otsikko, Integer alue, int v_yhteensa, String v_uusin) {
        this.keskustelu_id = id;
        this.otsikko = otsikko;
        this.alue_id = alue;
        this.v_yhteensa = v_yhteensa;
        this.v_uusin = v_uusin;
    }    
    
    public Keskustelu(Integer id, String otsikko, Integer alue, int v_yhteensa, String v_uusin, String alueenNimi) {
        this.alueenNimi = alueenNimi;
        this.keskustelu_id = id;
        this.otsikko = otsikko;
        this.alue_id = alue;
        this.v_yhteensa = v_yhteensa;
        this.v_uusin = v_uusin;
    }    
   
    public Integer getId() {
        return keskustelu_id;
    }

    public void setId(Integer id) {
        this.keskustelu_id = id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }
    
    public Integer getAlueId() {
        return alue_id;
    }

    public void setAlueId(Integer alue) {
        this.alue_id = alue;
    } 
    
    public Integer getMaara(){
        return v_yhteensa;
    }
    
    public String getUusinAika() {
        return v_uusin;
    } 
    
    public String getNimi(){
        return alueenNimi;
    } 
}

