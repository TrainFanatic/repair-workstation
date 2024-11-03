import java.io.FileNotFoundException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class Repairer extends User {
    int repairer_id;
    public static int PLACEHOLDER_REPAIRER_ID = -1;

    boolean retrievedRepairerIDFromSQL = false;
    public static String PLACEHOLDER_REPAIRER_USERNAME = "Repairer not assigned";

    public Repairer(String username, int repairer_id) {
        super(username, User.PERMISSION_REPAIRER);

    }

    public Repairer(int repairer_id) throws FileNotFoundException, SQLException { // just to make it easier to
                                                                                  // initialise if only repairer_id is
                                                                                  // known
        super(User.PLACEHOLDER_USERNAME, User.PERMISSION_REPAIRER);
        SQLRequest request = new SQLRequest();

        if (repairer_id != PLACEHOLDER_REPAIRER_ID) {
            ResultSet rs = request.SQLQuery(
                    "SELECT username FROM login WHERE repairer_id = " + repairer_id + ";");
            rs.next();
            this.username = rs.getString(1); // replaces placeholder username with actual username
        } else {
            this.username = PLACEHOLDER_REPAIRER_USERNAME; // need this special case so that it doesn't try to retrieve
                                                           // id -1 from SQL.
        }
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

    public Queue<Appointment> getAssignedAppointments() {
        // System.out.println("The program is trying to fetch all appointments assigned
        // to the repairer");
        Queue<Appointment> q = new LinkedList<>();
        SQLRequest sqlr;
        try {
            sqlr = new SQLRequest();

            ResultSet rs = sqlr
                    .SQLQuery(
                            "SELECT appointment_id FROM appointments WHERE repairer_id = \"" + getRepairerID() + "\";");

            while (rs.next()) {
                q.add(new Appointment(rs.getInt(1)));
                // System.out.println("Added" + String.valueOf(rs.getInt(1))); //debugging.
                // functionned ok
            }
        } catch (FileNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return q;
    }

    public static Queue<Appointment> getAllUnassignedAppointments() {
        // System.out.println("The program is trying to fetch all appointments assigned
        // to the repairer");
        Queue<Appointment> q = new LinkedList<>();
        SQLRequest sqlr;
        try {
            sqlr = new SQLRequest();

            ResultSet rs = sqlr
                    .SQLQuery(
                            "SELECT appointment_id FROM appointments WHERE repairer_id = -1;");

            while (rs.next()) {
                q.add(new Appointment(rs.getInt(1)));
                // System.out.println("Added" + String.valueOf(rs.getInt(1))); //debugging.
                // functionned ok
            }
        } catch (FileNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return q;
    }

}
