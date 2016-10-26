package visual.components;

import application.Application;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import model.UserAccount;
import remote.RemoteProcs;
import visual.EditUserFrame;

public class SearchUsersPanel extends JPanel {

    private final RemoteProcs connection;
    private JTextField search = new JTextField();
    private JButton searchButton = new JButton();
    private DefaultListModel<UserAccount> model = new DefaultListModel<>();
    private JList<UserAccount> users = new JList<>(this.model);
    private JScrollPane pane = new JScrollPane(this.users);
    private JPopupMenu popup = new JPopupMenu();
    private JMenuItem edit = new JMenuItem("Manage");
    private JMenuItem delete = new JMenuItem("Delete");
    
    public SearchUsersPanel(RemoteProcs connection) {
        super(new BorderLayout());
        this.connection = connection;

        this.configTheme();

        this.searchButton.addActionListener((e) -> {
            this.search();
        });
        this.users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(users, e.getX(), e.getY());
                }
            }
        });
        this.edit.addActionListener((e) -> {
            if (users.getSelectedIndex() == -1) {
                return;
            }
            
            UserAccount a = users.getSelectedValue();
            new EditUserFrame(a, connection).setVisible(true);
        });
        this.delete.addActionListener((e) -> {
            if (users.getSelectedIndex() == -1) {
                return;
            }
            
            UserAccount a = users.getSelectedValue();
            
            int r = JOptionPane.showConfirmDialog(null, "Delete " + a.getName() + "?", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r == JOptionPane.NO_OPTION) {
                return;
            }
            
            this.connection.deleteUserAccount(a.getId());
            this.search();
        });
    }

    private void configTheme() {
        JPanel north = new JPanel(new BorderLayout());
        north.add(this.search);
        north.add(this.searchButton, BorderLayout.EAST);

        super.add(north, BorderLayout.NORTH);

        super.add(this.pane);
        
        this.popup.add(this.edit);
        this.popup.add(new JSeparator());
        this.popup.add(this.delete);
        
        try {
            BufferedImage im = ImageIO.read(Application.class.getResource("/resources/search.png"));
            Image img = im.getScaledInstance(20, 20, BufferedImage.SCALE_SMOOTH);
            this.searchButton.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
            Logger.getLogger(SearchUsersPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void search() {
        String query = this.search.getText();

        Set<UserAccount> result = this.connection.searchUser(query);

        this.model.clear();
        result.forEach((r) -> this.model.addElement(r));
    }
}
