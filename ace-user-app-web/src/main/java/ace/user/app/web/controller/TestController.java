package ace.user.app.web.controller;

import ace.authentication.base.api.TestBaseApi;
import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import ace.fw.model.response.GenericResponseExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/20 14:19
 * @description
 */
@RestController
@Validated
@LogAspect
public class TestController {
    @Autowired
    private TestBaseApi testBaseApi;

    @RequestMapping(value = "/test")
    public GenericResponseExt<Boolean> test() {
        return testBaseApi.test();
    }
}
