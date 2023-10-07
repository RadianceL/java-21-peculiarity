package com.eddie;

import java.util.*;

/**
 * java21引入
 * {@link java.util.SequencedCollection}
 * SequencedCollection继承了Collection接口，同时定义了reversed 反转方法
 * 提供了addFirst、addLast、getFirst、getLast、removeFirst、removeLast的default实现
 * List、SequencedSet接口都继承了SequencedCollection接口
 * {@link java.util.SequencedMap}
 * SequencedMap接口继承了Map接口，它定义了reversed 反转方法
 * 同时提供了firstEntry、lastEntry、pollFirstEntry、pollLastEntry、putFirst、putLast、sequencedKeySet、sequencedValues、sequencedEntrySet方法的默认实现
 * 来统一各类集合的顺序方法方法
 * SequencedCollection -> List、Deque
 * SequencedSet        -> LinkedHashSet、SortedSet
 * SequencedMap        -> LinkedHashMap、SortedMap
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP431SequencedCollections {

    void main(String[] args) {
        List<String> sequencedCollectionList = new ArrayList<>(8);
        SequencedSet<String> sequencedCollectionSet = new LinkedHashSet<>(8);
        SequencedMap<String, String> sequencedCollectionMap = new LinkedHashMap<>(8);
        // 取反转倒序对象
        System.out.println(sequencedCollectionList.reversed());

        // immutable 方法
        SequencedCollection<String> immutableList = Collections.unmodifiableSequencedCollection(sequencedCollectionList);
        SequencedSet<String> immutableSet = Collections.unmodifiableSequencedSet(sequencedCollectionSet);
        SequencedMap<String, String> immutableMap = Collections.unmodifiableSequencedMap(sequencedCollectionMap);
    }


}
