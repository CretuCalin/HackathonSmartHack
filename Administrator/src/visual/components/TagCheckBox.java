package visual.components;

import javax.swing.JCheckBox;
import model.Tag;

public class TagCheckBox extends JCheckBox {
    private final Tag tag;

    public TagCheckBox(Tag tag) {
        this.tag = tag;
        super.setText(tag.getName());
    }

    public Tag getTag() {
        return tag;
    }
}
