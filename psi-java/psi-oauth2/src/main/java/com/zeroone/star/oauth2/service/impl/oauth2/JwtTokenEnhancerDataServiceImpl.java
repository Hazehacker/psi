package com.zeroone.star.oauth2.service.impl.oauth2;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.cloud.oauth2.entity.SecurityUser;
import com.zeroone.cloud.starter.oauth2.service.JwtTokenEnhancerDataService;
import com.zeroone.star.oauth2.entity.Annex;
import com.zeroone.star.oauth2.entity.Frame;
import com.zeroone.star.oauth2.entity.User;
import com.zeroone.star.oauth2.service.IAnnexService;
import com.zeroone.star.oauth2.service.IFrameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 描述：Jwt Token增强数据处理服务实现
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Service
public class JwtTokenEnhancerDataServiceImpl implements JwtTokenEnhancerDataService {
    @Resource
    private IFrameService frameService;
    @Resource
    private IAnnexService annexService;
    @Value("${fastdfs.nginx-servers}")
    private String fastdfsUrl;

    @Override
    public Map<String, Object> enhance(SecurityUser securityUser) {
        // 获取数据库用户DO对象
        User user = (User) securityUser.getExtendsObject();
        // 根据id查到frame
        Frame frame = frameService.getOne(new QueryWrapper<Frame>().lambda()
                .eq(Frame::getId, user.getFrame()));

        Map<String, Object> info = new HashMap<>(1);
        // FIXME: 如果要扩展凭证中的负载数据,需要修改这里的代码
        // 把用户ID设置到JWT中
        info.put("id", user.getId());
        info.put("frameName", frame.getName());
        info.put("frameId", frame.getId());
        if(user.getImg() != null){
            Annex one = annexService.getOne(new QueryWrapper<Annex>().lambda().eq(Annex::getId, user.getImg()));
            if(one != null){
                String savePath = "http://" + fastdfsUrl + "/" +one.getSavePath();
                info.put("avatar", savePath);
            }
        }
        return info;
    }
}
