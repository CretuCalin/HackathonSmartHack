package visual;

import javax.swing.JFrame;
import model.Book;
import remote.RemoteProcs;
import visual.components.AddBookPanel;
import visual.components.AddUserPanel;

public class EditBookFrame extends JFrame{
    
    private final Book book;
    private final RemoteProcs connection;

    private final AddBookPanel bookPanel;
    
    public EditBookFrame(Book book, RemoteProcs connection) {
        this.book = book;
        this.connection = connection;

        super.setTitle("Edit: " + book.getTitle());
        this.bookPanel = new AddBookPanel(connection, book);
        super.add(bookPanel);
        
        super.pack();
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
