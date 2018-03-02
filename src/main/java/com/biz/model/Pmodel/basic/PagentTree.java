package com.biz.model.Pmodel.basic;
// default package

/**
 * TUser entity. @author MyEclipse Persistence Tools
 */

public class PagentTree implements java.io.Serializable {
    private String id;
    private String parent;
    private String text;
    private String icon;
    private TreeState state=new TreeState();


    private String isselected;




    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIsselected() {
        return isselected;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public TreeState getState() {
        return state;
    }

    public void setState(TreeState state) {
        this.state = state;
    }
}