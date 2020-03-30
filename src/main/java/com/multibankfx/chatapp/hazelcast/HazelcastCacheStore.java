package com.multibankfx.chatapp.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ReplicatedMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HazelcastCacheStore<V> {

    private final HazelcastInstance hzInstance;
    private final String mapName;
    private final long ttl;


    public HazelcastCacheStore(HazelcastInstance hzInstance, String mapName, long ttl) {
        this.hzInstance = hzInstance;
        this.mapName = mapName;
        this.ttl = ttl;
    }

    public V get(String key) {
        Object optional = getReplicatedMap().get(key);
        if (optional==null) {
            if (containsKey(key)) {
                evict(key);
            }
            return null;
        }

        return (V) optional;
    }

    public boolean containsKey(String key) {
        return getReplicatedMap().containsKey(key);
    }

    public ReplicatedMap<String, Object> getReplicatedMap() {
        return hzInstance.getReplicatedMap(mapName);
    }

    public IMap<String, Object> getMap() {
        return hzInstance.getMap(mapName);
    }


    public void put(String key, V value) {
        put(key, value, ttl);
    }

    public void put(String key, V value, long ttl) {
        if (value== null) {
            ttl = 300;
            getReplicatedMap().put(key, null, ttl, TimeUnit.SECONDS);
        } else {
            getReplicatedMap().put(key, value, ttl, TimeUnit.SECONDS);
        }
    }

    public void evict(String key) {
        getReplicatedMap().remove(key);
    }

    public void evictMap(String key) {
        getMap().remove(key);
    }

    public void clear() {
        getReplicatedMap().clear();
    }

    public List<?> getList(String s) {
        return hzInstance.getList(s);
    }

    public HazelcastInstance getHzInstance() {
        return hzInstance;
    }

    public String getMapName() {
        return mapName;
    }



}
