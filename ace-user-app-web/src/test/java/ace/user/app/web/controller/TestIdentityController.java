package ace.user.app.web.controller;

import ace.account.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.account.base.define.enums.LoginSourceEnum;
import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.AceRandomUtils;
import ace.user.app.logic.api.service.IdentityLogicService;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import ace.user.app.web.UserWebApplication;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.asm.Type;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    private final static String TEST_MOBILE = "15099975787";
    private final static String TEST_APP_ID = "test_app_id";
    private final static String TEST_PASSWORD = "1QWa23456!@#";
    private final static String TEST_SMS_VERIFY_CODE = "888888";

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0001_registerByMobile() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(IdentityController.REGISTER_BY_MOBILE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(registerByMobileRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        TypeReference<GenericResponseExt<OAuth2TokenVo>> typeReference = new TypeReference<>() {
        };

        GenericResponseExt<OAuth2TokenVo> genericResponseExt = JsonUtils.toObject(response.getContentAsString(), typeReference.getType());

        OAuth2TokenVo oAuth2TokenVo = genericResponseExt.check();

        if (oAuth2TokenVo == null) {
            throw new RuntimeException();
        }
    }
//
//    /**
//     * 测试手机号码注册
//     */
//    @Test
//    public void test_0002_registerByUserName() {
//        OAuth2TokenVo oAuth2TokenVo = identityLogicService.registerByUserName(registerByUserNameRequest).check().getToken();
//
//        if (oAuth2TokenVo == null) {
//            throw new RuntimeException();
//        }
//    }
//
//    @Test
//    public void test_0003_loginByMobile() {
//        LoginByMobileResponse mobileResponse = identityLogicService
//                .loginByMobile(
//                        LoginByMobileRequest.builder()
//                                .appId(registerByMobileRequest.getAppId())
//                                .password(registerByMobileRequest.getPassword())
//                                .sourceType(LoginSourceEnum.PC.getCode())
//                                .mobile(registerByMobileRequest.getMobile())
//                                .build()
//                ).check();
//
//        Assert.assertNotNull(mobileResponse.getToken());
//    }
//
//    @Test
//    public void test_0004_loginByUserName() {
//        LoginByUserNameResponse loginByUserNameResponse = identityLogicService.loginByUserName(LoginByUserNameRequest.builder()
//                .appId(registerByUserNameRequest.getAppId())
//                .password(registerByUserNameRequest.getPassword())
//                .sourceType(LoginSourceEnum.PC.getCode())
//                .userName(registerByUserNameRequest.getUserName())
//                .build()).check();
//
//        Assert.assertNotNull(loginByUserNameResponse.getToken());
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

    private final static RegisterByMobileRequest registerByMobileRequest = RegisterByMobileRequest.builder()
            .appId(TEST_APP_ID)
            .invitorCode(null)
            .mobile(generateTestMobile())
            .password(TEST_PASSWORD)
            .sourceType(AccountRegisterSourceEnum.PC.getCode())
            .verifyCode(TEST_SMS_VERIFY_CODE)
            .build();
    private final static RegisterByUserNameRequest registerByUserNameRequest = RegisterByUserNameRequest.builder()
            .appId(TEST_APP_ID)
            .invitorCode(null)
            .userName(AceUUIDUtils.generateTimeUUIDShort32())
            .password(TEST_PASSWORD)
            .sourceType(AccountRegisterSourceEnum.PC.getCode())
            .verifyCode(TEST_SMS_VERIFY_CODE)
            .verifyCodeBizId(TEST_APP_ID)
            .build();
}