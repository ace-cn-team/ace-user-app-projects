package ace.user.app.logic.define.module.identity.register.request;

import ace.account.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.account.base.define.enums.LoginSourceEnum;
import ace.common.base.define.model.bo.IAppId;
import ace.common.base.define.model.constraint.AppIdConstraint;
import ace.fw.util.AceEnumUtils;
import ace.user.app.logic.define.constraint.InviterCodeConstraint;
import ace.user.app.logic.define.constraint.PasswordConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:46
 * @description
 */
public interface IRegisterRequest extends IAppId {

    /**
     * appId
     *
     * @return
     */
    String getAppId();

    void setAppId(String appId);

    /**
     * 密码
     *
     * @return
     */
    String getPassword();

    void setPassword(String password);

    /**
     * 邀请码
     *
     * @return
     */
    String getInvitorCode();

    void setInvitorCode(String invitorCode);

    /**
     * 来源 {@link LoginSourceEnum}
     *
     * @return
     */
    String getSourceType();

    void setSourceType(String pSourceType);

    default AccountRegisterSourceEnum getSourceEnum() {
        return AceEnumUtils.getEnum(AccountRegisterSourceEnum.class, this.getSourceType());
    }
}
