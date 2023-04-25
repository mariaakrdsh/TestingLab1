package com.beginsecure.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private StringLengthSort stringLengthSort; // Змінна для зберігання екземпляру класу StringLengthSort.

    // Метод, який викликається перед усіма тестами в класі.
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all tests.");
    }

    // Метод, який викликається перед кожним тестом в класі.
    @BeforeEach
    void setUp() {
        stringLengthSort = new StringLengthSort();
        System.out.println("Before each test.");
    }

    // Тестування виключень
    // Тест перевіряє, що метод compare класу StringLengthSort кидає NullPointerException, якщо один або обидва параметри null.
    @Test
    public void testNullPointerExceptionWhenComparingNullValues() {
        StringLengthSort stringLengthSort = new StringLengthSort();

        assertThrows(NullPointerException.class, () -> stringLengthSort.compare(null, "test"));
        assertThrows(NullPointerException.class, () -> stringLengthSort.compare("test", null));
        assertThrows(NullPointerException.class, () -> stringLengthSort.compare(null, null));
    }

    // Тест перевіряє, що вихідна послідовність слів відсортована за зростанням довжини слова і містить очікувані слова.
    @Test
    void testOutputContainsExpectedWords() throws IOException {
        String input = "dog, cat, horse. Elephant, snake.";

        FileWriter fileWriter = new FileWriter("src/main/java/com/beginsecure/domain/input.txt");
        fileWriter.write(input);
        fileWriter.close();

        Main.main(new String[]{});

        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        assertThat(actualOutput, hasItems("dog", "cat", "horse", "Elephant", "snake"));
    }

    // Тест перевіряє, що вихідна послідовність слів не містить неочікуваних слів.
    @Test
    void testOutputDoesNotContainUnexpectedWords() throws IOException {
        Main.main(new String[]{});

        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        assertThat(actualOutput, not(hasItems("Elephant1", "hamster")));
    }

    // Assert вирази. Цей тест перевіряє порівняння довжин рядків
    @Test
    void testStringLengthSort() {
        String str1 = "a";
        String str2 = "bb";
        String str3 = "ccc";

        assertTrue(stringLengthSort.compare(str1, str2) < 0);
        assertTrue(stringLengthSort.compare(str2, str3) < 0);
        assertTrue(stringLengthSort.compare(str1, str3) < 0);

        assertFalse(stringLengthSort.compare(str1, str2) > 0);
        assertFalse(stringLengthSort.compare(str2, str3) > 0);
        assertFalse(stringLengthSort.compare(str1, str3) > 0);

        assertEquals(0, stringLengthSort.compare(str1, str1));
        assertEquals(0, stringLengthSort.compare(str2, str2));
        assertEquals(0, stringLengthSort.compare(str3, str3));
    }

    @Test
    void testDistinctWordsSortedByLength() throws IOException {
        String input = "This is a test with some longer words, such as hippopotamus and antidisestablishmentarianism.";
        String expectedOutput = "a\nis\nas\nand\nThis\ntest\nwith\nsome\nsuch\nwords\nlonger\nhippopotamus\nantidisestablishmentarianism\n";

        // Write input to file
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/beginsecure/domain/input.txt"));
        writer.write(input);
        writer.close();

        // Run main method
        Main.main(new String[]{});

        // Read output from file
        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        // Assert that output contains distinct words sorted by length
        assertThat(actualOutput, Matchers.contains(expectedOutput.split("\n")));

        // Assert that output is not empty
        assertThat(actualOutput, Matchers.not(empty()));

        // Assert that output has size 13
        assertThat(actualOutput, hasSize(13));

    }


    // Цей тест перевіряє, чи слова відсортовані за довжиною
    // за допомогою параметризованого введення.
    @ParameterizedTest
    @ValueSource(strings = {"cat", "dog", "horse", "snake", "Elephant", "hippopotamus", "antidisestablishmentarianism"})
    void testSortingByLength(String word) throws IOException {
        // записати дані у файл input.txt
        FileWriter writer = new FileWriter("src/main/java/com/beginsecure/domain/input.txt");
        writer.write(word);
        writer.close();

        // запустити головний метод
        Main.main(new String[]{});

        // перевірити, чи відсортовані слова відповідають очікуваним
        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));
        String[] expectedOutput = word.split("\\W+");
        Arrays.sort(expectedOutput, new StringLengthSort());
        assertAll("Word Sorting Test",
                () -> assertThat(actualOutput, contains(expectedOutput)),
                () -> assertThat(actualOutput, hasSize(expectedOutput.length)),
                () -> assertTrue(Arrays.equals(actualOutput.toArray(), expectedOutput)),
                () -> assertArrayEquals(expectedOutput, actualOutput.toArray())
        );
    }

    // Параметризований тест перевіряє, що програма вірно сортує та виводить унікальні слова за зростанням довжини.
    @ParameterizedTest
    @MethodSource("provideInputArrays")
    void testDistinctWordsSortedByLength(String input, List<String> expectedOutput) throws IOException {
        // Записати вхідні дані у файл
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/beginsecure/domain/input.txt"));
        writer.write(input);
        writer.close();

        // Запустити головний метод
        Main.main(new String[]{});

        // Прочитати вихідні дані з файлу
        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        // Перевірити, що вихідні дані містять унікальні слова, відсортовані за зростанням довжини
        assertThat(actualOutput, Matchers.contains(expectedOutput.toArray()));

        // Перевірити, що вихідні дані не є порожніми
        assertThat(actualOutput, Matchers.not(empty()));

        // Перевірити, що кількість слів у вихідних даних відповідає очікуваному результату
        assertThat(actualOutput, hasSize(expectedOutput.size()));
    }

    // Метод, що надає параметри для тестування
    private static Stream<Object[]> provideInputArrays() {
        return Stream.of(
                new Object[]{ "dog, cat, horse. Elephant, snake.", Arrays.asList("dog", "cat", "horse", "snake", "Elephant") },
                new Object[]{ "A test input with a longer word hippopotamus.", Arrays.asList("A", "a", "test", "with", "word", "input", "longer", "hippopotamus") },
                new Object[]{ "another input with a capitalized word Badger.", Arrays.asList("a", "with", "word", "input", "Badger", "another", "capitalized") }
        );
    }




}

