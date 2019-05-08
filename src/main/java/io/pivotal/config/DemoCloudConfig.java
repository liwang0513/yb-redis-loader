package io.pivotal.config;

import java.io.IOException;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Profile("cloud")
public class DemoCloudConfig {
	
	private EnvParser env = EnvParser.getInstance();
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() throws IOException {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName(env.getHost());
		jedisConFactory.setPort(env.getPort());
		jedisConFactory.setPassword(env.getPasssword());
		return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() throws IOException {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

}
