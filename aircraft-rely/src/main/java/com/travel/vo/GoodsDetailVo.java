package com.travel.vo;

import lombok.Data;

/**
 * @author 邱润泽 bullock
 */
@Data
public class GoodsDetailVo {

    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private MiaoShaUserVo user;
}
