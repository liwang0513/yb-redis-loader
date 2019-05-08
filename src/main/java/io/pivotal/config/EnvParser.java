package io.pivotal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EnvParser {
	private final static Logger logger = LoggerFactory.getLogger(EnvParser.class);

	private static EnvParser instance;
	private final Pattern p = Pattern.compile("(.*)\\[(\\d*)\\]");

	private EnvParser() {
	}

	public static EnvParser getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (EnvParser.class) {
			if (instance == null) {
				instance = new EnvParser();
			}
		}
		return instance;
	}

	public String getHost() throws IOException {
		Map credentials = getCredentials();
		return (String) credentials.get("host");
	}

	public Integer getPort() throws IOException {
		Map credentials = getCredentials();
		return Integer.valueOf((String) credentials.get("port"));
	}

	public String getPasssword() throws IOException {
		Map credentials = getCredentials();
		return (String) credentials.get("password");
	}

	private Map getCredentials() throws IOException {
		Map credentials = null;
		String envContent = System.getenv().get("VCAP_SERVICES");
		ObjectMapper objectMapper = new ObjectMapper();
		Map services = objectMapper.readValue(envContent, Map.class);
		List gemfireService = getService(services);
		if (gemfireService != null) {
			Map serviceInstance = (Map) gemfireService.get(0);
			credentials = (Map) ((Map) serviceInstance.get("credentials")).get("yedis");
		}
		return credentials;

	}

	private List getService(Map services) {
		List l = (List) services.get("yugabyte-db");
		if (l == null) {
			throw new IllegalStateException("Yugabyte service is not bound to this application");
		}
		return l;
	}
}
