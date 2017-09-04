package be.vsko.jss.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	
	String columnName();
	
	boolean isForeignKey() default false;
}
