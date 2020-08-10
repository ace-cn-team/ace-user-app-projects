package ace.user.app.logic.api.core.junit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/3 15:21
 * @description
 */
@EnableFeignClients
@SpringBootApplication
public class JUnitApplication {
    public static void main(String[] args) {
        SpringApplication.run(JUnitApplication.class, args);
    }
}
