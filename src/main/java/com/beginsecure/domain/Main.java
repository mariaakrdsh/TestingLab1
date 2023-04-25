package com.beginsecure.domain;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

class StringLengthSort implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        if(o1.length() > o2.length()){
            return 1;
        }else{
            if(o1.length() < o2.length()){
                return -1;
            }else{
                return 0;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<String> words = new ArrayList<>();
        Scanner userPathInput = new Scanner(System.in);
        Scanner inputFile = null;
        String filePath;
        String nextLine;

        try {
            userPathInput.close();
            inputFile = new Scanner(Paths.get("/Users/mariakardas/Desktop/Laboratory/src/main/java/com/beginsecure/domain/input.txt"));
            if (!inputFile.hasNext()) { // Check if file is empty
                throw new IOException("Input file is empty");
            }
            while (inputFile.hasNext()) {
                nextLine = inputFile.nextLine();
                if(nextLine.isEmpty()) {
                    nextLine = " ";
                }
                List<String> wordsInLine = Arrays.asList(nextLine.split("[, ?.@]+"));
                for (String word : wordsInLine) {
                    //word = word.toLowerCase();
                    if (word.length() > 30) {
                        word = word.substring(0, 30);
                    }
                    Boolean correct = true;
                    if (words.contains(word) || word.isEmpty()) {
                        correct = false;
                    }
                    if (correct) {
                        words.add(word);
                    }
                }
            }

            Comparator<String> stringLengthComparator = new StringLengthSort();
            Collections.sort(words, stringLengthComparator);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/mariakardas/Desktop/Laboratory/src/main/java/com/beginsecure/domain/output.txt"));
            for (String word : words) {
                writer.write(word+'\n');
                System.out.println(word);
            }
            writer.close();

        } catch (IOException | NoSuchElementException | IllegalStateException e){
            System.out.println("Something is wrong\n");
        }
    }
}
