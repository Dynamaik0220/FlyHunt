package game;

public class Target extends MovingThing {
    private PixelArtLibrary pixelArtLibrary;
    private PixelArt flyArt;
    private PixelArt beeArt;
    private boolean isBee;

    public Target(int startX, int startY, double xVel, double yVel,
                  double xAccel, double yAccel,double aSinusAmplitude, double bSinusPeriodFactor, int movementType,
                  PixelArtLibrary pixelArtLibrary, boolean isBee) {
        // Call parent constructor (MovingThing)
        super(startX, startY, xVel, yVel, xAccel, yAccel, aSinusAmplitude, bSinusPeriodFactor, movementType);

        this.pixelArtLibrary = pixelArtLibrary;
        this.flyArt = pixelArtLibrary.getPixelArt("FLY");
        this.beeArt = pixelArtLibrary.getPixelArt(("BEE"));
        this.isBee = isBee;
    }

    public boolean isBee() {
        return isBee;

    }
    public void drawFly (Spielfeld spielfeld){
        int[] pos = getLocation();
        spielfeld.drawPixelArt(flyArt, pos[0], pos[1]);
    }
    public void drawBee (Spielfeld spielfeld){
        int[] pos = getLocation();
        spielfeld.drawPixelArt(beeArt, pos[0], pos[1]);
    }
    public boolean isHit(int x, int y) {
        int[] pos = getLocation();
        // Nutze Math.abs für eine großzügige Hitbox (5 Pixel Umkreis)
        return Math.abs(pos[0] - x) < 5 && Math.abs(pos[1] - y) < 5;
    }
}
