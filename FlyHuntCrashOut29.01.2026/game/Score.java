package game;

public class Score {
    // Initialize pixel art library
    private int startX;
    private int startY;
    private int spacing;  // Spacing between digits
    private PixelArtLibrary pixelArtLibrary;
    private PixelArt numbers[];
    private int currentScore;

    public Score(int startX, int startY, int spacing, PixelArtLibrary pixelArtLibrary) {
        this.startX = startX;
        this.startY = startY;
        this.spacing = spacing;
        this.pixelArtLibrary = pixelArtLibrary;
        this.currentScore = 0;

        // Initialize pixelart of every number
        this.numbers = new PixelArt[10];
        for (int i = 0; i < 10; i++){
            numbers[i] = pixelArtLibrary.getPixelArt("NUMBER_" + i);
        }
    }

    // Change position
    public void setPosition(int newX, int newY){
        this.startX = newX;
        this.startY = newY;
    }
    public int getCurrentScore(){
        return currentScore;
    }
    public void addScore(int value) {
        this.currentScore += value;
    }
    public void reset() {
    this.currentScore = 0;
    }

    public void draw(Spielfeld spielfeld) {
    // Erzeugt "00000"
    String scoreString = String.format("%05d", currentScore); 
    
        for (int i = 0; i < scoreString.length(); i++) {
            int digit = Character.getNumericValue(scoreString.charAt(i));
            PixelArt numberArt = numbers[digit];
            
            // startX sollte z.B. 115 sein, damit es rechts klebt
            int xPosition = startX + (i * spacing); 
            spielfeld.drawPixelArt(numberArt, xPosition, startY);
        }
    }
}