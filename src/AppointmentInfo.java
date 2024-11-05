import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AppointmentInfo implements ActionListener {
    Appointment currentAppointment;
    User currentUser;

    JFrame frame = new JFrame();
    JLabel appointmentInfo = new JLabel("<HTML><H1><B>Appointment Info</B></H1></HTML>");

    JLabel dateLabel = new JLabel("Date:");
    JTextField dateField = new JTextField();

    JLabel applianceLabel = new JLabel("Appliance:");
    JButton appliancePreviewButton = new JButton("Appliance Preview");

    JButton backButton = new JButton("Back");
    JButton editButton = new JButton("Edit");
    JButton deleteButton = new JButton("Delete");

    public AppointmentInfo(Appointment appointment, User currentUser) throws FileNotFoundException, SQLException {

        this.currentAppointment = appointment;
        this.currentUser = currentUser;

        // setup frame

        frame.setSize(250, 180); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();

        // appointmentInfo
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(appointmentInfo, constraints);

        // dateLabel
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(dateLabel, constraints);

        // dateField
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        dateField.setEditable(false);
        dateField.setText(Appointment.dateToString(appointment.getDate()));
        frame.add(dateField, constraints);

        // applianceLabel
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(applianceLabel, constraints);

        // appliancePreviewButton
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        appliancePreviewButton.addActionListener(this);
        frame.add(appliancePreviewButton, constraints);

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        // editButton
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        editButton.addActionListener(this);
        frame.add(editButton, constraints);

        // deleteButton
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        deleteButton.addActionListener(this);
        frame.add(deleteButton, constraints);
        deleteButton.setForeground(Color.RED);

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        if (actionCommand.equals("Appliance Preview")) {
            try {
                new ApplianceInfo(currentAppointment.getAppliance(), new User(User.PLACEHOLDER_USERNAME), false);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Back")) {
            frame.setVisible(false);
            frame.dispose();
        } else if (actionCommand.equals("Delete")) {
            deleteButton.setText("Are you sure?");
        } else if (actionCommand.equals("Are you sure?")) {
            try {
                currentAppointment.deleteFromSQL();
                frame.setVisible(false);
                frame.dispose();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (actionCommand.equals("Edit")) {
            try {
                new AppointmentBooking(currentUser, currentAppointment);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
