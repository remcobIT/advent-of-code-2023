package nl.remcoblom.day3;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.remcoblom.InputResource;

public class GearRatios {

    public static void main(String... args) throws IOException {
        try (var inputFile = InputResource.asBufferedReader("day3/input")) {
            FixedSizeQueue<String> fixedSizeQueue = new FixedSizeQueue<>(3);
            int total = 0;
            for (int lineNum = 0; lineNum <= 140; lineNum++) {
                String line = inputFile.readLine();
                fixedSizeQueue.addAndRemoveHead(line);
                ThreeLines threeLines = new ThreeLines(fixedSizeQueue.get(2), fixedSizeQueue.get(1),
                        fixedSizeQueue.get(0));
                //part 1
//                int lineTotal = calculateLinePart1(threeLines);
                //part 2
                int lineTotal = calculateLinePart2(threeLines);
                System.out.println("Line " + lineNum + ": " + lineTotal);
                total += lineTotal;
            }
            System.out.println("Total: " + total);
        }
    }

    private static int calculateLinePart1(ThreeLines threeLines) {
        if (threeLines.currentLine == null) {
            return 0;
        }
        List<DigitInLine> digitsInCurrentLine = threeLines.getDigitsWithIndexesFromCurrentLine();
        List<Integer> indexesNonDotCharactersOfPreviousLine = threeLines.getIndexesNonDotCharactersFromPreviousLine();
        List<Integer> indexesNonDotCharactersOfNextLine = threeLines.getIndexesNonDotCharactersFromNextLine();
        List<Integer> indexesNonDotAndNonDigitCharactersOfCurrentLine = threeLines.getIndexesNonDotAndNonDigitCharactersOfCurrentLine();

        for (DigitInLine digitInLine : digitsInCurrentLine) {
            boolean matchPreviousLine = indexesNonDotCharactersOfPreviousLine.stream().anyMatch(
                    i -> digitInLine.index == i - 1 || digitInLine.index == i || digitInLine.index == i + 1);
            boolean matchNextLine = indexesNonDotCharactersOfNextLine.stream().anyMatch(
                    i -> digitInLine.index == i - 1 || digitInLine.index == i || digitInLine.index == i + 1);
            boolean matchCurrentLine = indexesNonDotAndNonDigitCharactersOfCurrentLine.stream()
                    .anyMatch(i -> digitInLine.index == i - 1 || digitInLine.index == i + 1);
            boolean characterFound = matchPreviousLine || matchNextLine || matchCurrentLine;
            digitInLine.setAdjacentToOtherCharacter(characterFound);
        }

        return findAllNumbers(digitsInCurrentLine).stream()
                .map(NumberInLine::getValue)
                .reduce(0, Integer::sum);
    }

    private static int calculateLinePart2(ThreeLines threeLines) {
        if (threeLines.currentLine == null || !threeLines.currentLine.contains("*")){
            return 0;
        }
        List<Integer> indexesOfAsterisks = getIndexesOfAsterisks(threeLines.currentLine);
        int resultOfLine = 0;
        for (int index : indexesOfAsterisks) {
            Set<Integer> adjacentNumbersPreviousLine = getAdjacentNumbers(threeLines.previousLine, index);
            Set<Integer> adjacentNumbersCurrentLine = getAdjacentNumbers(threeLines.currentLine, index);
            Set<Integer> adjacentNumbersNextLine = getAdjacentNumbers(threeLines.nextLine, index);
            List<Integer> combinedAdjacentNumbers = Stream.of(adjacentNumbersPreviousLine, adjacentNumbersCurrentLine, adjacentNumbersNextLine).flatMap(
                    Collection::stream).toList();
            if (combinedAdjacentNumbers.size() == 2) {
                resultOfLine += combinedAdjacentNumbers.get(0) * combinedAdjacentNumbers.get(1);
            }
        }
        return resultOfLine;
    }

    private static Set<Integer> getAdjacentNumbers(String line, int indexOfAsterisk) {
        List<DigitInLine> digitsWithIndexesFromLine = ThreeLines.getDigitsWithIndexesFromLine(line);
        List<DigitInLine> adjacentDigits = new ArrayList<>();
        for (DigitInLine digitInLine : digitsWithIndexesFromLine) {
            if (digitInLine.index == indexOfAsterisk || digitInLine.index == indexOfAsterisk - 1 || digitInLine.index == indexOfAsterisk + 1) {
                adjacentDigits.add(digitInLine);
            }
        }
        return adjacentDigits.stream().map(digitInLine -> getCompleteIntFromAdjacentDigit(digitInLine, line)).collect(Collectors.toSet());
    }

