package ace.user.app.logic.api.core.biz.identity.register;


import ace.account.base.api.AccountEventBaseApi;
import ace.account.base.api.IdentityBaseApi;
import ace.account.base.define.dao.enums.account.AccountBizTypeEnum;
import ace.account.base.define.dao.enums.account.AccountStateEnum;
import ace.account.base.define.dao.enums.accountevent.AccountEventEventTypeEnum;
import ace.account.base.define.dao.model.entity.Account;
import ace.account.base.define.dao.model.entity.AccountEvent;
import ace.account.base.define.enums.AccountBusinessErrorEnum;
import ace.account.base.define.model.request.RegisterRequest;
import ace.account.base.define.model.vo.RegisterSuccessEventLogParams;
import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.fw.utils.web.WebUtils;
import ace.user.app.logic.define.module.identity.register.request.IRegisterRequest;
import ace.user.app.logic.define.module.identity.register.response.IRegisterResponse;
import ace.user.app.logic.define.module.model.dto.OAuth2TokenDto;
import ace.cas.base.define.model.request.facade.OAuth2GetTokenFacadeRequest;
import ace.fw.json.JsonUtils;
import ace.fw.util.AceLocalDateTimeUtils;
import ace.fw.util.BusinessErrorUtils;
import ace.user.app.logic.api.core.converter.OAuthTokenConverter;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.base.api.UserBaseApi;
import ace.user.base.define.dao.entity.User;
import ace.user.base.define.dao.enums.user.UserSexEnum;
import com.fasterxml.uuid.Generators;
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

    public Response register(Request request) {
        // 账号是否已存在
        this.checkExist(request);
        // 检查相关信息
        this.check(request);
        // 保存注册账号与注册账号事件
        Account savedAccount = this.saveAccountAndRegisterEvent(request);
        // 保存用户信息
        User savedUser = this.saveUser(request, savedAccount);
        // 生成登录信息
        OAuth2TokenDto oauth2TokenDto = this.getOAuth2TokenBo(savedAccount);
        // 创建返回结果
        Response response = this.newResponse();
        // 配置返回结果
        this.configResponse(response, request, savedAccount, savedUser, oauth2TokenDto);
        // 生成返回结果
        return response;
    }

    /**
     * 配置返回结果
     *
     * @param response
     * @param request
     * @param savedAccount
     * @param savedUser
     * @param oauth2TokenDto
     */
    protected void configResponse(Response response, Request request, Account savedAccount, User savedUser, OAuth2TokenDto oauth2TokenDto) {
        response.setToken(oauth2TokenDto);
    }


    protected User saveUser(Request request, Account savedAccount) {
        String nickName = this.resolveNickName(request);
        User user = User
                .builder()
                .id(Generators.timeBasedGenerator().generate().toString())
                .updateTime(LocalDateTime.now())
                .signature("")
                .sex(UserSexEnum.UNKNOWN.getCode())
                .rowVersion(1)
                .nickName(nickName)
                .createTime(LocalDateTime.now())
                .birthday(AceLocalDateTimeUtils.MIN_MYSQL)
                .avatarUrl("")
                .appId(savedAccount.getAppId())
                .accountId(savedAccount.getId())
                .build();

        user = this.saveUserBefore(request, savedAccount, user);

        this.userBaseApi.save(user).check();

        return user;
    }

    protected OAuth2TokenDto getOAuth2TokenBo(Account savedAccount) {

        ace.cas.base.define.model.bo.OAuth2Token oAuth2Token = oAuth2BaseApiFacade.getOAuth2Token(
                OAuth2GetTokenFacadeRequest
                        .builder()
                        .build()
                        .setAccountId(savedAccount.getId())
        ).check();

        return oAuthTokenConverter.toUserToken(oAuth2Token);
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
                .bizType(AccountBizTypeEnum.USER.getCode())
                .createTime(LocalDateTime.now())
                .id(Generators.timeBasedGenerator().generate().toString())
                .rowVersion(1)
                .updateTime(LocalDateTime.now())
                .registerSource(request.getSourceEnum().getCode())
                .build();


        account = this.saveAccountBefore(request, account, passwordUtils);

        RegisterSuccessEventLogParams eventParams = RegisterSuccessEventLogParams.builder()
                .accountId(account.getId())
                .appId(request.getAppId())
                .bizType(AccountBizTypeEnum.USER.getCode())
                .registerTime(LocalDateTime.now())
                .registerSource(request.getSourceEnum().getCode())
                .ip(WebUtils.getIpAddr())
                .build();
        AccountEvent accountEvent = AccountEvent
                .builder()
                .id(Generators.timeBasedGenerator().generate().toString())
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
                String.format("[accountId=%s][appId=%s][bizType=%s][registerSource=%s]注册成功",
                        account.getId(),
                        account.getAppId(),
                        account.getBizType(),
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
    protected abstract Response newResponse();

    /**
     * 获取昵称
     *
     * @param request
     * @return
     */
    protected abstract String resolveNickName(Request request);
}
