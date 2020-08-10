package ace.user.app.logic.define.model.request.user;

import ace.common.base.define.model.bo.IAccountId;
import ace.common.base.define.model.bo.IAppId;
import ace.user.app.logic.define.constraint.UserSexConstraint;
import ace.user.base.define.dao.enums.user.UserSexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/4 14:39
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserInfoRequest implements IAccountId {
    @ApiModelProperty(value = "用户Id")
    private String accountId;
    //    @ApiModelProperty(value = "应用Id")
//    private String appId;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * {@link UserSexEnum}
     */
    @UserSexConstraint
    @ApiModelProperty(value = "性别,0-未知,1-男,2-女")
    private Integer sex;
    @Length(min = 1, max = 255, message = "请输入正确的头像")
    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;
    @Length(min = 1, max = 255, message = "请输入正确的头像")
    @ApiModelProperty(value = "个人签名")
    private String signature;

}
