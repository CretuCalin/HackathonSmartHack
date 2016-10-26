package visual.components;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AddImagePanel extends JPanel {

    private JLabel image = new JLabel();
    private JButton select = new JButton("Select image");
    private File imageFile;

    public AddImagePanel() {
        super(new BorderLayout());

        super.add(this.image);
        super.add(this.select, BorderLayout.SOUTH);
        this.image.setHorizontalAlignment(JLabel.CENTER);

        this.select.addActionListener((a) -> {
            this.select();
        });
        super.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
    }

    private void select() {
        JFileChooser fc = new JFileChooser(".");
        int c = fc.showOpenDialog(null);
        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        this.imageFile = fc.getSelectedFile();

        this.resize();
    }

    public void resize() {
        try {
            if (this.imageFile == null) {
                return;
            }

            BufferedImage im = ImageIO.read(imageFile);
            int w = im.getWidth();
            int h = im.getHeight();
            double scv = (double) 150 / (double) h;
            Image img = im.getScaledInstance((int) (scv * w), (int) (scv * h), BufferedImage.SCALE_SMOOTH);
            this.image.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
            Logger.getLogger(AddImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void load(byte[] bs) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bs);
            BufferedImage im = ImageIO.read(is);
            int w = im.getWidth();
            int h = im.getHeight();
            double scv = (double) 150 / (double) h;
            Image img = im.getScaledInstance((int) (scv * w), (int) (scv * h), BufferedImage.SCALE_SMOOTH);
            this.image.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
            Logger.getLogger(AddImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File getImageFile() {
        return imageFile;
    }
}
