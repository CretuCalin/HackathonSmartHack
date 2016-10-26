package visual;

import javax.swing.JFrame;
import model.UserAccount;
import remote.RemoteProcs;
import visual.components.AddUserPanel;

public class EditUserFrame extends JFrame {

    private final UserAccount user;
    private final RemoteProcs connection;

    private final AddUserPanel userPanel;
    
    public EditUserFrame(UserAccount user, RemoteProcs connection) {
        this.user = user;
        this.connection = connection;

        super.setTitle("Edit: " + user.getName());
        this.userPanel = new AddUserPanel(connection, user);
        super.add(userPanel);
        
        super.pack();
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
