import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class User {
    static final int PERMISSION_UNINITIALISED = -1;
    static final int PERMISSION_CLIENT = 0;
    static final int PERMISSION_REPAIRER = 1;
    static final int PERMISSION_ADMIN = 2;

    String username;
    static final String PLACEHOLDER_USERNAME = "This is a placeholder username";

    public String getUsername() {
        return username;
    }

    int permission = PERMISSION_UNINITIALISED;

    public static boolean usernameExists(String username) throws FileNotFoundException, SQLException {
        SQLRequest sqlr = new SQLRequest();

        ResultSet rs = sqlr.SQLQuery("SELECT COUNT(1) FROM login WHERE username = \"" + username + "\";");

        rs.next();

        boolean usernameFound = rs.getInt(1) == 1;
        // System.out.println(usernameFound); // there is no bug here

        rs.close();
        return (usernameFound); // see if it returns 1 (indicating that it exists)

    }

    public int getPermission() throws SQLException, FileNotFoundException {
        if (this.permission == PERMISSION_UNINITIALISED) {
            SQLRequest sqlr = new SQLRequest();

            ResultSet rs = sqlr.SQLQuery("SELECT permission FROM login WHERE username = \"" + username + "\";");

            rs.next();

            this.permission = rs.getInt(1);

            rs.close();
        }
        return (permission); // see if it returns 1 (indicating that it exists)

    }

    public Queue<Appliance> getAllAppliances() throws FileNotFoundException, SQLException {
        SQLRequest sqlr = new SQLRequest();
        // System.out.println("test");
        ResultSet rs = sqlr.SQLQuery("SELECT * FROM appliances");

        Queue<Appliance> q = new LinkedList<>();

        while (rs.next()) {
            if (rs.getString("username").equals(getUsername())) {

                q.add(new Appliance(rs.getInt("appliance_id"), rs.getString("username"), rs.getString("note"),
                        rs.getString("location"), rs.getInt("repair_status"), rs.getString("type"), true)); // TODO: no
                                                                                                            // need to
                                                                                                            // do this??
            }
        }

        return q;

    }

    public Queue<Appointment> getAllAppointments() throws FileNotFoundException, SQLException {
        SQLRequest sqlr = new SQLRequest();
        System.out.println("test");
        ResultSet rs = sqlr
                .SQLQuery("SELECT appointment_id FROM appointments WHERE username = \"" + getUsername() + "\";");

        Queue<Appointment> q = new LinkedList<>();

        while (rs.next()) {
            q.add(new Appointment(rs.getInt(1)));
            // System.out.println("Added" + String.valueOf(rs.getInt(1))); //debugging.
            // functionned ok
        }

        return q;

    }

    public User(String username, int permission) {
        this.username = username;
    }

    public User(String username) {
        this.username = username;
        this.permission = PERMISSION_UNINITIALISED;
    }
}
