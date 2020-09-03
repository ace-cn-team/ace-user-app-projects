package ace.user.app.logic.api.core.junit;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.enums.LoginSourceEnum;
import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.util.QueryUtils;
import ace.fw.util.AceRandomUtils;
import ace.sms.base.api.SmsVerifyCodeBaseApi;
import ace.sms.define.base.enums.SmsVerifyCodeTypeEnum;
import ace.sms.define.base.model.bo.VerifyCodeId;
import ace.sms.define.base.model.request.SendVerifyCodeRequest;
import ace.user.app.logic.api.core.JUnitApplication;
import ace.user.app.logic.api.service.IdentityLogicService;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordByNoLimitRequest;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordByOldPasswordRequest;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordBySmsVerifyCodeRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JUnitApplication.class})
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TestIdentityLogicService {

    private final static String TEST_MOBILE = "15099975787";
    private final static String TEST_APP_ID = "test_app_id";
    private final static String TEST_PASSWORD = "123456";
    private final static String TEST_SMS_VERIFY_CODE = "888888";
    @Autowired
    private IdentityLogicService identityLogicService;
    @Autowired
    private AccountBaseApi accountBaseApi;
    @Autowired
    private SmsVerifyCodeBaseApi smsVerifyCodeBaseApi;

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0001_registerByMobile() {
        OAuth2TokenVo oAuth2TokenVo = identityLogicService.registerByMobile(registerByMobileRequest).check().getToken();

        Assert.assertTrue(StringUtils.isNotBlank(oAuth2TokenVo.getAccessToken()));
    }

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0002_registerByUserName() {
        OAuth2TokenVo oAuth2TokenVo = identityLogicService.registerByUserName(registerByUserNameRequest).check().getToken();

        Assert.assertTrue(StringUtils.isNotBlank(oAuth2TokenVo.getAccessToken()));
    }

    @Test
    public void test_0003_loginByMobile() {
        LoginByMobileResponse mobileResponse = identityLogicService
                .loginByMobile(
                        LoginByMobileRequest.builder()
                                .appId(registerByMobileRequest.getAppId())
                                .password(registerByMobileRequest.getPassword())
                                .sourceType(LoginSourceEnum.PC.getCode())
                                .mobile(registerByMobileRequest.getMobile())
                                .build()
                ).check();

        Assert.assertNotNull(mobileResponse.getToken());
    }

    @Test
    public void test_0004_loginByUserName() {
        Account account = this.findAccountUserNameIsNotNullFirst();

        Boolean isModifySuccess = identityLogicService.modifyPasswordByNoLimit(ModifyPasswordByNoLimitRequest.builder()
                .accountId(account.getId())
                .newPassword(TEST_PASSWORD)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);

        LoginByUserNameResponse loginByUserNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
                .appId(account.getAppId())
                .userName(account.getUserName())
                .password(TEST_PASSWORD)
                .sourceType(LoginSourceEnum.PC.getCode())

                .build()).check();

        Assert.assertNotNull(loginByUserNameResponse.getToken());
    }

    @Test
    public void test_0005_getCurrentUser() {
        LoginByUserNameResponse userNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
                .appId(registerByUserNameRequest.getAppId())
                .password(registerByUserNameRequest.getPassword())
                .sourceType(LoginSourceEnum.PC.getCode())
                .userName(registerByUserNameRequest.getUserName())
                .build()).check();
        GetCurrentUserResponse currentUserResponse = identityLogicService.getCurrentUser(
                GetCurrentUserRequest.builder()
                        .accessToken(userNameResponse.getToken().getAccessToken())
                        .build()
        ).check();

        Assert.assertNotNull(currentUserResponse);
    }

    @Test
    public void test_0006_logout() {
        LoginByUserNameResponse userNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
                .appId(registerByUserNameRequest.getAppId())
                .password(registerByUserNameRequest.getPassword())
                .sourceType(LoginSourceEnum.PC.getCode())
                .userName(registerByUserNameRequest.getUserName())
                .build()).check();
        identityLogicService.logout(LogoutRequest.builder()
                .accessToken(userNameResponse.getToken().getAccessToken())
                .build()
        )
                .check();
        GenericResponseExt<GetCurrentUserResponse> currentUserResponse = identityLogicService.getCurrentUser(
                GetCurrentUserRequest.builder()
                        .accessToken(userNameResponse.getToken().getAccessToken())
                        .build()
        );


        Assert.assertNull(currentUserResponse.getData());
    }

    @Test
    public void test_0007_modifyPasswordByNoLimit() {
        Account account = this.findAccountFirst();

        Boolean isModifySuccess = identityLogicService.modifyPasswordByNoLimit(ModifyPasswordByNoLimitRequest.builder()
                .accountId(account.getId())
                .newPassword(TEST_PASSWORD)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);

    }

    @Test
    public void test_0008_modifyPasswordByOldPassword() {
        Account account = this.findAccountFirst();

        Boolean isModifySuccess = identityLogicService.modifyPasswordByNoLimit(ModifyPasswordByNoLimitRequest.builder()
                .accountId(account.getId())
                .newPassword(TEST_PASSWORD)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);

        isModifySuccess = identityLogicService.modifyPasswordByOldPassword(ModifyPasswordByOldPasswordRequest.builder()
                .accountId(account.getId())
                .newPassword(TEST_PASSWORD)
                .oldPassword(TEST_PASSWORD)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);

    }


    @Test
    public void test_0009_modifyPasswordBySmsVerifyCode() {
        Account account = this.findAccountMobileIsNotNullFirst();

        Boolean isModifySuccess = identityLogicService.modifyPasswordByNoLimit(ModifyPasswordByNoLimitRequest.builder()
                .accountId(account.getId())
                .newPassword(TEST_PASSWORD)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);


        VerifyCodeId verifyCodeId = VerifyCodeId.builder()
                .mobile(account.getMobile())
                .appId(account.getAppId())
                .bizType(UserLogicConstants.SMS_VERIFY_CODE_BIZ_TYPE_MODIFY_PASSWORD)
                .build();

        String smsVerifyCode = smsVerifyCodeBaseApi.send(SendVerifyCodeRequest.builder()
                .verifyCodeId(verifyCodeId)
                .smsVerifyCodeType(SmsVerifyCodeTypeEnum.NUMBER.getCode())
                .verifyCodeLength(6)
                .build()).check();

        isModifySuccess = identityLogicService.modifyPasswordBySmsVerifyCode(ModifyPasswordBySmsVerifyCodeRequest.builder()
                .appId(account.getAppId())
                .newPassword(TEST_PASSWORD)
                .mobile(account.getMobile())
                .smsVerifyCode(smsVerifyCode)
                .build()
        ).check();
        Assert.assertTrue(isModifySuccess);

    }

    private static String generateTestMobile() {
        StringBuffer sb = new StringBuffer();
        sb.append("1");
        sb.append(AceRandomUtils.randomNumber(10));

        return sb.toString();
    }

    private Account findAccountFirst() {
        return accountBaseApi.page(PageRequest.builder()
                .orderBy(QueryUtils.orderByAsc(Account::getCreateTime))
                .pager(QueryUtils.pager(0, 1))
                .build()
        ).check().getData().get(0);
    }

    private Account findAccountMobileIsNotNullFirst() {
        return accountBaseApi.page(PageRequest.builder()
                .where(QueryUtils.where().isNotNull(Account::getMobile))
                .orderBy(QueryUtils.orderByDesc(Account::getCreateTime))
                .pager(QueryUtils.pager(0, 1))
                .build()
        ).check().getData().get(0);
    }

    private Account findAccountUserNameIsNotNullFirst() {
        return accountBaseApi.page(PageRequest.builder()
                .where(QueryUtils.where().isNotNull(Account::getUserName))
                .orderBy(QueryUtils.orderByDesc(Account::getCreateTime))
                .pager(QueryUtils.pager(0, 1))
                .build()
        ).check().getData().get(0);
    }

    private final static RegisterByMobileRequest registerByMobileRequest = RegisterByMobileRequest.builder()
            .appId(TEST_APP_ID)
            .invitorCode("")
            .mobile(generateTestMobile())
            .password(TEST_PASSWORD)
            .sourceType(AccountRegisterSourceEnum.PC.getCode())
            .verifyCode(TEST_SMS_VERIFY_CODE)
            .build();
    private final static RegisterByUserNameRequest registerByUserNameRequest = RegisterByUserNameRequest.builder()
            .appId(TEST_APP_ID)
            .invitorCode("")
            .userName(AceUUIDUtils.generateTimeUUIDShort32())
            .password(TEST_PASSWORD)
            .sourceType(AccountRegisterSourceEnum.PC.getCode())
            .verifyCode(TEST_SMS_VERIFY_CODE)
            .verifyCodeBizId(TEST_APP_ID)
            .build();
}