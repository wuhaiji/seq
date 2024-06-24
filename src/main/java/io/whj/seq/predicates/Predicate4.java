package io.whj.seq.predicates;

import java.util.function.Predicate;

public interface Predicate4<T1, T2, T3, T4> extends Predicate<T1> {
    boolean test(T1 var1, T2 var2, T3 var3, T4 var4);
}
