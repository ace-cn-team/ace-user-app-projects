package ace.user.app.logic.api.service;


import ace.fw.model.response.GenericResponseExt;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByUserNameResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByMobileResponse;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/18 10:07
 * @description
 */
public interface IdentityLogicService {
    /**
     * 根据验证码-手机号码注册
     *
     * @param request
     * @return
     */
    GenericResponseExt<RegisterByMobileResponse> registerByMobile(@Valid RegisterByMobileRequest request);

    /**
     * 根据账号注册
     *
     * @param request
     * @return
     */
    GenericResponseExt<RegisterByUserNameResponse> registerByUserName(@Valid RegisterByUserNameRequest request);

    /**
     * 根据手机账号登陆
     *
     * @param request
     * @return
     */
    GenericResponseExt<LoginByMobileResponse> loginByMobile(@Valid LoginByMobileRequest request);

    /**
     * 根据账号登陆
     *
     * @param request
     * @return
     */
    GenericResponseExt<LoginByUserNameResponse> loginByUserName(@Valid LoginByUserNameRequest request);

    /**
     * 获取当前账号个人信息
     *
     * @param request
     * @return
     */
    GenericResponseExt<GetCurrentUserResponse> getCurrentUser(@Valid GetCurrentUserRequest request);

    /**
     * 登出
     *
     * @param request
     */
    GenericResponseExt<Boolean> logout(@Valid LogoutRequest request);
}
