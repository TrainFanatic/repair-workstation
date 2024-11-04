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
    User displayedUser;
    JFrame frame = new JFrame();

    JLabel appointmentsText = new JLabel("This is the appointments JTextField");

    String columnNames[] = { "Appointment ID", "When", "Appliance", "Repairer" };
    DefaultTableModel appointmentTableModel = new DefaultTableModel(new Object[][] {}, columnNames);

    JTable applianceTable = new JTable(appointmentTableModel);
    JScrollPane scrollPane = new JScrollPane(applianceTable);

    JLabel UserInputLabel = new JLabel("Load another user's:");
    JTextField UserInputTextField = new JTextField();

    JButton loadUserButton = new JButton("Load User's Appointments");

    JButton backButton = new JButton("Back");
    JButton newAppointmentButton = new JButton("New Appointment");
    JButton refreshButton = new JButton("Refresh");

    Appointment[] listAppointments;

    private void addTableRow(Appointment appointment) throws SQLException, FileNotFoundException {
        int appointmentID = appointment.getAppointmentID();
        String dateAsString = Appointment.dateToString(appointment.getDate());
        String applianceType = appointment.getAppliance().getType();
        String repairer_username = appointment.getRepairer().getUsername();

        // JButton applianceInfoButton = new JButton("blabalb");

        appointmentTableModel.addRow(new Object[] { appointmentID, dateAsString, applianceType, repairer_username }); // do
                                                                                                                      // i
                                                                                                                      // need
                                                                                                                      // to
        // setVisible?

    }

    private void updateAppointmentListAccordingToUser(User u) throws FileNotFoundException, SQLException {
        appointmentTableModel.setRowCount(0);

        Queue<Appointment> allAppointments = u.getAllAppointments();

        Appointment curApp;

        while (!allAppointments.isEmpty()) { // DONE added list all functionaliy. does this work?
            curApp = allAppointments.remove(); // retrieves and removes tail of queue
            addTableRow(curApp); // pre-defined method that adds to the table one by one
        }
        appointmentsText.setText("<HTML><H1><B><U>" + u.getUsername() + "\'s appointments</U></B></H1></HTML>");
    }

    public AppointmentList(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;
        this.displayedUser = user;

        // setup frame

        frame.setSize(600, 600); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();

        // setup table
        // need to iterate thru all

        applianceTable.setDefaultEditor(Object.class, null);

        applianceTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent doubleClick) {
                Point point = doubleClick.getPoint();
                int row = applianceTable.rowAtPoint(point);
                if (doubleClick.getClickCount() == 2 && applianceTable.getSelectedRow() != -1) {
                    // code here

                    try {
                        new AppointmentInfo(new Appointment((int) applianceTable.getValueAt(row, 0)), currentUser);

                    } catch (NumberFormatException | FileNotFoundException | SQLException e) {

                        e.printStackTrace();
                    }

                }
            }
        });

        updateAppointmentListAccordingToUser(currentUser);

        // appointmentsText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(appointmentsText, constraints);

        // scrollPane
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(scrollPane, constraints);

        // UserInputLabel
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        if (currentUser.getPermission() >= User.PERMISSION_REPAIRER) {
            frame.add(UserInputLabel, constraints);
        }

        // UserInputTextField
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 0.8;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        if (currentUser.getPermission() >= User.PERMISSION_REPAIRER) {
            frame.add(UserInputTextField, constraints);
        }

        // loadUserButton
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        loadUserButton.addActionListener(this);
        if (currentUser.getPermission() >= User.PERMISSION_REPAIRER) {
            frame.add(loadUserButton, constraints);
        }

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        // refreshButton
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        refreshButton.addActionListener(this);

        frame.add(refreshButton, constraints);

        // newAppointmentButton
        constraints.gridx = 2;
        constraints.gridy = 4;
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
            try {
                new Landing(this.currentUser);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            frame.setVisible(false);
            frame.dispose();
        } else if (actionCommand.equals("Load User's Appointments")) {
            String inputtedUsername = UserInputTextField.getText();

            try {
                if (User.usernameExists(inputtedUsername)) {
                    displayedUser = new User(inputtedUsername);
                    appointmentsText.setText("<HTML><H1><B><U>Loading. Please wait...</U></B></H1></HTML>");

                    updateAppointmentListAccordingToUser(displayedUser);
                } else {
                    appointmentsText.setText("<HTML><H1><I>Username does not exist.</H1></I></HTML>");
                }
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (actionCommand.equals("Refresh")) {
            try {
                updateAppointmentListAccordingToUser(currentUser);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
