package com.zeroone.star.purchase.service.impl;

import com.zeroone.star.purchase.DO.LogDO;
import com.zeroone.star.purchase.mapper.LogMapper;
import com.zeroone.star.purchase.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements ILogService {

}
