package ace.user.app.logic.api.autoconfigure;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/27 14:30
 * @description
 */
@Configuration
@ComponentScan(value = {
        "ace.user.app.logic.api.core"
})
public class UserAppLogicApiAutoConfigure {
}
