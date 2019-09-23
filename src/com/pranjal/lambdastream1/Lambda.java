package com.pranjal.lambdastream1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import com.pranjal.lambdastream1.bean.Person;
import com.pranjal.lambdastream1.util.Comparator;
import com.pranjal.lambdastream1.util.Consumer;
import com.pranjal.lambdastream1.util.Predicate;


public class Lambda {

    @Test
    public void testConsumerInterface() {
        // Consumer Interface
        Consumer<List<String>> consumer = (List<String> strings) -> strings.clear();// method
                                                                                    // reference
                                                                                    // form
                                                                                    // List::clear
        List<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c"));
        consumer.accept(list);
        Assert.assertTrue(list.isEmpty());

        Consumer<List<String>> consumer1 = list1 -> list1.add("one");
        Consumer<List<String>> consumer2 = list2 -> list2.add("two");

        Consumer<List<String>> consumer3 = consumer1.andThen(consumer2);

        List<String> strings = new ArrayList<>();
        strings.add("zero");
        consumer3.accept(strings);
        System.out.println(strings);
    }

    @Test
    public void testPredicateInterface() {
        Predicate<String> predicate = s -> s.isEmpty();
        Predicate<String> predicateNegate = predicate.negate();

        Assert.assertTrue(predicate.test(""));
        Assert.assertFalse(predicate.test("Not Empty!"));

        Assert.assertFalse(predicateNegate.test(""));
        Assert.assertTrue(predicateNegate.test("Not Empty!"));

        // Fail-fast caution
        /*
         * Predicate<String> predicate1 = String::isEmpty; Predicate<String> predicate2 =
         * Objects::isNull;
         * 
         * Predicate<String> predicate3 = predicate1.and(null);
         * Assert.assertTrue(predicate3.test(""));
         */

        Predicate<String> predicate11 = s11 -> s11.startsWith("J");
        Predicate<String> predicate12 = s12 -> s12.length() == 4;

        Predicate<String> predicate13 = predicate11.xOr(predicate12);
        Assert.assertFalse(predicate13.test("Java"));
        Assert.assertFalse(predicate13.test("Jack"));
        Assert.assertTrue(predicate13.test("Abcd"));
    }

    @Test
    public void testComparatorInterface() {
        Person tom = new Person("Mike", "Johnson", 56);
        Person mike = new Person("Mike", "Klopenburg", 56);
        Person kim = new Person("Kim", "Kelly", 47);
        Person peter = new Person("Person", "James", 39);

        Function<Person, String> getLastName = p -> p.getLastName();
        Function<Person, String> getFirstName = p -> p.getFirstName();
        Function<Person, Integer> getAge = p -> p.getAge();

        Comparator<Person> cmp = Comparator.comparing(getLastName)
                                           .thenComparing(getAge)
                                           .thenComparing(getFirstName);

        int compare = cmp.compare(mike, tom);
        System.out.println(compare);

        Comparator<Person> nullsLast = Comparator.nullsLast(cmp);
        List<Person> people = Arrays.asList(tom, null, mike);
        //people.sort(nullsLast);
        //people.forEach(System.out::println);
    }
}
