package com.zwh.collections;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListDistinct {


    public static List<String> removeBySet(List<String> originalList) {
        // set remove
        return new ArrayList<>((new LinkedHashSet<>(originalList)));
    }

    public static List<String> removeByForLoop(List<String> originalList) {
        List<String> list1 = new ArrayList<>(originalList);
        List<String> list2 = new ArrayList<>();
        for (String a : list1) {
            if (!list2.contains(a)) {
                list2.add(a);
            }
        }
        return list2;
    }

    public static List<String> removeBy2ForLoop(List<String> originalList) {
        List<String> list1 = new ArrayList<>(originalList);
        for (int i = 0; i < list1.size() - 1; i++) {
            for (int j = list1.size() - 1; j > i; j--) {
                if (Objects.equals(list1.get(i), list1.get(j))) {
                    list1.remove(j);
                }
            }
        }
        return list1;
    }

    public static List<String> removeByStreamDistinct(List<String> originalList) {
        return originalList.stream().distinct().collect(Collectors.toList());
    }
}
