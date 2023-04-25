package com.beginsecure.domain;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private StringLengthSort stringLengthSort;
    private String input = "dog, cat, horse. Elephant, snake.";


    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all tests.");
    }

    @BeforeEach
    void setUp() {
        stringLengthSort = new StringLengthSort();
        System.out.println("Before each test.");
    }



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


    @Test
    void testOutputDoesNotContainUnexpectedWords() throws IOException {
        Main.main(new String[]{});

        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        assertThat(actualOutput, not(hasItems("Elephant1", "hamster")));
    }

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


    @ParameterizedTest
    @MethodSource("provideInputArrays")
    void testDistinctWordsSortedByLength(String input, List<String> expectedOutput) throws IOException {
        // Write input to file
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/beginsecure/domain/input.txt"));
        writer.write(input);
        writer.close();

        // Run main method
        Main.main(new String[]{});

        // Read output from file
        List<String> actualOutput = Files.readAllLines(Path.of("src/main/java/com/beginsecure/domain/output.txt"));

        // Assert that output contains distinct words sorted by length
        assertThat(actualOutput, Matchers.contains(expectedOutput.toArray()));

        // Assert that output is not empty
        assertThat(actualOutput, Matchers.not(empty()));

        // Assert that output has expected size
        assertThat(actualOutput, hasSize(expectedOutput.size()));
    }

    private static Stream<Object[]> provideInputArrays() {
        return Stream.of(
                new Object[]{ "dog, cat, horse. Elephant, snake.", Arrays.asList("dog", "cat", "horse", "snake", "Elephant") },
                new Object[]{ "A test input with a longer word hippopotamus.", Arrays.asList("A", "a", "test", "with", "word", "input", "longer", "hippopotamus") },
                new Object[]{ "another input with a capitalized word Badger.", Arrays.asList("a", "with", "word", "input", "Badger", "another", "capitalized") }
        );
    }




}

