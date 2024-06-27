package com.ametrinstudios.ametrin.util;

import java.awt.*;

public final class ColorHelper {
    public static int ColorToInt(Color color) { return color.getBlue() + (color.getGreen() * 256) + (color.getRed() * 65536); }
    public static int ColorToIntWithAlpha(Color color) { return (color.getAlpha()) + (color.getBlue()*256) + (color.getGreen() * 65536) + (color.getRed() * 16777216); }
}
