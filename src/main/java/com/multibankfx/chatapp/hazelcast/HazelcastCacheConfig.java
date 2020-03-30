package com.multibankfx.chatapp.hazelcast;

import java.util.Arrays;

import com.hazelcast.config.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@ConfigurationProperties(prefix = "cache.hz")
@EnableAsync
public class HazelcastCacheConfig extends CacheFactory {

	public static final String HZ_DEFAULT_MAP = "defaultMap";

	/**
	 * Port of hazelcast (is incremented if is in use
	 */
	@Value("${port:5900}")
	private int port;

	@Value("${cluster_ip:localhost:6010}")
	private String clusterIp;

	@Value("${connectionTimeout:3000}")
	private int connectionTimeout;

	@Value("${groupName:cluster}")
	private String groupName;

	@Value("${groupPasswd:cluster}")
	private String groupPasswd;

	private Multicast multicast = new Multicast();

	private Group group = new Group();

	private String[] interfaces;

	private Config getBaseConfig() {
		Config config = new Config();

		setInterfaces(config);

		applyProperties(config);

		if (StringUtils.isNoneBlank(group.name) && StringUtils.isNoneBlank(group.pwd)) {
			config.getGroupConfig().setName(group.name);
			config.getGroupConfig().setName(group.pwd);
		}

		config.getNetworkConfig().setPort(port);
		config.getNetworkConfig().setPortAutoIncrement(true);

		addReplicatedMapToConfig(config, HZ_DEFAULT_MAP);

		return config;
	}

	private static void applyProperties(Config config){
		/**
		 * Hazelcast Properties Reference (com/turkcell/fizy/common/util/cache/config/hazelcast-3.10.3-system-properties.csv)
		 */
//		config.setProperty("hazelcast.logging.type", "none");
//		config.setProperty("hazelcast.operation.generic.thread.count", "16");
//		config.setProperty("hazelcast.operation.thread.count", "16");
//		config.setProperty("hazelcast.executionservice.taskscheduler.remove.oncancel", "true");
//		config.setProperty("hazelcast.health.monitoring.level", "OFF");
	}

	public static void addDistributedMapToConfig(Config config, String mapName, int ttl){
		MapConfig mapConfig = new MapConfig();
		mapConfig.setName(mapName);
		mapConfig.setBackupCount(1);
		mapConfig.setAsyncBackupCount(0);
		mapConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
		mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
		mapConfig.setTimeToLiveSeconds(ttl);

		config.addMapConfig(mapConfig);
	}

	public static void addReplicatedMapToConfig(Config config, String replicatedMapName){
		ReplicatedMapConfig replicatedMapConfig = new ReplicatedMapConfig();
		replicatedMapConfig.setAsyncFillup(false);
		replicatedMapConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
		replicatedMapConfig.setName(replicatedMapName);

		config.addReplicatedMapConfig(replicatedMapConfig);
	}

	@Bean(name = "hzInstanceLocal")
	public HazelcastInstance getHzInstanceLocal() {
		Config config = getBaseConfig();
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		return Hazelcast.newHazelcastInstance(config);
	}

	@Bean(name = "hzInstanceMicroservice")
	@Lazy
	public HazelcastInstance getHzInstanceMicroservice() {
		Config config = getBaseConfig();
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastGroup(multicast.group);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastPort(multicast.microserviceport);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastTimeToLive(1); // same subnet
		return Hazelcast.newHazelcastInstance(config);
	}

	@Bean(name = "hzInstanceBroadcast")
	@Lazy
	public HazelcastInstance getHzInstanceBroadcast() {
		Config config = getBaseConfig();
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastGroup(multicast.group);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastPort(multicast.broadcastport);
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastTimeToLive(1); // router
		return Hazelcast.newHazelcastInstance(config);
	}
	
	private void setInterfaces(Config config) {
		if (!ArrayUtils.isEmpty(interfaces)) {
			InterfacesConfig ic = new InterfacesConfig();
			ic.setInterfaces(Arrays.asList(interfaces));
			ic.setEnabled(true);
			config.getNetworkConfig().setInterfaces(ic);
		}
	}


	public String[] getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Multicast getMulticast() {
		return multicast;
	}

	public void setMulticast(Multicast multicast) {
		this.multicast = multicast;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	static class Multicast {
		/**
		 * Multicast microserviceport (by convention is equal to #{50000+server.port})
		 */
		private int microserviceport;
		private String group = "230.0.0.1";
		private int broadcastport = 56666;

		public int getMicroserviceport() {
			return microserviceport;
		}

		public void setMicroserviceport(int microserviceport) {
			this.microserviceport = microserviceport;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public int getBroadcastport() {
			return broadcastport;
		}

		public void setBroadcastport(int broadcastport) {
			this.broadcastport = broadcastport;
		}
	}
	static class Group {
		String name = "";
		String pwd = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
	}

}