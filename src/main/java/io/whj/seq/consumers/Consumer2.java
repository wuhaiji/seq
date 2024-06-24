package io.whj.seq.consumers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Consumer2<T1, T2> extends Consumer<T1> , BiConsumer<T1,T2> {
    void accept(T1 var1, T2 var2);
}
