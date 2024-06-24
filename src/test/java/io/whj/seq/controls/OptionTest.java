package io.whj.seq.controls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.NoSuchElementException;


class OptionTest {
    
    @Test
    void testSomeCreation() {
        Option<String> some = Option.some("Hello");
        assertTrue(some.isSome());
        assertEquals("Hello", some.get());
    }
    
    @Test
    void testNoneCreation() {
        Option<String> none = Option.none();
        assertTrue(none.isNone());
        assertThrows(NoSuchElementException.class, none::get);
    }
    
//    @Test
//    void testOfNullable() {
//        Option<String> some = Option.ofNullable("Hello");
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Option<String> none = Option.ofNullable(null);
//        assertTrue(none.isNone());
//    }
    
    @Test
    void testGetOrElse() {
        Option<String> some = Option.some("Hello");
        assertEquals("Hello", some.getOrElse("Default"));
        
        Option<String> none = Option.none();
        assertEquals("Default", none.getOrElse("Default"));
    }
    
    @Test
    void testMap() {
        Option<Option<Integer>> hello = Option.some("Hello").map(String::length);
        assertTrue(hello.isSome());
        assertEquals(Option.some(5), hello.get());
        
    }
    
    @Test
    void testFilter() {
        Option<String> some = Option.some("Hello").filter(s -> s.length() > 3);
        assertTrue(some.isSome());
        
        Option<String> filteredNone = Option.some("Hi").filter(s -> s.length() > 3);
        assertTrue(filteredNone.isNone());
        
        Option<String> none = Option.none();
        assertTrue(none.filter(s -> s.length() > 3).isNone());
    }

//    @Test
//    void testPeek() {
//        StringBuilder sb = new StringBuilder();
//        Option.some("Hello").peek(sb::append);
//        assertEquals("Hello", sb.toString());
//
//        Option.none().peek(sb::append);
//        assertEquals("Hello", sb.toString()); // No change
//    }
//
//    @Test
//    void testIfSome() {
//        StringBuilder sb = new StringBuilder();
//        Option.some("Hello").ifSome(value -> {
//            sb.append(value);
//            return null;
//        });
//        assertEquals("Hello", sb.toString());
//
//        Option.none().ifSome(value -> {
//            sb.append(value);
//            return null;
//        });
//        assertEquals("Hello", sb.toString()); // No change
//    }
//
//    @Test
//    void testIfNone() {
//        StringBuilder sb = new StringBuilder();
//        Option.none().ifNone(() -> sb.append("None"));
//        assertEquals("None", sb.toString());
//
//        Option.some("Hello").ifNone(() -> sb.append("None"));
//        assertEquals("None", sb.toString()); // No change
//    }
//
//    @Test
//    void testOrElse() {
//        Option<String> some = Option.some("Hello").orElse(Option.some("Default"));
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Option<String> none = Option.none().orElse(Option.some("Default"));
//        assertTrue(none.isSome());
//        assertEquals("Default", none.get());
//    }
//
//    @Test
//    void testOrElseSupplier() {
//        Option<String> some = Option.some("Hello").orElse(() -> Option.some("Default"));
//        assertTrue(some.isSome());
//        assertEquals("Hello", some.get());
//
//        Option<String> none = Option.none().orElse(() -> Option.some("Default"));
//        assertTrue(none.isSome());
//        assertEquals("Default", none.get());
//    }
//
//    @Test
//    void testToOptional() {
//        Optional<String> optionalSome = Option.some("Hello").toOptional();
//        assertTrue(optionalSome.isPresent());
//        assertEquals("Hello", optionalSome.get());
//
//        Optional<String> optionalNone = Option.none().toOptional();
//        assertTrue(optionalNone.isEmpty());
//    }
}