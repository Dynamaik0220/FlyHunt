package game;

public class Health {
    private int startX;
    private int startY;
    private int spacing;
    private PixelArtLibrary pixelArtLibrary;
    private int currentHealth;
    private int maxHealth;
    private PixelArt fullHeart;
    private PixelArt halfHeart;
    private boolean isAnimating = false;
    private int animationFrames = 0;

    public Health(int startX, int startY, int spacing, int maxHealth, PixelArtLibrary pixelArtLibrary){
        this.startX = startX;
        this.startY = startY;
        this.spacing = spacing;
        this.pixelArtLibrary = pixelArtLibrary;
        this.maxHealth = maxHealth;
        this.currentHealth= maxHealth;
        fullHeart = pixelArtLibrary.getPixelArt("FULL_HEART");
        halfHeart = pixelArtLibrary.getPixelArt("HALF_HEART");
    }

    // Lose health
    public void decrease(int damage){
        currentHealth -= damage;
        isAnimating = true;
        animationFrames = 15;
    }
    public int getCurrentHealth(){
        return currentHealth;
    }
    // Update animation frames
    public void update() {
        if (isAnimating) {
            animationFrames--;
            if (animationFrames <= 0) {
                isAnimating = false;
            }
        }
    }

    public void draw(Spielfeld spielfeld){
        // Skip drawing every other frame when animating (creates flash effect)
        if (isAnimating && animationFrames % 2 == 0) {
            return; // Don't draw hearts this frame
        }
        for (int i = 0; i < maxHealth / 2; i++){
            int xPosition = startX + i * spacing;
            int heartsRemaining = currentHealth - (i * 2);

            if (heartsRemaining >= 2) {
                spielfeld.drawPixelArt(fullHeart, xPosition, startY);
            } else if (heartsRemaining == 1){
                spielfeld.drawPixelArt(halfHeart, xPosition, startY);
            }
        }
    }
}
