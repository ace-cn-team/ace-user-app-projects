package ace.user.app.logic.define.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/26 14:16
 * @description Integer[] USER = new Integer[]{130000, 139999};
 */
public enum UserLogicBusinessErrorEnum implements BaseEnum<String> {
    DATA_EXPIRE("130001", "数据已过期,请重试"),
    IMAGE_VERIFY_CODE_INCORRECT("130010", "请输入正确的验证码");
    @Getter
    private String code;
    @Getter
    private String desc;

    UserLogicBusinessErrorEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }
}