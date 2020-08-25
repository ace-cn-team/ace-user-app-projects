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
    EXIST_ACCOUNT("130001", "账号已存在"),
    NOT_EQUAL_SMS_VERIFY_CODE("130002", "请输入正确的验证码"),
    EXIST_NOT_ACCOUNT("130003", "账号不存在"),
    EXPIRE_ACCOUNT("130004", "账号已过期"),
    DISABLE_ACCOUNT("130005", "账号已失效"),
    LOCK_ACCOUNT("130006", "账号已锁定"),
    MUST_CHANGE_PASSWORD("130007", "账号需要修改密码"),
    NOT_EXIST_ACCOUNT_PROFILE("130008", "账号详情信息缺失"),
    PASSWORD_NOT_EQUAL("130009", "请输入正确的账号与密码"),
    IMAGE_VERIFY_CODE_INCORRECT("130010", "请输入正确的验证码"),
    DATA_EXPIRE("130011", "数据已过期,请重试"),
    USER_NOT_EXIST("130012", "用户数据不存在"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;

    UserLogicBusinessErrorEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }
}