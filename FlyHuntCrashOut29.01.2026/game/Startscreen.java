package game;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;


public class Startscreen {
    private int selectedIndex = 1;
    private final String[] options = {"[ ENTER, um das Spiel zu starten ]" , "[ ESC, um das Spiel zu verlassen ]"};
    
    // Animation
    private int rainbowOffset = 0;
    private long lastUpdateTime = 0;
    private static final int ANIMATION_DELAY_MS = 125;
    private Background background;
    private Spielfeld spielfeld;
    public Background getBackground;
    
    private static final TextColor[] LOGO_PALETTE = {
        null,                            // Transparent
        new TextColor.RGB(28, 142, 142), // Hellgruen
        
    };
    
    private static final TextColor[] RAINBOW_COLORS = {
        new TextColor.RGB(255, 0, 0),    // Rot
        new TextColor.RGB(255, 127, 0),  // Orange
        new TextColor.RGB(255, 255, 0),  // Gelb
        new TextColor.RGB(0, 255, 0),    // Grün
        new TextColor.RGB(0, 0, 255),    // Blau
        new TextColor.RGB(75, 0, 130),   // Indigo
        new TextColor.RGB(148, 0, 211)   // Violett
    };
    
    // Konstruktor mit Background
    public Startscreen(PixelArtLibrary pixelArtLibrary) {
        this.background = new Background(pixelArtLibrary, 0, 0);

    }
    
