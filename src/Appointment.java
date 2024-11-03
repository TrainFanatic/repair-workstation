import java.util.Date;
import java.io.FileNotFoundException;
import java.sql.*;

public class Appointment {

    int appointment_ID;

    Date appointmentDate;
    boolean retrievedDateFromSQL = false;

    Appliance appliance;
    boolean retrievedApplianceFromSQL = false;

    Repairer repairer;
    boolean retrievedRepairerfromSQL = false;
    boolean assignedRepairer = false;

    User client;
    boolean retrievedClientFromSQL = false;

    public Appointment(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    public int getAppointmentID() {
        return appointment_ID;

    }

    public Date getDate() throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        if (!retrievedDateFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT appointment_date FROM appointments WHERE appointment_id = "
                            + String.valueOf(getAppointmentID()));
            rs.next();
            this.appointmentDate = rs.getDate(1);
            retrievedDateFromSQL = true;
        }
        return appointmentDate;

    }

    public void setDate(Date date) throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        this.appointmentDate = date;

        // SQL
        request.SQLUpdate(
                "UPDATE appointments SET appointment_date = \"" + dateToString(date) + "\" WHERE appointment_id = "
                        + String.valueOf(getAppointmentID()));
    }

    public Appliance getAppliance() throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        int appliance_id;
        if (!retrievedApplianceFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT appliance_id FROM appointments WHERE appointment_id = "
                            + String.valueOf(getAppointmentID()));
            rs.next();
            appliance_id = rs.getInt(1);
            this.appliance = new Appliance(appliance_id);
            retrievedApplianceFromSQL = true;
        }
        return this.appliance;

    }

    // polymorphism below because only appliance_id is sent to the mySQL server
    public void setAppliance(Appliance appliance) throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        this.appliance = appliance;

        int appliance_id = appliance.getApplianceID();

        // SQL
        request.SQLUpdate("UPDATE appointments SET appliance_id = " + appliance_id + " WHERE appointment_id = "
                + String.valueOf(getAppointmentID()));
    }

    public void setAppliance(int appliance_id) throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        this.appliance = new Appliance(appliance_id); // this is dangerous. make sure it's ok.

        // SQL
        request.SQLUpdate("UPDATE appointments SET appliance_id = " + appliance_id + " WHERE appointment_id = "
                + String.valueOf(getAppointmentID()));
    }

    //

    public Repairer getRepairer() throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        int repairer_id;
        if (!retrievedRepairerfromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT repairer_id FROM appointments WHERE appointment_id = "
                            + String.valueOf(getAppointmentID()));
            // System.out.println("SELECT repairer_id FROM appointments WHERE appointment_id
            // = "
            // + String.valueOf(getAppointmentID()));
            rs.next();
            repairer_id = rs.getInt(1);
            this.repairer = new Repairer(repairer_id);
            retrievedClientFromSQL = true;
        }
        return this.repairer;
    }

    // polymorphism below because only repairer_id is sent to the mySQL server
    public void setRepairer(Repairer repairer) throws FileNotFoundException, SQLException {
        SQLRequest request = new SQLRequest();
        this.repairer = repairer;

        // SQL
        request.SQLUpdate(
                "UPDATE appointments SET repairer_id = " + repairer.getRepairerID() + " WHERE appointment_id = "
                        + String.valueOf(getAppointmentID()));
    }

    public void setRepairer(int repairer_id) throws FileNotFoundException, SQLException {
        SQLRequest request = new SQLRequest();
        this.repairer = new Repairer(repairer_id);

        // SQL
        request.SQLUpdate("UPDATE appointments SET repairer_id = " + repairer_id + " WHERE appointment_id = "
                + String.valueOf(getAppointmentID()));
    }

    //

    public User getClient() throws SQLException, FileNotFoundException {
        SQLRequest request = new SQLRequest();
        String client_username;
        if (!retrievedClientFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT username FROM appointments WHERE appointment_id = "
                            + String.valueOf(getAppointmentID()));
            rs.next();
            client_username = rs.getString(1);
            this.client = new User(client_username);
            retrievedClientFromSQL = true;
        }
        return this.client;
    }

    public void setClient(User client) throws FileNotFoundException {
        SQLRequest request = new SQLRequest();
        this.client = client;

        // SQL
        request.SQLUpdate("UPDATE appointments SET username = \"" + client.getUsername() + "\" WHERE appointment_id = "
                + String.valueOf(getAppointmentID()));
    }

    public void deleteFromSQL() throws FileNotFoundException {
        SQLRequest request = new SQLRequest();

        // SQL
        request.SQLUpdate("DELETE FROM appointments WHERE appointment_id = "
                + String.valueOf(getAppointmentID()));
    }

    public static String dateToString(Date d) {
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); // taken from
                                                                                       // https://stackoverflow.com/a/2400981

        return sdf.format(dt);
    }

}
