package com.multibankfx.chatapp.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.client.util.ClientStateListener;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartupInitializerService {

    @Value("${hazelcast.uri}")
    private String hazelcastUri;

    public static ClientConfig hazelcastConfig;
    public static ClientStateListener hazelcastClientStateListener;
    public static HazelcastInstance hazelcastClient;

    public void initializeHazelcast() {
        hazelcastConfig = new ClientConfig();
        //config.getSerializationConfig().addPortableFactory(2, new MT4PortableFactory());
        //config.getSerializationConfig().addDataSerializableFactory(3, new MT4DataSerializableFactory());
        hazelcastConfig.getNetworkConfig().setConnectionAttemptLimit(0);
        hazelcastConfig.getConnectionStrategyConfig().setAsyncStart(false);
        hazelcastConfig.getNetworkConfig().addAddress(hazelcastUri);
        hazelcastConfig.getConnectionStrategyConfig().setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);
        hazelcastClientStateListener = new ClientStateListener(hazelcastConfig);
        hazelcastClient = HazelcastClient.newHazelcastClient(hazelcastConfig);
    }

}
