package com.travel.function.service.impl;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.MD5Util;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.OrderInfoDao;
import com.travel.function.entity.OrderInfo;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.keysbean.MiaoshaKey;
import com.travel.service.OrderInfoService;
import com.travel.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private MiaoShaLogic mSLogic ;

    @Override
    public ResultGeekQ<Integer> insertSelective(OrderInfoVo record) {

        ResultGeekQ<Integer> resultGeekQ = ResultGeekQ.build();
        try{

            OrderInfo info = new OrderInfo();
            BeanUtils.copyProperties(record,info);
            int result = mSLogic.insertSelective(info);
            if(result<=0){
                log.error("***insertSelective*** fail");
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
                return resultGeekQ;
            }
            resultGeekQ.setData(result);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***查询失败insertSelective *** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }

}
