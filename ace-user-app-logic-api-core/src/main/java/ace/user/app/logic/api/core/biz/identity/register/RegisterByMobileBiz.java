package ace.user.app.logic.api.core.biz.identity.register;

import ace.account.base.define.dao.enums.account.AccountBizTypeEnum;
import ace.account.base.define.dao.model.entity.Account;
import ace.account.base.define.enums.AccountBusinessErrorEnum;
import ace.account.base.define.model.request.ExistsByMobileRequest;
import ace.fw.util.BusinessErrorUtils;
import ace.sms.base.api.SmsVerifyCodeBaseApi;
import ace.sms.define.base.model.bo.VerifyCodeId;
import ace.sms.define.base.model.request.CheckRequest;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.module.identity.register.request.RegisterByMobileRequest;
import ace.user.app.logic.define.module.identity.register.response.RegisterByMobileResponse;
import ace.user.base.define.dao.entity.User;
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
public class RegisterByMobileBiz extends AbstractRegisterCoreBiz<RegisterByMobileRequest, RegisterByMobileResponse> {

    @Autowired
    private SmsVerifyCodeBaseApi smsVerifyCodeBaseApi;

    @Override
    protected User saveUserBefore(RegisterByMobileRequest request, Account savedAccount, User user) {
        return user;
    }

    @Override
    protected Account saveAccountBefore(RegisterByMobileRequest request, Account account, PasswordUtils passwordUtils) {
        account.setMobile(request.getMobile());
        return account;
    }

    @Override
    protected boolean isExist(RegisterByMobileRequest request) {
        ExistsByMobileRequest existsByMobileRequest = ExistsByMobileRequest
                .builder()
                .appId(request.getAppId())
                .bizType(AccountBizTypeEnum.USER.getCode())
                .mobile(request.getMobile())
                .build();
        return super.getIdentityBaseApi().existsByMobile(existsByMobileRequest).check();
    }

    @Override
    protected void check(RegisterByMobileRequest request) {
        this.checkVerifyCode(request);
    }

    @Override
    protected RegisterByMobileResponse newResponse() {
        return new RegisterByMobileResponse();
    }



    @Override
    protected String resolveNickName(RegisterByMobileRequest request) {
        return String.format("%s***%s",
                request.getMobile().substring(0, 3),
                request.getMobile().substring(request.getMobile().length() - 3, request.getMobile().length() - 1)
        );
    }

    /**
     * 验证短信验证码
     *
     * @param request
     */
    private void checkVerifyCode(RegisterByMobileRequest request) {
        CheckRequest checkRequest = CheckRequest
                .builder()
                .verifyCode(request.getVerifyCode())
                .verifyCodeId(
                        VerifyCodeId.builder()
                                .appId(request.getAppId())
                                .bizType(UserLogicConstants.SMS_VERIFY_CODE_BIZ_TYPE_REGISTER)
                                .mobile(request.getMobile())
                                .build()
                )
                .build();

        boolean isEqualVerifyCode = smsVerifyCodeBaseApi.check(checkRequest).check();

        if (isEqualVerifyCode == false) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.NOT_EQUAL_SMS_VERIFY_CODE);
        }
    }
}
