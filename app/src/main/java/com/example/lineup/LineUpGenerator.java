package com.example.lineup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LineUpGenerator {
    private static final int RANDOM_BOUND_MIN = 2;
    private static final int RANDOM_BOUND_MAX = 10;

    private static Random random = new Random(System.currentTimeMillis());

    public static List<List<Integer>> generateSimpleLineUp() {
        List<Integer> firstLine = generateLineList(2);
        List<Integer> secondLine = generateLineList(5);
        List<Integer> thirdLine = generateLineList(3);
        List<Integer> fourthLine = generateLineList(1);
        return Arrays.asList(firstLine, secondLine, thirdLine, fourthLine);
    }

    public static List<List<Integer>> generateRandomLineUp() {
        List<List<Integer>> lineUpList = new ArrayList<>();

        for (int i = 0; i < getRandomCount(); i++) {
            lineUpList.add(generateLineList(getRandomCount()));
        }
        return lineUpList;
    }

    private static List<Integer> generateLineList(int columnCount) {
        List<Integer> lineList = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            lineList.add(i);
        }
        return lineList;
    }

    private static int getRandomCount() {
        return random.nextInt(RANDOM_BOUND_MAX - RANDOM_BOUND_MIN) + RANDOM_BOUND_MIN;
    }
}
