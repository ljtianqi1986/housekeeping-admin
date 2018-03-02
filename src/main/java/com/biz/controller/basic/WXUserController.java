package com.biz.controller.basic;

import com.biz.model.Pmodel.basic.UserList;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.basic.WXUserInfoServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.UuidUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/wxUserSynchronization")
public class WXUserController extends BaseController
{

    @Resource(name = "wxUserInfoService")
    private WXUserInfoServiceI wxUserInfoService;

	/**
	 * 同步微信所有用户
	 */
	@RequestMapping(value = "wx_user_synchronization")
	public void wx_user_synchronization(HttpServletResponse response) throws Exception {
        boolean pd_bool=false;
       //try{

        //清空列表
        wxUserInfoService.deleteUserList();

        String next_openid=null;
        boolean pd=true;
        int i=0;
        UserList userlist=new UserList();
        int add_count=0;//记录已经拉取了多少数量的用户
        do {
            //获取微信用户列表
            userlist= wxUserInfoService.UserListOpenid(next_openid);

            if(userlist !=null){
                //保存
                if(userlist.getData()!=null && userlist.getData().getOpenid().size()>0){
                    wxUserInfoService.adduserList(userlist.getData().getOpenid(), userlist.getCount());
                }
                add_count+=userlist.getCount();
                if(add_count>=userlist.getTotal()){
                    pd=false;//判断是否最后一批数据
                }
                next_openid=userlist.getNext_openid();//最后一个拉取的openid

                //防止死循环
                i++;
                if(i>100){
                    pd=false;
                }
            }


        }while(pd);


        //查询所有未匹配上的用户
        List<UserInfo> userisnull_List= wxUserInfoService.getUserIsNull();

        List<UserInfo> add_user=new ArrayList<UserInfo>();
        int ii=0;
        for(UserInfo tmpUser:userisnull_List){
            //获取这些用户的信息
            UserInfo temp = wxUserInfoService.getUserInfo(tmpUser.getOpenid());
            temp.setId(UuidUtil.get32UUID());
            add_user.add(temp);
            ii++;
            if(ii==200){
                break;
            }
        }
        //保存用户信息
        if(add_user.size()>0){
            wxUserInfoService.adduserInfoList(add_user);
        }
        pd_bool=true;
     /*   }catch (Exception e) {

        }*/
        writeJson(pd_bool, response);
	}



	

}
