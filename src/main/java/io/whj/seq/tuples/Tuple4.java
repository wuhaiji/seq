package io.whj.seq.tuples;

import java.util.Objects;

// Tuple4 class
public class Tuple4<T1, T2, T3, T4> implements Tuple {
    private final T1 _1;
    private final T2 _2;
    private final T3 _3;
    private final T4 _4;
    
    public Tuple4(T1 _1, T2 _2, T3 _3, T4 _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }
    
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }
    
    public T1 _1() {
        return _1;
    }
    
    public T2 _2() {
        return _2;
    }
    
    public T3 _3() {
        return _3;
    }
    
    public T4 _4() {
        return _4;
    }
    
    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple4<?, ?, ?, ?> tuple = (Tuple4<?, ?, ?, ?>) o;
        return Objects.equals(_1, tuple._1) && Objects.equals(_2, tuple._2)
                && Objects.equals(_3, tuple._3) && Objects.equals(_4, tuple._4);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4);
    }
}
