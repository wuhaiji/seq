package io.whj.seq.tuples;

import java.util.Objects;

// Tuple8 class
public class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> implements Tuple {
    private final T1 _1;
    private final T2 _2;
    private final T3 _3;
    private final T4 _4;
    private final T5 _5;
    private final T6 _6;
    private final T7 _7;
    private final T8 _8;
    
    public Tuple8(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
        this._7 = _7;
        this._8 = _8;
    }
    
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
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
    
    public T5 _5() {
        return _5;
    }
    
    public T6 _6() {
        return _6;
    }
    
    public T7 _7() {
        return _7;
    }
    
    public T8 _8() {
        return _8;
    }
    
    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple8<?, ?, ?, ?, ?, ?, ?, ?> tuple = (Tuple8<?, ?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(_1, tuple._1) && Objects.equals(_2, tuple._2) &&
                Objects.equals(_3, tuple._3) && Objects.equals(_4, tuple._4) &&
                Objects.equals(_5, tuple._5) && Objects.equals(_6, tuple._6) &&
                Objects.equals(_7, tuple._7) && Objects.equals(_8, tuple._8);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8);
    }
}
