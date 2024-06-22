package io.whj.seq;

import java.util.Objects;

// Tuple2 class
public class Tuple2<T1, T2> implements Tuple {
    private final T1 _1;
    private final T2 _2;
    
    public Tuple2(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }
    
    public static <T1, T2> io.whj.seq.Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new io.whj.seq.Tuple2<>(t1, t2);
    }
    
    public T1 _1() {
        return _1;
    }
    
    public T2 _2() {
        return _2;
    }
    
    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        io.whj.seq.Tuple2<?, ?> tuple = (io.whj.seq.Tuple2<?, ?>) o;
        return Objects.equals(_1, tuple._1) && Objects.equals(_2, tuple._2);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }
}
