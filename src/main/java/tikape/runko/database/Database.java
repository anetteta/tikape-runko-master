/* TIKAPE keskusutelufoorumi
 * Tietokanta -luokka: konstruktori ja getteri
 * @author Anette
 */

package tikape.runko.database;
import java.sql.*;
import java.util.*;
import java.net.*;

public class Database { 
    private String databaseAddress;

    public Database(String address) throws Exception {
        this.databaseAddress = address;
        
        init();
    }
    
    private void init() { 
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }
        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }
        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }
        return DriverManager.getConnection(databaseAddress);
    }
    
    // Taulujen luonti
    private List<String> postgreLauseet() {  
        ArrayList<String> lista = new ArrayList<>();
        lista.add("CREATE TABLE Alue (alue_id SERIAL PRIMARY KEY, nimi VARCHAR(20) NOT NULL);");
        lista.add("CREATE TABLE Keskustelu (keskustelu_id SERIAL PRIMARY KEY, otsikko VARCHAR(20) NOT NULL, alue INTEGER NOT NULL, FOREIGN KEY(alue) REFERENCES Alue (alue_id));");
        lista.add("CREATE TABLE Viesti (viesti_id SERIAL PRIMARY KEY, alue INTEGER NOT NULL, keskustelu INTEGER NOT NULL, nimimerkki VARCHAR(15) NOT NULL, "
                    + "pvm_kellonaika VARCHAR(20) NOT NULL, viesti VARCHAR(3000) NOT NULL, FOREIGN KEY(alue) REFERENCES Alue (alue_id), FOREIGN KEY(keskustelu) REFERENCES Keskustelu (keskustelu_id));");
        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("");
        lista.add("");
        return lista;
    }
}