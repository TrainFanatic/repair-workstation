import java.sql.*;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SQLRequest {
    String key;
    String driver = "com.mysql.cj.jdbc.Driver";

    public ResultSet SQLQuery(String SQL) {
        Statement sqlst;
        ResultSet result = null;

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost: 3306/repair_workshop", "root",
                    this.key);
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost: 3306/repair_workshop", "root",
                    this.key);
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost: 3306/repair_workshop", "root",
                    this.key);
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost: 3306/repair_workshop", "root",
                    this.key);

            PreparedStatement pstmt = con
                    .prepareStatement("update appliances_photos set image_object = ? where appliance_id = ?");

            // the cast to int is necessary because with JDBC 4 there is
            // also a version of this method with a (int, long)
            // but that is not implemented by Oracle

            ByteArrayInputStream in = new ByteArrayInputStream(imageBlob);
            pstmt.setBinaryStream(1, in, (int) imageBlob.length);

            pstmt.setInt(2, appliance_id); // set the PK value
            pstmt.executeUpdate();
            con.setAutoCommit(false); // SQL BROKEN Can't call commit when autocommit=true
            con.commit();
            pstmt.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + driver);
        }

        catch (SQLException ex) {
            System.out.println("SQL BROKEN " + ex.getMessage());
            System.out.println("For Image BLOB query");
        }
    }

    public SQLRequest() throws FileNotFoundException {
        File keyfile = new File(
                "/Users/christopherginting/Desktop/School lmfao/IB/Compsci/IA/cs_ia/src/mysql_server_key"); // read pwd
                                                                                                            // from file
        Scanner sc = new Scanner(keyfile);

        this.key = sc.nextLine();
    }
}
