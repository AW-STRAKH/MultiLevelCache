package com.awatansh.service;

import com.awatansh.model.ReadResponse;
import com.awatansh.model.StatsResponse;
import com.awatansh.model.WriteResponse;
import com.awatansh.provider.DefaultLevelCache;
import com.awatansh.provider.ILevelCache;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

//service that users will call
//ALWAYS POINTS TO L1 CACHE
public class CacheService<Key, Value> {

    private final ILevelCache<Key, Value> multiLevelCache;
    //BONUS
    private final List<Double> lastReadTimes;
    //BONUS
    private final List<Double> lastWriteTimes;
    //HOW MANY LASTS TO KEEP TRACK OF (I.E AVG TIME OF LAST 10 READ OR WRITE OPS)
    private final int lastCount;

    public CacheService(@NonNull final DefaultLevelCache<Key, Value> multiLevelCache, final int lastCount) {
        this.multiLevelCache = multiLevelCache;
        this.lastCount = lastCount;
        this.lastReadTimes = new ArrayList<>(lastCount);
        this.lastWriteTimes = new ArrayList<>(lastCount);
    }

    public WriteResponse set(@NonNull final Key key, @NonNull final Value value) {
        final WriteResponse writeResponse = multiLevelCache.set(key, value);
        addTimes(lastWriteTimes, writeResponse.getTimeTaken());
        return writeResponse;
    }

    public ReadResponse<Value> get(@NonNull final Key key) {
        final ReadResponse<Value> readResponse = multiLevelCache.get(key);
        addTimes(lastReadTimes, readResponse.getTotalTime());
        return readResponse;
    }

    public StatsResponse stats() {
        return new StatsResponse(getAvgReadTime(), getAvgWriteTime(), multiLevelCache.getUsages());
    }

    private Double getAvgReadTime() {
        return getSum(lastReadTimes)/lastReadTimes.size();
    }

    private Double getAvgWriteTime() {
        return getSum(lastWriteTimes)/lastWriteTimes.size();
    }

    private void addTimes(List<Double> times, Double time) {
        if (times.size() == this.lastCount) {
            times.remove(0);
        }

        times.add(time);
    }

    private Double getSum(List<Double> times) {
        Double sum = 0.0;
        for (Double time: lastReadTimes) {
            sum += time;
        }
        return sum;
    }
}
