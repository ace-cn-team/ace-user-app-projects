package ace.user.app.logic.define.model.response.identity.login;

import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
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
public class LoginByMobileResponse implements ILoginCoreResponse {
    @ApiModelProperty(value = "token")
    private OAuth2TokenVo token;

}