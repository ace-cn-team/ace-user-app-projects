package ace.user.app.logic.api.core.biz.identity;

import ace.cas.base.api.facade.OAuth2BaseApiFacade;
import ace.cas.base.define.model.bo.OAuth2Profile;
import ace.cas.base.define.model.request.facade.OAuth2GetProfileFacadeRequest;
import ace.fw.model.response.GenericResponseExt;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.base.api.cache.UserCacheClientBaseApi;
import ace.user.base.define.constant.CacheConstants;
import ace.user.base.define.dao.entity.User;;
import ace.user.base.define.module.user.request.FindByAppIdAndIdRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/25 14:51
 * @description 获取当前用户信息
 */
@Component
@Slf4j
public class GetCurrentUserBiz {

    @Autowired
    private OAuth2BaseApiFacade oAuth2BaseApiFacade;
    @Autowired
    private UserCacheClientBaseApi userCacheClientBaseApi;

    public GetCurrentUserResponse getCurrentUser(GetCurrentUserRequest request) {
        OAuth2Profile profile = this.getProfile(request);

        User user = userCacheClientBaseApi.findByAppIdAndIdFromMultiCacheOrDb(
                FindByAppIdAndIdRequest.builder()
                        .appId(profile.getAppId())
                        .id(profile.getAccountId())
                        .build()
        ).check();

        return GetCurrentUserResponse.builder().user(user).build();
    }

    private OAuth2Profile getProfile(GetCurrentUserRequest request) {
        GenericResponseExt<OAuth2Profile> profileResponse = oAuth2BaseApiFacade.getProfile(OAuth2GetProfileFacadeRequest.builder()
                .accessToken(request.getAccessToken())
                .build()
        );
        return profileResponse.check();
    }
}