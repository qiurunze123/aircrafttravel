package com.travel.function.logic;

import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 邱润泽 bullock
 */
@Service
public interface MiaoShaLogic {

    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods);

    public int insertMiaoshaOrder(MiaoShaOrder miaoshaOrder);

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

    // check 生成的验证码路径
    public boolean checkPath(MiaoShaUser user, long goodsId, String path);


    public List<MiaoShaOrder> listByGoodsId(Long goodsId);

    public MiaoShaUser getById(Long id);

    public HttpServletResponse addCookie(HttpServletResponse response, String token, MiaoShaUser user);

    Long insertSelective(OrderInfo record);

    public OrderInfo getOrderById(Long orderId);

    public MiaoShaUser getByToken(HttpServletResponse response , String token);

}