    // Buchstabe mit statischer Minecraft-Farbpalette zeichnen
    private void maleBuchstabe(Pixel[][] spielfeld, int[][] pattern, int startX, int startY) {
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = LOGO_PALETTE[colorIndex];
                    // Doppelte Breite fuer quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    
    // Buchstabe mit animierter Regenbogenfarbe zeichnen
    private void maleBuchstabeRegenbogen(Pixel[][] spielfeld, int[][] pattern, int startX, int startY) {
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    // Diagonal wandernde Regenbogenfarben
                    int colorPos = (x + y + rainbowOffset) % RAINBOW_COLORS.length;
                    TextColor farbe = RAINBOW_COLORS[colorPos];
                    
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }

    // Navigation mit Pfeiltasten
    public int handleInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowRight) {
            selectedIndex = (selectedIndex + 1) % options.length;
        } else if (key.getKeyType() == KeyType.ArrowLeft) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        } else if (key.getKeyType() == KeyType.Escape) {
            return -1; // Signal zum Beenden
        } else if (key.getKeyType() == KeyType.Enter) {
            return 2; // Signal für Start Spiel
        }
        return 0; // Kein besonderes Event
    }

    // Logo "FLY HUNT" mit Animation zeichnen
    public void zeichneLogo(Pixel[][] spielfeld) {
        // Regenbogenanimation aktualisieren
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > ANIMATION_DELAY_MS) {
            rainbowOffset = (rainbowOffset + 1) % (RAINBOW_COLORS.length * 2);
            lastUpdateTime = currentTime;
        }
        
        int zeile1Y = 5;
        int zeile2Y = 16;
        int buchstabenAbstand = 14;
        
        // Berechne Gesamtbreite für "FLY" (3 Buchstaben, jeder 6 Pixel in pattern × 2 = 12 Pixel breit)
        // F, L, Y sind jeweils 6 Pixel breit im Pattern, werden aber doppelt gezeichnet (12 Pixel)
        int breiteProBuchstabe = 12; // 6 × 2
        int gesamtBreiteFly = (3 * breiteProBuchstabe) + (2 * buchstabenAbstand);
        int startXFly = (spielfeld.length - gesamtBreiteFly) / 2;
        
        // Berechne Gesamtbreite für "HUNT" (H, U, N haben 6 Pixel, T hat 6 Pixel)
        int gesamtBreiteHunt = (1 * breiteProBuchstabe) + (2 * buchstabenAbstand);
        int startXHunt = (spielfeld.length - gesamtBreiteHunt) / 2;
        
        

        // Zeile 1: "FLY" mit Minecraft-Farben
        maleBuchstabe(spielfeld, Letters.F, startXFly, zeile1Y);
        maleBuchstabe(spielfeld, Letters.L, startXFly + buchstabenAbstand, zeile1Y);
        maleBuchstabe(spielfeld, Letters.Y, startXFly + (buchstabenAbstand * 2), zeile1Y);

        // Zeile 2: "HUNT" mit animierten Regenbogenfarben
        maleBuchstabeRegenbogen(spielfeld, Letters.H, startXHunt, zeile2Y);
        maleBuchstabeRegenbogen(spielfeld, Letters.U, startXHunt + buchstabenAbstand, zeile2Y);
        maleBuchstabeRegenbogen(spielfeld, Letters.N, startXHunt + (buchstabenAbstand * 2), zeile2Y);
        maleBuchstabeRegenbogen(spielfeld, Letters.T, startXHunt + (buchstabenAbstand * 3), zeile2Y);
        
        //Biene unten rechts zeichnen  
        int bieneX = spielfeld.length - 50;
        int bieneY = spielfeld[0].length - 50;
        zeichneBiene(spielfeld, bieneX, bieneY);
    
    
    	//Frosch zeichnen
    	int frogX = spielfeld.length - 140;
    	int frogY = spielfeld[0].length - 35;
    	zeichneFrog(spielfeld, frogX, frogY);
    	
    	//Fliegen mit Animation zeichnen - jede bekommt einen Index für Phasenverschiebung
    	int flyX = spielfeld.length - 120;
    	int flyY = spielfeld[0].length - 40;
    	zeichneFlyAnimated(spielfeld, flyX, flyY, 0);
    	
    	int fly2X = spielfeld.length - 140;
    	int fly2Y = spielfeld[0].length - 45;
    	zeichneFlyAnimated(spielfeld, fly2X, fly2Y, 1);
    	
    	int fly3X = spielfeld.length - 130;
    	int fly3Y = spielfeld[0].length - 55;
    	zeichneFlyAnimated(spielfeld, fly3X, fly3Y, 2);
    	
    	
    }
    // Einzelnen Block im Spielfeld setzen
    private void setBlock(Pixel[][] spielfeld, int x, int y, TextColor farbe) {
        if (x >= 0 && x < spielfeld.length && y >= 0 && y < spielfeld[0].length) {
            spielfeld[x][y].Text = '█';
            spielfeld[x][y].textColor = farbe;
            spielfeld[x][y].backColor = farbe;
        }
    }

    // Schwierigkeitsgrad-Menü zeichnen
    public void zeichneMenue(Pixel[][] spielfeld) {
        // Start-Y-Position unter HUNT (HUNT ist bei zeile2Y = 16, Buchstaben sind 9 Zeilen hoch)
        int startY = 16 + 9 + 3; // HUNT Position + Höhe + Abstand = 28
        
        // Optionen untereinander zeichnen
        for (int i = 0; i < options.length; i++) {
            String text = options[i]; 
            TextColor farbe;
            if (i == selectedIndex) {
                // Für ausgewählten Menüpunkt: besondere Farbe (z.B. wechselnde Regenbogenfarbe)
                farbe = RAINBOW_COLORS[(rainbowOffset) % RAINBOW_COLORS.length];
            } else {
                // Für nicht-ausgewählte Menüpunkte: feste Farbe oder andere Animation
                farbe = RAINBOW_COLORS[(rainbowOffset + i) % RAINBOW_COLORS.length];
            }
            
            // Jede Option zentrieren
            int startX = (spielfeld.length - text.length()) / 2;
            int y = startY + (i * 2); // 2 Zeilen Abstand zwischen den Optionen
            
            // Text zeichnen
            for (int j = 0; j < text.length(); j++) {
                if (startX + j < spielfeld.length && y < spielfeld[0].length) {
                    spielfeld[startX + j][y].Text = text.charAt(j);
                    spielfeld[startX + j][y].textColor = farbe;
                }
            }
        }
    }
    
    // Biene unten rechts zeichnen
    public void zeichneBiene(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.BEE;
        TextColor[] colors = AnimalColor.BEE;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite für quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    public void zeichneFrog(Pixel[][] spielfeld, int startX, int startY) {
        // Wähle Pattern basierend auf Animation
        int[][] pattern = AnimalColor.isFrogQuacking() ? Animals.FROGQUACK : Animals.FROG;
        TextColor[] colors = AnimalColor.FROGFLY;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite für quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    
    // Neue Methode: Fliege mit Animation zeichnen
    public void zeichneFlyAnimated(Pixel[][] spielfeld, int startX, int startY, int flyIndex) {
        int[][] pattern = Animals.FLY;
        TextColor[] colors = AnimalColor.FROGFLY;
        
        // Hole den Y-Offset von der Animation in AnimalColor
        int yOffset = AnimalColor.getFlyYOffset(flyIndex);
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite für quadratischen Effekt + Y-Offset für Animation
                    setBlock(spielfeld, startX + (x * 2), startY + y + yOffset, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y + yOffset, farbe);
                }
            }
        }
    }
}