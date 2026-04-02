package com.zeroone.star.login.controller;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import cn.hutool.core.util.StrUtil;
import com.zeroone.cloud.oauth2.entity.Oauth2Token;
import com.zeroone.star.login.service.IMenuService;
import com.zeroone.star.login.service.OauthService;
import com.zeroone.star.login.service.SmsService;
import com.zeroone.star.login.service.impl.CaptchaBusinessService;
import com.zeroone.star.project.components.sms.aliyun.SmsResult;
import com.zeroone.star.project.components.jwt.JwtComponent;
import com.zeroone.star.project.components.jwt.exception.JwtExpiredException;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.constant.RedisConstant;
import com.zeroone.star.project.dto.login.LoginDTO;
import com.zeroone.star.project.dto.login.Oauth2TokenDTO;
import com.zeroone.star.project.dto.login.RefreshTokenDTO;
import com.zeroone.star.project.login.LoginApis;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import com.zeroone.star.project.vo.login.LoginVO;
import com.zeroone.star.project.vo.login.MenuTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 描述：登录接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@RestController
@RequestMapping("login")
@Api(tags = "login")
@RefreshScope
public class LoginController implements LoginApis {
    @Resource
    OauthService oAuthService;
    @Resource
    UserHolder userHolder;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Value("${zo.cloud.starter.oauth2.mgr-id}")
    String clientId;
    @Value("${zo.cloud.starter.oauth2.mgr-password}")
    String clientPassword;
    @Value("${zo.captcha.enabled:true}")
    Boolean captchaEnabled;
//    @Resource
//    SmsService smsService;
    @Resource
    private CaptchaBusinessService captchaBusinessService;

    @Autowired
    private JwtComponent jwtComponent;


