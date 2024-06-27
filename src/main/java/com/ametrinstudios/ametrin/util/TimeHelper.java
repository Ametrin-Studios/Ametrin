package com.ametrinstudios.ametrin.util;

public final class TimeHelper {
    public static int SecondsToTicks(int seconds) { return seconds * 20; }
    public static int MinutesToTicks(int minutes) { return SecondsToTicks(minutes * 60); }
}
