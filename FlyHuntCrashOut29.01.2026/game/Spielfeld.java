package game;

import com.googlecode.lanterna.TextColor;

public class Spielfeld {
    public static int width;
    public static int height;
    public static Pixel[][] pixels;
    public static TextColor defaultBackColor;
    public static TextColor defaultTextColor;

    public Spielfeld(int width, int height, TextColor defaultBackColor, TextColor defaultTextColor) {
        this.width = width;
        this.height = height;
        this.defaultBackColor = defaultBackColor;
        this.defaultTextColor = defaultTextColor;
        this.pixels = new Pixel[width][height];

        initializePixels();
    }
    private void initializePixels() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = new Pixel();
            }
        }
    }

    // Clears the entire spielfeld to default values
    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j].textColor = defaultTextColor;
                pixels[i][j].backColor = defaultBackColor;
                pixels[i][j].Text = ' ';
            }
        }
    }

    public void drawPixelArt(PixelArt pixelArt, int startX, int startY) {
        int[][] pattern = pixelArt.getPattern();

        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];

                if (colorIndex > 0) {  // 0 = transparent/empty
                    TextColor color = pixelArt.getColor(colorIndex);

                    if (color != null) {
                        // Set two characters for each pixel
                        if (startX + x * 2 >= 0 && startX + x * 2 < width &&
                                startY + y >= 0 && startY + y < height){
                            pixels[startX + x * 2][startY + y].Text = '█';
                            pixels[startX + x * 2][startY + y].backColor = color;
                            pixels[startX + x * 2][startY + y].textColor = color;
                        }

                        if (startX + x * 2 + 1 >= 0 && startX + x * 2 + 1 < width &&
                                startY + y >= 0 && startY + y < height) {
                            pixels[startX + x * 2 + 1][startY + y].Text = '█';
                            pixels[startX + x * 2 + 1][startY + y].backColor = color;
                            pixels[startX + x * 2 + 1][startY + y].textColor = color;
                        }
                    }
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }
}
