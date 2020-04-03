package com.travel.function.logic;

import com.travel.function.entity.MiaoShaGoods;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 邱润泽 bullock
 */
@Service
public interface GoodsLogic {

    List<GoodsVo> goodsVoList();

    GoodsVo goodsVoByGoodsId(Long goodId);

    GoodsVo getGoodsVoByGoodsId(Long goodsId);

    public int reduceStock(MiaoShaGoods miaoShaGoods);
}
