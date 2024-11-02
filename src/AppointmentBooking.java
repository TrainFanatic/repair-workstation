import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

import java.awt.event.*;

import java.io.File;
import java.nio.file.Files;

import com.toedter.calendar.*;

import java.time.ZoneId;

import java.util.Date;
import java.util.Queue;

public class AppointmentBooking implements ActionListener {
    User currentUser;

    int appointmentID;

    JLabel makeAppointmentsText = new JLabel("<HTML><h1><b><u>Make an appointment</u></b></h1></HTML>");

    java.util.Date currentDate = Date.from(java.time.LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    JDateChooser dateChooser = new JDateChooser(currentDate);

    JButton backButton = new JButton("Back");

    JComboBox<String> applianceSelect = new JComboBox<String>();

    JButton okButton = new JButton("OK");

    JButton previewApplianceButton = new JButton("Preview appliance");

    JFrame frame = new JFrame();

    Appointment draftAppointment;

    public AppointmentBooking(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;
        frame.setSize(600, 600); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();

        // makeAppointmentsText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(makeAppointmentsText, constraints);

        // dateChooser
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        frame.add(dateChooser, constraints);

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        // applianceSelect
        Queue<Appliance> allAppliances = currentUser.getAllAppliances();
        Appliance curApp;
        while (!allAppliances.isEmpty()) { // DONE added list all functionality. does this work?
            curApp = allAppliances.remove(); // retrieves and removes tail of queue
            applianceSelect.addItem(String.valueOf(curApp.getApplianceID()));
        }

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        frame.add(applianceSelect, constraints);

        // previewApplianceButton
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        previewApplianceButton.addActionListener(this);
        frame.add(previewApplianceButton, constraints);

        // okButton
        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        okButton.addActionListener(this);
        frame.add(okButton, constraints);

        frame.setVisible(true);
    }

    public void createNewEntries() throws NumberFormatException, FileNotFoundException, SQLException {
        SQLRequest sqlr = new SQLRequest();
        draftAppointment = new Appointment(sqlr.nextAppointmentID());

        System.out.println("Trying to create new database entries for ID " + draftAppointment.getAppointmentID());

        sqlr.SQLUpdate("INSERT INTO appointments VALUES(" + draftAppointment.getAppointmentID()
                + ", " + String.valueOf(Repairer.PLACEHOLDER_REPAIRER_ID) + ", "
                + "DATE \'" + Appointment.dateToString(dateChooser.getDate()) + "\', "
                + Integer.parseInt((String) applianceSelect.getSelectedItem()) + ", \'"
                + currentUser.getUsername() + "\');");

    }

    public void actionPerformed(ActionEvent e) {
        String actionString = e.getActionCommand();

        if (actionString.equals("Preview appliance")) {
            try {
                new ApplianceInfo(new Appliance((Integer.parseInt((String) applianceSelect.getSelectedItem()))),
                        currentUser);
            } catch (FileNotFoundException | SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (actionString.equals("OK")) {
            // submit this as an appointment
            try {
                createNewEntries();
                new AppointmentList(currentUser);
            } catch (NumberFormatException | FileNotFoundException | SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            frame.setVisible(false);
            frame.dispose();

        }
        if (actionString.equals("Back")) {
            frame.setVisible(false);
            frame.dispose();
            try {
                new AppointmentList(currentUser);
            } catch (FileNotFoundException | SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
