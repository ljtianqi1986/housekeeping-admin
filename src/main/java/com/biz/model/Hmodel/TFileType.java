package com.biz.model.Hmodel;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * TFileType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_file_type")

public class TFileType  implements java.io.Serializable {


    // Fields    

     private String id;
     private Short fileType;
     private String typeName;
     private String fileIcon;
     private String filePath;
     private Short state;


    // Constructors

    /** default constructor */
    public TFileType() {
    }

    
    /** full constructor */
    public TFileType(Short fileType, String typeName, String fileIcon, String filePath, Short state) {
        this.fileType = fileType;
        this.typeName = typeName;
        this.fileIcon = fileIcon;
        this.filePath = filePath;
        this.state = state;
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
    
    @Column(name="fileType")

    public Short getFileType() {
        return this.fileType;
    }
    
    public void setFileType(Short fileType) {
        this.fileType = fileType;
    }
    
    @Column(name="typeName")

    public String getTypeName() {
        return this.typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    @Column(name="fileIcon")

    public String getFileIcon() {
        return this.fileIcon;
    }
    
    public void setFileIcon(String fileIcon) {
        this.fileIcon = fileIcon;
    }
    
    @Column(name="filePath")

    public String getFilePath() {
        return this.filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    @Column(name="state")

    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
   








}