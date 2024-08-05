package com.ametrinstudios.ametrin.util;

@SuppressWarnings("unused")

public final class TimeHelper {
    public static int SecondsToTicks(int seconds) { return seconds * 20; }
    public static int MinutesToTicks(int minutes) { return SecondsToTicks(minutes * 60); }
    public static int HoursToTicks(int hours) { return MinutesToTicks(hours * 60); }
}
