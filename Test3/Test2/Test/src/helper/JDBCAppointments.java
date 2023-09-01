package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;

import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.TimeZone;

public class JDBCAppointments {
    public static String[] generateTimeArray() {
        int numSlots = 24 * 60 / 15; // Total number of 15-minute slots in 24 hours
        String[] timeArray = new String[numSlots];

        int hours = 0;
        int minutes = 0;

        for (int i = 0; i < numSlots; i++) {
            String hourStr = (hours < 10) ? "0" + hours : String.valueOf(hours);
            String minuteStr = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);

            timeArray[i] = hourStr + ":" + minuteStr;

            minutes += 15;
            if (minutes >= 60) {
                minutes = 0;
                hours++;
            }
        }

        return timeArray;
    }
    /**This method holds an array string of times from 8:00am-10:00pm EST
     *
     */
    private static final String [] times = {"00:00","08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00",
            "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00",
            "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00",
            "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00",
            "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00",
            };

    /**This method is used to get all the appointments from the database and saves them to an observable list
     *
     * @return the list of all appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        //create a list to return
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM appointments;";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {


                //Pull out the data
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //make an object instance


                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return appointmentList;

    }

    /**This method gets any appointments that are within 15 minutes of the current time.
     *
     * @return an observable list of appointments
     */
    public static ObservableList<Appointment> getAppointments15Minutes(){
        ObservableList<Appointment> appointment15min = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Start > NOW() - interval 15 minute";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                //Pull out the data
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //make an object instance

                //add to list
                appointment15min.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointment15min;
    }

    /**This method gets a list of appointments that are within the current month
     *
     * @return a list of monthly appointments
     */
    public static ObservableList<Appointment> getMonthlyAppointments(){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT *\n" +
                "FROM appointments WHERE month(start) = month(now())";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentList;
    }

    /**This method gets a list of weekly appointments and saves them to an observable list.
     *
     * @return
     */
    public static ObservableList<Appointment> getWeeklyAppointments(){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT *\n" +
                "FROM appointments WHERE yearweek(start, 1) = yearweek(now(),1);";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {

                //Pull out the data
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //make an object instance

                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentList;
    }

    /**This method will return an observable list of appointments base on an input of selected contactID
     *
     * @param contactID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentByContactID(int contactID) throws SQLException {
        //create a list to return
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * \n" +
                "FROM appointments WHERE Contact_ID = ?";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, contactID);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );

                //make an object instance
                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return appointmentList;

    }

    /**This class will return an observable list of appointments with all the columns
     *  based on the customerID that is inputted
     *
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentInfoByCustomerID(int customerID) throws SQLException{
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1,customerID);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {


                //Pull out the data
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //make an object instance

                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return appointmentList;

    }

    public static ObservableList<Appointment> checkAppointmentOverlap(int customerID, int appointmentID) throws SQLException{
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? AND Appointment_ID != ?";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1,customerID);
            ps.setInt(2, appointmentID);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {


                //Pull out the data
                Appointment a = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getDate("Start").toLocalDate(),
                        rs.getDate("End").toLocalDate(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getInt("User_ID")
                );


                //make an object instance

                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return appointmentList;

    }

    /** This class will get appointments based on the customerID
     * and only includes the information AppointmentID, Title, CustomerID, Customer Name
     * and matches them based on two tables appointments and customers.
     *
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentByCustomerID(int customerID) throws SQLException{
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT a.Appointment_ID, a.Title, c.Customer_ID, c.Customer_Name FROM appointments AS a INNER JOIN customers AS c ON c.Customer_ID = a.Customer_ID WHERE a.Customer_ID = ?";

        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1,customerID);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            while (rs.next()) {


                //Pull out the data
                int aID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                int cID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");


                //make an object instance
                Appointment a = new Appointment(aID, title, cID, customerName);



                //add to list
                appointmentList.add(a);
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        //return the list
        return appointmentList;

    }

    /**This class returns a count of appointments based on the month that is inputted to a record
     *
     * @param month
     * @return
     */
    public static int getMonthlyAppointmentCount(int month) {
        //set up the sql
        String sql = "SELECT Count(*) AS count\n" +
                "FROM appointments WHERE extract(MONTH FROM Start) = ?;";

        int count = 0;
        try {
            //Make the prepared statement
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, month);

            //make the query ==> ResultSet
            ResultSet rs = ps.executeQuery();

            //Cycle through the result,
            if (rs.next()) {

                //Pull out the data
                count = rs.getInt("count");

            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return count;
    }

    /**This class returns the count of appointment types for reporting
     *
     * @param type
     * @return
     * @throws SQLException
     */
    public static int getTypeAppointmentCount(String type) throws SQLException {
        int count = 0;

        String sql = "SELECT Count(*) AS count \n" +
                "FROM appointments Where Type = ?;";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, type);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }


    /**This class is used to insert appointment data into the database
     *
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contact
     * @return
     */
    public static int insertAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, String contact){
        Contact newContact = new JDBCContacts().getContactID(contact);

        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?)";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);


            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setInt(7, customerID);
            ps.setInt(8, userID);
            ps.setInt(9, newContact.getContactID());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
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

    /**This class will delete an appointment and takes the parameter Appointment ID to ensure the correct
     * appointment is selected for deletion
     *
     * @param id
     * @return
     */
    public static int deleteAppointment(int id){
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
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

    /**This class will update an appointment that is selected to write it to the mySQL database.
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param user_ID
     * @param contact
     * @return
     */
    public static int updateAppointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int user_ID, String contact){
        Contact newContact = new JDBCContacts().getContactID(contact);

        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID =?";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6,Timestamp.valueOf(end));
            ps.setInt(7, customerID);
            ps.setInt(8, user_ID);
            ps.setInt(9, newContact.getContactID());
            ps.setInt(10, appointmentID);

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


    /**This class is used to access the times string
     *
     * @return the times
     */
    public static String[] getTimes() {
        return times;
    }
}
