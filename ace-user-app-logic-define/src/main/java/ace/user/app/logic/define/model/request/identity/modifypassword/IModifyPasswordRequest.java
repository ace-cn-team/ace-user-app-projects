package ace.user.app.logic.define.model.request.identity.modifypassword;

import ace.common.base.define.model.bo.IAccountId;
import ace.user.app.logic.define.constraint.NewPasswordConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/21 11:23
 * @description
 */
public interface IModifyPasswordRequest {

    /**
     * 新密码
     *
     * @return
     */
    String getNewPassword();

    void setNewPassword(String newPassword);

}
