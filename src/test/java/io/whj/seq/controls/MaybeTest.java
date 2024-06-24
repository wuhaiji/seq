package io.whj.seq.controls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.NoSuchElementException;


class MaybeTest {
    
    @Test
    void testSomeCreation() {
        Maybe<String> some = Maybe.some("Hello");
        assertTrue(some.isSome());
        assertEquals("Hello", some.get());
    }
    
    @Test
    void testNoneCreation() {
        Maybe<String> none = Maybe.none();
        assertTrue(none.isNone());
        assertThrows(NoSuchElementException.class, none::get);
    }
    
//    @Test
//    void testOfNullable() {
//        Maybe<String> some = Maybe.ofNullable("Hello");
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Maybe<String> none = Maybe.ofNullable(null);
//        assertTrue(none.isNone());
//    }
    
    @Test
    void testGetOrElse() {
        Maybe<String> some = Maybe.some("Hello");
        assertEquals("Hello", some.getOrElse("Default"));
        
        Maybe<String> none = Maybe.none();
        assertEquals("Default", none.getOrElse("Default"));
    }
    
    @Test
    void testMap() {
        Maybe<Maybe<Integer>> hello = Maybe.some("Hello").map(String::length);
        assertTrue(hello.isSome());
        assertEquals(Maybe.some(5), hello.get());
        
    }
    
    @Test
    void testFilter() {
        Maybe<String> some = Maybe.some("Hello").filter(s -> s.length() > 3);
        assertTrue(some.isSome());
        
        Maybe<String> filteredNone = Maybe.some("Hi").filter(s -> s.length() > 3);
        assertTrue(filteredNone.isNone());
        
        Maybe<String> none = Maybe.none();
        assertTrue(none.filter(s -> s.length() > 3).isNone());
    }

//    @Test
//    void testPeek() {
//        StringBuilder sb = new StringBuilder();
//        Maybe.some("Hello").peek(sb::append);
//        assertEquals("Hello", sb.toString());
//
//        Maybe.none().peek(sb::append);
//        assertEquals("Hello", sb.toString()); // No change
//    }
//
//    @Test
//    void testIfSome() {
//        StringBuilder sb = new StringBuilder();
//        Maybe.some("Hello").ifSome(value -> {
//            sb.append(value);
//            return null;
//        });
//        assertEquals("Hello", sb.toString());
//
//        Maybe.none().ifSome(value -> {
//            sb.append(value);
//            return null;
//        });
//        assertEquals("Hello", sb.toString()); // No change
//    }
//
//    @Test
//    void testIfNone() {
//        StringBuilder sb = new StringBuilder();
//        Maybe.none().ifNone(() -> sb.append("None"));
//        assertEquals("None", sb.toString());
//
//        Maybe.some("Hello").ifNone(() -> sb.append("None"));
//        assertEquals("None", sb.toString()); // No change
//    }
//
//    @Test
//    void testOrElse() {
//        Maybe<String> some = Maybe.some("Hello").orElse(Maybe.some("Default"));
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Maybe<String> none = Maybe.none().orElse(Maybe.some("Default"));
//        assertTrue(none.isSome());
//        assertEquals("Default", none.get());
//    }
//
//    @Test
//    void testOrElseSupplier() {
//        Maybe<String> some = Maybe.some("Hello").orElse(() -> Maybe.some("Default"));
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Maybe<String> none = Maybe.none().orElse(() -> Maybe.some("Default"));
//        assertTrue(none.isSome());
//        assertEquals("Default", none.get());
//    }
//
//    @Test
//    void testToOptional() {
//        Optional<String> optionalSome = Maybe.some("Hello").toOptional();
//        assertTrue(optionalSome.isPresent());
//        assertEquals("Hello", optionalSome.get());
//
//        Optional<String> optionalNone = Maybe.none().toOptional();
//        assertTrue(optionalNone.isEmpty());
//    }
}