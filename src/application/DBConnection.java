//Group 27 | Lara Daifallah 1230239 || Shatha Abualrub 1231279
package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static Properties config;

    private static Properties loadConfig() {
        if (config != null) return config;
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not find db.properties in the project root. " +
                            "Copy db.properties.example to db.properties and fill in your own MySQL credentials.", e);
        }
        config = props;
        return config;
    }

    public static Connection connect(){
        try {
            Properties props = loadConfig();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("database connected!");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
