package io.whj.seq.seqs;

import io.whj.seq.controls.More;
import io.whj.seq.tuples.Tuple;
import io.whj.seq.tuples.Tuple2;

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
@SuppressWarnings("unchecked")
public interface Seq<T> {
    
    
    Seq<Object> NONE = c -> {
    };
    
    void consume(Consumer<T> consumer);
    
    static <T> Seq<T> unit(T t) {
        return c -> c.accept(t);
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
            while (iterable.hasNext()) {
                T t = iterable.next();
                c.accept(t);
            }
            
        };
    }
    
    // 等差数列，左闭有开区间
    // arithmeticProgression(0,1,10) => 0,1,2,3,4,5,6,7,8,9
    static Seq<Integer> arithmeticProgression(int begin, int step, int limit) {
        int[] acc = {begin};
        return c -> {
            acc[0] += step;
            if (acc[0] < limit) {
                c.accept(acc[0]);
            }
        };
    }
    
    // 自然数列
    static Seq<Integer> naturalNumbers() {
        return naturalNumbers(Integer.MAX_VALUE);
    }
    
    static Seq<Integer> naturalNumbers(int limit) {
        return arithmeticProgression(0, 1, limit);
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
        consume(t -> stringJoiner.add(String.valueOf(t)));
        return prefix + stringJoiner + suffix;
    }
    
    default Set<T> toSet() {
        HashSet<T> elements = new HashSet<>();
        consume(elements::add);
        return elements;
    }
    
    
    default <K, V> Map<K, V> toMapOptional(BiConsumer<T, HashMap<K, V>> mapper) {
        HashMap<K, V> map = new HashMap<>();
        consume(t -> mapper.accept(t, map));
        return map;
    }
    
    default <K, V> Map<K, V> toMapOptional(
            Function<T, K> keyFn,
            Function<T, V> valueFn,
            // 元素冲突时默认采取策略（保留旧值，还是新值）
            BiFunction<V, V, V> conflictFn
    ) {
        return toMapOptional((t, map) -> {
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
    
    // 元素为空时直接放入
    default <K, V> Map<K, V> toMap(
            Function<T, K> keyFn,
            Function<T, V> valueFn
    ) {
        return toMapOptional((t, map) -> {
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
    default <K, V> Map<K, V> toMapOptional(
            Function<Optional<T>, K> keyFn,
            Function<Optional<T>, V> valueFn
    ) {
        return toMapOptional((t, map) -> {
            Optional<T> tOpt = Optional.ofNullable(t);
            map.put(keyFn.apply(tOpt), valueFn.apply(tOpt));
        });
    }
    
    
    default <R> Seq<R> map(Function<T, R> mapFn) {
        return c -> this.consume(t -> c.accept(mapFn.apply(t)));
    }
    
    // 如果条件为真就执行mapper
    default <R> Seq<R> map(Function<T, R> mapper, Predicate<T> predicate) {
        return c -> {
            this.consume(t -> {
                if (predicate.test(t)) {
                    c.accept(mapper.apply(t));
                }
            });
        };
    }
    
    // 如果element不为空就执行mapper
    default <R> Seq<R> mapNotNull(Function<T, R> mapper) {
        return this.map(mapper, Objects::nonNull);
    }
    
    // 转化为带索引的流
    default <R> BiSeq<Integer, R> mapWithIndex(Function<T, R> mapper) {
        int[] index = {0};
        return c -> {
            this.map(mapper, Objects::nonNull).consume(t -> {
                c.accept(index[0], t);
                index[0]++;
            });
        };
    }
    
    // 平铺元素
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
        } catch (Stop ignore) {
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
                Stop.stop();
            }
        });
    }
    
    default Seq<T> take(Predicate<T> predicate) {
        return c -> consumeUtilStop(t -> {
            if (predicate.test(t)) {
                c.accept(t);
            } else {
                Stop.stop();
            }
        });
    }
    
    // 注意： 如果先take再drop可能和你想的不一样
    default Seq<T> drop(int n) {
        int[] i = {0};
        return c -> this.consume(t -> {
            if (i[0] >= n) {
                c.accept(t);
            }
            i[0]++;
        });
    }
    
    default Seq<T> drop(Predicate<T> predicate) {
        return c -> this.consume(t -> {
            if (predicate.test(t)) {
                c.accept(t);
            }
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
                    Stop.stop();
                }
            });
        };
    }
    
    default <E> BiSeq<T, E> zipWith(Iterable<E> other) {
        return c -> {
            Iterator<E> iterator = other.iterator();
            consumeUtilStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(t, iterator.next());
                } else {
                    Stop.stop();
                }
            });
        };
    }
    
    default <E> BiSeq<T, E> zipWith(Seq<More<E>> other) {
        return c -> {
            consumeUtilStop(t -> {
                other.consume(e -> {
                    if (e.isNone()) {
                        Stop.stop();
                    } else {
                        c.accept(t, e.getOrElse(null));
                    }
                });
            });
        };
    }
    
    /**
     * 使用 more 信号包装流，最后一直返回none的元素指示seq流已耗尽, 如果一直返回some表示是无限流
     */
    default Seq<More<T>> more() {
        return c -> {
            this.consume(t -> c.accept(More.some(t)));
            while (true) {
                c.accept(More.none());
            }
        };
    }
    
    // 转化为带索引的流
    default BiSeq<T, Integer> zipWithIndex() {
        return this.zipWith(naturalNumbers().more());
    }
    
    // 集合内两两结合的函数
    default Seq<Tuple2<T, T>> zipWithNext() {
        return c -> {
            Object[] pre = {null};
            this.consume(t -> {
                if (pre[0] != null) {
                    c.accept(Tuple.of((T) pre[0], t));
                }
                pre[0] = t;
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
    default Seq<T> flatten() {
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
    
    default Optional<T> head() {
        return Optional.ofNullable(foldl(
                (r, t) -> {
                    if (r == null) {
                        return t;
                    } else {
                        return r;
                    }
                },
                null
        ));
    }
    
    default Optional<T> last() {
        return Optional.ofNullable(foldr(
                (t, r) -> {
                    if (r == null) {
                        return t;
                    } else {
                        return r;
                    }
                },
                null
        ));
    }
    
    default Seq<T> distinct() {
        return distinctBy(v -> v);
    }
    
    default Seq<T> distinctBy(Function<T, ?> keyExtractor) {
        return c -> {
            Set<Object> set = new HashSet<>();
            this.consume(t -> {
                Object key = keyExtractor.apply(t);
                if (!set.contains(key)) {
                    set.add(key);
                    c.accept(t);
                }
            });
        };
    }
    
    // filterNotNull
    default Seq<T> filterNotNull() {
        return c -> this.consume(t -> {
            if (t != null) {
                c.accept(t);
            }
        });
    }
    
    // tail
    default Seq<T> tail() {
        return c -> {
            final boolean[] first = {true};
            this.consume(t -> {
                if (!first[0]) {
                    c.accept(t);
                } else {
                    first[0] = false;
                }
            });
        };
    }
    
    // last
    default Seq<T> last(int n) {
        return c -> {
            final int[] i = {0};
            this.consume(t -> {
                if (i[0] >= n) {
                    c.accept(t);
                }
                i[0]++;
            });
        };
    }
    
    
    default Optional<T> findFirst(Predicate<T> predicate) {
        Object[] b = {null};
        consumeUtilStop(t -> {
            if (predicate.test(t)) {
                b[0] = t;
                Stop.stop();
            }
        });
        return Optional.ofNullable((T) b[0]);
    }
    
    // anyMatch
    default boolean anyMatch(Predicate<T> predicate) {
        return findFirst(predicate).isPresent();
    }
    
    // allMatch
    default boolean allMatch(Predicate<T> predicate) {
        return foldl((r, t) -> r && predicate.test(t), true);
    }
    
    // noneMatch
    default boolean noneMatch(Predicate<T> predicate) {
        return anyMatch(predicate);
    }
    
    // sort
    default Seq<T> sort(Comparator<T> comparator) {
        return c -> {
            List<T> list = new ArrayList<>();
            this.consume(list::add);
            list.sort(comparator);
            list.forEach(c);
        };
    }
    
    // sortBy
    default <R> Seq<T> sortBy(Function<T, R> keyExtractor, Comparator<R> comparator) {
        Comparator<T> keyComparator = (t1, t2) -> comparator.compare(keyExtractor.apply(t1), keyExtractor.apply(t2));
        return c -> this.sort(keyComparator).consume(c);
    }
    
    default <R extends Comparable<R>> Seq<T> sortBy(Function<T, R> keyExtractor) {
        return c -> this.sort(Comparator.comparing(keyExtractor));
    }
    
    // min
    default <R extends Comparable<R>> Optional<T> min(Function<T, R> keyExtractor) {
        Comparator<T> comparator = Comparator.comparing(keyExtractor);
        return this.sort(comparator).head();
    }
    
    // max
    default <R extends Comparable<R>> Optional<T> max(Function<T, R> keyExtractor) {
        Comparator<T> comparator = Comparator.comparing(keyExtractor).reversed();
        return this.sort(comparator).head();
    }
    
    
}





