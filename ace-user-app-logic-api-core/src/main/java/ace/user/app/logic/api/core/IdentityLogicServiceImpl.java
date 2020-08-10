package ace.user.app.logic.api.core;


import ace.fw.logic.common.aop.Interceptor.handler.annotations.ThrowableHandlerAspect;
import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.api.core.biz.identity.LogoutBiz;
import ace.user.app.logic.api.core.biz.identity.login.LoginByMobileBiz;
import ace.user.app.logic.api.core.biz.identity.login.LoginByUserNameBiz;
import ace.user.app.logic.api.core.biz.identity.register.RegisterByUserNameBiz;
import ace.user.app.logic.api.core.biz.identity.register.RegisterByMobileBiz;
import ace.user.app.logic.api.core.biz.identity.GetCurrentUserBiz;
import ace.user.app.logic.api.service.IdentityLogicService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/18 10:07
 * @description
 */
@ThrowableHandlerAspect
@LogAspect
@Slf4j
@Component
public class IdentityLogicServiceImpl implements IdentityLogicService {

    @Autowired
    private RegisterByMobileBiz registerByMobileBiz;
    @Autowired
    private RegisterByUserNameBiz registerByUserNameBiz;
    @Autowired
    private LoginByMobileBiz loginByMobileBiz;
    @Autowired
    private LoginByUserNameBiz loginByUserNameBiz;
    @Autowired
    private GetCurrentUserBiz getCurrentUserBiz;
    @Autowired
    private LogoutBiz logoutBiz;

    @Override
    public GenericResponseExt<RegisterByMobileResponse> registerByMobile(@Valid RegisterByMobileRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(registerByMobileBiz.register(request));
    }

    @Override
    public GenericResponseExt<RegisterByUserNameResponse> registerByUserName(@Valid RegisterByUserNameRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(registerByUserNameBiz.register(request));
    }

    @Override
    public GenericResponseExt<LoginByMobileResponse> loginByMobile(@Valid LoginByMobileRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(loginByMobileBiz.login(request));
    }

    @Override
    public GenericResponseExt<LoginByUserNameResponse> loginByUserName(@Valid LoginByUserNameRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(loginByUserNameBiz.login(request));
    }

    @Override
    public GenericResponseExt<GetCurrentUserResponse> getCurrentUser(@Valid GetCurrentUserRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(getCurrentUserBiz.getCurrentUser(request));
    }

    @Override
    public GenericResponseExt<Boolean> logout(@Valid LogoutRequest request) {
        logoutBiz.logout(request);
        return GenericResponseExtUtils.buildSuccessWithData(true);
    }
}
