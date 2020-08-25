package ace.user.app.logic.api.core.provider;

import ace.authentication.base.define.dao.model.entity.Account;
import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.cas.base.define.model.bo.OAuth2Profile;
import ace.cas.base.define.model.bo.OAuth2Token;
import ace.cas.base.define.model.request.facade.OAuth2GetTokenFacadeRequest;
import ace.user.app.logic.api.core.converter.OAuthTokenConverter;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/30 16:46
 * @description
 */
@Component
public class OAuth2Provider {
    @Autowired
    private OAuth2BaseApiFacade oAuth2BaseApiFacade;
    @Autowired
    private OAuthTokenConverter oAuthTokenConverter;

    public OAuth2TokenVo getOAuth2Token(Account savedAccount) {

        OAuth2Token oAuth2Token = oAuth2BaseApiFacade.getOAuth2Token(
                OAuth2GetTokenFacadeRequest
                        .builder()
                        .accountId(savedAccount.getId())
                        .profile(
                                OAuth2Profile.builder()
                                        .accountId(savedAccount.getId())
                                        .appId(savedAccount.getAppId())
                                        .build()
                        )
                        .build()
        ).check();

        return oAuthTokenConverter.toUserToken(oAuth2Token);
    }
}
