package com.awatansh.provider;

import com.awatansh.model.ReadResponse;
import com.awatansh.model.WriteResponse;
import lombok.NonNull;

import java.util.List;

//PARTICULAR LEVEL OF CACHE
public interface ILevelCache<Key, Value> {

    @NonNull
    WriteResponse set(Key key, Value value);

    @NonNull
    ReadResponse<Value> get(Key key);

    @NonNull
    List<Double> getUsages();
}
