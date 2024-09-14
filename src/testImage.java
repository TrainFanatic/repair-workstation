import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class testImage {
    public testImage() throws FileNotFoundException, SQLException {

        Appliance hairdryer = new Appliance(1);

        BufferedImage img = hairdryer.getImage();

        JFrame frame = new JFrame();

        frame.setSize(500, 500);

        JLabel picLabel = new JLabel(new ImageIcon(img));

        frame.add(picLabel);

        frame.setVisible(true);
    }
}
