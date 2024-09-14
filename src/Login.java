import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class Login implements ActionListener {

    JFrame frame = new JFrame();

    JLabel msgWelcome = new JLabel("<HTML><H1><U>Welcome!</U></H1><HTML>", SwingConstants.CENTER);

    JLabel msgRepairWorkshop = new JLabel("to Repair Workshop", SwingConstants.CENTER);

    JLabel usernameText = new JLabel("Username:");

    JTextField usernameField = new JTextField();

    JPasswordField passwordField = new JPasswordField();

    JLabel passwordText = new JLabel("Password");

    JButton registerButton = new JButton("Register");

    JButton loginButton = new JButton("Login");

    public void screensetup() {

        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;

        // msgWelcome
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(msgWelcome, constraints);

        // msgRepairWorkshop
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(msgRepairWorkshop, constraints);

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

        // registerButton
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        registerButton.addActionListener(this);
        frame.add(registerButton, constraints);

        // loginButton
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;

        loginButton.addActionListener(this);
        frame.add(loginButton, constraints);
        frame.setVisible(true);

    }

    private boolean isPasswordCorrect(String username, char[] input) throws SQLException, FileNotFoundException { // sample
                                                                                                                  // check
        // password correct by
        // oracle
        boolean isCorrect = true;

        SQLRequest sqlr = new SQLRequest();

        ResultSet rs = sqlr.SQLQuery("SELECT * FROM login WHERE username = \"" + username + "\";"); // get all info
        // from this
        // username

        rs.next();

        String salt = rs.getString("salt");
        char[] password_hash = rs.getString("password_hash").toCharArray();

        return saltAndHash.isExpectedPassword(input, salt, password_hash);
    }

    public Login() {
        screensetup();

    }

    public void actionPerformed(ActionEvent evt) {
        boolean shouldCloseWindow = false;
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand());
        if (actionCommand.equals("Login")) {

            String proposedUsername = usernameField.getText();
            User proposedUser = new User(proposedUsername, User.PERMISSION_UNINITIALISED);
            try {
                if (User.usernameExists(proposedUsername)) {
                    if (isPasswordCorrect(proposedUsername, passwordField.getPassword())) {

                        Landing landing = new Landing(proposedUser);
                        shouldCloseWindow = true;
                    } else {
                        msgRepairWorkshop.setText("Wrong username or password!");
                    }
                } else {
                    msgRepairWorkshop.setText("Wrong username or password!");
                }
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (actionCommand.equals("Register")) {
            Register register = new Register();
            shouldCloseWindow = true;
        }
        if (shouldCloseWindow) {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
