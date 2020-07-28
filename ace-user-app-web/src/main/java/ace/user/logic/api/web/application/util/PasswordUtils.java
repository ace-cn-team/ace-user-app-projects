package ace.user.logic.api.web.application.util;

import ace.fw.utils.AceCodecUtils;
import ace.fw.utils.AceRandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Caspar
 * @contact 279397942@qq.com
 * @date 2017/4/11
 * @description
 */
@Component
public class PasswordUtils {

    public String buildSalt() {
        String salt = AceRandomUtils.randomNumber(4);
        return salt;
    }

    public String encode(String password, String salt) {
        return AceCodecUtils.md5(password + salt).toLowerCase();
    }

    public boolean isEquals(String passwordEncrypt, String password2, String salt) {
        return StringUtils.equals(passwordEncrypt, encode(password2, salt));
    }

    public boolean isNotEquals(String passwordEncrypt, String password2, String salt) {
        return !isEquals(passwordEncrypt, password2, salt);
    }

    public static void main(String[] args) {
        PasswordUtils passwordUtils = new PasswordUtils();
        System.out.println(passwordUtils.encode("123456", "5478"));
        System.out.println(passwordUtils.encode("f13dd9e57eadf32b4a11764bef118d8e", "8947"));


    }
}
