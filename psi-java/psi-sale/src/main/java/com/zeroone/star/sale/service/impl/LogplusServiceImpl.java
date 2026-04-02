package com.zeroone.star.sale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.sale.entity.Log;
import com.zeroone.star.sale.mapper.LogplusMapper;
import com.zeroone.star.sale.service.LogplusService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogplusServiceImpl extends ServiceImpl<LogplusMapper,Log> implements LogplusService {
    @Override
    public void saveLog(String info) {
        UserHolder userHolder=new UserHolder();
        String userId;
        try {
            UserDTO currentUser = userHolder.getCurrentUser();
            userId = currentUser.getId();//设置用户id
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Log log=new Log();
        log.setUser(userId);
        log.setInfo(info);
        log.setTime(LocalDateTime.now());
        save(log);
    }
}
