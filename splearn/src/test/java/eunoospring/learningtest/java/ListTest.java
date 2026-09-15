package eunoospring.learningtest.java;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ListTest {

    @Test
    void init() {
        List<Integer> list1 = List.of(1, 2, 3, 4);

        // List.of()는 불변으로 만듦.
        assertThatThrownBy(() -> list1.set(0 ,0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list1.add(1, 5)).isInstanceOf(UnsupportedOperationException.class);

        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);

        list2.set(0, 0); // 값 변경은 가능
        // 값 추가는 불가능
        assertThatThrownBy(() -> list2.add(1, 5)).isInstanceOf(UnsupportedOperationException.class);

        List<Integer> list3 = new ArrayList<>(List.of(1, 2, 3, 4));

        list3.set(0, 0);
        list3.add(1,5);
        System.out.println(list3);

    }
}
