package io.whj;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * haskell 集合操作在java中的模拟
 * @param <T>
 */
@FunctionalInterface
public interface Seq<T> {
    
    void consume(Consumer<T> consumer);
    
    static <T> Seq<T> unit(T t) {
        return c -> {
            c.accept(t);
        };
    }
    
    @SafeVarargs
    static <T> Seq<T> of(T... ts) {
        return c -> {
            for (T t : ts) {
                c.accept(t);
            }
        };
    }
    
    static <T> Seq<T> of(Iterator<T> iterable) {
        return c -> {
            for (Iterator<T> it = iterable; it.hasNext(); ) {
                T t = it.next();
                c.accept(t);
            }
            
        };
    }
    
    static <T> Seq<T> of(Iterable<T> iterable) {
        return of(iterable.iterator());
    }
    
    
    
}
