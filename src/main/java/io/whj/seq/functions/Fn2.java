package io.whj.seq.functions;

public interface Fn2<T1, T2, R> extends Fn<R> {
    R apply(T1 var1, T2 var2);
}
