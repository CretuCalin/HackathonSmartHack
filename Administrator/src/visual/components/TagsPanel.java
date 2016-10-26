package visual.components;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JPanel;
import model.Book;
import model.Tag;
import remote.RemoteProcs;

public class TagsPanel extends JPanel {

    private final RemoteProcs connection;
    private final ArrayList<TagCheckBox> buttons = new ArrayList<>();
    
    public TagsPanel(RemoteProcs connection) {
        this.connection = connection;
        
        this.configTheme();
    }

    private void configTheme() {
        Set<Tag> all = this.connection.findTags();
        
        super.setLayout(new GridLayout(all.size(), 1));
        for (Tag t : all) {
            TagCheckBox b = new TagCheckBox(t);
            this.buttons.add(b);
            super.add(b);
        }
    }
    
    public TreeSet<Tag> selected() {
        TreeSet<Tag> res = new TreeSet<>();
        
        for (TagCheckBox b : this.buttons) {
            if (b.isSelected()) {
                res.add(b.getTag());
            }
        }
        return res;
    }
    
    public void load(Book b) {
        Set<Tag> tgs = this.connection.findTagsByBook(b.getId());
        for (TagCheckBox tb : buttons) {
            if (tgs.contains(tb.getTag())) {
                tb.setSelected(true);
            }
        }
    }
}
