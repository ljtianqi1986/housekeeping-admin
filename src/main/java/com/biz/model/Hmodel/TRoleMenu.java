package com.biz.model.Hmodel;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_sys_role_menu")

public class TRoleMenu implements java.io.Serializable {


    // Fields
    private String id;
    private String role_id;
    private String menu_id;
    private String permission;
    private String permission_nameSpace;

    // Constructors
    /** default constructor */
    public TRoleMenu() {
    }


    public TRoleMenu(String id, String role_id, String menu_id, String permission, String permission_nameSpace) {
        this.id = id;
        this.role_id = role_id;
        this.menu_id = menu_id;
        this.permission = permission;
        this.permission_nameSpace = permission_nameSpace;
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

    @Column(name="role_id")
    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    @Column(name="menu_id")
    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    @Column(name="permission")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Column(name="permission_nameSpace")
    public String getPermission_nameSpace() {
        return permission_nameSpace;
    }

    public void setPermission_nameSpace(String permission_nameSpace) {
        this.permission_nameSpace = permission_nameSpace;
    }
}