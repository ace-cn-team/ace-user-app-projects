package ace.user.app.web.controller;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.enums.LoginSourceEnum;
import ace.fw.data.model.*;
import ace.fw.data.model.request.resful.PageQueryRequest;
import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.AceRandomUtils;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByMobileResponse;
import ace.user.app.logic.define.model.response.identity.register.RegisterByUserNameResponse;
import ace.user.app.web.UserWebApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserWebApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TestIdentityController {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private AccountBaseApi accountBaseApi;
    private final static String TEST_MOBILE = "112345678910";
    private final static String TEST_USER_NAME = "test";
    private final static String TEST_APP_ID = "test_app_id";
    private final static String TEST_PASSWORD = "1QWa23456!@#";
    private final static String TEST_SMS_VERIFY_CODE = "888888";

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0001_registerByMobile() throws Exception {
        RegisterByMobileRequest request = RegisterByMobileRequest.builder()
                .appId(TEST_APP_ID)
                .invitorCode(null)
                .mobile(generateTestMobile())
                .password(TEST_PASSWORD)
                .sourceType(AccountRegisterSourceEnum.PC.getCode())
                .verifyCode(TEST_SMS_VERIFY_CODE)
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(IdentityController.REGISTER_BY_MOBILE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        TypeReference<GenericResponseExt<RegisterByMobileResponse>> typeReference = new TypeReference<>() {
        };

        GenericResponseExt<RegisterByMobileResponse> genericResponseExt = JsonUtils.toObject(response.getContentAsString(), typeReference.getType());

        Assert.assertTrue(StringUtils.isNotBlank(genericResponseExt.check().getToken().getAccessToken()));
    }

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0002_registerByUserName() throws Exception {
        RegisterByUserNameRequest request = RegisterByUserNameRequest.builder()
                .appId(TEST_APP_ID)
                .invitorCode(null)
                .userName(AceUUIDUtils.generateTimeUUIDShort32())
                .password(TEST_PASSWORD)
                .sourceType(AccountRegisterSourceEnum.PC.getCode())
                .verifyCode(TEST_SMS_VERIFY_CODE)
                .verifyCodeBizId(TEST_APP_ID)
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(IdentityController.REGISTER_BY_USERNAME_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        TypeReference<GenericResponseExt<RegisterByUserNameResponse>> typeReference = new TypeReference<>() {
        };

        GenericResponseExt<RegisterByUserNameResponse> genericResponseExt = JsonUtils.toObject(response.getContentAsString(), typeReference.getType());

        RegisterByUserNameResponse registerByUserNameResponse = genericResponseExt.check();

        Assert.assertTrue(StringUtils.isNotBlank(registerByUserNameResponse.getToken().getAccessToken()));
    }
//
//    @Test
//    public void test_0003_loginByMobile() throws Exception {
//        Account account = this.findAccountWithMobileFirst();
//        LoginByMobileRequest request = LoginByMobileRequest.builder()
//                .appId(account.getId())
//                .mobile(account.getMobile())
//                .password(account.getPassword())
//                .sourceType(LoginSourceEnum.PC.getCode())
//                .build();
//        MockHttpServletResponse response = mockMvc.perform(
//                MockMvcRequestBuilders.post(IdentityController.LOGIN_BY_MOBILE_URL)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(JsonUtils.toJson(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn()
//                .getResponse();
//
//        TypeReference<GenericResponseExt<LoginByMobileResponse>> typeReference = new TypeReference<>() {
//        };
//
//        GenericResponseExt<LoginByMobileResponse> genericResponseExt = JsonUtils.toObject(response.getContentAsString(), typeReference.getType());
//
//        Assert.assertTrue(StringUtils.isNotBlank(genericResponseExt.check().getToken().getAccessToken()));
//    }
//
//    @Test
//    public void test_0004_loginByUserName() throws Exception {
//        Account account = this.findAccountWithUserNameFirst();
//        LoginByUserNameRequest request = LoginByUserNameRequest.builder()
//                .appId(account.getId())
//                .userName(account.getMobile())
//                .password(account.getPassword())
//                .sourceType(LoginSourceEnum.PC.getCode())
//                .build();
//        MockHttpServletResponse response = mockMvc.perform(
//                MockMvcRequestBuilders.post(IdentityController.LOGIN_BY_MOBILE_URL)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(JsonUtils.toJson(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn()
//                .getResponse();
//
//        TypeReference<GenericResponseExt<LoginByMobileResponse>> typeReference = new TypeReference<>() {
//        };
//
//        GenericResponseExt<LoginByMobileResponse> genericResponseExt = JsonUtils.toObject(response.getContentAsString(), typeReference.getType());
//
//        Assert.assertTrue(StringUtils.isNotBlank(genericResponseExt.check().getToken().getAccessToken()));
//    }
//
//    @Test
//    public void test_0005_getCurrentUser() {
//        LoginByUserNameResponse userNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
//                .appId(registerByUserNameRequest.getAppId())
//                .password(registerByUserNameRequest.getPassword())
//                .sourceType(LoginSourceEnum.PC.getCode())
//                .userName(registerByUserNameRequest.getUserName())
//                .build()).check();
//        GetCurrentUserResponse currentUserResponse = identityLogicService.getCurrentUser(
//                GetCurrentUserRequest.builder()
//                        .accessToken(userNameResponse.getToken().getAccessToken())
//                        .build()
//        ).check();
//
//        Assert.assertNotNull(currentUserResponse);
//    }
//
//    @Test
//    public void test_0006_logout() {
//        LoginByUserNameResponse userNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
//                .appId(registerByUserNameRequest.getAppId())
//                .password(registerByUserNameRequest.getPassword())
//                .sourceType(LoginSourceEnum.PC.getCode())
//                .userName(registerByUserNameRequest.getUserName())
//                .build()).check();
//        identityLogicService.logout(LogoutRequest.builder()
//                .accessToken(userNameResponse.getToken().getAccessToken())
//                .build()
//        )
//                .check();
//        GenericResponseExt<GetCurrentUserResponse> currentUserResponse = identityLogicService.getCurrentUser(
//                GetCurrentUserRequest.builder()
//                        .accessToken(userNameResponse.getToken().getAccessToken())
//                        .build()
//        );
//
//
//        Assert.assertNull(currentUserResponse.getData());
//    }

    private static String generateTestMobile() {
        StringBuffer sb = new StringBuffer();
        sb.append("1");
        sb.append(AceRandomUtils.randomNumber(10));

        return sb.toString();
    }

    private Account findAccountWithMobileFirst() {
        List<GenericCondition<String>> conditions = Arrays.asList(GenericCondition.<String>builder()
                .field(Account.MOBILE)
                .relationalOp(RelationalOpConstVal.IS_NOT_NULL)
                .build()
        );
        List<Sort> sorts = Arrays.asList(Sort.builder()
                .field(Account.ID)
                .asc(true)
                .build());
        Account account = accountBaseApi.page(PageQueryRequest.builder()
                .pageSize(1)
                .pageIndex(1)
                .sorts(sorts)
                .conditions(conditions)
                .build()
        ).check().getData().get(0);
        return account;
    }

    private Account findAccountWithUserNameFirst() {
        List<GenericCondition<String>> conditions = Arrays.asList(GenericCondition.<String>builder()
                .field(Account.USER_NAME)
                .relationalOp(RelationalOpConstVal.IS_NOT_NULL)
                .build()
        );
        List<Sort> sorts = Arrays.asList(Sort.builder()
                .field(Account.ID)
                .asc(true)
                .build());
        Account account = accountBaseApi.page(PageQueryRequest.builder()
                .pageSize(1)
                .pageIndex(1)
                .sorts(sorts)
                .conditions(conditions)
                .build()
        ).check().getData().get(0);
        return account;
    }
}