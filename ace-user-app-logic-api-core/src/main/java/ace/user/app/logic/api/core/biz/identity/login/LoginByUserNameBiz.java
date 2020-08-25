package ace.user.app.logic.api.core.biz.identity.login;

import ace.authentication.base.api.AccountBaseApi;

import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.enums.LoginTypeEnum;
import ace.authentication.base.define.model.request.FindByAppIdAndUserNameRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 用户名登录核心逻辑
 */
@Component
@Slf4j
public class LoginByUserNameBiz extends AbstractLoginCoreBiz<LoginByUserNameRequest, LoginByUserNameResponse> {

    @Autowired
    private AccountBaseApi accountBaseApi;

    @Override
    protected LoginByUserNameResponse newLoginResponse() {
        return new LoginByUserNameResponse();
    }

    @Override
    protected LoginTypeEnum getLoginTypeEnum() {
        return LoginTypeEnum.USER_NAME;
    }

    @Override
    protected Account findAccount(LoginByUserNameRequest request) {
        Account account = accountBaseApi.findByAppIdAndUserName(
                FindByAppIdAndUserNameRequest.builder()
                        .appId(request.getAppId())
                        .userName(request.getUserName())
                        .build()
        ).check();
        return account;
    }
}
