package me.litzrsh.consbits.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArraySortTests {

    @Test
    public void test1() {
        List<TestItem> items = new ArrayList<>(List.of(
                new TestItem("1", 1),
                new TestItem("2", 2),
                new TestItem("3", 3),
                new TestItem("4", 4),
                new TestItem("5", 5)
        ));
        log.info("{}", items);
        log.info("{}", items.stream().sorted().toList());
    }

    @Data
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestItem implements Comparable<TestItem> {

        String id;
        Integer sort;

        @Override
        public String toString() {
            return String.format("%s -> %d", id, sort);
        }

        @Override
        public int compareTo(TestItem o) {
            return o.sort - sort;
        }
    }
}
