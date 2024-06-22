package io.whj;

import io.whj.seq.Seq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SeqTest {
    @SafeVarargs
    static <T> Iterable<T> listOf(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }
    
    @Test
    void testToMapNonNull() {
        Function<String, String> keyFn = v -> v + "_i32";
        Integer[] integers = {1, 2, 3, 4, 5, 6};
        
        Map<String, String> actualMap = Seq.of(integers)
                .map(Object::toString).toMapNonNull(keyFn, Function.identity());
        
        Map<String, String> expectMap = Stream.of(integers)
                .map(Object::toString).collect(Collectors.toMap(keyFn, v -> v));
        
        Assertions.assertEquals(expectMap, actualMap);
    }
    
    @Test
    void testToMapNullable() {
        
        Function<Optional<String>, Optional<String>> keyFn = v -> v.map(e -> e + "_i32");
        Integer[] integers = {1, 2, 3};
        
        Map<Optional<String>, Optional<String>> actualMap = Seq.of(integers)
                .map(Object::toString).toMapNullable(keyFn, Function.identity());
        
        Assertions.assertEquals(
                "{Optional[1_i32]=Optional[1], Optional[2_i32]=Optional[2], Optional[3_i32]=Optional[3]}",
                actualMap.toString());
    }
    
    
    @Test
    public void testToJoinString() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        Assertions.assertEquals("123", seq.toJoinString(""));
        Assertions.assertEquals("1, 2, 3", seq.toJoinString(", "));
        Assertions.assertEquals("[1, 2, 3]", seq.toArrayString());
    }
    
    
    @Test
    public void testCreateMethods() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        
        Assertions.assertEquals("[1, 2, 3]", seq.toArrayString());
        
        Seq<Integer> seq1 = Seq.of(listOf(1, 2, 3));
        
        Assertions.assertEquals("[1, 2, 3]", seq1.toArrayString());
        
        Seq<Integer> seq2 = Seq.of(listOf(1, 2, 3).iterator());
        
        Assertions.assertEquals("[1, 2, 3]", seq2.toArrayString());
    }
    
    @Test
    void testFilter() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5).filter(v -> v % 2 == 0);
        Assertions.assertEquals("[2, 4]", seq.toArrayString());
        
        ArrayList<Integer> nums = new ArrayList<>();
        
        Seq<Integer> seq1 = c -> {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int r = random.nextInt();
                nums.add(r);
                c.accept(r);
            }
        };
        seq1 = seq1.filter(v -> v % 2 == 0);
        
        String actual = seq1.toArrayString();
        
        String expect = nums.stream().filter(v -> v % 2 == 0).collect(Collectors.toList()).toString();
        
        Assertions.assertEquals(expect, actual);
    }
    
    @Test
    void testMap() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5).map(v -> v * 2);
        Assertions.assertEquals("[2, 4, 6, 8, 10]", seq.toArrayString());
        
        ArrayList<Integer> nums = new ArrayList<>();
        
        Seq<Integer> seq1 = c -> {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int r = random.nextInt();
                nums.add(r);
                c.accept(r);
            }
        };
        seq1 = seq1.map(v -> v % 2);
        
        String actual = seq1.toArrayString();
        
        String expect = nums.stream().map(v -> v % 2).collect(Collectors.toList()).toString();
        
        Assertions.assertEquals(expect, actual);
    }
    
    
    @Test
    void testFlatMap() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5).flatMap(v -> Seq.of(v, v));
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5).flatMap(v -> Stream.of(v, v));
        
        Assertions.assertEquals(Arrays.toString(stream.toArray()), seq.toArrayString());
        
        ArrayList<Integer> nums = new ArrayList<>();
        
        Seq<Integer> seq1 = c -> {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int r = random.nextInt();
                nums.add(r);
                c.accept(r);
            }
        };
        
        seq1 = seq1.flatMap(v -> Seq.of(v, v));
        
        String actual = seq1.toJoinString(",");
        
        String expect = nums.stream()
                .flatMap(v -> Stream.of(v, v)).map(Object::toString).collect(Collectors.joining(","));
        
        Assertions.assertEquals(expect, actual);
    }
    
    
    @Test
    void testTakeAndDrop() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5, 6);
        
        String actual = seq.drop(3).take(2).toJoinString(", ");
        
        Assertions.assertEquals("4, 5", actual);
        
    }
    
    @Test
    void zipWith() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        
        String joinString = seq.zipWith(Seq.of('a', 'b', 'c').toList(), (q, w) -> q + "_" + w).toJoinString(",");
        
        Assertions.assertEquals("1_a,2_b,3_c", joinString);
    }
}