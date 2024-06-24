package io.whj.seq.functions;

import java.util.function.Function;

public interface Fn1<T1, R> extends Fn<R>, Function<T1,R> {
    R apply(T1 var1);
}
