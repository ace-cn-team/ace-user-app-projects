package ace.user.app.logic.define.constants;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/19 17:37
 * @description
 */
public interface UserLogicConstants {
    /**
     * 图形验证业务类型
     */
    String CAPTCHA_BIZ_TYPE_REGISTER = "register";
    /**
     * SMS验证业务类型
     */
    String SMS_VERIFY_CODE_BIZ_TYPE_REGISTER = "register";
    /**
     * SMS验证业务类型,修改密码
     */
    String SMS_VERIFY_CODE_BIZ_TYPE_MODIFY_PASSWORD = "modify_password";
    /**
     * 登录事件标识
     */
    String PARAMS_ID_LOGIN_EVENT_ ="user_login";
    /**
     * 注册事件标识
     */
    String PARAMS_ID_REGISTER_EVENT ="user_register";
}
