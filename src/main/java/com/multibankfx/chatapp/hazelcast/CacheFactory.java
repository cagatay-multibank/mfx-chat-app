package com.multibankfx.chatapp.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.multibankfx.chatapp.data.dto.UserSessionDto;
import org.springframework.context.annotation.Bean;

public class CacheFactory {

    public static final int ONE_MINUTE_TTL = 60;
    public static final int FIVE_MINUTE_TTL = 5 * ONE_MINUTE_TTL;
    public static final int ONE_HOUR_TTL = 60 * ONE_MINUTE_TTL;

    @Bean(name = "hzUserSessions")
    public HazelcastCacheStore<UserSessionDto> hzUserSessions(HazelcastInstance hzInstanceMicroservice) {
        String mapName = HazelcastCacheConfig.HZ_DEFAULT_MAP + "_" + (ONE_HOUR_TTL * 3);
        HazelcastCacheConfig.addReplicatedMapToConfig(hzInstanceMicroservice.getConfig(), mapName);
        return new HazelcastCacheStore<>(hzInstanceMicroservice, mapName, ONE_HOUR_TTL * 3);
    }
}
