package ace.user.app.logic.api.core.biz.identity.modifypassword;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.constant.RestApiConstants;
import ace.fw.util.BusinessErrorUtils;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.api.core.util.PasswordUtils;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.identity.modifypassword.IModifyPasswordRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 修改密码业务逻辑基类
 */
@Slf4j
public abstract class AbstractModifyPasswordBiz<Request extends IModifyPasswordRequest> {

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private AccountBaseApi accountBaseApi;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private PasswordUtils passwordUtils;

    public GenericResponseExt<Boolean> modifyPassword(Request request) {
        Account account = this.findAndCheckAccount(request);

        this.check(request, account);

        this.modifyPassword(request, account);

        return GenericResponseExtUtils.buildSuccessWithData(true);
    }

    /**
     * 更新密码
     *
     * @param request
     * @param account
     */
    protected void modifyPassword(Request request, Account account) {
        String salt = passwordUtils.buildSalt();
        String encodeNewPassword = passwordUtils.encode(request.getNewPassword(), salt);
        Account modifyAccount = Account.builder()
                .id(account.getId())
                .password(encodeNewPassword)
                .passwordEncryptionFactor(salt)
                .build();

        accountBaseApi.updateByIdVersionAutoUpdate(modifyAccount).check();
    }

    /**
     * 查找对应账号，或者抛出相关业务异常
     *
     * @param request
     * @return object
     * @throws {@link UserLogicBusinessErrorEnum#EXIST_NOT_ACCOUNT)
     */
    protected Account findAndCheckAccount(Request request) {
        Account account = this.findAccount(request);
        if (account == null) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.EXIST_NOT_ACCOUNT);
        }
        return account;
    }

    /**
     * 验证相关参数
     *
     * @param request
     * @param account
     */
    protected abstract void check(Request request, Account account);

    /**
     * 查找对应账号
     *
     * @param request
     * @return null or object
     */
    protected abstract Account findAccount(Request request);


}