package io.whj.seq.seqs;

import io.whj.seq.functions.Function2;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface BiSeq<T1, T2> {
    void consume(BiConsumer<T1, T2> consumer);
    
    default BiSeq<T1, T2> unit(T1 t1, T2 t2) {
        return c -> c.accept(t1, t2);
    }
    
    default <E1, E2> BiSeq<E1, E2> flatMap(Function2<T1, T2, Seq<BiSeq<E1, E2>>> mapper) {
        return c -> this.consume((t1, t2) -> mapper.apply(t1, t2).consume(s -> s.consume(c)));
    }
    
    default <E1, E2> BiSeq<E1, E2> flatMapIterable(Function2<T1, T2, Iterable<BiSeq<E1, E2>>> mapper) {
        return c -> this.consume((t1, t2) -> mapper.apply(t1, t2).forEach(s -> s.consume(c)));
    }
    
    default <E1, E2> BiSeq<E1, E2> map(Function2<T1, T2, BiSeq<E1, E2>> mapper) {
        return c -> this.consume((t1, t2) -> mapper.apply(t1, t2).consume(c));
    }
    
    default <E1, E2> BiSeq<E1, E2> filter(Function<T1, E1> keyMapper, Function<T2, E2> valueMapper) {
        return c -> this.consume((t1, t2) -> c.accept(keyMapper.apply(t1), valueMapper.apply(t2)));
    }
    
    default String toJoinString(String s) {
        // todo
        return "";
    }
}