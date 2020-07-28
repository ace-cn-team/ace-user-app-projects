package ace.user.app.logic.api.service;


import ace.fw.model.response.GenericResponseExt;
import ace.user.app.logic.define.module.identity.login.request.LoginByUserNameRequest;
import ace.user.app.logic.define.module.identity.login.request.LoginByMobileRequest;
import ace.user.app.logic.define.module.identity.register.request.RegisterByUserNameRequest;
import ace.user.app.logic.define.module.identity.register.request.RegisterByMobileRequest;
import ace.user.app.logic.define.module.identity.login.response.LoginByUserNameResponse;
import ace.user.app.logic.define.module.identity.login.response.LoginByMobileResponse;
import ace.user.app.logic.define.module.identity.register.response.RegisterByUserNameResponse;
import ace.user.app.logic.define.module.identity.register.response.RegisterByMobileResponse;

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
    GenericResponseExt<RegisterByUserNameResponse> registerByAccount(@Valid RegisterByUserNameRequest request);

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
    GenericResponseExt<LoginByUserNameResponse> loginByAccount(@Valid LoginByUserNameRequest request);


}
