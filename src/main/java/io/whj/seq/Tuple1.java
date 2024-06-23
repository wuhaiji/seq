package io.whj.seq;

import java.util.Objects;

// Tuple1 class
public class Tuple1<T> implements Tuple {
    private final T first;
    
    public Tuple1(T first) {
        this.first = first;
    }
    
    public static <T> io.whj.seq.Tuple1<T> of(T t) {
        return new io.whj.seq.Tuple1<>(t);
    }
    
    public T _1() {
        return first;
    }
    
    @Override
    public String toString() {
        return "(" + first + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        io.whj.seq.Tuple1<?> tuple = (io.whj.seq.Tuple1<?>) o;
        return Objects.equals(first, tuple.first);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first);
    }
}
