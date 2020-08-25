package ace.user.app.logic.define.model.request.identity.modifypassword;

import ace.common.base.define.model.bo.IAccountId;
import ace.common.base.define.model.bo.IAppId;
import ace.common.base.define.model.constraint.AppIdConstraint;
import ace.common.base.define.model.constraint.MobileConstraint;
import ace.user.app.logic.define.constants.UserLogicConstants;
import ace.user.app.logic.define.constraint.NewPasswordConstraint;
import ace.user.app.logic.define.constraint.SMSVerifyCodeConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/21 11:23
 * @description 修改密码，没有限制
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswordByNoLimitRequest implements IModifyPasswordRequest, IAccountId {
    @NewPasswordConstraint
    @ApiModelProperty(required = true, value = NewPasswordConstraint.FIELD_NAME)
    private String newPassword;
    @NotBlank(message = "账号Id不能为空")
    @ApiModelProperty(required = true, value = "账号ID")
    private String accountId;
}
