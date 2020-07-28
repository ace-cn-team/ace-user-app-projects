package ace.user.app.logic.api.core.biz.identity.login;

import ace.account.base.api.AccountBaseApi;
import ace.account.base.define.dao.enums.account.AccountBizTypeEnum;
import ace.account.base.define.dao.model.entity.Account;
import ace.account.base.define.enums.LoginTypeEnum;
import ace.account.base.define.model.request.FindByAppIdAndUserNameRequest;
import ace.cas.base.define.model.bo.OAuth2Token;
import ace.user.app.logic.define.module.identity.login.request.LoginByUserNameRequest;
import ace.user.app.logic.define.module.identity.login.response.LoginByUserNameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description
 */
@Component
@Slf4j
public class LoginByUserNameBiz extends AbstractLoginCoreBiz<LoginByUserNameRequest, LoginByUserNameResponse> {

    @Autowired
    private AccountBaseApi accountBaseApi;

    @Override
    protected LoginByUserNameResponse newResponse() {
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
                        .bizType(AccountBizTypeEnum.USER.getCode())
                        .build()
        ).check();
        return account;
    }
}
