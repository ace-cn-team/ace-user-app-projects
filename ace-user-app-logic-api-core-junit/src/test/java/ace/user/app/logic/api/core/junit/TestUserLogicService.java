package ace.user.app.logic.api.core.junit;

import ace.account.base.define.dao.enums.account.AccountRegisterSourceEnum;
import ace.account.base.define.enums.LoginSourceEnum;
import ace.fw.data.model.PageResponse;
import ace.fw.data.model.Sort;
import ace.fw.data.model.request.resful.PageQueryRequest;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.AceRandomUtils;
import ace.fw.util.DateUtils;
import ace.user.app.logic.api.service.IdentityLogicService;
import ace.user.app.logic.api.service.UserLogicService;
import ace.user.app.logic.define.model.request.identity.GetCurrentUserRequest;
import ace.user.app.logic.define.model.request.identity.LogoutRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByMobileRequest;
import ace.user.app.logic.define.model.request.identity.login.LoginByUserNameRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByMobileRequest;
import ace.user.app.logic.define.model.request.identity.register.RegisterByUserNameRequest;
import ace.user.app.logic.define.model.request.user.ModifyUserInfoRequest;
import ace.user.app.logic.define.model.response.identity.GetCurrentUserResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByMobileResponse;
import ace.user.app.logic.define.model.response.identity.login.LoginByUserNameResponse;
import ace.user.app.logic.define.model.vo.OAuth2TokenVo;
import ace.user.base.api.UserBaseApi;
import ace.user.base.define.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JUnitApplication.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TestUserLogicService {

    @Autowired
    private UserLogicService userLogicService;
    @Autowired
    private UserBaseApi userBaseApi;

    /**
     * 测试手机号码注册
     */
    @Test
    public void test_0001_modifyUserInfo() {
        User user = userBaseApi.page(PageQueryRequest.builder()
                .pageIndex(1)
                .pageSize(1)
                .sorts(Arrays.asList(
                        Sort.builder()
                                .asc(true)
                                .field(User.ID)
                                .build()
                        )
                )
                .build()
        ).check().getData().get(0);

        String avatarUrl = DateUtils.getNowFormat(DateUtils.FROMAT_yyyyMMddHHmmssSS);
        String nickName = avatarUrl;

        userLogicService.modifyUserInfo(ModifyUserInfoRequest.builder()
                .avatarUrl(avatarUrl)
                .birthday(null)
                .nickName(nickName)
                .accountId(user.getId())
                .build()
        ).check();

        User updateUser = userBaseApi.getById(user.getId()).check();

        Assert.assertEquals(updateUser.getAvatarUrl(), avatarUrl);
        Assert.assertEquals(updateUser.getNickName(), nickName);

        Assert.assertNotEquals(user.getAvatarUrl(), avatarUrl);
        Assert.assertNotEquals(user.getNickName(), nickName);

        Assert.assertEquals(user.getSignature(), updateUser.getSignature());
    }

}