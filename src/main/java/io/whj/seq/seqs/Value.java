package io.whj.seq.seqs;

import io.vavr.collection.List;

public interface Value<T,R> {
    
    R unit(T t);
    
    default void test(){
    
        List.of();
    }
    
}
