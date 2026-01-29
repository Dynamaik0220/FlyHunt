package game;

/*Bewegung von "forg" und "aim" werden getrennt.
 
 Die Klasse enthält bewusst keine Game-Loop- oder Input-Logik,
 sondern reagiert nur auf aufgerufene Bewegungsmethoden
 und zeichnet ihren aktuellen Zustand auf das Terminal.*/

import com.googlecode.lanterna.TextColor;

public class Frog {

    // Spielfeldgröße
    private int width;
    private int height;

    // Aim Position - centered on frog
    private int aimX = 48;  // frogX + half of frog width (34/2 = 17)
    private int aimY = 10;   // frogY + half of frog height (15/2 ≈ 7)

    // Frog Position
    private int frogX = 40;
    private int frogY = 55;

    public static Spielfeld spielfeld;
    private PixelArtLibrary pixelArtLibrary;
    private PixelArt frogArt;
    private PixelArt aimArt;

    public Frog(int width, int height, PixelArtLibrary pixelArtLibrary) {
        this.width = width;
        this.height = height;
        this.frogArt = pixelArtLibrary.getPixelArt("FROG");
        this.aimArt = pixelArtLibrary.getPixelArt("AIM");
    }

    /*Bewegt "aim" und "frog" nach links, solange die linke Spielfeldgrenze nicht überschritten wird.*/

    public void moveLeft() {
        if (aimX > 7) aimX--;
        if (frogX > 1) frogX--;
    }

    /*Bewegt "aim" und "frog" nach rechts, solange die rechte Spielfeldgrenze nicht überschritten wird.*/

    public void moveRight() {
        if (aimX < width - 22) aimX++;
        if (frogX < width - 30) frogX++;
    }

    /*Bewegt den "aim" nach oben.*/
    public void moveUp() {
        if (aimY > 5) aimY--;
    }

    /*Bewegt den "aim" nach unten.*/
    public void moveDown() {
        if (aimY < height - 35) aimY++;
    }

    /*Bewegt den Frosch basierend auf der Taste: a=links, d=rechts, w=oben, s=unten*/
    public void move(char key) {
        switch (key) {
            case 'a':
                moveLeft();
                break;
            case 'd':
                moveRight();
                break;
            case 'w':
                moveUp();
                break;
            case 's':
                moveDown();
                break;
            case ' ':
                break;
        }
    }

    /*Zeichnung von "aim" und "frog"*/
    public void draw(Spielfeld spielfeld) {
        drawaim(spielfeld);
        drawFrog(spielfeld);
    }

    /*Zeichnet "aim" an seiner aktuellen Position.*/
    private void drawaim(Spielfeld spielfeld) {
        spielfeld.drawPixelArt(aimArt, aimX, aimY);
    }


    /*Zeichnet den Frosch mithilfe des vordefinierten Musters.*/

    private void drawFrog(Spielfeld spielfeld) {
        spielfeld.drawPixelArt(frogArt, frogX, frogY);
    }

    private void drawChar(Spielfeld spielfeld, int x, int y, char c, TextColor color) {
        if (x >= 0 && x < Spielfeld.width && y >= 0 && y < Spielfeld.height) {
            Spielfeld.pixels[x][y].Text = c;
            Spielfeld.pixels[x][y].textColor = color;
            Spielfeld.pixels[x][y].backColor = Spielfeld.defaultBackColor;
        }
    }

    private void drawChar(Spielfeld spielfeld, int x, int y, char c, TextColor fg, TextColor bg) {
        if (x >= 0 && x < Spielfeld.width && y >= 0 && y < Spielfeld.height) {
            Spielfeld.pixels[x][y].Text = c;
            Spielfeld.pixels[x][y].textColor = fg;
            Spielfeld.pixels[x][y].backColor = bg;
        }
    }

    // Füge dies in Frog.java hinzu
    public int[] getAimLocation() {
    return new int[] {aimX + 2, aimY + 2};
    }

}