package com.biz.model.Pmodel.basic;

public class UserList
{
	private Integer total =0;//关注该公众账号的总用户数
    private Integer count=0;//拉取的OPENID个数，最大值为10000
    private UserOpenid data;//列表数据，OPENID的列表
    private String  next_openid;//拉取列表的最后一个用户的OPENID

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserOpenid getData() {
        return data;
    }

    public void setData(UserOpenid data) {
        this.data = data;
    }

    public String getNext_openid() {
        return next_openid;
    }

    public void setNext_openid(String next_openid) {
        this.next_openid = next_openid;
    }
}
