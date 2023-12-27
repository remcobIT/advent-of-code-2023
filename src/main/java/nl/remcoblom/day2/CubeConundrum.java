package nl.remcoblom.day2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nl.remcoblom.InputResource;

public class CubeConundrum {

    private static final int MAX_NUMBER_RED = 12;
    private static final int MAX_NUMBER_GREEN = 13;
    private static final int MAX_NUMBER_BLUE = 14;

    public static void main(String... args) throws IOException {
        try (var inputFile = InputResource.asBufferedReader("day2/input")) {
            // part 1
//                inputFile.lines()
//                        .map(line -> isGamePossible(line))
//                        .reduce(Integer::sum)
//                        .ifPresent(System.out::println);

            // part 2
                inputFile.lines()
                        .map(line -> lowestGamePossible(line))
                        .map(game -> power(game))
                        .reduce(Integer::sum)
                        .ifPresent(System.out::println);
        }
    }

    private static GamePart lowestGamePossible(String line) {
        String[] colonParts = line.split(": ");
        List<GamePart> gameParts = parseToGameParts(colonParts[1]);
        return calculateLowestGamePartPossible(gameParts);
    }

    private static List<GamePart> parseToGameParts(String lineAfterColon) {
        return Arrays.stream(lineAfterColon.split("; "))
                .map(semicolonPart -> new GamePart(semicolonPart))
                .toList();
    }

    private static GamePart calculateLowestGamePartPossible(List<GamePart> gameParts) {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;

        for (GamePart gamePart : gameParts) {
            if (gamePart.red > maxRed) {
                maxRed = gamePart.red;
            }
            if (gamePart.green > maxGreen) {
                maxGreen = gamePart.green;
            }
            if (gamePart.blue > maxBlue) {
                maxBlue = gamePart.blue;
            }
        }

        return new GamePart(maxRed, maxGreen, maxBlue);
    }

    private static int power(GamePart gamePart) {
        return gamePart.red * gamePart.green * gamePart.blue;
    }

    private static int isGamePossible(String line) {
        System.out.println("-------");
        String[] colonParts = line.split(": ");
        boolean isGamePossible = parseToGameParts(colonParts[1]).stream()
                .allMatch(gamePart -> isGamePartPossible(gamePart));
        System.out.println("Game possible: " + isGamePossible);
        return isGamePossible ? Integer.parseInt(colonParts[0].split("Game ")[1]) : 0 ;
    }

    private static boolean isGamePartPossible(GamePart gamePart) {
        System.out.println("red:" + gamePart.red);
        System.out.println("green:" + gamePart.green);
        System.out.println("blue:" + gamePart.blue);
        boolean isPossible = gamePart.red <= MAX_NUMBER_RED && gamePart.green <= MAX_NUMBER_GREEN
                && gamePart.blue <= MAX_NUMBER_BLUE;
        System.out.println(isPossible);
        System.out.println();
        return isPossible;
    }

    public static class GamePart {
        private int red = 0;
        private int green = 0;
        private int blue = 0;

        public GamePart(String semicolonPart) {
            String[] colonParts = semicolonPart.split(", ");
            for (String colonPart : colonParts) {
                String[] gamePartsAsString = colonPart.split(" ");
                int numberOfCubes = Integer.parseInt(gamePartsAsString[0]);
                switch (gamePartsAsString[1]) {
                    case "red" -> red = numberOfCubes;
                    case "green" -> green = numberOfCubes;
                    case "blue" -> blue = numberOfCubes;
                    default -> throw new RuntimeException();
               }
            }
        }

        public GamePart(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }
}
