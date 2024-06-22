package io.whj.seq;

import java.util.Objects;

// Tuple3 class
public class Tuple3<T1, T2, T3> implements Tuple {
    private final T1 _1;
    private final T2 _2;
    private final T3 _3;
    
    public Tuple3(T1 _1, T2 _2, T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }
    
    public static <T1, T2, T3> io.whj.seq.Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new io.whj.seq.Tuple3<>(t1, t2, t3);
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
    
    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        io.whj.seq.Tuple3<?, ?, ?> tuple = (io.whj.seq.Tuple3<?, ?, ?>) o;
        return Objects.equals(_1, tuple._1) && Objects.equals(_2, tuple._2) && Objects.equals(_3, tuple._3);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3);
    }
}
