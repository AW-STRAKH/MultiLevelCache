package com.awatansh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//this is for level of cache not the particular cache
// storage capacity is a property of a cache in a particular level not its level hence no property for storage capacity
public class LevelCacheData {
    int readTime;
    int writeTime;
}
