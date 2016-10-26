package visual.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Book;
import model.Tag;
import remote.RemoteProcs;

public class AddBookPanel extends JPanel {

    private final RemoteProcs connection;
    private AddImagePanel imagePan = new AddImagePanel();
    private TagsPanel tags;
    private JTextField title = new JTextField();
    private JTextField author = new JTextField();
    private JTextArea desc = new JTextArea(15, 50);
    private JScrollPane pane = new JScrollPane(this.desc);
    private JButton done = new JButton("Add book");

    private Book book;

    public AddBookPanel(RemoteProcs connection) {
        super(new BorderLayout());
        this.connection = connection;
        this.tags = new TagsPanel(connection);

        this.configTheme();

        this.done.addActionListener((e) -> {
            this.done();
        });
    }

    public AddBookPanel(RemoteProcs connection, Book book) {
        this(connection);
        this.book = book;

        this.title.setText(book.getTitle());
        this.author.setText(book.getAuthor());
        this.desc.setText(book.getDescription());
        this.imagePan.load(book.getImage());
        this.tags.load(book);
        
        this.done.setText("Edit book");
    }

    private void done() {
        String t = this.title.getText();
        String a = this.author.getText();
        String d = this.desc.getText();
        File img = this.imagePan.getImageFile();

        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Required field is missing!", "Error!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (a.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Required field is missing!", "Error!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (d.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Required field is missing!", "Error!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (img == null && this.book == null) {
            JOptionPane.showMessageDialog(null, "An image is required!", "Error!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (this.book == null && img.length() > 0xffff) {
            JOptionPane.showMessageDialog(null, "The image is to big!", "Error!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (this.book == null) {
            try (FileInputStream in = new FileInputStream(img)) {
                byte[] imgbs = new byte[in.available()];
                in.read(imgbs);

                Book b = new Book(t, a, d, imgbs);
                int bId = this.connection.createBook(b);
                Set<Tag> sel = this.tags.selected();
                this.connection.tagBook(bId, sel);
                JOptionPane.showMessageDialog(null, "Book created!", "Error!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to load image!", "Error!", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            this.book.setTitle(t);
            this.book.setAuthor(a);
            this.book.setDescription(d);

            if (img != null) {
                try (FileInputStream in = new FileInputStream(img)) {
                    byte[] imgbs = new byte[in.available()];
                    in.read(imgbs);
                    this.book.setImage(imgbs);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to load image!", "Error!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            this.connection.editBook(book);
            Set<Tag> sel = this.tags.selected();
            this.connection.tagBook(this.book.getId(), sel);
            
            JOptionPane.showMessageDialog(null, "Book edited!", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void configTheme() {
        JPanel north = new JPanel(new GridLayout(2, 1));
        JPanel f = new JPanel(new BorderLayout());
        f.add(new JLabel("Title:"), BorderLayout.WEST);
        f.add(this.title);
        north.add(f);
        f = new JPanel(new BorderLayout());
        f.add(new JLabel("Author:"), BorderLayout.WEST);
        f.add(this.author);
        north.add(f);

        super.add(north, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(this.imagePan);

        f = new JPanel(new BorderLayout());
        f.add(new JLabel("Tags", JLabel.CENTER), BorderLayout.NORTH);
        f.add(this.tags);

        center.add(f);
        center.add(this.imagePan);

        super.add(center);

        f = new JPanel(new BorderLayout());
        f.add(new JLabel("Description", JLabel.CENTER), BorderLayout.NORTH);
        f.add(this.pane);
        f.add(this.done, BorderLayout.SOUTH);
        super.add(f, BorderLayout.SOUTH);

        this.desc.setWrapStyleWord(true);
        this.desc.setLineWrap(true);
    }
}
