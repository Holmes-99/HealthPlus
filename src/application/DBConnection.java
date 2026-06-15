//Group 27 | Lara Daifallah 1230239 || Shatha Abualrub 1231279
package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect(){
        try {
            String url ="jdbc:mysql://localhost:3306/healthplus?useSSL=false&serverTimezone=UTC";
            String user= "root";
            String password="0102";
            Connection conn =DriverManager.getConnection(url, user, password);
            System.out.println("database connected!");
            return conn;
        } 
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}