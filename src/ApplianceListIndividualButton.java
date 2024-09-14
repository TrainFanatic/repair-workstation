import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

class JTableButtonRenderer implements TableCellRenderer {
    private TableCellRenderer defaultRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultRenderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value instanceof Component)
            return (Component) value;
        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

class JTableButtonModel extends AbstractTableModel {
    private Object[][] rows = { { "Button1", new JButton("Button1") }, { "Button2", new JButton("Button2") },
            { "Button3", new JButton("Button3") }, { "Button4", new JButton("Button4") } };
    private String[] columns = { "Count", "Buttons" };

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return rows.length;
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Object getValueAt(int row, int column) {
        return rows[row][column];
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}

public class ApplianceListIndividualButton {
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

    public ApplianceListIndividualButton(User user) throws FileNotFoundException, SQLException {
        this.currentUser = user;

        // setup frame

        // frame.setSize(400, 400); // todo calibrate

        frame.setTitle("JTableButton Test");
        TableCellRenderer tableRenderer;
        JTable table = new JTable(new JTableButtonModel());
        tableRenderer = table.getDefaultRenderer(JButton.class);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 300);
        frame.setVisible(true);

        applianceText.setText("<HTML><H1><B><U>" + user.getUsername() + "\'s appliances</U></B></H1></HTML>");
    }
}
