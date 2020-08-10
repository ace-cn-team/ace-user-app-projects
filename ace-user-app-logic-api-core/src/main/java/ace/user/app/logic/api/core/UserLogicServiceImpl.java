package ace.user.app.logic.api.core;

import ace.fw.logic.common.aop.Interceptor.handler.annotations.ThrowableHandlerAspect;
import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.api.core.biz.user.ModifyUserInfoBiz;
import ace.user.app.logic.api.service.UserLogicService;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.user.ModifyUserInfoRequest;
import ace.user.app.logic.define.model.response.identity.register.RegisterByMobileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/4 17:12
 * @description 用户接口逻辑
 */
@LogAspect
@ThrowableHandlerAspect
@Slf4j
@Component
public class UserLogicServiceImpl implements UserLogicService {
    @Autowired
    private ModifyUserInfoBiz modifyUserInfoBiz;

    @Override
    public GenericResponseExt<Boolean> modifyUserInfo(@Valid ModifyUserInfoRequest request) {
        modifyUserInfoBiz.modifyUserInfo(request);
        return GenericResponseExtUtils.buildSuccessWithData(true);
    }
}
