package com.biz.model.Pmodel.sys;

import java.util.Date;
import java.util.List;


/*************************************************************************
 * 版本：         V1.0
 *
 * 文件名称 ：Prole.java 描述说明 ：
 *
 * 创建信息 : create by 杨帆 on 2017-01-09 上午09:27:04  修订信息 : modify by ( ) on (date) for ( )
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class Prole {
    private String id;
    private String roleName;
    private String roleGroup;
    private String roleMark;
    private String iconCls;
    private Integer isdel = 0;
    private Date createTime = new Date();


    private String role_id;
    private String menu_id;
    private String menu_id_sql;
    private String user_id;

    private String userName;
    private String text;
    private String path;
    private String parent;
    private String permission;
    private String permission_nameSpace;

    private String menu_name;
    private String nameSpace;
    private String href;
    private String add;
    private String del;
    private String update;
    private String query;

    private List<Prole> roleButtonList;


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Prole> getRoleButtonList() {
        return roleButtonList;
    }

    public void setRoleButtonList(List<Prole> roleButtonList) {
        this.roleButtonList = roleButtonList;
    }

    public String getPermission_nameSpace() {
        return permission_nameSpace;
    }

    public void setPermission_nameSpace(String permission_nameSpace) {
        this.permission_nameSpace = permission_nameSpace;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMenu_id_sql() {
        return menu_id_sql;
    }

    public void setMenu_id_sql(String menu_id_sql) {
        this.menu_id_sql = menu_id_sql;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public String getRoleMark() {
        return roleMark;
    }

    public void setRoleMark(String roleMark) {
        this.roleMark = roleMark;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}