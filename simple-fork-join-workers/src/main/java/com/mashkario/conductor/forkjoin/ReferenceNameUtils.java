package com.mashkario.conductor.forkjoin;

public class ReferenceNameUtils {

    public static String createReferenceName(String taskName, int num) {
        return taskName + "_" + num;
    }

    public static boolean isReferenceNameForTask(String referenceName, String taskName) {
        int lastUnderscore = getDelimiterIndex(referenceName);

        if (lastUnderscore < 0) {
            return false;
        }

        return referenceName.substring(0, lastUnderscore).equals(taskName);

    }

    public static int getReferenceOrder(String referenceName) {
        int lastUnderscore = getDelimiterIndex(referenceName);

        return Integer.parseInt(referenceName.substring(lastUnderscore + 1));
    }

    private static int getDelimiterIndex(String referenceName) {
        return referenceName.lastIndexOf('_');
    }

}
