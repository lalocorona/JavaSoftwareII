package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCCustomers {
    /**This function is used to get all the Costumers and saves them in a list
     *
     * @return The list of all Costumers in the database.
     */
    public static ObservableList<Customer> getAllCostumers(){
        //Create a list to return
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        //set up te sql
        String sql = "SELECT * FROM customers AS c INNER JOIN first_level_divisions As d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID = d.COUNTRY_ID";
                try{
                    //Make the prepared statement
                    PreparedStatement ps = JDBC.connection.prepareStatement(sql);

                    //Make the query ==> ResultSet
                    ResultSet rs = ps.executeQuery();

                    //Cycle through the result set,
                    while (rs.next()){

                        //Pull out the data
                        int customerID = rs.getInt("Customer_ID");
                        String customerName = rs.getString("Customer_Name");
                        String address = rs.getString("Address");
                        String postalCode = rs.getString("Postal_Code");
                        String phone = rs.getString("Phone");
                        int divisionID = rs.getInt("Division_ID");
                        String country = rs.getString("Country");
                        String division = rs.getString("Division");


                        //Make an object instance
                        Customer c = new Customer(customerID, customerName, address, postalCode, phone, divisionID, country, division);

                        //Add to list
                        customerList.add(c);
                    }

                } catch (SQLException e) {


                    throw new RuntimeException(e);
                }

        return customerList;
    }

    public static int getCustomerCount(String country) throws SQLException {
        int count = 0;

        String sql = "SELECT count(*) AS count FROM customers \n" +
                "AS c INNER JOIN first_level_divisions As d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID = d.COUNTRY_ID \n" +
                "WHERE Country = ?;";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, country);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public Customer getCustomerID(String customer){
        String sql = "SELECT * FROM customers WHERE Customer_Name = ?";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1,customer);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Customer c = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getInt("Division_ID"),
                        rs.getString("Country"),
                        rs.getString("Division")
                );
                        return c;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /** This class inserts data into the mySQL database
     *
     * @param id
     * @param customerName
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division
     * @return
     */
    public static int insertCustomer(int id, String customerName, String address, String postalCode, String phoneNumber, String division){
        System.out.println(id + " " + customerName + " " + address + " " + postalCode + " " + phoneNumber + " "  + " " + division);

        Division newDivision = new JDBCDivisions().getDivisionID(division);

        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?,?,?,?,?,?) ";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, customerName);
            ps.setString(3,address);
            ps.setString(4,postalCode);
            ps.setString(5,phoneNumber);
            ps.setInt(6, newDivision.getDivisionID());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Rows affected: " + ps.getUpdateCount());
            }
                else{
                    System.out.println("No change");
                }

           return rowsAffected;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**This class will update the customer data in the mySQL database
     *
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     * @return
     */
    public static int updateCustomer(int id, String name, String address, String postalCode, String phone, String division){
        Division newDivision = new JDBCDivisions().getDivisionID(division);

        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, newDivision.getDivisionID());
            ps.setInt(6, id);

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Rows affected: " + ps.getUpdateCount());
            }
            else{
                System.out.println("No change");
            }

            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**This class will delete customer records in the mySQL database using the Customer ID as parameter
     *
     * @param id
     * @return
     */
    public static int deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Rows affected: " + ps.getUpdateCount());
            }
            else{
                System.out.println("No change");
            }

            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
