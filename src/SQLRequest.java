import java.sql.*;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SQLRequest {
    String key;
    String driver = "com.mysql.cj.jdbc.Driver";
    // String url = "jdbc:mysql://192.168.1.93: 3306/repair_workshop"; //windows
    String url = "jdbc:mysql://localhost: 3306/repair_workshop"; // macos
    String user = "root"; // macos (localhost)
    // String user = "techie410"; // windows

    public ResultSet SQLQuery(String SQL) {
        Statement sqlst;
        ResultSet result = null;

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, this.key);
            sqlst = con.createStatement();
            result = sqlst.executeQuery(SQL);

            // con.close(); // this causes ResultSet to be closed!!!!!

        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + driver);
        }

        catch (SQLException ex) {
            System.out.println("SQL BROKEN " + ex.getMessage());
            System.out.println("For Query: " + SQL);
        }
        return result;
    }

    public void SQLUpdate(String SQL) {
        Statement sqlst;

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, this.key);
            sqlst = con.createStatement();
            sqlst.executeUpdate(SQL);
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + driver);
        }

        catch (SQLException ex) {
            System.out.println("SQL BROKEN " + ex.getMessage());
            System.out.println("For Query: " + SQL);
            ex.printStackTrace();
        }
    }

    public byte[] BLOBQuery(String SQL) {
        Statement sqlst;

        byte[] blobbytes = null;

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, this.key);
            sqlst = con.createStatement();

            ResultSet query = sqlst.executeQuery(SQL);

            query.next();

            Blob blob = query.getBlob(1);

            blobbytes = blob.getBytes(1, (int) blob.length());

            query.close();
            con.close();
            return blobbytes;

        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + driver);
        }

        catch (SQLException ex) {
            System.out.println("SQL BROKEN " + ex.getMessage());
            System.out.println("For Query: " + SQL);
        }

        return blobbytes;

    }

    public void UpdateAppliancesImageObject(byte[] imageBlob, int appliance_id) { // there needs to be an extra method
                                                                                  // because trying to create a single
                                                                                  // query as a string and then passing
                                                                                  // it to SQLUpdate causes
                                                                                  // malformation.
        Statement sqlst;

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, this.key);
            // Connection con = DriverManager.getConnection("jdbc:mysql://localhost: 3306/repair_workshop", "root",
            //         this.key); // open a connection to the repair workshop database

            PreparedStatement pstmt = con
                    .prepareStatement("update appliances_photos set image_object = ? where appliance_id = ?");

            ByteArrayInputStream in = new ByteArrayInputStream(imageBlob);
            pstmt.setBinaryStream(1, in, (int) imageBlob.length); // image_object is set to this value
            // the (int) typecasting is to comply with the library's strict int argument
            // type

            pstmt.setInt(2, appliance_id); // appliance_id is set to this value
            pstmt.executeUpdate();
            con.setAutoCommit(false); // Patch to fix the thrown exception "SQL BROKEN Can't call commit when
                                      // autocommit=true"
            con.commit();
            pstmt.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + driver); // this can be thrown when dependencies are not present
        }

        catch (SQLException ex) {
            System.out.println("SQL BROKEN " + ex.getMessage());
            System.out.println("For Image BLOB query"); // throw message clarification for debugging
        }
    }

    public SQLRequest() throws FileNotFoundException {
        // File keyfile = new File("/Users/christopherginting/Desktop/School
        // lmfao/IB/Compsci/IA/cs_ia/src/mysql_server_key"); // macOS build

        // File keyfile = new File("C:\\Users\\Christopher
        // Ginting\\Documents\\GitHub\\repair-workstation\\src\\mysql_server_key"); //
        // wind
        // ws build
        File keyfile = new File(
                "/Users/christopherginting/Desktop/School lmfao/IB/Compsci/IA/cs_ia/src/mysql_server_key"); // read pwd
                                                                                                            // from file
                                                                                                            // todo:
                                                                                                            // update
                                                                                                            // this with
                                                                                                            // placeholder
        Scanner sc = new Scanner(keyfile);

        this.key = sc.nextLine();
    }
}
