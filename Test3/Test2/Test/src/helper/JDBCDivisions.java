package helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCDivisions {

    /**This function is used to get the division ID based on division name
     *
     * @param division
     * @return
     */
    public Division getDivisionID(String division){
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";

        try{
            PreparedStatement  ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, division);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Division d = new Division(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        rs.getInt("Country_ID")
                );
                return d;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    /**This function is used to get the Division based on the Country name that is selected in the GUI and saves it in a list.
     *
     * @param country
     * @return
     * @throws SQLException
     */
    public static ObservableList<Division> getDivisionByCountryName(String country) throws SQLException {
        Country newCountry = JDBCCountries.getCountryID(country);

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setInt(1,newCountry.getID());

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                int idD = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int idC = rs.getInt("Country_ID");

                Division d = new Division(idD, name, idC);

                divisions.add(d);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return divisions;
    }

    /** This function is used to get all the divisions
     *
     * @return
     */
    public static ObservableList<Division> getAllDivisons(){
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                int divisionID = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int countrtyID = rs.getInt("Country_ID");

                Division d = new Division(divisionID, name, countrtyID);
                divisionsList.add(d);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return divisionsList;
    }

}
