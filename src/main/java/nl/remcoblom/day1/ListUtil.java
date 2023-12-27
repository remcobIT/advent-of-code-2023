package nl.remcoblom.day1;

import java.util.List;

public class ListUtil {

    private ListUtil(){}

    public static int getIndexOfSmallestIntegerInList(List<Integer> list) {
        int index = 0;
        int smallestInt = Integer.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < smallestInt) {
                smallestInt = list.get(i);
                index = i;
            }
        }
        return index;
    }

    public static int getIndexOfBiggestIntegerInList(List<Integer> list) {
        int index = 0;
        int biggestInt = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > biggestInt) {
                biggestInt = list.get(i);
                index = i;
            }
        }
        return index;
    }
}
