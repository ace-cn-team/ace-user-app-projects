package ace.user.app.logic.api.core.converter;

import ace.cas.base.define.model.bo.OAuth2Token;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import org.mapstruct.Mapper;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/23 15:13
 * @description
 */
@Mapper(componentModel = "spring")
public interface OAuthTokenConverter {
    OAuth2TokenVo toUserToken(OAuth2Token oAuth2Token);
}
