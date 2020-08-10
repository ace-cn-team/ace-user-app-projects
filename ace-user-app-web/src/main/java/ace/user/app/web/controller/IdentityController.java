package ace.user.app.web.controller;

import ace.fw.model.response.GenericResponseExt;
import ace.user.app.logic.api.service.IdentityLogicService;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByMobileResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByUserNameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/10 13:43
 * @description
 */
@RestController
@Validated
public class IdentityController {

    public final static String REGISTER_BY_MOBILE_URL = "/user/identity/register/mobile";
    @Autowired
    private IdentityLogicService identityLogicService;

    /**
     * 根据验证码-手机号码注册
     *
     * @param request
     * @return
     */
    @PostMapping(REGISTER_BY_MOBILE_URL)
    public GenericResponseExt<RegisterByMobileResponse> registerByMobile(@Valid @RequestBody RegisterByMobileRequest request) {
        return identityLogicService.registerByMobile(request);
    }

    /**
     * 根据账号注册
     *
     * @param request
     * @return
     */
    @PostMapping("/user/identity/register/username")
    public GenericResponseExt<RegisterByUserNameResponse> registerByUserName(@Valid @RequestBody RegisterByUserNameRequest request) {
        return identityLogicService.registerByUserName(request);
    }

    /**
     * 根据手机账号登陆
     *
     * @param request
     * @return
     */
    @PostMapping("/user/identity/login/mobile")
    public GenericResponseExt<LoginByMobileResponse> loginByMobile(@Valid @RequestBody LoginByMobileRequest request) {
        return identityLogicService.loginByMobile(request);
    }

    /**
     * 根据账号登陆
     *
     * @param request
     * @return
     */
    @PostMapping("/user/identity/login/username")
    public GenericResponseExt<LoginByUserNameResponse> loginByUserName(@Valid @RequestBody LoginByUserNameRequest request) {
        return identityLogicService.loginByUserName(request);
    }

    /**
     * 获取当前账号个人信息
     *
     * @param request
     * @return
     */
    @PostMapping("/user/identity/current")
    public GenericResponseExt<GetCurrentUserResponse> getCurrentUser(@Valid @RequestBody GetCurrentUserRequest request) {
        return identityLogicService.getCurrentUser(request);
    }

    /**
     * 登出
     *
     * @param request
     */
    @PostMapping("/user/identity/logout")
    public GenericResponseExt<Boolean> logout(@Valid @RequestBody LogoutRequest request) {
        return identityLogicService.logout(request);
    }
}
