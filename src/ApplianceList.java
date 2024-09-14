import java.sql.SQLException;

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
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener { // from
                                                                        // https://forums.oracle.com/ords/apexds/post/jbutton-inside-jtable-cell-7953
    JTable table;
    JButton renderButton;
    JButton editButton;
    String text;

    public ButtonColumn(JTable table, int column) {
        super();
        this.table = table;
        renderButton = new JButton();

        editButton = new JButton();
        editButton.setFocusPainted(false);

        ActionListener clickedButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ApplianceInfo applianceToBeOpened = new ApplianceInfo(new Appliance(1),
                            new User("techie410", User.PERMISSION_CLIENT));
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }; // ok

        editButton.addActionListener(clickedButton);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton.setText((value == null) ? "" : value.toString());
        return renderButton;
    }

    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        text = (value == null) ? "" : value.toString();
        editButton.setText(text);
        return editButton;
    }

    public Object getCellEditorValue() {
        return text;
    }

    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        System.out.println(e.getActionCommand() + " : " + table.getSelectedRow());
    }
}

public class ApplianceList {
    User currentUser;

    JTextField applianceText = new JTextField("This is the appliance JTextField");

    String columnNames[] = { "ID", "Appliance", "Status", "Details" };
    DefaultTableModel applianceTableModel = new DefaultTableModel(new Object[][] {}, columnNames); // this
                                                                                                   // is
                                                                                                   // super
                                                                                                   // jank
                                                                                                   // and I
                                                                                                   // need
                                                                                                   // to
                                                                                                   // figure
                                                                                                   // out
                                                                                                   // the
                                                                                                   // proper
                                                                                                   // way
                                                                                                   // to do
                                                                                                   // this.

    JTable applianceTable = new JTable(applianceTableModel);
    JScrollPane scrollPane = new JScrollPane(applianceTable);

    JFrame frame = new JFrame();

    int[] listIDs;
    String[] listApplianceType;
    int[] listStatus;

    private void addTableRow(Appliance appliance) throws SQLException {
        int ID = appliance.getApplianceID();
        String applianceType = appliance.getType();
        int status = appliance.getStatus();

        JButton applianceInfoButton = new JButton("blabalb");

        applianceTableModel.addRow(new Object[] { ID, applianceType, status, "Info" }); // do i need to
                                                                                        // setVisible?

    }

    public ApplianceList(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;

        // setup frame

        frame.setSize(400, 400); // todo calibrate

        // setup table
        addTableRow(new Appliance(1)); // does this work????
        ButtonColumn bcolumn = new ButtonColumn(applianceTable, 3);

        frame.add(scrollPane);

        frame.setVisible(true);

        applianceText.setText("<HTML><H1><B><U>" + user.getUsername() + "\'s appliances</U></B></H1></HTML>");
    }
}
