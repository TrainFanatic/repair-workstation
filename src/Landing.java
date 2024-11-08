import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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

    JLabel repairerUtilitiesLabel = new JLabel("<HTML><I><U>Repairer tools:</U></I></HTML>");

    JButton assignedAppointmentsButton = new JButton("Check your assigned appointments");
    JButton unassignedAppointmentsButton = new JButton("Unassigned appointments");

    JLabel adminToolsLabel = new JLabel("<html><i>Admin tools:</i></html>");
    JButton nominateRepairerButton = new JButton("Nominate repairer");

    public void screensetup() throws FileNotFoundException, SQLException {

        frame.setSize(480, 300);
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

        // checkAppliancesButton
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

        // repairerUtilitiesLabel
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;

        // constraints.weighty = 1;

        if (user.getPermission() >= User.PERMISSION_REPAIRER) {
            repairerUtilitiesLabel.setBorder(new EmptyBorder(20, 0, 5, 0));
            frame.add(repairerUtilitiesLabel, constraints);
        }

        // assignedAppointmentsButton
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        constraints.ipady = 1;
        // constraints.weighty = 1;

        if (user.getPermission() >= User.PERMISSION_REPAIRER) {
            assignedAppointmentsButton.addActionListener(this);
            frame.add(assignedAppointmentsButton, constraints);
        }

        // unassignedAppointmentsButton
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;

        if (user.getPermission() >= User.PERMISSION_REPAIRER) {
            unassignedAppointmentsButton.addActionListener(this);
            frame.add(unassignedAppointmentsButton, constraints);
        }

        // adminToolsLabel
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        if (user.getPermission() >= User.PERMISSION_ADMIN) {
            frame.add(adminToolsLabel, constraints);
        }

        // nominateRepairerButton
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;

        if (user.getPermission() >= User.PERMISSION_ADMIN) {
            nominateRepairerButton.addActionListener(this);
            frame.add(nominateRepairerButton, constraints);
        }

        // logoutButton
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1; // no longer full width component
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.1;
        // constraints.weighty = 1;
        logoutButton.addActionListener(this);
        logoutButton.setForeground(Color.red);
        frame.add(logoutButton, constraints);

        frame.setVisible(true);

    }

    public Landing(User user) throws FileNotFoundException, SQLException {
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
        } else if (actionCommand.equals("Check your assigned appointments")) {
            try {
                new ListAssignedAppointments(user);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Unassigned appointments")) {
            try {
                new PendingAppointments(user);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Nominate repairer")) {
            try {
                new NominateRepairer(user);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        frame.setVisible(false);
        frame.dispose();
    }
}
