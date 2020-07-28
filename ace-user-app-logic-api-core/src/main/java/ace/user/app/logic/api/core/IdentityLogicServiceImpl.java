package ace.user.app.logic.api.core;


import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.api.core.biz.identity.register.RegisterByUserNameBiz;
import ace.user.app.logic.api.core.biz.identity.register.RegisterByMobileBiz;
import ace.user.app.logic.api.service.IdentityLogicService;
import ace.user.app.logic.define.module.identity.login.request.LoginByUserNameRequest;
import ace.user.app.logic.define.module.identity.login.request.LoginByMobileRequest;
import ace.user.app.logic.define.module.identity.register.request.RegisterByUserNameRequest;
import ace.user.app.logic.define.module.identity.register.request.RegisterByMobileRequest;
import ace.user.app.logic.define.module.identity.login.response.LoginByUserNameResponse;
import ace.user.app.logic.define.module.identity.login.response.LoginByMobileResponse;
import ace.user.app.logic.define.module.identity.register.response.RegisterByUserNameResponse;
import ace.user.app.logic.define.module.identity.register.response.RegisterByMobileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/18 10:07
 * @description
 */
@Component
public class IdentityLogicServiceImpl implements IdentityLogicService {

    @Autowired
    private RegisterByMobileBiz registerByMobileBiz;
    @Autowired
    private RegisterByUserNameBiz registerByUserNameBiz;

    @Override
    public GenericResponseExt<RegisterByMobileResponse> registerByMobile(@Valid RegisterByMobileRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(registerByMobileBiz.register(request));
    }

    @Override
    public GenericResponseExt<RegisterByUserNameResponse> registerByAccount(@Valid RegisterByUserNameRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(registerByUserNameBiz.register(request));
    }

    @Override
    public GenericResponseExt<LoginByMobileResponse> loginByMobile(@Valid LoginByMobileRequest request) {
        return null;
    }

    @Override
    public GenericResponseExt<LoginByUserNameResponse> loginByAccount(@Valid LoginByUserNameRequest request) {
        return null;
    }
}
