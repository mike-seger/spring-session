package org.springframework.session.data.redis.config.annotation.web.http;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.*;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Configuration(proxyBeanMethods = false)
public class RedisHttpSessionConfiguration extends SpringHttpSessionConfiguration
		implements BeanClassLoaderAware, EmbeddedValueResolverAware, ImportAware {

	static final String DEFAULT_CLEANUP_CRON = "0 * * * * *";

	private Integer maxInactiveIntervalInSeconds = MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

	private String redisNamespace = "spring:session";//RedisIndexedSessionRepository.DEFAULT_NAMESPACE;

	private FlushMode flushMode = FlushMode.ON_SAVE;

	private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;

	private String cleanupCron = DEFAULT_CLEANUP_CRON;

	private RedisConnectionFactory redisConnectionFactory;

	private IndexResolver<Session> indexResolver;

	private RedisSerializer<Object> defaultRedisSerializer;

	private ApplicationEventPublisher applicationEventPublisher;

	private Executor redisTaskExecutor;

	private Executor redisSubscriptionExecutor;

	private ClassLoader classLoader;

	private StringValueResolver embeddedValueResolver;

	@Bean
	public MapSessionRepository sessionRepository() {
		return new MapSessionRepository(new ConcurrentHashMap<>());
	}

	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}

	public void setRedisNamespace(String namespace) {
		this.redisNamespace = namespace;
	}

	public void setFlushMode(FlushMode flushMode) {
		Assert.notNull(flushMode, "flushMode cannot be null");
		this.flushMode = flushMode;
	}

	public void setSaveMode(SaveMode saveMode) {
		this.saveMode = saveMode;
	}

	public void setCleanupCron(String cleanupCron) {
		this.cleanupCron = cleanupCron;
	}

	@Autowired
	public void setRedisConnectionFactory(
			/*@SpringSessionRedisConnectionFactory ObjectProvider<RedisConnectionFactory> springSessionRedisConnectionFactory,*/
			ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
//		RedisConnectionFactory redisConnectionFactoryToUse = springSessionRedisConnectionFactory.getIfAvailable();
//		if (redisConnectionFactoryToUse == null) {
//			redisConnectionFactoryToUse = redisConnectionFactory.getObject();
//		}
//		this.redisConnectionFactory = redisConnectionFactoryToUse;
	}

	@Autowired(required = false)
	@Qualifier("springSessionDefaultRedisSerializer")
	public void setDefaultRedisSerializer(RedisSerializer<Object> defaultRedisSerializer) {
		this.defaultRedisSerializer = defaultRedisSerializer;
	}

	@Autowired
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Autowired(required = false)
	public void setIndexResolver(IndexResolver<Session> indexResolver) {
		this.indexResolver = indexResolver;
	}

	@Autowired(required = false)
	@Qualifier("springSessionRedisTaskExecutor")
	public void setRedisTaskExecutor(Executor redisTaskExecutor) {
		this.redisTaskExecutor = redisTaskExecutor;
	}

	@Autowired(required = false)
	@Qualifier("springSessionRedisSubscriptionExecutor")
	public void setRedisSubscriptionExecutor(Executor redisSubscriptionExecutor) {
		this.redisSubscriptionExecutor = redisSubscriptionExecutor;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> attributeMap = importMetadata
				.getAnnotationAttributes(EnableRedisHttpSession.class.getName());
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(attributeMap);
		this.maxInactiveIntervalInSeconds = attributes.getNumber("maxInactiveIntervalInSeconds");
		String redisNamespaceValue = attributes.getString("redisNamespace");
		if (StringUtils.hasText(redisNamespaceValue)) {
			this.redisNamespace = this.embeddedValueResolver.resolveStringValue(redisNamespaceValue);
		}
		this.flushMode = attributes.getEnum("flushMode");
		this.saveMode = attributes.getEnum("saveMode");
		String cleanupCron = attributes.getString("cleanupCron");
		if (StringUtils.hasText(cleanupCron)) {
			this.cleanupCron = cleanupCron;
		}
	}
}
