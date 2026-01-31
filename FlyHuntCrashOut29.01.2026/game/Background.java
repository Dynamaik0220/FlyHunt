package game;

public class Background  {
	private int startX;
    private int startY;
    private PixelArtLibrary pixelArtLibrary;
	private PixelArt backroundArt;
    private PixelArt endscreenArt;
	
	public Background(PixelArtLibrary pixelArtLibrary,int startX ,int startY) {
		this.startX = startX;
        this.startY = startY;
        this.pixelArtLibrary = pixelArtLibrary;
        backroundArt = pixelArtLibrary.getPixelArt("BACKGROUND");
        endscreenArt = pixelArtLibrary.getPixelArt("ENDSCREEN_BACKGROUND");
	}
	
	public void drawBackground(Spielfeld spielfeld,int startX ,int startY){
		spielfeld.drawPixelArt(backroundArt, startX, startY);
	}
	
    public void drawEndscreen(Spielfeld spielfeld,int startX ,int startY){
        // Zeichne den speziellen Endscreen-Hintergrund
        // (Nachthimmel oben, vollständiges Gras unten, keine Sonne, keine Wolken)
        spielfeld.drawPixelArt(endscreenArt, startX, startY);
    }
}