/* TIKAPE keskusutelufoorumi
 * Kaikkien viestien haku keskustelussa ja viestien lisäys sekä viestien lisäyksessä käytettävä metodi, joka muodostaa ajan ja pvm viestille.
 *
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tikape.runko.domain.Viesti;
import java.util.*;
import java.text.SimpleDateFormat;


public class ViestiDao {
    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }
    
    public List<Viesti> findAllIn(Integer key, int sivu) throws SQLException { // kolmas taso : viestit keskustelussa
        int offset = (sivu - 1) * 10;
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT viesti_id, viesti.alue, keskustelu, pvm_kellonaika, nimimerkki, viesti, nimi, otsikko FROM Viesti, Alue, Keskustelu WHERE alue.alue_id = viesti.alue AND viesti.keskustelu = keskustelu.keskustelu_id AND keskustelu = ? LIMIT 10 OFFSET ?");
        stmt.setObject(1, key);
        stmt.setObject(2, offset);        

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            String alueNimi=rs.getString("nimi");
            String otsikko = rs.getString("otsikko");
            Integer id = rs.getInt("viesti_id");
            Integer alue_id = rs.getInt("alue");
            Integer keskustelu_id = rs.getInt("keskustelu");
            String nimimerkki = rs.getString("nimimerkki");
            String p_a = rs.getString("pvm_kellonaika");
            String viesti = rs.getString("viesti");

        Viesti v = new Viesti(id, alue_id, keskustelu_id, nimimerkki, p_a, viesti, otsikko, alueNimi);
        viestit.add(v);
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public void lisaaViesti(int alue, int keskustelu, String nimimerkki, String viesti) throws SQLException { // viestien lisäys
        String sql ="INSERT INTO Viesti(alue, keskustelu, nimimerkki, viesti, pvm_kellonaika) VALUES (?, ?, ?, ?, ?)";
        Connection connection = database.getConnection();       
        PreparedStatement stmt = connection.prepareStatement(sql);        
        stmt.setInt(1, alue);  
        stmt.setInt(2, keskustelu); 
        stmt.setString(3, nimimerkki); 
        stmt.setString(4, viesti); 
        stmt.setString(5,getCurrentTimeStamp());
        stmt.executeUpdate();        
        stmt.close();       
        connection.close();
    }
    
    private String getCurrentTimeStamp() { // viestien lisäyksessä käytettävä metodi, joka muodostaa ajan ja pvm viestille	        
        long utc2 = 7200000l; //Lisätään 2h jotta saadaan serverin UTC+0 ajasta  UTC + 2
        long timeNow = System.currentTimeMillis(); // Nykyinen aika millisekunneissa
        Date today = new Date(timeNow+utc2); //Luodaan uusi aika olio arvoilla
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //Formatoidaan millisekunnit pois
        String date = DATE_FORMAT.format(today);
	return date;
    }
}
