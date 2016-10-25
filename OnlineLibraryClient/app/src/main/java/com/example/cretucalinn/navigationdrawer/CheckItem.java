package com.example.cretucalinn.navigationdrawer;

import model.Tag;



public class CheckItem
{

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    String name = null;
    boolean selected = false;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    Tag tag;

    public CheckItem(String name, boolean selected, Tag tag)
    {
        this.tag = tag;
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }
}