package io.whj.seq.consumers;

import java.util.function.Consumer;

public interface Consumer3<T1, T2, T3> extends Consumer<T1> {
    void accept(T1 var1, T2 var2, T3 var3);
}
