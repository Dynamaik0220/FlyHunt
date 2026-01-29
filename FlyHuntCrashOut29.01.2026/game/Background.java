package game;

public class Background  {
	private int startX;
    private int startY;
    private PixelArtLibrary pixelArtLibrary;
	private PixelArt BackroundArt;
	
	public Background(PixelArtLibrary pixelArtLibrary,int startX ,int startY) {
		this.startX = startX;
        this.startY = startY;
        this.pixelArtLibrary = pixelArtLibrary;
        BackroundArt = pixelArtLibrary.getPixelArt("BACKGROUND");

	}
	public void draw_Background(Spielfeld spielfeld,int startX ,int startY){
		
		
		spielfeld.drawPixelArt(BackroundArt, startX, startY);
	}
}
