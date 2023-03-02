package com.zwh.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListDistinctTest {

    List<String> originalList = List.of("zhangsan", "lisi", "zhangsan", "zhangsan", "lisi", "wangwu");
    List<String> expected = List.of("zhangsan", "lisi", "wangwu");

    @Test
    void removeBySet() {
        List<String> actual = ListDistinct.removeBySet(originalList);
        assertIterableEquals(expected, actual);
    }

    @Test
    void removeByForLoop() {
        List<String> actual = ListDistinct.removeByForLoop(originalList);
        assertIterableEquals(expected, actual);
    }

    @Test
    void removeBy2ForLoop() {
        List<String> actual = ListDistinct.removeBy2ForLoop(originalList);
        assertIterableEquals(expected, actual);
    }

    @Test
    void removeByStreamDistinct() {
        List<String> actual = ListDistinct.removeByStreamDistinct(originalList);
        assertIterableEquals(expected, actual);
    }
}