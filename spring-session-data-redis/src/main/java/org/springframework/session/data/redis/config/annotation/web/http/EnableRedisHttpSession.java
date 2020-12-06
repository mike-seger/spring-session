package org.springframework.session.data.redis.config.annotation.web.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RedisHttpSessionConfiguration.class)
@Configuration(proxyBeanMethods = false)
public @interface EnableRedisHttpSession {
	int maxInactiveIntervalInSeconds() default MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
	String redisNamespace() default "spring:session";//RedisIndexedSessionRepository.DEFAULT_NAMESPACE;
	FlushMode flushMode() default FlushMode.ON_SAVE;
	String cleanupCron() default RedisHttpSessionConfiguration.DEFAULT_CLEANUP_CRON;
	SaveMode saveMode() default SaveMode.ON_SET_ATTRIBUTE;
}
