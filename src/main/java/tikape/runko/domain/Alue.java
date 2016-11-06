/* TIKAPE keskusutelufoorumi
 * Alue -luokka: konstruktorit, setterit ja getterit
 * @author Anette
 */
package tikape.runko.domain;

public class Alue {
    private Integer alue_id;
    private String nimi;
    private int v_yhteensa;
    private String v_uusin;   

    public Alue(Integer id, String nimi) {
        this.alue_id = id;
        this.nimi = nimi;         
    }
    
    public Alue(int id, String nimi, int v_yhteensa, String v_uusin) {
        this.alue_id = id;
        this.nimi = nimi; 
        this.v_yhteensa = v_yhteensa;
        this.v_uusin = v_uusin;
    }
    
     public Alue(String nimi) {        
        this.nimi = nimi;         
    }
     
    public Integer getId() {
        return alue_id;
    }
    
    public Integer getId(int nimi) {
        this.alue_id = nimi;
        return alue_id;
    }
    
    public void setId(Integer id) {
        this.alue_id = id;
    }
    
    public String getNimi() {
        return nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public void setMaara(Integer v_yhteensa){
        this.v_yhteensa = v_yhteensa;
    }
    
    public Integer getMaara(){
        return v_yhteensa;
    }
    
    public String getUusinAika() {
        return v_uusin;
    }   
}