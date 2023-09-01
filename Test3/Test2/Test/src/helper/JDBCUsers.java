package helper;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUsers {

    /**This function gets all the users in the mySQL database and returns
     * them in an observable list.
     *
     * @return
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM users";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                User u = new User(userID, userName, password);

                userList.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**This class is used to test for validation. It will take
     * @param username
     * @param password
     *
     * and return a boolean value of true if it matches the data in
     * the mySQL database.
     * @return
     * @throws SQLException
     */
    public static boolean selectAllUser(String username, String password) throws SQLException{
        String sql = "SELECT * FROM users WHERE User_Name=? AND Password=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);


        try{
            ps.execute();
            ResultSet rs = ps.getResultSet();
            return (rs.next());
        }
        catch(Exception e){
        System.out.println("Error");
            return false;
        }


    }

    /**This class is used to convert the username allowing you to pull data saved as an object u.
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public static User convertUserName(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE User_Name=? ";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, username);

            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){

                User u = new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password")
                );
                return u;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("The username is: " + username);

        return null;
    }


}
