package com.winsafe.jiangmenzhibao.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shijie.yang on 2017/5/19.
 */

public class CollectionHelper {
    public static int indexOf(int[] lst, int item) {
        if (lst == null || lst.length == 0) return -1;
        //
        for (int i = 0; i < lst.length; i++) {
            int t = lst[i];
            if (item == t) {
                return i;
            }
        }
        //
        return -1;
    }

    public static <T> int indexOf(T[] lst, T item) {
        if (lst == null || lst.length == 0) return -1;
        //
        for (int i = 0; i < lst.length; i++) {
            T t = lst[i];
            if (item.equals(t)) {
                return i;
            }
        }
        //
        return -1;
    }

    public static <T> int indexOf(T[] lst, T item, Comparator<T> comparator) {
        if (lst == null || lst.length == 0) return -1;
        //
        for (int i = 0; i < lst.length; i++) {
            T t = lst[i];
            if (comparator.compare(item, t) == 0) {
                return i;
            }
        }
        //
        return -1;
    }

    public static <T> List<T> take(List<T> lst, int start, int count) {
        if (lst == null) return null;
        //
        if (start + count > lst.size()) {
            count = lst.size() - start;
        }
        if (count <= 0) return null;
        //
        return lst.subList(start, start + count);
    }

    public static boolean isNullOrEmpty(Collection<?> lst) {
        return lst == null || lst.size() == 0;
    }
}
