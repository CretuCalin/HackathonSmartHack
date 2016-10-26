package visual;

import visual.components.AddUserPanel;
import visual.components.AddBookPanel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import model.Admin;
import remote.RemoteProcs;
import visual.components.SearchBooksPanel;
import visual.components.BorrowsPanel;
import visual.components.SearchUsersPanel;

public class MainFrame extends JFrame {

    private JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private AddUserPanel addUserPanel;
    private AddBookPanel addBookPanelOld;
    private SearchUsersPanel searchUser;
    private SearchBooksPanel searchBook;
    private BorrowsPanel borrows;

    private final RemoteProcs connection;
    private final Admin admin;

    public MainFrame(RemoteProcs connection, Admin admin) {
        this.connection = connection;
        this.admin = admin;

        this.addUserPanel = new AddUserPanel(connection);
        this.addBookPanelOld = new AddBookPanel(connection);
        this.searchUser = new SearchUsersPanel(connection);
        this.searchBook = new SearchBooksPanel(connection);
        this.borrows = new BorrowsPanel(connection);
        
        this.configTheme();
    }

    private void configTheme() {
        super.setTitle("Administrator: " + this.admin.getUsername());
        super.add(this.pane);

        this.pane.add(this.searchUser, "Manage existent users");
        this.pane.add(this.searchBook, "Manage existent books");
        this.pane.add(this.addUserPanel, "Add an user to the library");
        this.pane.add(this.addBookPanelOld, "Add a book to the library");
        this.pane.add(this.borrows, "See borrows");
        
        super.pack();
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
