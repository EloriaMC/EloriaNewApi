package fr.eloria.api.utils.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ECommand {

    String name();

    String[] aliases() default {};

    String permission() default "";

    boolean appendStrings() default true;

}