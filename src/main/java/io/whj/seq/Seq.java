package io.whj.seq;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * 一种新的流：为Java加入生成器(Generator)特性
 *
 * @param <T> 元素类型
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
    
    default List<T> toList() {
        ArrayList<T> elements = new ArrayList<>();
        consume(elements::add);
        return elements;
    }
    
    default String toArrayString() {
        ArrayList<T> elements = new ArrayList<>();
        consume(elements::add);
        return elements.toString();
    }
    
    default String toJoinString(String delimiter) {
        return toJoinString("", delimiter, "");
    }
    
    default String toJoinString(String prefix, String delimiter, String suffix) {
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        consume(t -> stringJoiner.add(t.toString()));
        return prefix + stringJoiner + suffix;
    }
    
    default Set<T> toSet() {
        HashSet<T> elements = new HashSet<>();
        consume(elements::add);
        return elements;
    }
    
    
    default <K, V> Map<K, V> toMap(BiConsumer<T, HashMap<K, V>> mapper) {
        HashMap<K, V> map = new HashMap<>();
        consume(t -> mapper.accept(t, map));
        return map;
    }
    
    default <K, V> Map<K, V> toMapNonNull(
            Function<T, K> keyFn,
            Function<T, V> valueFn,
            // 元素冲突时默认采取策略（保留旧值，还是新值）
            BiFunction<V, V, V> conflictFn
    ) {
        return toMap((t, map) -> {
            if (t == null) {
                map.put(null, null);
            } else {
                K key = keyFn.apply(t);
                V oldValue = map.get(key);
                V newValue = valueFn.apply(t);
                if (oldValue != null) {
                    map.put(key, conflictFn.apply(oldValue, newValue));
                } else {
                    map.put(key, newValue);
                }
            }
        });
    }
    
    // 元素冲突时默认采取新值放入map
    default <K, V> Map<K, V> toMapNonNull(
            Function<T, K> keyFn,
            Function<T, V> valueFn
    ) {
        return toMap((t, map) -> {
            if (t == null) {
                map.put(null, null);
            } else {
                K key = keyFn.apply(t);
                V newValue = valueFn.apply(t);
                map.put(key, newValue);
            }
        });
    }
    
    // 转Map, 允许null元素所以不会对集合中的null元素过滤, keyFn,valueFn 需要自行处理nullable元素
    default <K, V> Map<K, V> toMapNullable(
            Function<Optional<T>, K> keyFn,
            Function<Optional<T>, V> valueFn
    ) {
        return toMap((t, map) -> {
            Optional<T> tOpt = Optional.ofNullable(t);
            map.put(keyFn.apply(tOpt), valueFn.apply(tOpt));
        });
    }
    
    
    default <R> Seq<R> map(Function<T, R> mapFn) {
        return c -> this.consume(t -> c.accept(mapFn.apply(t)));
    }
    
    default <R> Seq<R> flatMap(Function<T, Seq<R>> flatmapFn) {
        return c -> this.consume(t -> flatmapFn.apply(t).consume(c));
    }
    
    default Seq<T> filter(Predicate<T> predicate) {
        return c -> {
            this.consume(t -> {
                if (predicate.test(t)) {
                    c.accept(t);
                }
            });
        };
    }
    
    default void consumeUtilStop(Consumer<T> consumer) {
        try {
            this.consume(consumer);
        } catch (SeqStopException ignore) {
            // 这里异常只是用来传递break信号，打断循环
        }
    }
    
    default Seq<T> take(int n) {
        int[] i = {0};
        return c -> consumeUtilStop(t -> {
            if (i[0] < n) {
                c.accept(t);
                i[0]++;
            } else {
                SeqStopException.stop();
            }
        });
    }
    
    // 注意 先 take 再 drop 可能和你想的不一样
    default Seq<T> drop(int n) {
        int[] i = {0};
        return c -> this.consume(t -> {
            if (i[0] >= n) {
                c.accept(t);
            }
            i[0]++;
        });
    }
    
    default Seq<T> peek(Consumer<T> peeker) {
        return c -> this.consume(peeker.andThen(c));
    }
    
    //zip
    default <E> Seq<Tuple2<T, E>> zip(Iterable<E> other) {
        return c -> {
            Iterator<E> iterator = other.iterator();
            consumeUtilStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(Tuple.of(t, iterator.next()));
                } else {
                    SeqStopException.stop();
                }
            });
        };
    }
    
    default <E, R> Seq<R> zipWith(Iterable<E> other, BiFunction<T, E, R> function) {
        return c -> {
            Iterator<E> iterator = other.iterator();
            consumeUtilStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(function.apply(t, iterator.next()));
                } else {
                    SeqStopException.stop();
                }
            });
        };
    }
    
    @SuppressWarnings("unchecked")
    default <R> R foldl(BiFunction<R, T, R> f, R initialValue) {
        Object[] b = {initialValue};
        this.consume(t -> b[0] = f.apply(initialValue, t));
        return (R) b[0];
    }
    
    @SuppressWarnings("unchecked")
    default <R> R foldr(BiFunction<T, R, R> f, R initialValue) {
        Object[] b = {initialValue};
        AtomicReference<R> result = new AtomicReference<>(initialValue);
        this.consume(t -> b[0] = f.apply(t, initialValue));
        return (R) b[0];
    }
    
    
    default Seq<T> append(T of) {
        return c -> {
            this.consume(c);
            c.accept(of);
        };
    }
    
    default Seq<T> append(Iterable<T> other) {
        return c -> {
            this.consume(c);
            other.forEach(c);
        };
    }
    
    default Seq<T> append(Seq<T> other) {
        return c -> {
            this.consume(c);
            other.consume(c);
        };
    }
    
    default Seq<T> append(Stream<T> other) {
        return c -> {
            this.consume(c);
            other.forEach(c);
        };
    }
    
    @SuppressWarnings("unchecked")
    default Seq<T> flat() {
        return c -> {
            this.consume(t -> {
                if (t instanceof Object[]) {
                    for (Object o : (Object[]) t) {
                        c.accept((T) o);
                    }
                } else if (t instanceof Iterable) {
                    ((Iterable<T>) t).forEach(c);
                } else if (t instanceof Seq) {
                    ((Seq<T>) t).consume(c);
                } else if (t instanceof Stream) {
                    ((Stream<T>) t).forEach(c);
                } else {
                    c.accept(t);
                }
            });
        };
    }
    
    // reverse
    default Seq<T> reverse() {
        // 没有优化的办法吗，一定要遍历一遍吗
        return c -> {
            List<T> list = new ArrayList<>();
            this.consume(list::add);
            Collections.reverse(list);
            list.forEach(c);
        };
    }
    
    default int length() {
        int[] i = {0};
        this.consume(t -> i[0]++);
        return i[0];
    }
    
    default boolean isEmpty() {
        return length() == 0;
    }
    
    default boolean matchAny(T e) {
        try {
            return foldl(
                    (r, t) -> {
                        if (r) {
                            // 直接跳出循环
                            SeqStopException.stop();
                            return true;
                        } else {
                            return t.equals(e);
                        }
                    },
                    false
            );
        } catch (SeqStopException ex) {
            return true;
        }
    }
    
    default <R> Seq<R> map(BiFunction<T, Integer, R> mapFn) {
        return c -> {
            int[] i = {0};
            this.consume(t -> c.accept(mapFn.apply(t, i[0]++)));
        };
    }
    
}
