package ace.user.app.logic.api.service;


import ace.fw.model.response.GenericResponseExt;
import ace.user.app.logic.define.model.request.user.ModifyUserInfoRequest;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/18 10:07
 * @description
 */
public interface UserLogicService {
    /**
     * 更新用户信息
     *
     * @param request
     * @return
     */
    GenericResponseExt<Boolean> modifyUserInfo(@Valid ModifyUserInfoRequest request);
}
