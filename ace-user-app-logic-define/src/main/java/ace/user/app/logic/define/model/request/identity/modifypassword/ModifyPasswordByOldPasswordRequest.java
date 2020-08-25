package ace.user.app.logic.define.model.request.identity.modifypassword;

import ace.common.base.define.model.bo.IAccountId;
import ace.user.app.logic.define.constants.PatternConstants;
import ace.user.app.logic.define.constraint.NewPasswordConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/21 11:23
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswordByOldPasswordRequest implements IModifyPasswordRequest {
    @NotBlank(message = "旧密码不能为空")
    @Length(min = 1, max = 36, message = "请输入正确的旧密码")
    @ApiModelProperty(required = true, value = "旧密码")
    private String oldPassword;
    @NewPasswordConstraint
    @ApiModelProperty(required = true, value = NewPasswordConstraint.FIELD_NAME)
    private String newPassword;
    @ApiModelProperty(required = true, value = "用户ID,自动注入")
    private String accountId;
}
