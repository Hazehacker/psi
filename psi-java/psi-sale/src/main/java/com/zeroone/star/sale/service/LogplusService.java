package com.zeroone.star.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.sale.entity.Log;

public interface LogplusService extends IService<Log> {
     void saveLog(String info);
}
