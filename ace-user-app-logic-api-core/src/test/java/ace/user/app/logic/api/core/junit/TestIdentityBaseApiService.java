package ace.user.app.logic.api.core.junit;

import ace.authentication.base.api.IdentityBaseApi;
import ace.authentication.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.authentication.base.define.model.request.ExistsByMobileRequest;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.util.AceRandomUtils;
import ace.user.app.logic.api.core.JUnitApplication;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import lombok.extern.slf4j.Slf4j;
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
public class TestIdentityBaseApiService {

    private final static String TEST_MOBILE = "15099975787";
    private final static String TEST_APP_ID = "test_app_id";
    private final static String TEST_PASSWORD = "123456";
    private final static String TEST_SMS_VERIFY_CODE = "888888";
    @Autowired
    private IdentityBaseApi identityBaseApi;

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0001_existByMobile() {
        log.debug("222");
        identityBaseApi.existsByMobile(ExistsByMobileRequest.builder()
                .appId("1")
                .mobile("15099975786")
                .build());

    }


    private static String generateTestMobile() {
        StringBuffer sb = new StringBuffer();
        sb.append("1");
        sb.append(AceRandomUtils.randomNumber(10));

        return sb.toString();
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