package ace.user.app.logic.api.core.biz.identity.login;

import ace.account.base.api.AccountEventBaseApi;
import ace.account.base.define.dao.enums.account.AccountBizTypeEnum;
import ace.account.base.define.dao.enums.account.AccountStateEnum;
import ace.account.base.define.dao.enums.accountevent.AccountEventEventTypeEnum;
import ace.account.base.define.dao.model.entity.Account;
import ace.account.base.define.dao.model.entity.AccountEvent;
import ace.account.base.define.enums.AccountBusinessErrorEnum;
import ace.account.base.define.enums.LoginTypeEnum;
import ace.account.base.define.model.vo.LoginSuccessEventLogParams;
import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.cas.base.define.model.bo.OAuth2Token;
import ace.cas.base.define.model.request.facade.OAuth2GetTokenFacadeRequest;
import ace.fw.json.JsonUtils;
import ace.fw.util.BusinessErrorUtils;
import ace.user.app.logic.api.core.converter.OAuthTokenConverter;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.module.identity.login.request.ILoginCoreRequest;
import ace.user.app.logic.define.module.identity.login.response.ILoginCoreResponse;
import com.fasterxml.uuid.Generators;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:42
 * @description
 */
@Data
@Slf4j
public abstract class AbstractLoginCoreBiz<Request extends ILoginCoreRequest, Response extends ILoginCoreResponse> {
    @Autowired
    private PasswordUtils passwordUtils;
    @Autowired
    private AccountEventBaseApi accountEventBaseApi;
    @Autowired
    private OAuth2BaseApiFacade oAuth2BaseApiFacade;
    @Autowired
    private OAuthTokenConverter oAuthTokenConverter;

    public Response login(Request request) {
        // 查找账号
        Account account = this.findAccount(request);
        // 检查账号登录信息、账号状态等相关信息
        this.checkAccount(request, account);
        // 创建token
        OAuth2Token token = this.buildToken(account);
        // 创建返回数据
        Response response = this.newResponse();
        // 配置返回数据
        this.configResponse(response, request, account, token);
        // 登录事件入库
        this.saveLoginEvent(request, account);

        return response;
    }

    /**
     * @param request
     * @param account
     * @param oAuth2Token
     */
    protected void configResponse(Response response, Request request, Account account, OAuth2Token oAuth2Token) {
        response.setToken(oAuthTokenConverter.toUserToken(oAuth2Token));
    }

    /**
     * 创建token
     *
     * @param account
     * @return
     */
    protected OAuth2Token buildToken(Account account) {
        return oAuth2BaseApiFacade.getOAuth2Token
                (
                        OAuth2GetTokenFacadeRequest
                                .builder()
                                .accountId(account.getId())
                                .build()
                ).check();
    }

    /**
     * 创建
     *
     * @return
     */
    protected abstract Response newResponse();

    /**
     * 获取登录类型
     *
     * @return
     */
    protected abstract LoginTypeEnum getLoginTypeEnum();

    /**
     * 查找账号
     *
     * @param request
     * @return
     */
    protected abstract Account findAccount(Request request);


    /**
     * 记录登录事件
     *
     * @param request
     * @param account
     */
    protected void saveLoginEvent(Request request, Account account) {
        log.info(
                String.format("[account=%s][appId=%s][bizType=%s][loginSource=%s][loginType=%s]登录成功",
                        account.getId(),
                        account.getAppId(),
                        account.getBizType(),
                        request.getLoginSourceEnum().getCode(),
                        this.getLoginTypeEnum().getDesc()
                )
        );
        LoginSuccessEventLogParams eventParams = LoginSuccessEventLogParams.builder()
                .accountId(account.getId())
                .appId(request.getAppId())
                .bizType(AccountBizTypeEnum.USER.getCode())
                .loginTime(LocalDateTime.now())
                .ip(ace.fw.utils.web.WebUtils.getIpAddr())
                .loginSource(request.getLoginSourceEnum().getCode())
                .build();
        AccountEvent accountEvent = AccountEvent
                .builder()
                .id(Generators.timeBasedGenerator().generate().toString())
                .rowVersion(1)
                .eventType(AccountEventEventTypeEnum.LOGIN.getCode())
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .appId(request.getAppId())
                .accountId(account.getId())
                .eventParams(JsonUtils.toJson(eventParams))
                .build();
        accountEventBaseApi.save(accountEvent);
    }

    /**
     * 验证账号信息
     *
     * @param request
     * @param account
     */
    protected void checkAccount(Request request, Account account) {

        if (account == null) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.PASSWORD_NOT_EQUAL);
        }

        if (passwordUtils.isNotEquals(
                account.getPassword(),
                request.getPassword(),
                account.getPasswordEncryptionFactor())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.PASSWORD_NOT_EQUAL);
        }

        this.checkState(account);
    }

    /**
     * 验证账号状态
     *
     * @param account
     */
    protected void checkState(Account account) {

        if (account.getExpireTime().isBefore(LocalDateTime.now())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.EXPIRE_ACCOUNT);
        }
        if (AccountStateEnum.DISABLE.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.DISABLE_ACCOUNT);
        }
        if (AccountStateEnum.EXPIRED.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.EXPIRE_ACCOUNT);
        }
        if (AccountStateEnum.MUST_CHANGE_PASSWORD.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.MUST_CHANGE_PASSWORD);
        }
        if (AccountStateEnum.LOCKED.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.LOCK_ACCOUNT);
        }
    }
}
