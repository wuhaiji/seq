package io.whj.seq.functions;

public interface Function2<T1, T2, R> extends Function<R> {
    R apply(T1 var1, T2 var2);
}
