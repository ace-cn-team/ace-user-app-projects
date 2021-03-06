package ace.user.app.logic.api.core.biz.identity.register;


import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.model.request.ExistsByUserNameRequest;
import ace.captcha.base.api.CaptchaBaseApi;
import ace.captcha.base.define.model.bo.CaptchaVerifyCodeId;
import ace.captcha.base.define.model.request.CheckRequest;
import ace.fw.util.BusinessErrorUtils;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.response.identity.register.RegisterByUserNameResponse;
import ace.user.base.define.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description
 */
@Component
@Slf4j
public class RegisterByUserNameBiz extends AbstractRegisterCoreBiz<RegisterByUserNameRequest, RegisterByUserNameResponse> {
    @Autowired
    private CaptchaBaseApi captchaBaseApi;

    @Override
    protected User saveUserBefore(RegisterByUserNameRequest request, Account savedAccount, User user) {
        return user;
    }

    @Override
    protected Account saveAccountBefore(RegisterByUserNameRequest request, Account account, PasswordUtils passwordUtils) {
        account.setUserName(request.getUserName());
        return account;
    }

    @Override
    protected boolean isExist(RegisterByUserNameRequest request) {
        ExistsByUserNameRequest existsByUserNameRequest = ExistsByUserNameRequest.builder()
                .userName(request.getUserName())
                .appId(request.getAppId())
                .build();
        return super.getIdentityBaseApi().existsByUserName(existsByUserNameRequest).check();
    }

    @Override
    protected void check(RegisterByUserNameRequest request) {
        CheckRequest checkRequest = new CheckRequest();
        checkRequest
                .setVerifyCode(request.getVerifyCode())
                .setVerifyCodeId(
                        CaptchaVerifyCodeId.builder()
                                .bizType(UserLogicConstants.CAPTCHA_BIZ_TYPE_REGISTER)
                                .appId(request.getAppId())
                                .bizId(request.getVerifyCodeBizId())
                                .build()
                );
        Boolean isCheckPass = captchaBaseApi.check(checkRequest).check();
        if (Boolean.FALSE.equals(isCheckPass)) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.IMAGE_VERIFY_CODE_INCORRECT);
        }
    }

    @Override
    protected RegisterByUserNameResponse newRegisterResponse() {
        return new RegisterByUserNameResponse();
    }

    @Override
    protected String resolveNickName(RegisterByUserNameRequest request) {
        return new BigInteger(UUID.randomUUID().toString().replaceAll("-", ""), 16).toString(36);

    }
}
