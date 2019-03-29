package bmatser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 通过该注解可以在方法中获取LoginInfoUtil登录信息
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Login {
    public String saas() default "";

    public String app() default "";

    public String mall() default "";
}