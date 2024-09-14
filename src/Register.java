import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Register implements ActionListener {

    JFrame frame = new JFrame();

    JLabel textRegister = new JLabel("<HTML><H1><U>Register</U></H1><HTML>", SwingConstants.CENTER);

    JLabel usernameText = new JLabel("Username:");

    JTextField usernameField = new JTextField();

    JPasswordField passwordField = new JPasswordField();

    JLabel passwordText = new JLabel("Password:");

    JPasswordField repeatField = new JPasswordField();

    JLabel repeatText = new JLabel("Repeat:");

    JLabel warningText = new JLabel();

    JButton registerButton = new JButton("Register");

    public void screensetup() {

        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;

        // textRegister
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(textRegister, constraints);

        // warningText
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        frame.add(warningText, constraints);

        // usernameText
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.3;
        // constraints.weighty = 1;

        frame.add(usernameText, constraints);

        // usernameField
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.7;
        // constraints.weighty = 1;

        frame.add(usernameField, constraints);

        // passwordField
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.7;
        // constraints.weighty = 1;

        frame.add(passwordField, constraints);

        // passwordText
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.3;
        // constraints.weighty = 1;
        frame.add(passwordText, constraints);

        // repeatField
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.7;
        // constraints.weighty = 1;

        frame.add(repeatField, constraints);

        // repeatText
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.3;
        // constraints.weighty = 1;
        frame.add(repeatText, constraints);

        // registerButton
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        registerButton.addActionListener(this);
        frame.add(registerButton, constraints);

        frame.setVisible(true);
    }

    private String getUsernameField() {
        return usernameField.getText();
    }

    private boolean registerDatabaseEntry() throws FileNotFoundException, SQLException {
        String username = getUsernameField();
        if (username.length() > 30) {
            warningText.setText("Username must be 30 characters or less.");
            return false;
        } else if (User.usernameExists(username)) {
            warningText.setText("Username already exists.");
            return false;
        } else {

            String salt = saltAndHash.getNextSalt();

            String password_hash = saltAndHash.hash(passwordField.getPassword(), salt);

            String update = "INSERT INTO login VALUES (\"" + username + "\", NULL, 0, \"" + salt + "\", \"" +
                    password_hash +
                    "\");";

            System.out.println(update);

            SQLRequest sqlr = new SQLRequest();

            sqlr.SQLUpdate(update);
            return true;

        }
    }

    public Register() {
        screensetup();

    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand());

        if (actionCommand.equals("Register")) {
            boolean successful = false;

            if (Arrays.equals(passwordField.getPassword(), repeatField.getPassword())) {
                try {
                    successful = registerDatabaseEntry();
                } catch (FileNotFoundException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                warningText.setText("Passwords do not match. Try again.");
            }

            if (successful) {
                Login login = new Login();

                frame.setVisible(false);
                frame.dispose();
            }
        }

    }

}
