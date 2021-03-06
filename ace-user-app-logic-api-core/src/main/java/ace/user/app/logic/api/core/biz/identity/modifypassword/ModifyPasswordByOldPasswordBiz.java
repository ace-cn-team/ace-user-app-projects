package ace.user.app.logic.api.core.biz.identity.modifypassword;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.constant.RestApiConstants;
import ace.fw.util.BusinessErrorUtils;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.modifypassword.ModifyPasswordByOldPasswordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 根据旧密码修改密码
 */
@Component
@Slf4j
public class ModifyPasswordByOldPasswordBiz extends AbstractModifyPasswordBiz<ModifyPasswordByOldPasswordRequest> {

    @Override
    protected void check(ModifyPasswordByOldPasswordRequest request, Account account) {
        if (getPasswordUtils().isNotEquals(account.getPassword(), request.getOldPassword(), account.getPasswordEncryptionFactor())) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.PASSWORD_NOT_EQUAL);
        }
    }

    @Override
    protected Account findAccount(ModifyPasswordByOldPasswordRequest request) {
        return getAccountBaseApi().findById(request.getAccountId()).check();
    }
}