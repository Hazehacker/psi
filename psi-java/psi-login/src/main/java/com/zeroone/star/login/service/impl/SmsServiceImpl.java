package com.zeroone.star.login.service.impl;

import com.zeroone.star.login.service.SmsService;
import com.zeroone.star.project.components.sms.aliyun.SmsComponent;
import com.zeroone.star.project.components.sms.aliyun.SmsResult;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 描述：验证码相关服务
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author Altolia
 * @version 1.0.0
 */
@Component
public class SmsServiceImpl implements SmsService {

    @Resource
    SmsComponent sms;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final String SMS_REDIS_KEY_PREFIX = "sms:login:";

    private static final long SMS_EXPIRE_MINUTES = 5;

    /**
     * 发送验证码 调试时要修改阿里云配置
     * @param phone
     * @return
     */
    public SmsResult sendMessage(String phone) {

        HashMap<String, String> params = new HashMap<>();
        String code = new Random().nextInt(999999) + "";
        params.put("code", code);

        SmsResult smsResult = sms.sendSms(phone, "阿里云短信测试", "SMS_154950909", params);
        //发送成功存入redis
        if ("OK".equalsIgnoreCase(smsResult.getCode())) {
            String redisKey = SMS_REDIS_KEY_PREFIX + phone;
            redisTemplate.opsForValue()
                    .set(redisKey,phone,SMS_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }
        return smsResult;
    }

    /**
     * 验证短信验证码
     * @param phone
     * @param code
     * @return
     */
    public boolean verifyLoginCode(String phone, String code) {
        if (phone == null || code == null) {
            return false;
        }

        String redisKey = SMS_REDIS_KEY_PREFIX + phone;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        // 验证码匹配且不为空
        if (storedCode != null && storedCode.equals(code)) {
            // 验证成功后删除Redis中的验证码
            //redisTemplate.delete(redisKey);
            return true;
        }

        return false;
    }


}
