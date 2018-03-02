package com.framework.security;

/*************************************************************************
 * 版本：         V1.0
 *
 * 文件名称 ：MyShiro.java 描述说明 ：
 *
 * 创建信息 : create by 刘佳佳 on 2016-6-24 下午4:45:33  修订信息 : modify by ( ) on (date) for ( )
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

import com.biz.model.Pmodel.User;
import com.biz.service.basic.UserServiceI;
import com.framework.utils.MD5;
import com.framework.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyShiro extends AuthorizingRealm {

	@Resource(name = "userService")
	private UserServiceI userService;

    @Autowired
    private SessionDAO sessionDAO;
    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       String username=(String)principalCollection.fromRealm(getName()).iterator().next();
        if (username!=null){
            this.getSession().getAttribute("code").toString();
            User user=userService.findByName(username);
            if (user!=null){
                SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
//                //获取用户角色
//                List<Map> roleList=userService.findRolesByUser(user);
//                List<String> permissionList=new ArrayList<>();
//                if (roleList!=null&&roleList.size()>0) {
//                    for (Map map : roleList) {
//                        if (map != null) {
//                            //设置用户的角色
//                            info.addRole(map.get("roleName").toString());
//                            if (map.get("permission") != null && !StringUtil.isNullOrEmpty(map.get("permission").toString())) {
//                                String[] permissions = map.get("permission").toString().split(",");
//                                for (String permission : permissions) {
//                                    permissionList.add(permission);
//                                }
//                            }
//
//                        }
//                    }
//                }

                /**  使用命名空间前缀的新角色**/
                //获取用户角色
                List<Map> roleList=userService.findNameSpaceRolesByUser(user);
                List<Map> permissionListAll=userService.findPermissionList(user);
                List<String> permissionList=new ArrayList<>();
                if (roleList!=null&&roleList.size()>0) {
                    for (Map map : roleList) {
                        if (map != null) {
                            //设置用户的角色
                            info.addRole(map.get("roleName").toString());
                            info.addRole(map.get("id").toString()); //加入id作为权限判断
                        }
                    }
                }
                //添加permission列表
                if(permissionListAll!=null && permissionListAll.size()>0){
                    for (Map permi : permissionListAll) {
                        if (permi.get("permission") != null && !StringUtil.isNullOrEmpty(permi.get("permission").toString())) {
                            String[] permissions = permi.get("permission").toString().split(",");
                            for (String permission : permissions) {
                                permissionList.add(permission);
                            }
                        }
                    }
                }


                //设置用户权限
                info.addStringPermissions(permissionList);
                return info;
            }
        }
        return null;


	}
	
    /**
     * 登录认证;
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        //使用新用户表需要对密码进行md5加密，否则shiro验证无法通过
        if(token.getPassword()!=null){
            char[] c_old = token.getPassword();
            String s_old = String.valueOf(c_old);
            String s_new = MD5.md5(s_old);
            char[] c_new=s_new.toCharArray();
            token.setPassword(c_new);
        }

        System.out.println(token.getCredentials());
        //查出是否有此用户
        User user=userService.findByName(token.getUsername());
//        String loginName=token.getUsername();
//        Session currentSession = null;
//        Collection<Session> sessions = sessionDAO.getActiveSessions();
//        //apache shiro获取所有在线用户
//        for(Session session:sessions){
//            System.out.println("登录ip:"+session.getHost());
//            System.out.println("登录用户"+session.getAttribute(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY))));
//            System.out.println("最后操作日期:"+session.getLastAccessTime());
//
//        }
//        for(Session session:sessions){
//            //清除该用户以前登录时保存的session
//            if(loginName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
//                sessionDAO.delete(session);
//                //session.setTimeout(0);//设置session立即失效，即将其踢出系统
//                //break;
//            }
//        }
        if(user!=null){
            System.out.print(this.getSession().getAttribute("code"));
            //session 放置全局变量
            this.setSession("currentUser", "刘佳佳");
            //若存在，将此用户存放到登录认证info中
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
        }else{
            throw new AuthenticationException("msg:用户不存在。");
        }

	}

    /**
     * 保存登录名
     */
    private void setSession(Object key, Object value){
        Session session = getSession();
        System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
        if(null != session){
            session.setAttribute(key, value);
        }
    }

    /**
     * 从Subject 中获取Session
     * @return
     */
    private Session getSession(){
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session != null){
                return session;
            }else if(session==null){
                session = subject.getSession();
            }
        }catch (InvalidSessionException e){

        }
        return null;
    }

}
