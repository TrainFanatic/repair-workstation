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

public class ApplianceList implements ActionListener {
    User currentUser;
    JFrame frame = new JFrame();

    JLabel applianceText = new JLabel("This is the appliance JTextField");

    String columnNames[] = { "ID", "Appliance", "Status" };
    DefaultTableModel applianceTableModel = new DefaultTableModel(new Object[][] {}, columnNames);

    JTable applianceTable = new JTable(applianceTableModel);
    JScrollPane scrollPane = new JScrollPane(applianceTable);

    JLabel IDInputLabel = new JLabel("ID:");
    JTextField IDInputTextField = new JTextField();

    JButton backButton = new JButton("Back");
    JButton newApplianceButton = new JButton("New Appliance");

    int[] listIDs;
    String[] listApplianceType;
    int[] listStatus;

    private void addTableRow(Appliance appliance) throws SQLException {
        int ID = appliance.getApplianceID();
        String applianceType = appliance.getType();
        int status = appliance.getStatus();

        // JButton applianceInfoButton = new JButton("blabalb");

        applianceTableModel.addRow(new Object[] { ID, applianceType, status }); // do i need to
                                                                                // setVisible?

    }

    public ApplianceList(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;

        // setup frame

        frame.setSize(600, 600); // TODO: calibrate
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();

        // setup table
        // need to iterate thru all

        Queue allAppliances = user.getAllAppliances(); // loads all appliances in database into the Queue

        Appliance curApp;

        while (!allAppliances.isEmpty()) { // DONE added list all functionality. does this work?
            curApp = (Appliance) allAppliances.remove(); // retrieves and removes tail of queue
            addTableRow(curApp); // pre-defined method that adds to the table one by one
        }
        applianceTable.setDefaultEditor(Object.class, null);

        applianceTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent doubleClick) {
                Point point = doubleClick.getPoint();
                int row = applianceTable.rowAtPoint(point);
                if (doubleClick.getClickCount() == 2 && applianceTable.getSelectedRow() != -1) {
                    // code here
                    try {
                        new ApplianceInfo(
                                new Appliance((int) applianceTable.getValueAt(row, 0)),
                                currentUser);
                    } catch (NumberFormatException | FileNotFoundException | SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });

        // addTableRow(new Appliance(1)); // todo need to add functionality to list all
        // appliances of user

        // applianceText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(applianceText, constraints);

        // scrollPane
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(scrollPane, constraints);

        // IDInputLabel
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(IDInputLabel, constraints);

        // IDInputTextField
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.8;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(IDInputTextField, constraints);

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

        // newApplianceButton
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        newApplianceButton.addActionListener(this);

        frame.add(newApplianceButton, constraints);

        frame.setVisible(true);

        applianceText.setText("<HTML><H1><B><U>" + user.getUsername() + "\'s appliances</U></B></H1></HTML>");
    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand()); // debugging

        if (actionCommand.equals("New Appliance")) {

            try {
                newAppliance na = new newAppliance(currentUser);

            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            frame.setVisible(false);
            frame.dispose();
        } else if (actionCommand.equals("Back")) {
            Landing l = new Landing(this.currentUser);
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
