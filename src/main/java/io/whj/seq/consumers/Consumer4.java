package io.whj.seq.consumers;

import java.util.function.Consumer;

public interface Consumer4<T1, T2, T3, T4> extends Consumer<T1> {
    void accept(T1 var1, T2 var2, T3 var3, T4 var4);
}
