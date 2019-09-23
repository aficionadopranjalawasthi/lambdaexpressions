package com.pranjal.lambdastream1.util;

import java.util.Objects;
import java.util.function.Function;


@FunctionalInterface
public interface Comparator<T> {
    int compare(T p1, T p2);

    default Comparator<T> reversed() {
        return (p1, p2) -> this.compare(p2, p1);
    }

    default <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<T, U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (p1, p2) -> {
            int cmp = this.compare(p1, p2);
            if (cmp == 0) {
                Comparator<T> other = comparing(keyExtractor);
                return other.compare(p1, p2);
            } else {
                return cmp;
            }
        };
    }

    static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<T, U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (p1, p2) -> {
            U u1 = keyExtractor.apply(p1);
            U u2 = keyExtractor.apply(p2);
            return u1.compareTo(u2);
        };
    }

    static <T> Comparator<T> nullsLast(Comparator<T> cmp) {
        return (t1, t2) -> {
            if (t1 == t2) {
                return 0;
            } else if (t1 == null) {
                return 1;
            } else if (t2 == null) {
                return -1;
            } else {
                return cmp.compare(t1, t2);
            }
        };
    }
}
