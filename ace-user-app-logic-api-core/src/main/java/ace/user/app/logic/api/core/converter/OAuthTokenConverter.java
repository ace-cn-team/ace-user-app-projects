package ace.user.app.logic.api.core.converter;

import ace.user.app.logic.define.module.model.dto.OAuth2TokenDto;
import org.mapstruct.Mapper;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/23 15:13
 * @description
 */
@Mapper(componentModel = "spring")
public interface OAuthTokenConverter {
    OAuth2TokenDto toUserToken(ace.cas.base.define.model.bo.OAuth2Token oAuth2Token);
}
