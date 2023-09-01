package model;

/**
 * This class constructs the Country object and includes getters and setters
 */
public class Country {
    private int id;
    private String name;



    //This is a constructor for the Countries
    public Country(int id, String name){
        this.id = id;
        this.name = name;
    }

    //This is the getter for the country ID
    public int getID() {
        return id;
    }

    //This is the setter for the country ID
    public void setID(int id){
        this.id = id;
    }
    //This is the getter for the country name
    public String getName(){
        return name;
    }

    //This is the setter for the country name
    public void setName(String name){
        this.name = name;
    }


}

