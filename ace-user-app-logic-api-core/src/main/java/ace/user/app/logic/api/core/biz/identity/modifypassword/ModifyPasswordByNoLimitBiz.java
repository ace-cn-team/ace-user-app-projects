package ace.user.app.logic.api.core.biz.identity.modifypassword;

import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.model.request.FindByAppIdAndMobileRequest;
import ace.fw.util.BusinessErrorUtils;
import ace.sms.base.api.SmsVerifyCodeBaseApi;
import ace.sms.define.base.model.bo.VerifyCodeId;
import ace.sms.define.base.model.request.CheckRequest;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordByNoLimitRequest;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordBySmsVerifyCodeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 直接修改密码
 */
@Component
@Slf4j
public class ModifyPasswordByNoLimitBiz extends AbstractModifyPasswordBiz<ModifyPasswordByNoLimitRequest> {

    @Override
    protected void check(ModifyPasswordByNoLimitRequest request, Account account) {

    }

    @Override
    protected Account findAccount(ModifyPasswordByNoLimitRequest request) {
        Account account = getAccountBaseApi().getById(request.getAccountId()).check();
        return account;
    }
}