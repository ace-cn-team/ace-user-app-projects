package ace.user.app.logic.api.core.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestIdentityBaseApiService.class,
        TestIdentityLogicService.class,
        TestUserLogicService.class
})
public class TestSuite {


}
