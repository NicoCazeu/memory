package com.softcaze.memory.util;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtil {
    public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element: col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
