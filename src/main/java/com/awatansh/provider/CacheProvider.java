package com.awatansh.provider;

import com.awatansh.policy.EvictionPolicy;
import com.awatansh.storage.Storage;
import com.awatansh.exceptions.StorageFullException;

public class CacheProvider<Key, Value> {

    private final EvictionPolicy<Key> evictionPolicy;
    //capacity used for storage
    private final Storage<Key, Value> storage;

    public CacheProvider(EvictionPolicy<Key> evictionPolicy, Storage<Key, Value> storage) {
        this.evictionPolicy = evictionPolicy;
        this.storage = storage;
    }

    public void set(Key key, Value value) {
        try {
            this.storage.add(key, value);
            this.evictionPolicy.keyAccessed(key);
        } catch (StorageFullException exception) {
            final Key keyToRemove = evictionPolicy.evictKey();
            if (keyToRemove == null) {
                throw new RuntimeException("Unexpected State.");
            }

            this.storage.remove(keyToRemove);
            set(key, value);
        }
    }

    public Value get(Key key) {
        final Value value = this.storage.get(key);
        this.evictionPolicy.keyAccessed(key);
        return value;
    }

    public double getCurrentUsage() {
        return this.storage.getCurrentUsage();
    }
}
