package ace.user.app.logic.define.model.request.identity.register;

import ace.account.base.define.constraint.AccountRegisterSourceConstraint;
import ace.captcha.base.define.constraint.CaptchaVerifyCodeBizIdConstraint;
import ace.captcha.base.define.constraint.CaptchaVerifyCodeConstraint;
import ace.common.base.define.model.constraint.AppIdConstraint;
import ace.user.app.logic.define.constraint.InviterCodeConstraint;
import ace.user.app.logic.define.constraint.PasswordConstraint;
import ace.user.app.logic.define.constraint.UserNameConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/18 11:16
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterByUserNameRequest implements IRegisterRequest {
    @AppIdConstraint
    @ApiModelProperty(value = AppIdConstraint.FIELD_NAME, required = true)
    private String appId;
    @PasswordConstraint
    @ApiModelProperty(value = PasswordConstraint.FIELD_NAME, required = true)
    private String password;
    @InviterCodeConstraint
    @ApiModelProperty(value = InviterCodeConstraint.FIELD_NAME, required = false)
    private String invitorCode;
    @UserNameConstraint
    @ApiModelProperty(value = UserNameConstraint.FIELD_NAME, required = true)
    private String userName;
    @CaptchaVerifyCodeBizIdConstraint
    @ApiModelProperty(value = CaptchaVerifyCodeBizIdConstraint.FIELD_NAME, required = true)
    private String verifyCodeBizId;
    @CaptchaVerifyCodeConstraint
    @ApiModelProperty(value = CaptchaVerifyCodeConstraint.FIELD_NAME, required = true)
    private String verifyCode;

    @AccountRegisterSourceConstraint
    @ApiModelProperty(value = AccountRegisterSourceConstraint.FIELD_NAME, required = true)
    private String sourceType;
}