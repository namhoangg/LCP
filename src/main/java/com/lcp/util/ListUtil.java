package com.lcp.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {

    // This function return a list of elements that are in list1 but not in list2
    public static <T> List<T> difference(List<T> list1, List<T> list2) {
        Set<T> set2 = new HashSet<>(list2);
        List<T> result = new ArrayList<>();

        for (T t : list1) {
            if (!set2.contains(t)) {
                result.add(t);
            }
        }

        return result;
    }
}
