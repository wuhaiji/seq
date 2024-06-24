package io.whj.seq.predicates;

import java.util.function.Predicate;

public interface Predicate1<T1, T2> extends Predicate<T1>{
    boolean test(T1 var1, T2 var2);
}
