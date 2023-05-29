package view.utils;

import java.awt.*;
import java.util.Map;

public class ColorUtil {

    private final static Map<Color, String> names = Map.ofEntries(
            Map.entry(Color.BLUE, "blue"),
            Map.entry(Color.ORANGE, "orange")
    );

    public static String ColorName(Color color){
        String name = "unknown";
        if (names.containsKey(color)){
            name = names.get(color);
        }
        return name;
    }
}
