package io.whj.seq.tuples;

import java.util.Objects;

// Tuple2 class
public class Tuple2<T1, T2> implements Tuple {
    private final T1 first;
    private final T2 second;
    
    public Tuple2(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    
    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }
    
    public T1 _1() {
        return first;
    }
    
    public T2 _2() {
        return second;
    }
    
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple = (Tuple2<?, ?>) o;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
