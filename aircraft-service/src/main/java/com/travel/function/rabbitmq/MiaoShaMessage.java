package com.travel.function.rabbitmq;


import com.travel.function.entity.MiaoShaUser;
import lombok.Data;

@Data
public class MiaoShaMessage {
    private MiaoShaUser user;
    private long goodsId;
}
