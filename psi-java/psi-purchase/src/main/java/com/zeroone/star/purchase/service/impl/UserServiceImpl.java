package com.zeroone.star.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.purchase.DO.UserDO;
import com.zeroone.star.purchase.mapper.UserMapper;
import com.zeroone.star.purchase.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 斗气化码
 * @since 2025-11-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

}
