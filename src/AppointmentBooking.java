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

public class AppointmentBooking implements ActionListener {
    User currentUser;

    int appointmentID;

    JLabel makeAppointmentsText = new JLabel("<HTML><b><u>Make an appointment</u></b></HTML>");

    java.util.Date currentDate = Date.from(java.time.LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    JDateChooser dateChooser = new JDateChooser(currentDate);

    JButton backButton = new JButton("Back");

    JComboBox<String> applianceSelect;

    JButton okButton = new JButton("OK");

    JFrame frame = new JFrame();

    public AppointmentBooking(User user) {
        this.currentUser = user;
        frame.setSize(600, 600); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        frame.add(dateChooser);

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String actionString = e.getActionCommand();
    }
}
