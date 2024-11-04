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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.io.FileNotFoundException;

public class NominateRepairer implements ActionListener {
    User currentUser;
    User displayedUser;
    JFrame frame = new JFrame();

    JLabel appointmentsText = new JLabel("<HTML><H1><B><U>List of all (non-repairer) clients</U></B></H1></HTML>");

    String columnNames[] = { "Username" };
    DefaultTableModel userTableModel = new DefaultTableModel(new Object[][] {}, columnNames);

    JTable applianceTable = new JTable(userTableModel);
    JScrollPane scrollPane = new JScrollPane(applianceTable);

    JButton backButton = new JButton("Back");

    JButton refreshButton = new JButton("Refresh");

    Appointment[] listAppointments;

    private void addTableRow(User user) throws SQLException, FileNotFoundException {
        String username = user.getUsername();

        // JButton applianceInfoButton = new JButton("blabalb");

        userTableModel.addRow(new Object[] { username }); // do
                                                          // i
                                                          // need
                                                          // to
        // setVisible?

    }

    private void updateUserList() throws FileNotFoundException, SQLException {
        userTableModel.setRowCount(0);

        Queue<User> allUsers = User.getAllUsersWithPermissionLevel(User.PERMISSION_CLIENT);

        User curUser;

        while (!allUsers.isEmpty()) { // DONE added list all functionaliy. does this work?
            curUser = allUsers.remove(); // retrieves and removes tail of queue
            addTableRow(curUser); // pre-defined method that adds to the table one by one
        }

    }

    public NominateRepairer(User user) throws FileNotFoundException, SQLException {
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
                int column = applianceTable.columnAtPoint(point);
                String username = (String) applianceTable.getValueAt(row, 0);
                if (doubleClick.getClickCount() == 2 && applianceTable.getSelectedRow() != -1) {
                    // code here

                    try {
                        JFrame areYouSureFrame = new JFrame();
                        areYouSureFrame.setLocationRelativeTo(null);
                        areYouSureFrame.setLayout(new GridBagLayout());
                        areYouSureFrame.setSize(200, 200);

                        JLabel areYouSureLabel = new JLabel(
                                "<HTML><B>Are you sure you wish to promote this user?</B></HTML>");
                        constraints.gridx = 0;
                        constraints.gridy = 0;
                        constraints.gridwidth = 2;
                        areYouSureFrame.add(areYouSureLabel, constraints);

                        JButton noButton = new JButton("No");
                        constraints.gridwidth = 1;
                        constraints.gridy = 1;
                        noButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // go back
                                areYouSureFrame.setVisible(false);
                                areYouSureFrame.dispose();
                            }
                        });
                        areYouSureFrame.add(noButton, constraints);
                        JButton yesButton = new JButton("Yes");
                        constraints.gridx = 1;
                        yesButton.setForeground(Color.red);
                        yesButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // go back
                                areYouSureFrame.setVisible(false);
                                areYouSureFrame.dispose();
                                // assign
                                User userToBePromoted = new User(username);
                                try {
                                    userToBePromoted.setPermission(User.PERMISSION_REPAIRER);

                                } catch (FileNotFoundException | SQLException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                            }
                        });
                        areYouSureFrame.add(yesButton, constraints);

                        areYouSureFrame.setVisible(true);

                    } catch (NumberFormatException e) {

                        e.printStackTrace();
                    }

                }
            }
        });

        updateUserList();

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

        // refreshButton
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        refreshButton.addActionListener(this);

        frame.add(refreshButton, constraints);

        frame.setVisible(true);

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
        } else if (actionCommand.equals("Refresh")) {
            try {
                updateUserList();
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
