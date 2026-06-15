//Group 27 | Lara Daifallah 1230239 & Shatha Abualrub 1231279 
package application;

import java.sql.*;
import java.util.ArrayList;

public class WarehouseDAO {

    public static ArrayList<Warehouse> getAllWarehouses() {
        ArrayList<Warehouse> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT * FROM Warehouse";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Warehouse w = new Warehouse(
                        rs.getInt("WarehouseID"),
                        rs.getString("WarehouseName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Phone")
                );

                list.add(w);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addWarehouse(Warehouse w) {
        try {
            Connection conn = DBConnection.connect();

            String sql = "INSERT INTO Warehouse (WarehouseName, Address, City, Phone) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, w.getName());
            stmt.setString(2, w.getAddress());
            stmt.setString(3, w.getCity());
            stmt.setString(4, w.getPhone());

            int rows = stmt.executeUpdate();
            conn.close();

            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateWarehouse(Warehouse w) {
        try {
            Connection conn = DBConnection.connect();

            String sql = "UPDATE Warehouse SET WarehouseName = ?, Address = ?, City = ?, Phone = ? "
                    + "WHERE WarehouseID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, w.getName());
            stmt.setString(2, w.getAddress());
            stmt.setString(3, w.getCity());
            stmt.setString(4, w.getPhone());
            stmt.setInt(5, w.getId());

            stmt.executeUpdate();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteWarehouse(int id) {
        try {
            Connection conn = DBConnection.connect();

            String sql = "DELETE FROM Warehouse WHERE WarehouseID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            conn.close();

            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Warehouse getWarehouseById(int id) {
        try {
            Connection conn = DBConnection.connect();

            String sql = "SELECT * FROM Warehouse WHERE WarehouseID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Warehouse w = new Warehouse(
                        rs.getInt("WarehouseID"),
                        rs.getString("WarehouseName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Phone")
                );

                conn.close();
                return w;
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}