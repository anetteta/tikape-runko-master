/* TIKAPE keskusutelufoorumi
 * Kaikkien alueiden haku ja lisäys
 *
 */
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.*;

public class AlueDao {
    private Database database;
   
    public AlueDao(Database database) {
        this.database = database;
    }
    
    public List<Alue> findAll() throws SQLException { //etusivu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT alue_id, nimi, COUNT(viesti) AS v_yht, MAX(pvm_kellonaika) as v_uusin "
                    + "FROM Alue LEFT JOIN Viesti ON Viesti.alue = alue.alue_id GROUP BY alue_id ORDER BY nimi");
        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("alue_id");
            String nimi = rs.getString("nimi"); 
            Integer v_yht = rs.getInt("v_yht");
            String v_uusin = rs.getString("v_uusin");
            alueet.add(new Alue(id, nimi, v_yht, v_uusin));
        }
        rs.close();
        stmt.close();
        connection.close();
        return alueet;
    }
    
    public void lisaaAlue(String asetus) throws SQLException { // alueen lisäys
        String sql ="INSERT INTO Alue(nimi) VALUES (?)";
        Connection connection = database.getConnection();               
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, asetus);        
        stmt.executeUpdate();
        stmt.close();  
        connection.close();
    }
} 
