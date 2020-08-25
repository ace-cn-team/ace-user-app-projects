package ace.user.app.logic.define.model.request.identity.modifypassword;

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
public class ModifyPasswordBySmsVerifyCodeRequest implements IModifyPasswordRequest, IAppId {
    private final static String SMS_VERIFY_CODE_REMARK = SMSVerifyCodeConstraint.FIELD_NAME + "," + "短信业务类型标识：" + UserLogicConstants.SMS_VERIFY_CODE_BIZ_TYPE_MODIFY_PASSWORD;
    @SMSVerifyCodeConstraint
    @ApiModelProperty(required = true, value = ModifyPasswordBySmsVerifyCodeRequest.SMS_VERIFY_CODE_REMARK)
    private String smsVerifyCode;
    @MobileConstraint
    @ApiModelProperty(required = true, value = MobileConstraint.FIELD_NAME)
    private String mobile;
    @NewPasswordConstraint
    @ApiModelProperty(required = true, value = NewPasswordConstraint.FIELD_NAME)
    private String newPassword;
    @AppIdConstraint
    @ApiModelProperty(required = true, value = AppIdConstraint.FIELD_NAME)
    private String appId;
}
