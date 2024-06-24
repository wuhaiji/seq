package io.whj.seq.controls;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// 定义 Option 接口

public abstract class Option<T> {
    
    // 不允许其他包的类继承该类
    Option() {
    }
    
    public abstract T get();
    
    @SuppressWarnings("unchecked")
    public T getOrElse(T defaultValue) {
        return isNone() ? (T) none() : get();
    }
    
    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        return isNone() ? none() : some(mapper.apply(this.get()));
    }
    
    public <U> Option<U> flatMap(Function<? super T, Option<U>> mapper) {
        return isNone() ? none() : mapper.apply(this.get());
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
    
    public static <T> Option<T> ofNullable(T value) {
        if (value == null) {
            return None.none();
        }
        return new Some<>(value);
    }
    
    
    public static <T> Option<T> some(T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        return new Some<>(value);
    }
    
    public static <T> Option<T> none() {
        return None.none();
    }
    
    // Implementation of Some
    private static final class Some<T> extends Option<T> {
        private final T value;
        
        private Some(T value) {
            this.value = value;
        }
        
        @Override
        public T get() {
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
        
        @SuppressWarnings("unchecked")
        public static <T> None<T> none() {
            return (None<T>) NONE;
        }
        
        @Override
        public T get() {
            throw new NoSuchElementException("No value present");
        }
        
        
    }
}

