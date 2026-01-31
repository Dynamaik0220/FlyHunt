package game;

import com.googlecode.lanterna.TextColor;

public class PixelArt {
    private int[][] pattern;
    private TextColor[] colorPalette;
    private String name;

    public PixelArt(String name, int[][] pattern, TextColor[] colorPalette){
        this.name = name;
        this.pattern = pattern;
        this.colorPalette = colorPalette;
    }
    public String getName(){
        return name;
    }
    public int[][] getPattern(){
        return pattern;
    }
    public TextColor getColor(int colorIndex) {
        if (colorIndex >= 0 && colorIndex < colorPalette.length) {
            return colorPalette[colorIndex];
        }
        return null;  // Transparent
    }
}
