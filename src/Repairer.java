import java.io.FileNotFoundException;
import java.sql.*;

public class Repairer extends User {
    int repairer_id;
    boolean retrievedRepairerIDFromSQL = false;

    public Repairer(String username, int repairer_id) {
        super(username, User.PERMISSION_REPAIRER);

    }

    public Repairer(int repairer_id) throws FileNotFoundException, SQLException { // just to make it easier to
                                                                                  // initialise if only repairer_id is
                                                                                  // known
        super(User.PLACEHOLDER_USERNAME, User.PERMISSION_REPAIRER);
        SQLRequest request = new SQLRequest();
        ResultSet rs = request.SQLQuery(
                "SELECT username FROM login WHERE repairer_id = " + repairer_id + ";");
        rs.next();
        this.username = rs.getString(1); // replaces placeholder username with actual username
    }

    public int getRepairerID() throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        if (!retrievedRepairerIDFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT repairer_id FROM login WHERE username = \"" + String.valueOf(this.getUsername()) + "\";");
            rs.next();
            this.repairer_id = rs.getInt(1);
            retrievedRepairerIDFromSQL = true;
        }
        return this.repairer_id;
    }

    public void setRepairerID(int repairer_id) throws FileNotFoundException {
        SQLRequest request = new SQLRequest();

        this.repairer_id = repairer_id;

        // SQL
        request.SQLUpdate("UPDATE login SET repairer_id = " + repairer_id + " WHERE username = \""
                + String.valueOf(getUsername()) + "\";");
    }

}
