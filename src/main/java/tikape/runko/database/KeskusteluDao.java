/* 
 * TIKAPE keskusutelufoorumi
 * Kaikkien keskustelujen haku alueella ja keskusteluiden lisäys
 *
 */
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.*;


public class KeskusteluDao {
    private Database database;

    public KeskusteluDao(Database database) {
        this.database = database;
    }
    
    public List<Keskustelu> findAllIn(Integer key) throws SQLException { // (toinen taso:) haetaan keskustelut alueessa     
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT keskustelu_id, otsikko, keskustelu.alue, COUNT(viesti) AS v_yht, MAX(pvm_kellonaika) as v_uusin FROM Keskustelu LEFT JOIN Viesti ON Viesti.keskustelu = Keskustelu.keskustelu_id WHERE keskustelu.alue = ? GROUP BY keskustelu_id ORDER BY keskustelu_id DESC LIMIT 10");
        stmt.setObject(1, key);       
        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        String alueNimi = getAlueNimi(key);
        
        while (rs.next()) {         
            Integer id = rs.getInt("keskustelu_id");
            String otsikko = rs.getString("otsikko");
            int alue = rs.getInt("alue");
            int v_yht = rs.getInt("v_yht");
            String v_uusin = rs.getString("v_uusin");            
            keskustelut.add(new Keskustelu(id, otsikko, alue, v_yht, v_uusin, alueNimi));
        }        
        rs.close();
        stmt.close();
        connection.close();
        return keskustelut;
    }     
    
    public List<Keskustelu> getAlueNimiListana(Integer key) throws SQLException { // Keskustelu2.html varten tehty metodi, 
        List<Keskustelu> keskustelut = new ArrayList<>();                           // jolla saadaan alueen nimi, vaikka
        String alueNimi = getAlueNimi(key);                                         // alueella ei olisi keskusteluja.
        keskustelut.add(new Keskustelu(alueNimi));
        return keskustelut;
    }
    
    public String getAlueNimi(int key)throws SQLException{ // edellinen metodi hakee alueen nimen tällä metodilla eli kts. getKeskustelulistana
        Connection connection2 = database.getConnection();
        PreparedStatement stmt2 = connection2.prepareStatement("Select nimi from alue where alue_id = ?");        
        stmt2.setInt(1, key);
        ResultSet rs2 = stmt2.executeQuery();
        String alueenNimi = "";
        if (rs2.next()) {
            alueenNimi = rs2.getString("nimi");
        }        
        rs2.close();
        stmt2.close();
        connection2.close();
        return alueenNimi;
    }
    
    public void lisaaKeskustelu(int id, String otsikko) throws SQLException { // lisää keskustelu
        String sql ="INSERT INTO Keskustelu(alue, otsikko) VALUES (?, ?)";
        Connection connection = database.getConnection(); 
        PreparedStatement stmt = connection.prepareStatement(sql);        
        stmt.setInt(1, id);  
        stmt.setString(2, otsikko);
        stmt.executeUpdate();      
        stmt.close();       
        connection.close();
    }
  
    public int getKeskusteluId()throws SQLException {                   // määrittelee keskustelun (id:n) viestiin, kun alueelle lisätään 
       String sql ="SELECT MAX(keskustelu_id) as keskusteluid from keskustelu";         // sekä keskustelu että viesti samanaikaisesti
       Connection connection = database.getConnection();        
       PreparedStatement stmt = connection.prepareStatement(sql);       
       ResultSet rs = stmt.executeQuery();
       int keskustelu_id = 0;
        if (rs.next()) {
            keskustelu_id = rs.getInt("keskusteluid");
        }       
       keskustelu_id++;
       rs.close();
       stmt.close();
       connection.close();
       return keskustelu_id;
    }
}
