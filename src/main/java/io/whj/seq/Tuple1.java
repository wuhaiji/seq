package io.whj.seq;

import java.util.Objects;

// Tuple1 class
public class Tuple1<T> implements Tuple {
    private final T _1;
    
    public Tuple1(T _1) {
        this._1 = _1;
    }
    
    public static <T> io.whj.seq.Tuple1<T> of(T t) {
        return new io.whj.seq.Tuple1<>(t);
    }
    
    public T _1() {
        return _1;
    }
    
    @Override
    public String toString() {
        return "(" + _1 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        io.whj.seq.Tuple1<?> tuple = (io.whj.seq.Tuple1<?>) o;
        return Objects.equals(_1, tuple._1);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_1);
    }
}
