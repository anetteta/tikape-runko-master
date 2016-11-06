/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

/**
 *
 * @author Anette
 */
public class nykyinenSijainti {
    private int id;
    
    public nykyinenSijainti(int id) {
        this.id = id;       
    }
    
    public void setSijainti(int id) {
        this.id = id;
    }
   
    public int getSijainti() {
        return this.id;
    }
}
