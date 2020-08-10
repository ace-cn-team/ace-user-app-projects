package ace.user.app.logic.define.model.request.identity.login;

import ace.account.base.define.constraint.AccountLoginSourceConstraint;
import ace.common.base.define.model.constraint.AppIdConstraint;
import ace.common.base.define.model.constraint.MobileConstraint;
import ace.user.app.logic.define.constraint.PasswordConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/19 18:28
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginByMobileRequest implements ILoginCoreRequest {

    @AppIdConstraint
    @ApiModelProperty(value = AppIdConstraint.FIELD_NAME, required = true)
    private String appId;
    @MobileConstraint
    @ApiModelProperty(value = MobileConstraint.FIELD_NAME, required = true)
    private String mobile;
    @PasswordConstraint
    @ApiModelProperty(value = PasswordConstraint.FIELD_NAME, required = true)
    private String password;
    @AccountLoginSourceConstraint
    @ApiModelProperty(value = AccountLoginSourceConstraint.FIELD_NAME, required = true)
    private String sourceType;
}
