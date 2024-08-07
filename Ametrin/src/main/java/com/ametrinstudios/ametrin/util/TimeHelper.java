package com.ametrinstudios.ametrin.util;

@SuppressWarnings("unused")

public final class TimeHelper {
    public static int secondsToTicks(int seconds) { return seconds * 20; }
    public static int minutesToTicks(int minutes) { return secondsToTicks(minutes * 60); }
    public static int hoursToTicks(int hours) { return minutesToTicks(hours * 60); }
}
