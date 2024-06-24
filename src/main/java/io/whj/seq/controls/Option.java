package io.whj.seq.controls;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// 定义 Option 接口, 完全遵照rust Option的规范语义,这里和java optional的语义有很大区别

public class Option<T> {
    
    // 不允许其他包的类继承该类
    Option() {
        throw new IllegalStateException("can not new Option class");
    }
    
    T get() {
        throw new IllegalStateException("can not invoke get on Option class");
    }
    
    @SuppressWarnings("unchecked")
    public T getOrElse(T defaultValue) {
        return isNone() ? (T) none() : get();
    }
    
    public <U> Option<Option<U>> map(Function<? super T, ? extends U> mapper) {
        return isNone() ? none() : some(ofNullable(mapper.apply(this.get())));
    }
    
    public Option<T> filter(Predicate<T> predicate) {
        Predicate<T> negate = predicate.negate();
        if (isNone() || negate.test(this.get())) {
            return none();
        } else {
            return this;
        }
    }
    
    public Option<T> peek(Consumer<T> consumer) {
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
    
    public Option<T> orElse(Option<T> other) {
        if (isNone()) {
            return other;
        } else {
            return this;
        }
    }
    
    public Option<T> orElse(Supplier<Option<T>> supplier) {
        if (isNone()) {
            return supplier.get();
        } else {
            return this;
        }
    }
    
    private static <T> Option<T> ofNullable(T value) {
        if (value == null) {
            return None.none();
        }
        return new Some<>(value);
    }
    
    public Optional<T> toOptional() {
        if (isNone()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(this.get());
        }
    }
    
    
    public static <T> Option<T> some(T value) {
        return new Some<>(value);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) None.NONE;
    }
    
    // Implementation of Some
    private static final class Some<T> extends Option<T> {
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
    private static final class None<T> extends Option<T> {
        
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

