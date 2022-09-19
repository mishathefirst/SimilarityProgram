import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            //reading file
            Path path = Paths.get("src/input.txt");
            String read = Files.readAllLines(path).get(0);
            int firstArrayLength = 0;
            try {
                firstArrayLength = Integer.parseInt(read);
            } catch (NumberFormatException ex) {
                System.out.println("Format error!");
            }

            String[] firstArray = new String[firstArrayLength];

            for (int i = 0; i < firstArrayLength; i++) {
                firstArray[i] = Files.readAllLines(path).get(i + 1);
            }


            int positionNumber = firstArrayLength + 1;
            String stringNumber = Files.readAllLines(path).get(positionNumber);
            int secondArrayLength = 0;
            try {
                secondArrayLength = Integer.parseInt(stringNumber);
            } catch (NumberFormatException ex) {
                System.out.println("Format error!");
            }
            String[] secondArray = new String[secondArrayLength];

            for (int i = 0; i < secondArrayLength; i++) {
                secondArray[i] = Files.readAllLines(path).get(i + firstArrayLength + 2);
            }


            int longerLength = secondArray[0].length();

            //counting coefficients of similarity for every string (word) pair
            Double[][] coefficients = new Double[firstArrayLength][secondArrayLength];
            for (int i = 0; i < firstArrayLength; i++) {
                for (int j = 0; j < secondArrayLength; j++) {
                    String longString;
                    String shortString;
                    if (firstArray[i].length() < secondArray[j].length()) {
                        longString = secondArray[j];
                        shortString = firstArray[i];
                    } else {
                        longString = firstArray[i];
                        shortString = secondArray[j];
                    }

                    coefficients[i][j] = similarityCoefficient(longString, shortString) / (double) longString.length();
                }
            }

            //selecting pairs with the greatest similarity down to pairs with less similarity,
            // and writing them into the file
            double globalGreatestCoefficient = 1.0;
            double greatestCoefficient = 0.0;
            String firstWord = "", secondWord = "";
            boolean[] firstArrayUsed = new boolean[firstArrayLength], secondArrayUsed = new boolean[secondArrayLength];
            int firstWordNumber = -1, secondWordNumber = -1;
            String prevFirstWord = "";

            File file = new File("output.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            for (int z = 0; z < firstArrayLength * secondArrayLength; z++) {
                for (int i = 0; i < firstArrayLength; i++) {
                    for (int j = 0; j < secondArrayLength; j++) {

                        if (coefficients[i][j] > greatestCoefficient && globalGreatestCoefficient > coefficients[i][j]
                                && !firstArrayUsed[i] && !secondArrayUsed[j]) {
                            firstWord = firstArray[i];
                            secondWord = secondArray[j];
                            greatestCoefficient = coefficients[i][j];
                            firstWordNumber = i;
                            secondWordNumber = j;

                        }
                    }
                }
                //avoiding printing the same pair in a loop
                if (!prevFirstWord.equals(firstWord)) {
                    fileOutputStream.write((firstWord + ":" + secondWord + "\n").getBytes());
                    prevFirstWord = firstWord;
                }
                globalGreatestCoefficient = greatestCoefficient;
                firstArrayUsed[firstWordNumber] = true;
                secondArrayUsed[secondWordNumber] = true;
                greatestCoefficient = 0.0;

            }

            for(int i = 0; i < firstArrayLength; i++) {
                if (!firstArrayUsed[i]) {
                    fileOutputStream.write((firstArray[i] + ":?\n").getBytes());
                }
            }

            for(int j = 0; j < secondArrayLength; j++) {
                if (!secondArrayUsed[j]) {
                    fileOutputStream.write((secondArray[j] + ":?\n").getBytes());
                }
            }

            fileOutputStream.close();

        } catch (IOException ex) {
            System.out.println("Error reading file or writing into it!");
        }


    }



    public static int similarityCoefficient(String s1, String s2) {
        int similarityCoefficient = 0;
        boolean[] firstStringValues = new boolean[s1.length()];
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    firstStringValues[i] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < s1.length(); i++) {
            if (firstStringValues[i]) {
                similarityCoefficient++;
            }
        }
        return similarityCoefficient;
    }

}
