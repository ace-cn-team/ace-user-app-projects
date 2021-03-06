package ace.user.app.logic.define.model.response.identity.register;

import ace.user.app.logic.define.model.vo.OAuth2TokenVo;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 16:46
 * @description
 */
public interface IRegisterResponse {

    OAuth2TokenVo getToken();

    void setToken(OAuth2TokenVo oAuth2TokenVo);
}
