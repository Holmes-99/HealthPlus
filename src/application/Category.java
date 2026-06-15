//Group 27 | Lara Daifallah 1230239 & Shatha Abualrub 1231279 
package application;

public class Category {

    private int id;
    private String name;
    private String description;

    //reading from DB
    public Category(int id, String name,String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    //adding new category
    public Category(String name,String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }
}