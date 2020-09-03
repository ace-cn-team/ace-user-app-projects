package ace.user.app.logic.api.core.biz.user;

import ace.authentication.base.api.AccountBaseApi;
import ace.authentication.base.define.dao.model.entity.Account;
import ace.authentication.base.define.enums.AccountBusinessErrorEnum;
import ace.fw.restful.base.api.constant.RestApiConstants;
import ace.fw.util.BusinessErrorUtils;
import ace.user.app.logic.define.enums.UserLogicBusinessErrorEnum;
import ace.user.app.logic.define.model.request.user.ModifyUserInfoRequest;
import ace.user.base.api.UserBaseApi;
import ace.user.base.define.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/4 16:41
 * @description 更新用户基本信息或者新增用户基本信息
 */
@Component
@Slf4j
public class ModifyUserInfoBiz {
    @Autowired
    private UserBaseApi userBaseApi;
    @Autowired
    private AccountBaseApi accountBaseApi;

    public void modifyUserInfo(ModifyUserInfoRequest request) {

        User user = userBaseApi.findById(request.getAccountId()).check();

        if (user == null) {
            this.insert(request);
        } else {
            this.update(request, user);
        }
    }

    private void update(ModifyUserInfoRequest request, User originalUser) {

        User modifyUser = User.builder()
                .id(originalUser.getId())
                .sex(request.getSex())
                .avatarUrl(request.getAvatarUrl())
                .signature(request.getSignature())
                .nickName(request.getNickName())
                .birthday(request.getBirthday())
                .build();

        boolean result = userBaseApi.updateByIdVersionAutoUpdate(modifyUser).check();

        if (result == false) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.DATA_EXPIRE);
        }
    }

    private void insert(ModifyUserInfoRequest request) {
        Account account = accountBaseApi.findById(request.getAccountId()).check();

        if (account == null) {
            BusinessErrorUtils.throwNew(AccountBusinessErrorEnum.EXIST_NOT_ACCOUNT);
        }

        User insertUser = User.builder()
                .id(request.getAccountId())
                .rowVersion(1)
                .sex(request.getSex())
                .avatarUrl(request.getAvatarUrl())
                .signature(request.getSignature())
                .nickName(request.getNickName())
                .birthday(request.getBirthday())
                .appId(account.getAppId())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        boolean result = userBaseApi.save(insertUser).check();

        if (result == false) {
            BusinessErrorUtils.throwNew(UserLogicBusinessErrorEnum.DATA_EXPIRE);
        }
    }
}
