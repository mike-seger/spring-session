package org.springframework.session.data.redis.config.annotation.web.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RedisWebSessionConfiguration.class)
@Configuration(proxyBeanMethods = false)
public @interface EnableRedisWebSession {
	int maxInactiveIntervalInSeconds() default MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
	String redisNamespace() default "spring:session";
	SaveMode saveMode() default SaveMode.ON_SET_ATTRIBUTE;
}
