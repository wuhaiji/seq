package io.whj.seq.controls;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 定义 More 接口, 表示容器是否还有值, 传递存在信号，用于无限流的终止控制
 * @param <T>
 */

public class More<T> {
    
    // 不允许其他包的类继承该类
    More() {
        throw new IllegalStateException("can not new More class");
    }
    
    T get() {
        throw new IllegalStateException("can not invoke get on More class");
    }
    
    public T getOrElse(T defaultValue) {
        return isNone() ? defaultValue : get();
    }
    
    public <U> More<U> map(Function<? super T, ? extends U> mapper) {
        return isNone() ? none() : some(mapper.apply(this.get()));
    }
    
    public More<T> filter(Predicate<T> predicate) {
        if (isNone() || predicate.negate().test(this.get())) {
            return none();
        } else {
            return this;
        }
    }
    
    public More<T> peek(Consumer<T> consumer) {
        if (this.isSome()) {
            consumer.accept(this.get());
        }
        return this;
    }
    
    public void ifSome(Function<? super T, ? extends Void> consumer) {
        if (isSome()) {
            consumer.apply(this.get());
        }
    }
    
    public void ifNone(Runnable consumer) {
        if (isNone()) {
            consumer.run();
        }
    }
    
    public boolean isNone() {
        return this instanceof None;
    }
    
    public boolean isSome() {
        return !isNone();
    }
    
    public More<T> orElse(More<T> other) {
        if (isNone()) {
            return other;
        } else {
            return this;
        }
    }
    
    public More<T> orElse(Supplier<More<T>> supplier) {
        if (isNone()) {
            return supplier.get();
        } else {
            return this;
        }
    }
    
    public Optional<T> toOptional() {
        if (isNone()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(this.get());
        }
    }
    
    public static <T> Some<T> some(T value) {
        return new Some<>(value);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) None.NONE;
    }
    
    // Implementation of Some
    private static final class Some<T> extends More<T> {
        private final T value;
        
        private Some(T value) {
            this.value = value;
        }
        
        @Override
        T get() {
            return value;
        }
    }
    
    // Implementation of None
    private static final class None<T> extends More<T> {
        
        private None() {
            // Get the caller class name
            String callerClassName = new Throwable().getStackTrace()[1].getClassName();
            if (!None.class.getName().equals(callerClassName)) {
                throw new UnsupportedOperationException("Not allowed to new this class");
            }
        }
        
        static final None<Object> NONE = new None<>();
        
        @Override
        T get() {
            throw new NoSuchElementException("No value present");
        }
        
    }
}

