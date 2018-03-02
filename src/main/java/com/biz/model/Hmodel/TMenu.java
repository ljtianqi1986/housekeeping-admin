package com.biz.model.Hmodel;
// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * TMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_menu"
)

public class TMenu  implements java.io.Serializable {


    // Fields    

     private String id;
     private String pid;
     private String text;
     private String href;
     private String classIcon;
     private Short state;
     private Date createTime=new Date();
     private String nameSpace;

    // Constructors

    /** default constructor */
    public TMenu() {
    }

    public TMenu(String id, String pid, String text, String href, String classIcon, Short state, Date createTime, String nameSpace) {
        this.id = id;
        this.pid = pid;
        this.text = text;
        this.href = href;
        this.classIcon = classIcon;
        this.state = state;
        this.createTime = createTime;
        this.nameSpace = nameSpace;
    }


   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    @Column(name="id", unique=true, nullable=false, length=40)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="pid", length=40)

    public String getPid() {
        return this.pid;
    }
    
    public void setPid(String pid) {
        this.pid = pid;
    }
    
    @Column(name="text")

    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    @Column(name="href")

    public String getHref() {
        return this.href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
    
    @Column(name="classIcon")

    public String getClassIcon() {
        return this.classIcon;
    }
    
    public void setClassIcon(String classIcon) {
        this.classIcon = classIcon;
    }
    
    @Column(name="state")

    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
    
    @Column(name="createTime", length=19)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="nameSpace", length=19)
    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}