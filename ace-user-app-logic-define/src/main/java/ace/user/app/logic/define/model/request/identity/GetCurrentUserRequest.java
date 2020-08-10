package ace.user.app.logic.define.model.request.identity;

import ace.common.base.define.model.constraint.AppIdConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/30 15:37
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCurrentUserRequest {
    @ApiModelProperty(value = "access token", required = true)
    private String accessToken;
}
