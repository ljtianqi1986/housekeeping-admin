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
 * TFileLabel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_file_label")

public class TFileLabel  implements java.io.Serializable {


    // Fields    

     private String id;
     private String labelName;
     private Integer labelSort;
     private Short state;
     private Timestamp createTime;


    // Constructors

    /** default constructor */
    public TFileLabel() {
    }

    
    /** full constructor */
    public TFileLabel(String labelName, Integer labelSort, Short state, Timestamp createTime) {
        this.labelName = labelName;
        this.labelSort = labelSort;
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
    
    @Column(name="labelName")

    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
    
    @Column(name="labelSort")

    public Integer getLabelSort() {
        return this.labelSort;
    }
    
    public void setLabelSort(Integer labelSort) {
        this.labelSort = labelSort;
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