import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ApplianceInfo implements ActionListener {
    User user;
    JLabel infoText = new JLabel("<HTML><H1><B>Info on appliance</B></H1></HTML>", SwingConstants.CENTER);

    JLabel ownerText = new JLabel("<HTML><B>Repairer:</B></HTML>");

    JTextField ownerValue = new JTextField("Bob");

    JLabel statusText = new JLabel("<HTML><B>Status:</B></HTML>");
    JTextField statusValue = new JTextField("Under repair...");

    JLabel notesText = new JLabel("<HTML><B>Notes:</B></HTML>");
    JTextArea notesArea = new JTextArea(
            "“Heating element OK, fan blade + capacitor OK. Toggle switch has no continuity and must be replaced.”");

    JButton backButton = new JButton("Back");

    JLabel picLabel;

    JFrame frame = new JFrame();

    Appliance displayedAppliance;

    public ApplianceInfo(Appliance appliance, User user) throws SQLException {
        displayedAppliance = appliance;

        // re-set the relevant text fields
        ownerValue.setText(appliance.getOwnerString());
        statusValue.setText(Appliance.statusToString(appliance.getStatus()));
        notesArea.setText(appliance.getNote());

        frame.setSize(800, 400);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        // make all text comps non editable
        ownerValue.setEditable(false);
        statusValue.setEditable(false);
        notesArea.setEditable(false);
        // notes must wrap
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;

        // infoText
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(infoText, constraints);

        // add the image label by loading image
        BufferedImage imageRaw = appliance.getImage();
        Image imageScaled = imageRaw.getScaledInstance(250, 250, 0);
        picLabel = new JLabel(new ImageIcon(imageScaled));
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 3;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        // picLabel.setPreferredSize(new Dimension(250, 250)); //no need since
        // imageScaled is now scaled to 250x250

        frame.add(picLabel, constraints);

        // ownerText/Value
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;

        frame.add(ownerText, constraints);
        constraints.gridx = 1;
        frame.add(ownerValue, constraints);

        // statusText/Value
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        // constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        // constraints.weighty = 1;
        frame.add(statusText, constraints);
        constraints.gridx = 1;
        frame.add(statusValue, constraints);

        // notesText/Value
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        // constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        constraints.weighty = 1;
        // notesArea.setText(appliance.getNote());
        frame.add(notesText, constraints);
        constraints.gridx = 1;
        frame.add(notesArea, constraints);

        // backButton
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.weightx = 1;
        constraints.weighty = 0;
        backButton.addActionListener(this);
        frame.add(backButton, constraints);

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        // if the user presses the save button show the save dialog
        // System.out.println(evt.getActionCommand());
        if (actionCommand.equals("Back")) {
            frame.setVisible(false);
            frame.dispose();

        }

    }
}