    @ApiOperation(value = "授权登录")
    @PostMapping("auth-login")
    @Override
    public JsonVO<Oauth2TokenDTO> authLogin(LoginDTO loginDTO) {
            // TODO:未实现验证码验证,注意：接入验证码后需要加一个启动或关闭验证码验证功能的开关，此开关可以在Nacos配置中心中动态的调整
        // 验证码二次校验
        if (captchaEnabled){
            //校验LoginDTO
            if(StrUtil.isBlank(loginDTO.getCode())){
                JsonVO.create(null, ResultStatus.FAIL.getCode(), "验证码不能为空");
            }
            //校验验证码
            ResponseModel responseModel = captchaBusinessService.verification(loginDTO.getCode());
            if (!responseModel.isSuccess()) {
                // TODO: 可能错误码冲突
                return JsonVO.create(null,Integer.parseInt(responseModel.getRepCode()),responseModel.getRepMsg());
            }
              //验证码缓存删除 源码已经实现 详情见BlockPuzzleCaptchaServiceImpl
              //二次校验也是同理详情见BlockPuzzleCaptchaServiceImpl和DefaultCaptchaServiceImpl
//            redisTemplate.delete(RedisConstant.RUNNING_CAPTCHA + )
        }
        // 账号密码认证
        Map<String, String> params = new HashMap<>(5);
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("client_secret", clientPassword);
        params.put("username", loginDTO.getUsername());
        params.put("password", loginDTO.getPassword());
        Oauth2Token oauth2Token = oAuthService.postAccessToken(params);

        // 认证失败
        if (oauth2Token.getErrorMsg() != null) {
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), oauth2Token.getErrorMsg());
        }

        // TODO:未实现认证成功后如何实现注销凭证（如记录凭证到内存数据库）
        Oauth2TokenDTO tokenDTO = BeanUtil.toBean(oauth2Token, Oauth2TokenDTO.class);
        //缓存token到白名单
        redisTemplate.opsForValue().set(
                RedisConstant.LOGOUT_TOKEN_PREFIX + tokenDTO.getToken(),
                RedisConstant.TOKEN_STATUS_ACTIVE,
                oauth2Token.getExpiresIn(),
                TimeUnit.SECONDS
        );

        // 响应认证成功数据
        return JsonVO.success(tokenDTO);
    }

    @ApiOperation(value = "刷新登录")
    @PostMapping("refresh-token")
    @Override
    public JsonVO<Oauth2TokenDTO> refreshToken(RefreshTokenDTO refreshTokenDTO) {
        // TODO:未实现注销凭证验证
        // 注销凭证验证
        try {
            jwtComponent.defaultRsaVerify(refreshTokenDTO.getToken());
        }catch (Exception e){
            if (!(e instanceof JwtExpiredException))
                return JsonVO.create(null, ResultStatus.FAIL.getCode(),e.getMessage());
        }

        // 刷新凭证
        Map<String, String> params = new HashMap<>(4);
        params.put("grant_type", "refresh_token");
        params.put("client_id", clientId);
        params.put("client_secret", clientPassword);
        params.put("refresh_token", refreshTokenDTO.getRefreshToken());
        Oauth2Token oauth2Token = oAuthService.postAccessToken(params);
        // 刷新失败
        if (oauth2Token.getErrorMsg() != null) {
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), oauth2Token.getErrorMsg());
        }

        Oauth2TokenDTO tokenDTO = BeanUtil.toBean(oauth2Token, Oauth2TokenDTO.class);
        // TODO:未实现刷新成功后如何刷新注销凭证（如删除与更新内存数据库）
        // 刷新注销凭证（如删除与更新内存数据库）,更新accessToken
        redisTemplate.opsForValue().getOperations()
                .delete(RedisConstant.LOGOUT_TOKEN_PREFIX+refreshTokenDTO.getToken());
        redisTemplate.opsForValue().set(
                RedisConstant.LOGOUT_TOKEN_PREFIX+oauth2Token.getToken()
                ,RedisConstant.TOKEN_STATUS_ACTIVE,
                oauth2Token.getExpiresIn(), TimeUnit.SECONDS);

        // 响应刷新成功数据
        return JsonVO.success(tokenDTO);
    }
    @ApiOperation(value = "获取当前用户")
    @GetMapping("current-user")
    @Override
    public JsonVO<LoginVO> getCurrUser() {
        UserDTO currentUser;
        try {
            currentUser = userHolder.getCurrentUser();
        } catch (Exception e) {
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), e.getMessage());
        }
        if (currentUser == null) {
            return JsonVO.fail(null);
        } else {
            // TODO:这里需要根据业务需求，重新实现
            LoginVO vo = new LoginVO();
            BeanUtil.copyProperties(currentUser, vo);
            return JsonVO.success(vo);
        }
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("logout")
    @Override
    public JsonVO<String> logout() {
        // 获取当前请求的token
        String token = userHolder.getCurrentToken();
        if (token != null){
            // TODO:登出逻辑，需要配合登录逻辑实现
            // 构造 Redis 中存储 token 的 key
            String redisKey = RedisConstant.LOGOUT_TOKEN_PREFIX + token;
            // 删除 Redis 中的 token 记录
            redisTemplate.delete(redisKey);
            return JsonVO.success("退出成功");
        }
        return JsonVO.fail("获取凭证失败，退出失败");
    }

    @Resource
    IMenuService menuService;

    @ApiOperation(value = "获取菜单")
    @GetMapping("get-menus")
    @Override
    public JsonVO<List<MenuTreeVO>> getMenus() throws Exception {
        // 获取当前用户拥有的菜单
        List<MenuTreeVO> menus = menuService.listMenuByRoleName();
        return JsonVO.success(menus);
    }

//    @ApiOperation(value = "发送短信")
//    @PostMapping("/send")
//    @Override
//    public JsonVO<String> sendMessage(String phone) {
//        SmsResult smsResult = smsService.sendMessage(phone);
//        if ("OK".equalsIgnoreCase(smsResult.getCode())) {
//            return JsonVO.success(smsResult.toString());
//        }
//        return JsonVO.fail(smsResult.toString());
//
//    }



//    /**
//     * 验证码二次校验 获取验证码和一次校验验证码使用默认依赖包中的CaptchaController
//     * @param code
//     * @return
//     */
//    @PostMapping("/captcha/second-check")
//    public JsonVO<String> get(String code) {
//        ResponseModel responseModel = captchaBusinessService.verification(code);
//        if(!responseModel.isSuccess()) {
//            return JsonVO.fail(responseModel.getRepMsg());
//        }
//        return JsonVO.success(responseModel.getRepMsg());
//    }

}
