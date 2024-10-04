import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;

import com.mysql.cj.jdbc.exceptions.SQLError;

public class Appliance {
    public static final int STATUS_UNINITIALISED = -1;
    public static final int STATUS_UNDELIVERED = 0;
    public static final int STATUS_UNTOUCHED = 1;
    public static final int STATUS_DIAGNOSED = 2;
    public static final int STATUS_UNDER_REPAIR = 3;
    public static final int STATUS_REPAIRED = 4;
    public static final int STATUS_IRREPARABLE = 5;

    int ApplianceID;
    public static final int ID_NOT_SET = -1;

    int status = STATUS_UNINITIALISED;
    boolean retrievedStatusFromSQL = false;

    String owner; // todo: add functionality to retrieve or set owner okok
    boolean retrievedOwnerFromSQL = false;

    public String getOwnerString() throws SQLException {
        if (!retrievedOwnerFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT username FROM appliances WHERE appliance_id = " + String.valueOf(getApplianceID()));
            rs.next();
            owner = rs.getString(1);
            retrievedOwnerFromSQL = true;
        }
        return owner;
    }

    public User getOwnerUser() throws SQLException {
        if (!retrievedOwnerFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT username FROM appliances WHERE appliance_id = " + String.valueOf(getApplianceID()));
            rs.next();
            owner = rs.getString(1);
            retrievedOwnerFromSQL = true;
        }
        return new User(owner);
    }

    public void setOwner(String owner) { // be VERY CAREFUL with this method
        this.owner = owner;

        // SQL
        request.SQLUpdate("UPDATE appliances SET username = \"" + owner + "\" WHERE appliance_id = "
                + String.valueOf(getApplianceID()));
    }

    String note;
    boolean retrievedNoteFromSQL = false;

    String location;
    boolean retrievedLocationFromSQL = false;

    String type;
    boolean retrievedTypeFromSQL = false;

    BufferedImage image = null;

    SQLRequest request;

    // image = SQL BLOB

    // getters and setters
    public String getLocation() throws SQLException {
        if (!retrievedLocationFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT location FROM appliances WHERE appliance_id = " + String.valueOf(getApplianceID()));
            rs.next();
            location = rs.getString(1);
            retrievedLocationFromSQL = true;
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;

        // SQL
        request.SQLUpdate("UPDATE appliances SET location = \"" + location + "\" WHERE appliance_id = "
                + String.valueOf(getApplianceID()));
    }

    public String getType() throws SQLException {
        if (!retrievedTypeFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT type FROM appliances WHERE appliance_id = " + String.valueOf(getApplianceID()));
            rs.next();
            type = rs.getString(1);
            retrievedTypeFromSQL = true;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;

        // SQL
        request.SQLUpdate("UPDATE appliances SET type = \"" + type + "\" WHERE appliance_id = "
                + String.valueOf(getApplianceID()));
    }

    public int getApplianceID() { // no need for SQL because this will never change
        return ApplianceID;
    }

    // public void setApplianceID(int applianceID) { // NOT POSSIBLE!
    // this.ApplianceID = applianceID;
    // }

    public int getStatus() throws SQLException {
        if (!retrievedStatusFromSQL) { // if NOT retrieved
            // retrieve
            ResultSet rs = request.SQLQuery(
                    "SELECT repair_status FROM appliances WHERE appliance_id = " + String.valueOf(getApplianceID()));
            rs.next();
            status = rs.getInt(1);
            retrievedStatusFromSQL = true;
        }

        return status;
    }

    public int getNextApplianceIDNumber() throws FileNotFoundException, SQLException {
        String query = "SELECT COUNT(username) FROM appliances";

        SQLRequest sqlr = new SQLRequest();
        ResultSet rs = sqlr.SQLQuery(query);

        rs.next();
        int count = rs.getInt(1) + 1;
        rs.close();
        return count;
    }

    public void setStatus(int status) {
        this.status = status;

        // SQL
        request.SQLUpdate("UPDATE appliances SET type = \"" + status + "\" WHERE appliance_id = "
                + String.valueOf(getApplianceID()));
    }

    public String getNote() throws SQLException {
        if (!retrievedNoteFromSQL) { // if NOT retrieved
            String query = "SELECT note FROM appliances WHERE appliance_id = " + getApplianceID();
            System.out.println(query);
            // retrieve
            ResultSet rs = request
                    .SQLQuery(query);
            rs.next();
            note = rs.getString(1);
            System.out.println(note);
            retrievedNoteFromSQL = true;
        }

        return note;
    }

    public void setNote(String note) {
        this.note = note;

        // SQL
        request.SQLUpdate("UPDATE appliances SET note = \"" + note + "\" WHERE appliance_id = "
                + String.valueOf(getApplianceID()));
    }

    // Image handling shenanigans

    private BufferedImage createImageFromBytes(byte[] imageData) { // source:
        // https://stackoverflow.com/questions/12705385/how-to-convert-a-byte-to-a-bufferedimage-in-java
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getImage() throws SQLException {
        if (this.image == null) {
            // load the image

            byte[] blobbytes = request.BLOBQuery(
                    "SELECT image_object FROM appliances_photos WHERE appliance_id = " + String.valueOf(ApplianceID));

            this.image = createImageFromBytes(blobbytes);
        }

        return image;
    }

    public void setImage(BufferedImage image) throws IOException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos); // this means ONLY JPEG IS SUPPORTED
        byte[] bytes = baos.toByteArray();

        Blob blob = new SerialBlob(bytes);

        request.UpdateAppliancesImageObject(bytes, ApplianceID);

        this.image = image;
    }

    public boolean checkApplianceIDExists() throws SQLException {
        ResultSet existsQuery = request
                .SQLQuery("SELECT COUNT(1) FROM appliances WHERE appliance_id = " + 1);

        existsQuery.next();

        // System.out.println(existsQuery.getInt(1)); //debugging to sysout about
        // whether or not the appliance_id exists/ 1 means that it does exist, while 0
        // means that it doesnt exist.

        boolean exists = existsQuery.getInt(1) == 1;

        existsQuery.close();
        return exists;

    }

    public static String statusToString(int status) {
        if (status == -1) {
            return "Uninitialised";
        } else if (status == 0) {
            return "Undelivered";
        } else if (status == 1) {
            return "Untouched";
        } else if (status == 2) {
            return "Diagnosed";
        } else if (status == 3) {
            return "Under Repair";
        } else if (status == 4) {
            return "Repaired";
        } else if (status == 5) {
            return "Irreparable";
        } else {
            System.err
                    .println("Unknown status number " + String.valueOf(status) + " has been passed to statusToString");
            return "App broken. See error logs";
        }
    }

    public Appliance(int ID) throws FileNotFoundException {
        request = new SQLRequest();

        // System.out.println("set appliance ID to " + ID);
        this.ApplianceID = ID;
    }

    public Appliance() throws FileNotFoundException { // for new appliances
        request = new SQLRequest();

        // System.out.println("set appliance ID to " + ID);
        this.ApplianceID = ID_NOT_SET;
    }

    public Appliance(int ID, String username, String note, String location, int status, String type, boolean inDatabase)
            throws FileNotFoundException {
        request = new SQLRequest();

        if (inDatabase) {
            retrievedLocationFromSQL = true;
            retrievedNoteFromSQL = true;
            retrievedOwnerFromSQL = true;
            retrievedStatusFromSQL = true;
            retrievedTypeFromSQL = true;
        }

        this.ApplianceID = ID;
        setOwner(username);
        setNote(note);
        setLocation(location);
        setStatus(status);
        setType(type);
    }
}
