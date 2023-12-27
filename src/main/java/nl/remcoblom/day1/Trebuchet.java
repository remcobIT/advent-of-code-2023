package nl.remcoblom.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import nl.remcoblom.InputResource;

public class Trebuchet
{
    private static final LinkedHashMap<String, Integer> NUMBER_DICTIONARY = new LinkedHashMap<>();
    private static final boolean TRANSLATE_WORDS_TO_DIGITS = true;

    public static void main( String[] args ) throws IOException
    {
        initDictionary();
        try (var inputFile = InputResource.asBufferedReader("day1/input")) {
            inputFile.lines()
                    .peek(line -> System.out.println("Original line: " + line))
                    .map(line -> changeFirstAndLastNumbertextsToDigits(line))
                    .map(lines -> concatFirstAndLastDigit(lines))
                    .reduce(Integer::sum)
                    .ifPresent(result -> System.out.println("Result: " + result));
        }
    }

    private static void initDictionary() {
        NUMBER_DICTIONARY.put("one", 1);
        NUMBER_DICTIONARY.put("two", 2);
        NUMBER_DICTIONARY.put("three", 3);
        NUMBER_DICTIONARY.put("four", 4);
        NUMBER_DICTIONARY.put("five", 5);
        NUMBER_DICTIONARY.put("six", 6);
        NUMBER_DICTIONARY.put("seven", 7);
        NUMBER_DICTIONARY.put("eight", 8);
        NUMBER_DICTIONARY.put("nine", 9);
    }

    private static String[] changeFirstAndLastNumbertextsToDigits(String line) {
        if (TRANSLATE_WORDS_TO_DIGITS) {
            return new String[]{ changeFirstNumbertextToDigit(line), changeLastNumbertextToDigit(line) };
        }
        return new String[]{line, line};
    }

    private static String changeFirstNumbertextToDigit(String line) {
        String firstNumbertextInLine = getFirstNumbertextInLine(line);
        return line.replaceFirst(firstNumbertextInLine, NUMBER_DICTIONARY.get(firstNumbertextInLine).toString());
    }

    private static String getFirstNumbertextInLine(String line) {
        List<Integer> firstIndexesOfAllNumbertexts = getFirstIndexesOfAllNumbertextsInLine(line);
        String[] allPossibleNumbertexts = getAllPossibleNumbertexts();
        int indexOfSmallestInt = ListUtil.getIndexOfSmallestIntegerInList(firstIndexesOfAllNumbertexts);
        return allPossibleNumbertexts[indexOfSmallestInt];
    }

    private static String[] getAllPossibleNumbertexts() {
        return NUMBER_DICTIONARY.sequencedKeySet().toArray(new String[0]);
    }

    private static List<Integer> getFirstIndexesOfAllNumbertextsInLine(String line) {
        List<Integer> numbertextFirstIndexesInLine = new ArrayList<>(NUMBER_DICTIONARY.keySet().stream().map(line::indexOf).toList());
        numbertextFirstIndexesInLine.replaceAll(keyIndex -> keyIndex == -1 ? Integer.MAX_VALUE : keyIndex);
        return numbertextFirstIndexesInLine;
    }

    private static String changeLastNumbertextToDigit(String line) {
        String lastNumbertextInLine = getLastNumbertextInLine(line);
        return line.replace(lastNumbertextInLine, NUMBER_DICTIONARY.get(lastNumbertextInLine).toString());
    }

    private static String getLastNumbertextInLine(String line) {
        List<Integer> lastIndexesOfAllNumbertexts = getLastIndexesOfAllNumbertextsInLine(line);
        String[] allPossibleNumbertexts = getAllPossibleNumbertexts();
        int indexOfBiggestInt = ListUtil.getIndexOfBiggestIntegerInList(lastIndexesOfAllNumbertexts);
        return allPossibleNumbertexts[indexOfBiggestInt];
    }

    private static List<Integer> getLastIndexesOfAllNumbertextsInLine(String line) {
        return new ArrayList<>(NUMBER_DICTIONARY.keySet().stream().map(line::lastIndexOf).toList());
    }

    private static int concatFirstAndLastDigit(String[] translatedLines) {
        int foundNumber = Integer.parseInt(findFirstDigit(translatedLines[0]) + findLastDigit(translatedLines[1]));
        System.out.println("Number: " + foundNumber);
        return foundNumber;
    }

    private static String findFirstDigit(String line) {
        String digit = line.chars()
                .filter(character -> character >= '0' && character <= '9')
                .mapToObj(Character::toString)
                .findFirst()
                .orElseThrow();
        System.out.println("Digit: " + digit);
        return digit;
    }

    private static String findLastDigit(String line) {
        String reversedLine = line.chars()
                .mapToObj(c -> (char) c)
                .toList()
                .reversed().stream()
                .map(Object::toString)
                .collect(Collectors.joining());
        return findFirstDigit(reversedLine);
    }
}
