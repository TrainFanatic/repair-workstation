import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Landing implements ActionListener {
    User user;

    JFrame frame = new JFrame();

    JLabel msgLanding = new JLabel("<HTML><H1><U>Landing</U></H1><HTML>", SwingConstants.CENTER);

    JButton logoutButton = new JButton("Log out");

    JButton appointmentsButton = new JButton("Check your appointments");

    JButton checkAppliancesButton = new JButton("Check your appliances");

    public void screensetup() {

        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;

        // msgLanding
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(msgLanding, constraints);

        // logoutButton
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        logoutButton.addActionListener(this);
        frame.add(logoutButton, constraints);

        // loginButton
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;

        checkAppliancesButton.addActionListener(this);
        frame.add(checkAppliancesButton, constraints);

        // appointmentsButton
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;

        appointmentsButton.addActionListener(this);
        frame.add(appointmentsButton, constraints);

        frame.setVisible(true);

    }

    public Landing(User user) {
        this.user = user;

        screensetup();

    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand());

        if (actionCommand.equals("Check your appliances")) {

            Appliance hairdryer = null;
            try {

                try {
                    ApplianceList ALWUI = new ApplianceList(user);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (actionCommand.equals("Log out")) {
            Login login = new Login();
        } else if (actionCommand.equals("Check your appointments")) {
            try {
                new AppointmentList(user);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        frame.setVisible(false);
        frame.dispose();
    }
}
