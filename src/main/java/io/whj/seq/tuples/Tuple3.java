package io.whj.seq.tuples;

import java.util.Objects;

// Tuple3 class
public class Tuple3<T1, T2, T3> implements Tuple {
    private final T1 first;
    private final T2 second;
    private final T3 third;
    
    public Tuple3(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }
    
    public T1 getFirst() {
        return first;
    }
    
    public T2 getSecond() {
        return second;
    }
    
    public T3 getThird() {
        return third;
    }
    
    public T1 first() {
        return first;
    }
    
    public T2 second() {
        return second;
    }
    
    public T3 third() {
        return third;
    }
    
    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple = (Tuple3<?, ?, ?>) o;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second) && Objects.equals(third, tuple.third);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
