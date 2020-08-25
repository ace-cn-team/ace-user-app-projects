package ace.user.app.logic.api.core.biz.identity.modifypassword;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.model.request.FindByAppIdAndMobileRequest;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.constant.RestApiConstants;
import ace.fw.util.BusinessErrorUtils;
import ace.fw.util.GenericResponseExtUtils;
import ace.sms.base.api.SmsVerifyCodeBaseApi;
import ace.sms.define.base.enums.SmsVerifyCodeBizTypeEnum;
import ace.sms.define.base.enums.SmsVerifyCodeTypeEnum;
import ace.sms.define.base.model.bo.VerifyCodeId;
import ace.sms.define.base.model.request.CheckRequest;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordByOldPasswordRequest;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordBySmsVerifyCodeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 根据短信验证码修改密码
 */
@Component
@Slf4j
public class ModifyPasswordBySmsVerifyCodeBiz extends AbstractModifyPasswordBiz<ModifyPasswordBySmsVerifyCodeRequest> {

    @Autowired
    private SmsVerifyCodeBaseApi smsVerifyCodeBaseApi;

    @Override
    protected void check(ModifyPasswordBySmsVerifyCodeRequest request, Account account) {
        Boolean isSmsVerifyCodeOk = smsVerifyCodeBaseApi.check(CheckRequest.builder()
                .verifyCodeId(VerifyCodeId.builder()
                        .bizType(UserLogicConstants.SMS_VERIFY_CODE_BIZ_TYPE_MODIFY_PASSWORD)
                        .appId(account.getAppId())
                        .mobile(request.getMobile())
                        .build()
                )
                .verifyCode(request.getSmsVerifyCode())
                .build()).check();
        if (isSmsVerifyCodeOk == false) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.NOT_EQUAL_SMS_VERIFY_CODE);
        }
    }

    @Override
    protected Account findAccount(ModifyPasswordBySmsVerifyCodeRequest request) {
        Account account = getAccountBaseApi().findByAppIdAndMobile(
                FindByAppIdAndMobileRequest.builder()
                        .appId(request.getAppId())
                        .mobile(request.getMobile())
                        .build()
        ).check();
        return account;
    }
}