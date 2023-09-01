package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCCountries {
    /**
     * This method gets a list of all the countries
     * @return the list of countries
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        //create a list to return
        ObservableList<Country> theList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * from countries";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {

                //Pull out the data
                int id = rs.getInt("Country_ID");
                String name = rs.getString("Country");

                //make an object instance
                Country c = new Country(id, name);

                //add to list
                theList.add(c);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return theList;

    }

    /** This function is used to get the country ID based on the name of the country
     * @return returns the country based on the country name that is input. Otherwise, returns null;
     *
     **/
    public static Country getCountryID(String country) throws SQLException {
        //set up the sql
        String sql = "SELECT * from countries WHERE Country = ?";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            //Set the prepared statement
            ps.setString(1, country);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {

                //make an object instance
                Country c = new Country(

                        //Pull out the data based on the country name
                        rs.getInt("Country_ID"),
                        rs.getString("Country")

                );
                return c;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}