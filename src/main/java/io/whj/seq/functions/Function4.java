package io.whj.seq.functions;

public interface Function4<T1, T2, T3, T4, R> extends Function<R> {
    R apply(T1 var1, T2 var2, T3 var3, T4 var4);
}
