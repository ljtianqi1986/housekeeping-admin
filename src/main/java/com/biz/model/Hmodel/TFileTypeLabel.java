package com.biz.model.Hmodel;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * TFileTypeLabel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_file_type_label")

public class TFileTypeLabel  implements java.io.Serializable {


    // Fields    

     private String id;
     private String fileTypeId;
     private String fileLabelId;
     private String fileId;
     private Short state;
     private Timestamp createTime;


    // Constructors

    /** default constructor */
    public TFileTypeLabel() {
    }

    
    /** full constructor */
    public TFileTypeLabel(String fileTypeId, String fileLabelId, String fileId, Short state, Timestamp createTime) {
        this.fileTypeId = fileTypeId;
        this.fileLabelId = fileLabelId;
        this.fileId = fileId;
        this.state = state;
        this.createTime = createTime;
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
    
    @Column(name="fileTypeId", length=40)

    public String getFileTypeId() {
        return this.fileTypeId;
    }
    
    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }
    
    @Column(name="fileLabelId", length=40)

    public String getFileLabelId() {
        return this.fileLabelId;
    }
    
    public void setFileLabelId(String fileLabelId) {
        this.fileLabelId = fileLabelId;
    }
    
    @Column(name="fileId", length=40)

    public String getFileId() {
        return this.fileId;
    }
    
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    @Column(name="state")

    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
    
    @Column(name="createTime", length=19)

    public Timestamp getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
   








}