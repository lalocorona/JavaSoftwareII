package helper;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCContacts {

    /**This class will get all the contacts from the mySQL databse and returns them in an
     * observable list.
     *
     * @return
     */
    public static ObservableList<Contact> getAllContacts(){
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
            try{
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    int contactID = rs.getInt("Contact_ID");
                    String contactName = rs.getString("Contact_Name");
                    String email = rs.getString("Email");

                    Contact c = new Contact(contactID, contactName, email);

                    contactList.add(c);

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return contactList;
    }

    /**This class will get the contact name from the mySQL database
     * when @param ID is inputted
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public  Contact getContactNameByContactID(int id) throws SQLException{
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Contact c = new Contact(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact Name"),
                        rs.getString("Email")
                );
                return c;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**This class will get the correct contact ID from the mySQL database
     * based on what name is inputted.
     *
     * @param contact
     * @return
     */
    public Contact getContactID(String contact){
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1,contact);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Contact c = new Contact(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email")
                );
                return c;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
