package com.biz.model.Hmodel;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * TRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_user_role")

public class TRoleUser implements java.io.Serializable {


    // Fields
    private String id;
    private String user_id;
    private String role_id;


    // Constructors
    /** default constructor */
    public TRoleUser() {
    }


    /** full constructor */
    public TRoleUser(String id, String user_id, String role_id) {
        this.id = id;
        this.user_id = user_id;
        this.role_id = role_id;
    }



    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")
    @Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="user_id")
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Column(name="role_id")
    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }


}