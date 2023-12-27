package nl.remcoblom.day4;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import nl.remcoblom.InputResource;

public class ScratchCards {

    private static final Queue<Integer> queue = new ArrayDeque<>();
    private static List<String> lines;

    public static void main(String... args) throws IOException {
        try (var inputFile = InputResource.asBufferedReader("day4/input")) {
            // part 1
//            Optional<Integer> result = inputFile.lines()
//                    .map(ScratchCards::getPointsOfLine)
//                    .reduce(Integer::sum);
//            System.out.println("Total: " + result.orElse(0));

            // part 2
            int total = 0;
            lines = inputFile.lines().toList();
            initQueue(lines);
            while (queue.peek() != null) {
                Integer lineNumber = queue.poll();
                System.out.println(lineNumber);
                long numberOfMatches = getNumberOfMatches(lines.get(lineNumber - 1));
                addMatchesToQueue(lineNumber, numberOfMatches);
                total++;
            }
            System.out.println("Total: " + total);
        }
    }

    private static void initQueue(List<String> lines) {
        for (int i = 1; i <= lines.size(); i++) {
            queue.add(i);
        }
    }

    private static void addMatchesToQueue(int lineNumber, long numberOfMatches) {
        for(int i = 0; i < numberOfMatches; i++) {
            if(lineNumber <= lines.size()) {
                queue.add(++lineNumber);
            }
        }
    }

    public static long getNumberOfMatches(String line) {
        Set<Integer> winningNumbers = getWinningNumbers(line);
        Set<Integer> numbersYouHave = getNumbersYouHave(line);
        return numbersYouHave.stream()
                .filter(winningNumbers::contains)
                .count();
    }

    public static int getPointsOfLine(String line) {
        Set<Integer> winningNumbers = getWinningNumbers(line);
        Set<Integer> numbersYouHave = getNumbersYouHave(line);
        return numbersYouHave.stream()
                .filter(winningNumbers::contains)
                .reduce(0, (points, number) -> points == 0 ? 1 : points * 2);
    }

    public static Set<Integer> getWinningNumbers(String line) {
        return getNumbersFromString(line.substring(line.indexOf(':') + 1, line.indexOf('|')));
    }

    public static Set<Integer> getNumbersYouHave(String line) {
        return getNumbersFromString(line.substring(line.indexOf('|') + 1));
    }

    public static Set<Integer> getNumbersFromString(String theString) {
        return Arrays.stream(theString.trim().split(" "))
                .filter(substring -> !substring.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
