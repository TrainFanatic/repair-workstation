import java.sql.SQLException;
import java.util.Queue;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.io.FileNotFoundException;

public class AppointmentList implements ActionListener {
    User currentUser;
    JFrame frame = new JFrame();

    JLabel appointmentsText = new JLabel("This is the appointments JTextField");

    String columnNames[] = { "When", "Appliance", "Repairer" };
    DefaultTableModel appointmentTableModel = new DefaultTableModel(new Object[][] {}, columnNames);

    JTable applianceTable = new JTable(appointmentTableModel);
    JScrollPane scrollPane = new JScrollPane(applianceTable);

    JButton backButton = new JButton("Back");
    JButton newAppointmentButton = new JButton("New Appointment");

    Appointment[] listAppointments;

    private void addTableRow(Appointment appointment) throws SQLException, FileNotFoundException {
        String dateAsString = Appointment.dateToString(appointment.getDate());
        String applianceType = appointment.getAppliance().getType();
        String repairer_username = appointment.getRepairer().getUsername();

        // JButton applianceInfoButton = new JButton("blabalb");

        appointmentTableModel.addRow(new Object[] { dateAsString, applianceType, repairer_username }); // do i need to
        // setVisible?

    }

    public AppointmentList(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;

        // setup frame

        frame.setSize(600, 600); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();

        // setup table
        // need to iterate thru all

        Queue<Appointment> allAppointments = user.getAllAppointments(); // TODO: add all appointments loading

        Appointment curApp;

        while (!allAppointments.isEmpty()) { // DONE added list all functionaliy. does this work?
            curApp = allAppointments.remove(); // retrieves and removes tail of queue
            addTableRow(curApp); // pre-defined method that adds to the table one by one
        }
        applianceTable.setDefaultEditor(Object.class, null);

        // applianceTable.addMouseListener(new MouseAdapter() {
        // public void mousePressed(MouseEvent doubleClick) {
        // Point point = doubleClick.getPoint();
        // int row = applianceTable.rowAtPoint(point);
        // if (doubleClick.getClickCount() == 2 && applianceTable.getSelectedRow() !=
        // -1) {
        // // code here
        // try {
        // new AppointmentInfo(...); // TODO: appointment info
        // } catch (NumberFormatException | FileNotFoundException | SQLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // }
        // }
        // });

        // addTableRow(new Appliance(1)); // todo need to add functionality to list all
        // appliances of user

        // appointmentsText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(appointmentsText, constraints);

        // scrollPane
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(scrollPane, constraints);

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

        // newAppointmentButton
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        newAppointmentButton.addActionListener(this);

        frame.add(newAppointmentButton, constraints);

        frame.setVisible(true);

        appointmentsText.setText("<HTML><H1><B><U>" + user.getUsername() + "\'s appointments</U></B></H1></HTML>");
    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand()); // debugging

        if (actionCommand.equals("New Appointment")) {

            try {
                new AppointmentBooking(this.currentUser);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            frame.setVisible(false);
            frame.dispose();
        } else if (actionCommand.equals("Back")) {
            new Landing(this.currentUser);
            frame.setVisible(false);
            frame.dispose();
        }
    }

}
