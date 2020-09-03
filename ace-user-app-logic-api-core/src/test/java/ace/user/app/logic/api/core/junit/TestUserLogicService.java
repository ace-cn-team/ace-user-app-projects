package ace.user.app.logic.api.core.junit;

import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.util.QueryUtils;
import ace.fw.util.DateUtils;
import ace.user.app.logic.api.core.JUnitApplication;
import ace.user.app.logic.api.service.UserLogicService;
import ace.user.app.logic.define.model.request.user.ModifyUserInfoRequest;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JUnitApplication.class})
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
        User user = userBaseApi.page(PageRequest.builder()
                .pager(QueryUtils.pager(0, 1))
                .orderBy(QueryUtils.orderByAsc(User::getId))
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

        User updateUser = userBaseApi.findById(user.getId()).check();

        Assert.assertEquals(updateUser.getAvatarUrl(), avatarUrl);
        Assert.assertEquals(updateUser.getNickName(), nickName);

        Assert.assertNotEquals(user.getAvatarUrl(), avatarUrl);
        Assert.assertNotEquals(user.getNickName(), nickName);

        Assert.assertEquals(user.getSignature(), updateUser.getSignature());
    }

}