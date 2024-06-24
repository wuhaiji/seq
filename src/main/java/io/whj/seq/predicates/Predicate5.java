package io.whj.seq.predicates;

import java.util.function.Predicate;

public interface Predicate5<T1, T2, T3, T4, T5> extends Predicate<T1> {
    boolean test(T1 var1, T2 var2, T3 var3, T4 var4, T5 var5);
}
