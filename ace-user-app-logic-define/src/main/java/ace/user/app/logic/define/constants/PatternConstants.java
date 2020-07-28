package ace.user.app.logic.define.constants;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/21 11:22
 * @description
 */
public class PatternConstants {
    /**
     * 密码格式,只能输入由数字和26个英文字母或者下划线组成的字符串：
     */
    public static final String PASSWORD = "^/w{6,32}$";
    /**
     * 账号格式
     */
    public static final String USER_NAME = "^[0-9a-zA-Z_@\\.]{6,32}$";
    /**
     * 手机账号格式
     */
    public static final String MOBILE = "^1\\d{10}$";
}
