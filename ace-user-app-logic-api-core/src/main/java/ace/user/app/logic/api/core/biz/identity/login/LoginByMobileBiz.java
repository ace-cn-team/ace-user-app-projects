package ace.user.app.logic.api.core.biz.identity.login;

import ace.account.base.api.AccountBaseApi;
import ace.account.base.define.dao.enums.account.AccountBizTypeEnum;
import ace.account.base.define.dao.model.entity.Account;
import ace.account.base.define.enums.LoginTypeEnum;
import ace.account.base.define.model.request.FindByAppIdAndMobileRequest;
import ace.account.base.define.model.request.FindByAppIdAndUserNameRequest;
import ace.user.app.logic.define.module.identity.login.request.LoginByMobileRequest;
import ace.user.app.logic.define.module.identity.login.response.LoginByMobileResponse;
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
public class LoginByMobileBiz extends AbstractLoginCoreBiz<LoginByMobileRequest, LoginByMobileResponse> {

    @Autowired
    private AccountBaseApi accountBaseApi;

    @Override
    protected LoginByMobileResponse newResponse() {
        return new LoginByMobileResponse();
    }

    @Override
    protected LoginTypeEnum getLoginTypeEnum() {
        return LoginTypeEnum.MOBILE;
    }

    @Override
    protected Account findAccount(LoginByMobileRequest request) {
        Account account = accountBaseApi.findByAppIdAndMobile(
                FindByAppIdAndMobileRequest.builder()
                        .appId(request.getAppId())
                        .mobile(request.getMobile())
                        .bizType(AccountBizTypeEnum.USER.getCode())
                        .build()
        ).check();
        return account;
    }
}