    private static int getCompleteIntFromAdjacentDigit(DigitInLine digitInLine, String line) {
        char[] charArray = line.toCharArray();

        Deque<Character> characters = new ArrayDeque<>();
        characters.add(digitInLine.character);
        int i = 0;
        try {
            while (isCharADigit(charArray[digitInLine.index - ++i])) {
                characters.push(charArray[digitInLine.index - i]);
            }
        } catch (IndexOutOfBoundsException e) {}

        int j = 0;
        try {
            while (isCharADigit(charArray[digitInLine.index + ++j])) {
                characters.offer(charArray[digitInLine.index + j]);
            }
        } catch (IndexOutOfBoundsException e) {}

        return Integer.parseInt(characters.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    private static List<Integer> getIndexesOfAsterisks(String line) {
        char[] lineCharacters = line.toCharArray();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < lineCharacters.length; i++) {
            if (lineCharacters[i] == '*') {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private static List<NumberInLine> findAllNumbers(List<DigitInLine> digitsInLine) {
        List<NumberInLine> numbers = new ArrayList<>();
        NumberInLine numberInLine = new NumberInLine();
        for(int i = 0; i < digitsInLine.size(); i++) {
            numberInLine.addDigit(digitsInLine.get(i));
            if (!isCharNextToIndexCharADigit(digitsInLine, i)) {
                numbers.add(numberInLine);
                numberInLine = new NumberInLine();
            }
        }
        numbers.add(numberInLine);
        return numbers;
    }

    private static boolean isCharADigit(char character) {
        return character >= '0' && character <= '9';
    }

    private static boolean isCharNextToIndexCharADigit(List<DigitInLine> digitsInLine, int i) {
        return digitsInLine.size() == i + 1
                || digitsInLine.get(i).index + 1 == digitsInLine.get(i + 1).index;
    }

    public static class NumberInLine {
        private final List<DigitInLine> digitsInLine = new ArrayList<>();

        public void addDigit(DigitInLine digitInLine) {
            digitsInLine.add(digitInLine);
        }

        public int getValue() {
            if (digitsInLine.stream().noneMatch(digit -> digit.adjacentToOtherCharacter)) {
                return 0;
            }
            String numberAsString = digitsInLine.stream().map(digitInLine -> String.valueOf(digitInLine.character))
                    .collect(Collectors.joining());
            return Integer.parseInt(numberAsString);
        }

        public String toString() {
            return digitsInLine.stream().map(digitInLine -> String.valueOf(digitInLine.character)).collect(Collectors.joining());
        }
    }

    public static class DigitInLine {
        private final int index;
        private final char character;
        private boolean adjacentToOtherCharacter;

        public DigitInLine(int index, char character) {
            this.index = index;
            this.character = character;
        }

        public void setAdjacentToOtherCharacter(boolean isAdjacentToOtherCharacter) {
            adjacentToOtherCharacter = isAdjacentToOtherCharacter;
        }

        public String toString() {
            return "{index: " + index + ", char: " + character + ", adjacent: " + adjacentToOtherCharacter + "}";
        }
    }

    public static class ThreeLines {
        private final String previousLine;
        private final String currentLine;
        private final String nextLine;

        public ThreeLines(String previousLine, String currentLine, String nextLine) {
            this.previousLine = previousLine;
            this.currentLine = currentLine;
            this.nextLine = nextLine;
        }

        public List<DigitInLine> getDigitsWithIndexesFromCurrentLine() {
            return getDigitsWithIndexesFromLine(currentLine);
        }

        public static List<DigitInLine> getDigitsWithIndexesFromLine(String line) {
            if (line == null) {
                return Collections.emptyList();
            }
            List<DigitInLine> list = new ArrayList<>();
            char[] characters = line.toCharArray();
            for (int i = 0; i < characters.length; i++) {
                if (characters[i] >= '0' && characters[i] <= '9') {
                    list.add(new DigitInLine(i, characters[i]));
                }
            }
            return list;
        }

        public List<Integer> getIndexesNonDotAndNonDigitCharactersOfCurrentLine() {
            if (currentLine == null) {
                return Collections.emptyList();
            }
            return getIndexesFromLineBasedOnCriteria(currentLine, character -> character != '.' && (!(isCharADigit(character))));
        }

        public List<Integer> getIndexesNonDotCharactersFromPreviousLine() {
            if (previousLine == null) {
                return Collections.emptyList();
            }
            return getIndexesFromLineBasedOnCriteria(previousLine, character -> character != '.');
        }

        public List<Integer> getIndexesNonDotCharactersFromNextLine() {
            if (nextLine == null) {
                return Collections.emptyList();
            }
            return getIndexesFromLineBasedOnCriteria(nextLine, character -> character != '.');
        }

        private List<Integer> getIndexesFromLineBasedOnCriteria(String line, Predicate<Character> criteria) {
            char[] characters = line.toCharArray();
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < characters.length; i++) {
                if (criteria.test(characters[i])) {
                    indexes.add(i);
                }
            }
            return indexes;
        }
    }
}
