package ace.user.app.logic.api.core.biz.identity;

import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.cas.base.define.model.bo.OAuth2Profile;
import ace.cas.base.define.model.request.facade.OAuth2GetProfileFacadeRequest;
import ace.cas.base.define.model.request.facade.OAuth2RevokeFacadeRequest;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.GenericResponseExtUtils;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.base.api.cache.UserCacheClientBaseApi;
import ace.user.base.define.dao.entity.User;
import ace.user.base.define.module.user.request.FindByAppIdAndIdRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 登出
 */
@Component
@Slf4j
public class LogoutBiz {

    @Autowired
    private OAuth2BaseApiFacade oAuth2BaseApiFacade;

    public GenericResponseExt<Boolean> logout(LogoutRequest request) {
        try {
            oAuth2BaseApiFacade.revoke(OAuth2RevokeFacadeRequest.builder()
                    .token(request.getAccessToken())
                    .build()
            ).check();
        } catch (Throwable ex) {
            log.error("登出失败", ex);
        }
        return GenericResponseExtUtils.buildSuccessWithData(true);
    }
}