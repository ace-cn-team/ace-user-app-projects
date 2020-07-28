package ace.user.app.logic.define.module.identity.login.response;

import ace.user.app.logic.define.module.model.dto.OAuth2TokenDto;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:46
 * @description
 */
public interface ILoginCoreResponse {
    OAuth2TokenDto getToken();

    void setToken(OAuth2TokenDto oAuth2Token);
}
