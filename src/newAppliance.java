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

public class newAppliance implements ActionListener {

    static final String EMPTY_FILEPATH = "this is an EMPTY filepath";
    JLabel newApplianceText = new JLabel("<HTML><H1><U>New Appliance</U></H1></HTML");

    SQLRequest request = new SQLRequest();

    JLabel applianceIDText = new JLabel("Appliance ID");
    JTextField applianceIDField = new JTextField();

    JLabel ownerText = new JLabel("Owner");
    JTextField ownerField = new JTextField();

    JLabel applianceTypeText = new JLabel("Appliance Type");
    JTextField applianceTypeField = new JTextField();

    JLabel notesText = new JLabel("Notes");
    JTextArea notesArea = new JTextArea();

    JLabel imageText = new JLabel("Image");
    JButton imageFileChooserButton = new JButton("Select file");
    String selectedPath = ApplianceEditor.EMPTY_FILEPATH;

    JButton submitButton = new JButton("Submit");
    JButton backButton = new JButton("Back");

    JFrame frame = new JFrame();

    Appliance toBeEditedAppliance;
    boolean isNewAppliance = false;
    User currentUser;

    public newAppliance(User currentUser) // new Appliance
            throws FileNotFoundException, SQLException {
        this.currentUser = currentUser;
        SQLRequest sqlr = new SQLRequest();
        this.toBeEditedAppliance = new Appliance(sqlr.nextApplianceID(), currentUser.getUsername(), "", "", 0, "",
                false);

        isNewAppliance = true;
        setUpFrame();

    }

    public void setUpFrame() throws SQLException {
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        // ownerText
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // applianceTypeText.setVerticalAlignment(SwingConstants.CENTER);

        frame.add(ownerText, constraints);
        // ownerField
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        ownerField.setText(currentUser.getUsername());

        ownerField.setEditable(false);
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(ownerField, constraints);

        // newApplianceText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(newApplianceText, constraints);

        // applianceTypeText
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // applianceTypeText.setVerticalAlignment(SwingConstants.CENTER);

        frame.add(applianceTypeText, constraints);

        // applianceTypeField
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;

        frame.add(applianceTypeField, constraints);

        // notesText
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;

        frame.add(notesText, constraints);

        // notesArea
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(notesArea, constraints);

        // imageText
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 10;

        frame.add(imageText, constraints);

        // imageFileChooserButton
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        imageFileChooserButton.addActionListener(this);

        frame.add(imageFileChooserButton, constraints);

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;

        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        // submitButton
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        submitButton.addActionListener(this);

        frame.add(submitButton, constraints);

        frame.setVisible(true);
    }

    public void selectFile() {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG Image Files", "jpg", "jpeg");

        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        j.setFileFilter(filter);
        // invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog(null);

        // if the user selects a file
        if (r == JFileChooser.APPROVE_OPTION)

        {
            // set the label to the path of the selected file
            selectedPath = j.getSelectedFile().getAbsolutePath();
        }
        // if the user cancelled the operation
        else {
            selectedPath = ApplianceEditor.EMPTY_FILEPATH;
        }
    }

    public void submitAppliancePhoto() {
        // update image
        System.out.println(selectedPath);
        if (selectedPath == ApplianceEditor.EMPTY_FILEPATH) {
            // reject
            // System.out.println("rejected filepath!");
            submitButton.setText("Select valid JPEG file!");
        } else {
            File imageToBeUploaded = new File(selectedPath);
            try {
                byte[] fileContent = Files.readAllBytes(imageToBeUploaded.toPath());
                // System.out.println(fileContent.length);

                request.UpdateAppliancesImageObject(fileContent, toBeEditedAppliance.getApplianceID());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void updateCredentials() {
        toBeEditedAppliance.setType(applianceTypeField.getText());
        toBeEditedAppliance.setNote(notesArea.getText());
        submitAppliancePhoto();
    }

    public void createNewEntries() {
        System.out.println("Trying to create new database entries for ID " + toBeEditedAppliance.getApplianceID());
        try {
            SQLRequest sqlr = new SQLRequest();
            sqlr.SQLUpdate("INSERT INTO appliances VALUES(" + toBeEditedAppliance.getApplianceID()
                    + ", \'" + currentUser.getUsername() + "\', \'\', NULL, 0, \'note\');"); // appliances table

            sqlr.SQLUpdate("INSERT INTO appliances_photos VALUES(" + toBeEditedAppliance.getApplianceID()
                    + ", NULL);"); // appliances_photos table
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand()); // debugging

        if (actionCommand.equals("Select file")) {

            selectFile();

        } else if (actionCommand.equals("Submit") || actionCommand.equals("Select valid JPEG file!")) {
            if (isNewAppliance) {
                createNewEntries();
                isNewAppliance = false;

            }

            updateCredentials();
            frame.setVisible(false);
            frame.dispose();
            try {
                new ApplianceList(currentUser);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
