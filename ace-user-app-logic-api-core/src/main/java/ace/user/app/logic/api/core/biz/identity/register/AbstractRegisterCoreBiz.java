package ace.user.app.logic.api.core.biz.identity.register;


import ace.authentication.base.api.AccountEventBaseApi;
import ace.authentication.base.api.IdentityBaseApi;

import ace.authentication.base.define.dao.enums.account.AccountStateEnum;
import ace.authentication.base.define.dao.enums.accountevent.AccountEventEventTypeEnum;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.dao.model.entity.AccountEvent;
import ace.authentication.base.define.enums.AccountBusinessErrorEnum;
import ace.authentication.base.define.model.request.RegisterRequest;
import ace.authentication.base.define.model.vo.RegisterSuccessEventLogParams;
import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.fw.enums.SystemCodeEnum;
import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.AceLocalDateTimeUtils;
import ace.fw.util.BusinessErrorUtils;
import ace.fw.utils.web.WebUtils;
import ace.user.app.logic.api.core.converter.OAuthTokenConverter;
import ace.user.app.logic.api.core.provider.OAuth2Provider;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.model.request.identity.register.IRegisterRequest;
import ace.user.app.logic.define.model.response.identity.register.IRegisterResponse;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import ace.user.base.api.UserBaseApi;
import ace.user.base.define.dao.entity.User;
import ace.user.base.define.dao.enums.user.UserSexEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:42
 * @description 注册核心逻辑
 */
@Data
@Slf4j
public abstract class AbstractRegisterCoreBiz<Request extends IRegisterRequest, Response extends IRegisterResponse> {
    @Autowired
    private IdentityBaseApi identityBaseApi;
    @Autowired
    private UserBaseApi userBaseApi;
    @Autowired
    private OAuth2BaseApiFacade oAuth2BaseApiFacade;
    @Autowired
    private PasswordUtils passwordUtils;
    @Autowired
    private OAuthTokenConverter oAuthTokenConverter;
    @Autowired
    private AccountEventBaseApi accountEventBaseApi;
    @Autowired
    private OAuth2Provider oAuth2Provider;

    public GenericResponseExt<Response> register(Request request) {
        // 账号是否已存在
        this.checkExist(request);
        // 检查相关信息
        this.check(request);
        // 保存注册账号与注册账号事件
        Account savedAccount = this.saveAccountAndRegisterEvent(request);
        // 保存用户信息,所有异常屏蔽
        this.saveUser(request, savedAccount);
        // 生成登录信息
        OAuth2TokenVo oauth2TokenVo = this.getOAuth2Token(savedAccount);
        // 创建返回业务结果
        Response registerResponse = this.newRegisterResponse();
        // 配置返回业务结果
        this.configResponse(registerResponse, request, savedAccount, oauth2TokenVo);
        // 生成返回结果
        GenericResponseExt<Response> responseExt = this.newGenericResponseExt(registerResponse, request, savedAccount, oauth2TokenVo);
        return responseExt;
    }

    protected GenericResponseExt<Response> newGenericResponseExt(Response registerResponse, Request request, Account savedAccount, OAuth2TokenVo oauth2TokenVo) {
        GenericResponseExt<Response> responseExt = new GenericResponseExt<>();
        responseExt.setData(registerResponse);
        responseExt.setCode(SystemCodeEnum.SUCCESS.getCode());
        responseExt.setMessage(SystemCodeEnum.SUCCESS.getDesc());
        return responseExt;
    }

    /**
     * 配置返回结果
     *
     * @param response
     * @param request
     * @param savedAccount
     * @param oauth2TokenVo
     */
    protected void configResponse(Response response, Request request, Account savedAccount, OAuth2TokenVo oauth2TokenVo) {
        response.setToken(oauth2TokenVo);
    }


