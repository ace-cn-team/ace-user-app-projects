package ace.user.app.logic.define.model.request.identity.login;

import ace.account.base.define.enums.LoginSourceEnum;
import ace.common.base.define.model.bo.IAppId;
import ace.fw.util.AceEnumUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:46
 * @description
 */
public interface ILoginCoreRequest extends IAppId {

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
     * 来源 {@link LoginSourceEnum}
     *
     * @return
     */
    String getSourceType();

    void setSourceType(String sourceType);

    default LoginSourceEnum getLoginSourceEnum() {
        return AceEnumUtils.getEnum(LoginSourceEnum.class, this.getSourceType());
    }
}
