//TODO: need to have all of this functionality imported to newAppliance

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
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

public class ApplianceEditor implements ActionListener {

    static final String EMPTY_FILEPATH = "this is an EMPTY filepath";
    JLabel newApplianceText = new JLabel("<HTML><H1><U>Edit Appliance</U></H1></HTML>");

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

    JLabel locationText = new JLabel("Location");
    JTextField locationField = new JTextField();

    JButton submitButton = new JButton("Submit");
    JButton backButton = new JButton("Back");

    JFrame frame = new JFrame();

    JLabel picLabel = new JLabel();

    Appliance toBeEditedAppliance;
    User user;
    boolean isNewAppliance = false;

    public ApplianceEditor(User user)
            throws FileNotFoundException, SQLException {
        SQLRequest sqlr = new SQLRequest();
        this.toBeEditedAppliance = new Appliance(sqlr.nextApplianceID(), user.getUsername(), "", "", 0, "",
                false);
        this.isNewAppliance = true;
        this.user = user;

        this.newApplianceText.setText("<HTML><H1><U>New Appliance</U></H1></HTML>");

        setUpFrame();

    }

    public ApplianceEditor(Appliance toBeEditedAppliance, User user)
            throws FileNotFoundException, SQLException {
        this.toBeEditedAppliance = toBeEditedAppliance;
        this.user = user;

        setUpFrame();

    }

    public void setUpFrame() throws SQLException, FileNotFoundException {
        frame.setMinimumSize(new Dimension(450, 320));
        frame.setMaximumSize(new Dimension(450, 10000)); // jank
                                                         // fix
                                                         // to
                                                         // get
                                                         // screen
                                                         // height
        frame.setSize(450, 320); // need to replace with method that updates size.
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        // frame.setAlwaysOnTop(true); // spawn on top so that ApplianceList doesn't
        // obscure frame.
        // above line is no longer true

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;

        // newApplianceText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3; // very strange bug where the component is aligned left?

        constraints.fill = GridBagConstraints.BOTH;

        // constraints.weightx = 1;
        // constraints.weighty = 1;
        newApplianceText.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(newApplianceText, constraints);

        // picLabel
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 4;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // applianceTypeText.setVerticalAlignment(SwingConstants.CENTER);

        if (isNewAppliance) {
            picLabel.setIcon(new ImageIcon("src/Dependencies/PlaceholderImageForNewAppliances.png"));
        } else {
            BufferedImage imageRaw = toBeEditedAppliance.getImage();
            Image imageScaled = imageRaw.getScaledInstance(200, 200, 0);
            picLabel.setIcon(new ImageIcon(imageScaled));
        }

        frame.add(picLabel, constraints);

        // ownerText
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;

        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipady = 10;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // applianceTypeText.setVerticalAlignment(SwingConstants.CENTER);

        frame.add(applianceTypeText, constraints);
        // ownerField
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;

        if (!isNewAppliance) {
            ownerField.setText(toBeEditedAppliance.getOwnerString());
        }

        ownerField.setEditable(false);
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        // applianceTypeText
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipady = 10;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // applianceTypeText.setVerticalAlignment(SwingConstants.CENTER);

        frame.add(applianceTypeText, constraints);

        // applianceTypeField
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;
        if (!isNewAppliance) {
            applianceTypeField.setText(toBeEditedAppliance.getType());
        }

        frame.add(applianceTypeField, constraints);

        // notesText
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;

        frame.add(notesText, constraints);

        // notesArea
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        notesArea.setLineWrap(true); // line wrap so that width of frame stays the same?
        // the line wrap is done BEFORE setting the text so that it isn't way too long.
        notesArea.setWrapStyleWord(true);
        if (!isNewAppliance) {
            notesArea.setText(toBeEditedAppliance.getNote());
        }

        frame.add(notesArea, constraints);

        // imageText
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 10;

        frame.add(imageText, constraints);

        // imageFileChooserButton
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipady = 0;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        imageFileChooserButton.addActionListener(this);

        frame.add(imageFileChooserButton, constraints);

        // locationText
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        ;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 10;

        frame.add(locationText, constraints);

        // locationField
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        if (!isNewAppliance) {
            locationField.setText(toBeEditedAppliance.getLocation());
        }

        if (user.getPermission() < User.PERMISSION_REPAIRER) { // only editable if repairer or more
            locationField.setEditable(false);
        }
        frame.add(locationField, constraints);

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        constraints.ipady = 0;

        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        // submitButton
        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.gridwidth = 1;

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
            // set preview image
            ImageIcon imageAtSelectedPath = new ImageIcon(selectedPath);
            Image scaledImage = imageAtSelectedPath.getImage().getScaledInstance(200, 200, 0);
            picLabel.setIcon(new ImageIcon(scaledImage));
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
            // update so we don't need to reject!
            // submitButton.setText("Select valid JPEG file!");
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
        // toBeEditedAppliance.setLocation();
        submitAppliancePhoto();
    }

    public void createNewEntries() {
        System.out.println("Trying to create new database entries for ID " + toBeEditedAppliance.getApplianceID());
        try {
            SQLRequest sqlr = new SQLRequest();
            sqlr.SQLUpdate("INSERT INTO appliances VALUES(" + toBeEditedAppliance.getApplianceID()
                    + ", \'" + user.getUsername() + "\', \'\', \"\", 0, \'note\');"); // appliances table

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

        } else if (actionCommand.equals("Back")) {
            if (!isNewAppliance) {
                try {
                    new ApplianceInfo(toBeEditedAppliance, user);

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    new ApplianceList(user);
                } catch (FileNotFoundException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            frame.setVisible(false);
            frame.dispose();

        } else if (actionCommand.equals("Submit") || actionCommand.equals("Select valid JPEG file!")) {
            if (isNewAppliance) {
                createNewEntries();
                isNewAppliance = false;

            }
            updateCredentials();

            try {
                new ApplianceList(user);
            } catch (FileNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            frame.setVisible(false);
            frame.dispose();

        }
    }
}
