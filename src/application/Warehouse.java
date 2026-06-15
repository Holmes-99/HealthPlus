//Group 27 | Lara Daifallah 1230239 & Shatha Abualrub 1231279 
package application;

public class Warehouse {

    private int id;
    private String name;
    private String address;
    private String city;
    private String phone;

    // reading from DB
    public Warehouse(int id, String name, String address, String city, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    // adding new warehouse
    public Warehouse(String name, String address, String city, String phone) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
}