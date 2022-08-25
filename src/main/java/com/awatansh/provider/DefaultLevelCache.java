package com.awatansh.provider;

import com.awatansh.model.LevelCacheData;
import com.awatansh.model.ReadResponse;
import com.awatansh.model.WriteResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

//CacheService -> L1 -> L2 -> L3...Ln -> null
//                C1    C2    ...
@AllArgsConstructor
public class DefaultLevelCache<Key, Value> implements ILevelCache<Key, Value> {
    //info about levels i.e reAD AND WRITE
    private final LevelCacheData levelCacheData;

    //data is stored here
    // capacity is the property of cache provider
    private final CacheProvider<Key, Value> cacheProvider;

    @NonNull
    //pointer to next level
    private final ILevelCache<Key, Value> next;

    @NonNull
    //recursion
    public WriteResponse set(Key key, Value value) {
        Double curTime = 0.0;
        Value curLevelValue = cacheProvider.get(key);
        curTime += levelCacheData.getReadTime();
        if (!value.equals(curLevelValue)) {
            cacheProvider.set(key, value);
            curTime += levelCacheData.getWriteTime();
        }

        curTime += next.set(key, value).getTimeTaken();
        return new WriteResponse(curTime);
    }

    @NonNull
    //recursion
    public ReadResponse<Value> get(Key key) {
        Double curTime = 0.0;
        Value curLevelValue = cacheProvider.get(key);
        curTime += levelCacheData.getReadTime();

        // L1 -> L2 -> L3 -> L4
        if (curLevelValue == null) {
            ReadResponse<Value> nextResponse = next.get(key);
            curTime += nextResponse.getTotalTime();
            curLevelValue = nextResponse.getValue();
            if (curLevelValue != null) {
                cacheProvider.set(key, curLevelValue);
                curTime += levelCacheData.getWriteTime();
            }
        }

        return new ReadResponse<>(curLevelValue, curTime);
    }

    @NonNull
    //recursion
    public List<Double> getUsages() {
        final List<Double> usages;
        if (next == null) {
            usages = Collections.emptyList();
        } else {
            usages = next.getUsages();
        }

        usages.add(0, cacheProvider.getCurrentUsage());
        return usages;
    }
}
