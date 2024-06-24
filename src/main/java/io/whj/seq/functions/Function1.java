package io.whj.seq.functions;

public interface Function1<T1, R> extends Function<R>, java.util.function.Function<T1,R> {
    R apply(T1 var1);
}
