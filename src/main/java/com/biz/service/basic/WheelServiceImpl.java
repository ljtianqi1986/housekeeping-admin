package com.biz.service.basic;


import com.biz.model.Hmodel.basic.TwxWheel;
import com.biz.model.Hmodel.basic.TwxWheelMain;
import com.biz.model.Hmodel.basic.TwxWheelRecord;
import com.biz.model.Pmodel.basic.PwxWheel;
import com.biz.model.Pmodel.basic.PwxWheelMain;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("wheelService")
public class WheelServiceImpl extends BaseServiceImpl<TwxWheelMain> implements WheelServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<TwxWheelMain> wheelMainDao;
    @Autowired
    private BaseDaoI<TwxWheel> wheelDao;
    @Autowired
    private BaseDaoI<TwxWheelRecord> recordDao;

    @Override
    public Paging findWheelRecordGrid(Params sqlParams) throws Exception {
        return dao.findForPaging("WheelMapper.findWheelRecordPage",sqlParams,"WheelMapper.countWheelRecord",sqlParams);
    }

    @Override
    public Paging findWheelRecordForLevelGrid(Params sqlParams) throws Exception {
        String qishu= (String) dao.findForObject("WheelMapper.getNowQishu",null);
        sqlParams.getParm().put("qishu",qishu);
        return  dao.findForPaging("WheelMapper.findWheelRecordPage",sqlParams,"WheelMapper.countWheelRecord",sqlParams);
    }

    @Override
    public void updateGetGoods(String id) {
        TwxWheelRecord record=recordDao.getById(TwxWheelRecord.class,id);
        record.setState(1);
        recordDao.update(record);
    }

    @Override
    public Map<String, Object> getWheelInfo() throws Exception {
        PwxWheelMain main= (PwxWheelMain) dao.findForObject("WheelMapper.getWheelInfo",null);
        List<PwxWheel> list= (List<PwxWheel>) dao.findForList("WheelMapper.getWheelList",null);
        Map<String,Object> res=new HashMap<>();
        res.put("main",main);
        res.put("list",list);
        return res;
    }

    @Override
    public Map<String,Object> getFreightSetting()throws Exception{
        Map<String,Object> res=(Map<String,Object>) dao.findForObject("WheelMapper.getFreightSetting",null);
        return res;
    }
    @Override
    public void updateMain(String id, String state, String qishu) {
        TwxWheelMain main=wheelMainDao.getById(TwxWheelMain.class,id);
        if("0".equals(state))
        {
            main.setState(0);
        }else
        {
            main.setState(1);
            main.setQishu(qishu);
        }
        wheelMainDao.update(main);
    }

    @Override
    public void updateFreight(Map<String,Object> map)throws Exception{
        dao.update("WheelMapper.updateFreight",map);
    }
    @Override
    public void saveWheel(PwxWheel pwxWheel) throws Exception {
        TwxWheelMain main=wheelMainDao.getById(TwxWheelMain.class,pwxWheel.getMainId());
        if(main!=null)
        {
            wheelMainDao.delete(main);
        }
        dao.delete("WheelMapper.clearWheel",null);
        //以上先清空大转盘数据
        //保存main表
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat qssdf = new SimpleDateFormat("yyyyMMddHHmmss");
        TwxWheelMain wheelMain=new TwxWheelMain();
        wheelMain.setQishu(qssdf.format(new Date()));
        wheelMain.setState(1);
        wheelMain.setName("大转盘活动");
        wheelMain.setStartTime(sdf.parse(pwxWheel.getStartTime()));
        wheelMain.setEndTime(sdf.parse(pwxWheel.getEndTime()));
        wheelMainDao.save(wheelMain);
        saveDetails(pwxWheel);
    }



    private void saveDetails(PwxWheel pwxWheel) {
        String[] names=pwxWheel.getName().split(",");
        String[] nums=pwxWheel.getNum().split(",");
        String[] types=pwxWheel.getType().split(",");
        String[] num90s=pwxWheel.getNum90().split(",");
        String[] percents=pwxWheel.getPercent().split(",");
        String[] imgPath=pwxWheel.getImgPath().split(",");
        for(int i=0;i<8;i++)
        {
            TwxWheel wheel=new TwxWheel();
            wheel.setName(names[i]);
            wheel.setNum(Integer.valueOf(nums[i]));
            wheel.setType(Integer.valueOf(types[i]));
            wheel.setNum90(Double.valueOf(num90s[i]));
            wheel.setPercent(Double.valueOf(percents[i]));
            wheel.setImgPath(imgPath[i]);
            wheel.setLevel(i+1);
            wheelDao.save(wheel);
        }
    }
}
