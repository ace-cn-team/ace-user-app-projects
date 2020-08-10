package ace.user.app.logic.define.constraint;

import ace.user.app.logic.define.constants.PatternConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/26 13:27
 * @description
 */
@NotBlank(message = "请输入" + PasswordConstraint.FIELD_NAME)
@Pattern(regexp = PatternConstants.PASSWORD, message = "请输入正确的" + PasswordConstraint.FIELD_NAME +
        ",必须包含大写字母、小写字母、数字与特殊符号的6至32位字符组合")
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface PasswordConstraint {
    String FIELD_NAME = "密码";

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
