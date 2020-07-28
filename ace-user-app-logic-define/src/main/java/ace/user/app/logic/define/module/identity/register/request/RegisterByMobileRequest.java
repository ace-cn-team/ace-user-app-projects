package ace.user.app.logic.define.module.identity.register.request;

import ace.account.base.define.constraint.AccountRegisterSourceConstraint;
import ace.common.base.define.model.constraint.AppIdConstraint;
import ace.common.base.define.model.constraint.MobileConstraint;
import ace.user.app.logic.define.constraint.InviterCodeConstraint;
import ace.user.app.logic.define.constraint.PasswordConstraint;
import ace.user.app.logic.define.constraint.SMSVerifyCodeConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class RegisterByMobileRequest implements IRegisterRequest {
    @AppIdConstraint
    @ApiModelProperty(value = AppIdConstraint.FIELD_NAME, required = true)
    private String appId;
    @PasswordConstraint
    @ApiModelProperty(value = PasswordConstraint.FIELD_NAME, required = true)
    private String password;
    @InviterCodeConstraint
    @ApiModelProperty(value = InviterCodeConstraint.FIELD_NAME, required = false)
    private String invitorCode;
    @MobileConstraint
    @ApiModelProperty(value = MobileConstraint.FIELD_NAME, required = true)
    private String mobile;
    @SMSVerifyCodeConstraint
    @ApiModelProperty(value = SMSVerifyCodeConstraint.FIELD_NAME, required = true)
    private String verifyCode;

    @AccountRegisterSourceConstraint
    @ApiModelProperty(value = AccountRegisterSourceConstraint.FIELD_NAME, required = true)
    private String sourceType;

}