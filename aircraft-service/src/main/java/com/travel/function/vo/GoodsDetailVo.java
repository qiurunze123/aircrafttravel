package com.travel.function.vo;

import com.travel.function.entity.MiaoShaUser;
import lombok.Data;

/**
 * @author 邱润泽 bullock
 */
@Data
public class GoodsDetailVo {

    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private MiaoShaUser user;
}
