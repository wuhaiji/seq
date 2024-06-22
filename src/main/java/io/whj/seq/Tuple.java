package io.whj.seq;

import java.util.Objects;

public interface Tuple {
    // Factory methods for creating Tuple instances
    static <T> Tuple1<T> of(T t) {
        return Tuple1.of(t);
    }
    
    static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return Tuple2.of(t1, t2);
    }
    
    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return Tuple3.of(t1, t2, t3);
    }
    
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return Tuple4.of(t1, t2, t3, t4);
    }
    
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return Tuple5.of(t1, t2, t3, t4, t5);
    }
    
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return Tuple6.of(t1, t2, t3, t4, t5, t6);
    }
    
    static <T1, T2, T3, T4, T5, T6, T7>
    Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return Tuple7.of(t1, t2, t3, t4, t5, t6, t7);
    }
    
    static <T1, T2, T3, T4, T5, T6, T7, T8>
    Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return Tuple8.of(t1, t2, t3, t4, t5, t6, t7, t8);
    }
    
}
