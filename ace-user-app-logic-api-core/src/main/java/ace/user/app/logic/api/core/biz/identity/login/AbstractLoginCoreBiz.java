package ace.user.app.logic.api.core.biz.identity.login;

import ace.authentication.base.api.AccountEventBaseApi;

import ace.authentication.base.define.dao.enums.account.AccountStateEnum;
import ace.authentication.base.define.dao.enums.accountevent.AccountEventEventTypeEnum;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.dao.model.entity.AccountEvent;
import ace.authentication.base.define.enums.LoginTypeEnum;
import ace.authentication.base.define.model.vo.LoginSuccessEventLogParams;
import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.fw.enums.SystemCodeEnum;
import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.BusinessErrorUtils;
import ace.user.app.logic.api.core.converter.OAuthTokenConverter;
import ace.user.app.logic.api.core.provider.OAuth2Provider;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.login.ILoginCoreRequest;
import ace.user.app.logic.define.model.response.identity.login.ILoginCoreResponse;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import ace.user.base.api.UserBaseApi;
import ace.user.base.define.dao.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:42
 * @description 登录核心逻辑
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
    @Autowired
    private OAuth2Provider oAuth2Provider;
    @Autowired
    private UserBaseApi userBaseApi;

    public GenericResponseExt<Response> login(Request request) {
        // 查找账号
        Account account = this.findAccount(request);
        // 检查账号登录信息、账号状态等相关信息
        this.checkAccount(request, account);
        // 创建token
        OAuth2TokenVo token = this.getOAuth2Token(account);
        // 创建返回业务数据
        Response loginCoreResponse = this.newLoginResponse();
        // 配置返回业务数据
        this.configResponse(loginCoreResponse, request, account, token);
        // 初始化返回结果
        GenericResponseExt<Response> responseExt = this.newGenericResponseExt(loginCoreResponse, request, account);
        // 验证是否存在用户信息
        this.checkUserIsExistNoThrowable(responseExt, loginCoreResponse, request, account);
        // 登录事件入库,屏蔽所有异常
        this.saveLoginEventNoThrowable(request, account);

        return responseExt;
    }

    protected GenericResponseExt<Response> newGenericResponseExt(Response loginCoreResponse, Request request, Account account) {
        GenericResponseExt<Response> responseExt = new GenericResponseExt<>();
        responseExt.setData(loginCoreResponse);
        responseExt.setCode(SystemCodeEnum.SUCCESS.getCode());
        responseExt.setMessage(SystemCodeEnum.SUCCESS.getDesc());
        return responseExt;
    }

    /**
     * 验证是否存在用户信息
     *
     * @param response
     * @param request
     * @param account
     */
    protected void checkUserIsExistNoThrowable(GenericResponseExt<Response> responseExt, ILoginCoreResponse response, Request request, Account account) {
        User user = userBaseApi.getById(account.getId()).check();
        if (user == null) {
            responseExt.setCode(UserLogicBusinessErrorEnum.USER_NOT_EXIST.getCode());
            responseExt.setMessage(UserLogicBusinessErrorEnum.USER_NOT_EXIST.getDesc());
        }
    }

    /**
     * @param request
     * @param account
     * @param oAuth2TokenVo
     */
    protected void configResponse(ILoginCoreResponse response, Request request, Account account, OAuth2TokenVo oAuth2TokenVo) {
        response.setToken(oAuth2TokenVo);
    }

    /**
     * 创建token
     *
     * @param account
     * @return
     */
    protected OAuth2TokenVo getOAuth2Token(Account account) {
        return oAuth2Provider.getOAuth2Token(account);
    }

    /**
     * 创建
     *
     * @return
     */
    protected abstract Response newLoginResponse();

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
    protected void saveLoginEventNoThrowable(Request request, Account account) {
        log.info(
                String.format("[account=%s][appId=%s][loginSource=%s][loginType=%s]登录成功",
                        account.getId(),
                        account.getAppId(),
                        request.getLoginSourceEnum().getCode(),
                        this.getLoginTypeEnum().getDesc()
                )
        );
        LoginSuccessEventLogParams eventParams = LoginSuccessEventLogParams.builder()
                .accountId(account.getId())
                .appId(request.getAppId())
                .loginTime(LocalDateTime.now())
                .ip(ace.fw.utils.web.WebUtils.getIpAddr())
                .loginSource(request.getLoginSourceEnum().getCode())
                .paramsId(UserLogicConstants.PARAMS_ID_LOGIN_EVENT_)
                .params(null)
                .build();
        AccountEvent accountEvent = AccountEvent
                .builder()
                .id(AceUUIDUtils.generateTimeUUIDShort32())
                .rowVersion(1)
                .eventType(AccountEventEventTypeEnum.LOGIN.getCode())
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .appId(request.getAppId())
                .accountId(account.getId())
                .eventParams(JsonUtils.toJson(eventParams))
                .build();
        try {
            accountEventBaseApi.save(accountEvent);
        } catch (Throwable ex) {
            log.error("保存用户登录事件失败", ex);
        }
    }

    /**
     * 验证账号信息
     *
     * @param request
     * @param account
     */
    protected void checkAccount(Request request, Account account) {

        if (account == null) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.PASSWORD_NOT_EQUAL);
        }

        if (passwordUtils.isNotEquals(
                account.getPassword(),
                request.getPassword(),
                account.getPasswordEncryptionFactor())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.PASSWORD_NOT_EQUAL);
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
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.EXPIRE_ACCOUNT);
        }
        if (AccountStateEnum.DISABLE.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.DISABLE_ACCOUNT);
        }
        if (AccountStateEnum.EXPIRED.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.EXPIRE_ACCOUNT);
        }
        if (AccountStateEnum.MUST_CHANGE_PASSWORD.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.MUST_CHANGE_PASSWORD);
        }
        if (AccountStateEnum.LOCKED.getCode().equals(account.getState())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.LOCK_ACCOUNT);
        }
    }
}
