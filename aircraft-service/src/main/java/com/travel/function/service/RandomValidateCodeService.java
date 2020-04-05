package com.travel.function.service;

import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.MiaoshaKey;
import com.travel.function.entity.MiaoShaUser;
import com.travel.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author 邱润泽 bullock
 */
@Slf4j
@Service
public class RandomValidateCodeService {

    //随机产生只有数字的字符串 private String
    private String randString = "0123456789";
    //private String randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生只有字母的字符串
    //private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生数字与字母组合的字符串
    // 图片宽
    private int width = 95;
    // 图片高
    private int height = 25;
    // 干扰线数量
    private int lineSize = 40;
    // 随机产生字符数量
    private int stringNum = 4;

    @Autowired
    private RedisClient redisClient;


    private Random random = new Random();

    /**
     * 获得字体
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    /**
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public BufferedImage getRandcode(MiaoShaUser user, long goodsId) {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        // 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        Graphics g = image.getGraphics();
        //图片大小
        g.fillRect(0, 0, width, height);
        //字体大小
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        //字体颜色
        g.setColor(getRandColor(110, 133));
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drowString(g, randomString, i);
        }
        log.info(randomString);
        g.dispose();
        redisClient.set(MiaoshaKey.getMiaoshaVerifyCode, user.getNickname() + ":" + goodsId, randomString);
        return image;
    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString
                .length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 16);
        return randomString;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机的字符
     */
    public String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }

    public boolean checkVerifyCode(MiaoShaUserVo user, long goodsId, String verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        String codeOld = (String) redisClient.get(MiaoshaKey.getMiaoshaVerifyCode,
                user.getNickname() + ":" + goodsId, String.class);
        //验证验证码 是否 正确
        if(StringUtils.isEmpty(codeOld) || !codeOld.equals(verifyCode)){
            return false;
        }
        redisClient.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getNickname() + "," + goodsId);
        return true;
    }
}