    protected User saveUser(Request request, Account savedAccount) {
        String nickName = this.resolveNickName(request);
        User user = User
                .builder()
                .id(savedAccount.getId())
                .updateTime(LocalDateTime.now())
                .signature("")
                .sex(UserSexEnum.UNKNOWN.getCode())
                .rowVersion(1)
                .nickName(nickName)
                .createTime(LocalDateTime.now())
                .birthday(AceLocalDateTimeUtils.MIN_MYSQL)
                .avatarUrl("")
                .appId(savedAccount.getAppId())
                .build();

        user = this.saveUserBefore(request, savedAccount, user);

        try {
            this.userBaseApi.save(user).check();
        } catch (Throwable ex) {
            log.error("保存用户失败", ex);
        }

        return user;
    }

    protected OAuth2TokenVo getOAuth2Token(Account savedAccount) {
        return oAuth2Provider.getOAuth2Token(savedAccount);
    }

    private Account saveAccountAndRegisterEvent(Request request) {
        String nickName = this.resolveNickName(request);
        String salt = passwordUtils.buildSalt();
        String encodePassword = passwordUtils.encode(request.getPassword(), salt);
        Account account = Account.builder()
                .appId(request.getAppId())
                .expireTime(AceLocalDateTimeUtils.MAX_MYSQL)
                .passwordEncryptionFactor(salt)
                .password(encodePassword)
                .avatarUrl("")
                .email(null)
                .mobile(null)
                .nickName(nickName)
                .state(AccountStateEnum.ENABLE.getCode())
                .userName(null)
                .createTime(LocalDateTime.now())
                .id(AceUUIDUtils.generateTimeUUIDShort32())
                .rowVersion(1)
                .updateTime(LocalDateTime.now())
                .registerSource(request.getSourceEnum().getCode())
                .build();


        account = this.saveAccountBefore(request, account, passwordUtils);

        RegisterSuccessEventLogParams eventParams = RegisterSuccessEventLogParams.builder()
                .accountId(account.getId())
                .appId(request.getAppId())
                .registerTime(LocalDateTime.now())
                .registerSource(request.getSourceEnum().getCode())
                .ip(WebUtils.getIpAddr())
                .paramsId(UserLogicConstants.PARAMS_ID_REGISTER_EVENT)
                .params(null)
                .build();
        AccountEvent accountEvent = AccountEvent
                .builder()
                .id(AceUUIDUtils.generateTimeUUIDShort32())
                .rowVersion(1)
                .eventType(AccountEventEventTypeEnum.REGISTER.getCode())
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .appId(request.getAppId())
                .accountId(account.getId())
                .eventParams(JsonUtils.toJson(eventParams))
                .build();

        accountEvent = this.saveAccountEventBefore(request, account, accountEvent);

        identityBaseApi.register(
                RegisterRequest.builder()
                        .account(account)
                        .accountEvent(accountEvent)
                        .build()
        ).check();

        log.info(
                String.format("[accountId=%s][appId=%s][registerSource=%s]注册成功",
                        account.getId(),
                        account.getAppId(),
                        request.getSourceEnum().getCode()
                )
        );

        return account;
    }

    /**
     * 是否存在该账号
     *
     * @param request
     */
    protected void checkExist(Request request) {

        boolean isExist = this.isExist(request);

        if (isExist) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.EXIST_ACCOUNT);
        }
    }

    /**
     * 初始化用户信息对象
     *
     * @param request
     * @param savedAccount
     * @param user
     * @return
     */
    protected abstract User saveUserBefore(Request request, Account savedAccount, User user);

    /**
     * 初始化注册事件对象
     *
     * @param request
     * @param account
     * @param accountEvent
     * @return
     */
    protected AccountEvent saveAccountEventBefore(Request request, Account account, AccountEvent accountEvent) {
        return accountEvent;
    }

    /**
     * 初始化账号对象
     *
     * @param request
     * @param account
     * @param passwordUtils
     * @return
     */
    protected abstract Account saveAccountBefore(Request request, Account account, PasswordUtils passwordUtils);


    /**
     * 是否存在对应账号
     *
     * @param request
     * @return
     */
    protected abstract boolean isExist(Request request);

    /**
     * 检查相关信息
     *
     * @param request
     */
    protected abstract void check(Request request);

    /**
     * 创建返回结果
     *
     * @return
     */
    protected abstract Response newRegisterResponse();

    /**
     * 获取昵称
     *
     * @param request
     * @return
     */
    protected abstract String resolveNickName(Request request);
}
