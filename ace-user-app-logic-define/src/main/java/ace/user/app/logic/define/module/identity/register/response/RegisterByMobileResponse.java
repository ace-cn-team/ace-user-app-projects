package ace.user.app.logic.define.module.identity.register.response;

import ace.user.app.logic.define.module.model.dto.OAuth2TokenDto;
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
@NoArgsConstructor
@AllArgsConstructor
public class RegisterByMobileResponse implements IRegisterResponse {
    @ApiModelProperty(value = "token")
    private OAuth2TokenDto token;
}