package io.whj.seq.predicates;

import java.util.function.Predicate;

public interface Predicate3<T1, T2, T3> extends Predicate<T1> {
    boolean test(T1 var1, T2 var2, T3 var3);
}
