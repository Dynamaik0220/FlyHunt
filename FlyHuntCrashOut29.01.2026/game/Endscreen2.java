package game;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.Random;

public class Endscreen2 {
    private int selectedIndex = 1;
    private final String[] options = {"[ ENTER, um zurück zu kehren zum Startbilschirm ]", "[ ESC, um das Spiel zu verlassen ]"};
    
    // Animation
    private int bloodColorOffset = 0;
    private long lastAnimationTime = 0;
    private static final int ANIMATION_DELAY_MS = 150;
    private final Random random = new Random();

    private Background background;
    private Spielfeld spielfeld;

    // Farbpaletten
    private static final TextColor[] BLOOD_COLORS = {
        new TextColor.RGB(82, 13, 1),    // Dunkelrot
        new TextColor.RGB(120, 20, 2),
        new TextColor.RGB(160, 27, 3),
        new TextColor.RGB(200, 34, 4),   // Hellrot
        new TextColor.RGB(140, 23, 3),
        new TextColor.RGB(100, 16, 2)
    };

    // Buchstabe mit Blut-Effekt zeichnen (pulsierende Farbe + Tropfen)
    private void maleBuchstabeBlut(Pixel[][] spielfeld, int[][] pattern, int startX, int startY, boolean isOver) {
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                if (pattern[y][x] > 0) {
                    // Farbe basierend auf Position und Animation
                    int colorPos = (bloodColorOffset + (isOver ? x*2 + y*3 : x + y)) % BLOOD_COLORS.length;
                    TextColor farbe = BLOOD_COLORS[colorPos];
                    
                    // ZufÃ¤llige leichte Position-Variation fÃ¼r "zittrigen" Effekt
                    int drawX = startX + (x * 2);
                    int drawY = startY + y;
                    if (random.nextInt(isOver ? 8 : 10) == 0) {
                        drawX += random.nextInt(3) - 1;
                        if (isOver) drawY += random.nextInt(2);
                    }
                    
                    // Doppelte Breite fÃ¼r quadratischen Effekt
                    setBlock(spielfeld, drawX, drawY, farbe);
                    setBlock(spielfeld, drawX + 1, drawY, farbe);
                    
                    // ZufÃ¤llige Bluttropfen
                    if (random.nextInt(isOver ? 15 : 20) == 0) {
                        int dripX = drawX + random.nextInt(isOver ? 3 : 2);
                        int dripY = drawY + 1;
                        if (dripY < spielfeld[0].length) {
                            spielfeld[dripX][dripY].Text = '█';
                            spielfeld[dripX][dripY].textColor = farbe;
                        }
                    }
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
            return 1; // Signal für zurück zum Startscreen
        }
        return 0; // Kein besonderes Event
    }

    // Logo "GAME OVER" mit Blut-Animation zeichnen
    public void zeichneLogo(Pixel[][] spielfeld) {
        // Blut-Animation aktualisieren
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnimationTime > ANIMATION_DELAY_MS) {
            bloodColorOffset++;
            lastAnimationTime = currentTime;
        }
        
        int zeile1Y = 5;
        int zeile2Y = 16;
        int buchstabenAbstand = 14;
        
        int breiteProBuchstabe = 12; 
        int gesamtBreiteFly = (3 * breiteProBuchstabe) + (2 * buchstabenAbstand);
        int startXGAME = (spielfeld.length - gesamtBreiteFly) / 2;
        
        int gesamtBreiteHunt = (1 * breiteProBuchstabe) + (2 * buchstabenAbstand);
        int startXOVER = (spielfeld.length - gesamtBreiteHunt) / 2;
        
        // Zeile 1: "GAME"
        maleBuchstabeBlut(spielfeld, Letters.G, startXGAME, zeile1Y, true);
        maleBuchstabeBlut(spielfeld, Letters.A, startXGAME + buchstabenAbstand, zeile1Y, true);
        maleBuchstabeBlut(spielfeld, Letters.M, startXGAME + (buchstabenAbstand * 2), zeile1Y, true);
        maleBuchstabeBlut(spielfeld, Letters.E, startXGAME + 2 + (buchstabenAbstand * 3), zeile1Y, true);

        // Zeile 2: "OVER"
        int versatz = 10;
        maleBuchstabeBlut(spielfeld, Letters.O, startXOVER + versatz, zeile2Y, true);
        maleBuchstabeBlut(spielfeld, Letters.V, startXOVER + versatz + buchstabenAbstand, zeile2Y, true);
        maleBuchstabeBlut(spielfeld, Letters.E, startXOVER + versatz + (buchstabenAbstand * 2), zeile2Y, true);
        maleBuchstabeBlut(spielfeld, Letters.R, startXOVER + versatz + (buchstabenAbstand * 3), zeile2Y, true);
        
      //Frosch zeichnen
    	int frogX = spielfeld.length - 140;
    	int frogY = spielfeld[0].length - 35;
    	zeichneFrog(spielfeld, frogX, frogY);
    	
    	//Fliege zeichnen
    	int flyX = spielfeld.length - 120;
    	int flyY = spielfeld[0].length - 40;
    	zeichneFly(spielfeld, flyX, flyY);
    	
    	int fly2X = spielfeld.length - 140;
    	int fly2Y = spielfeld[0].length - 45;
    	zeichneFly2(spielfeld, fly2X, fly2Y);
    	
    	int fly3X = spielfeld.length - 130;
    	int fly3Y = spielfeld[0].length - 55;
    	zeichneFly3(spielfeld, fly3X, fly3Y);
    	
    	 //Biene unten rechts zeichnen  
        int bieneX = spielfeld.length - 45;
        int bieneY = spielfeld[0].length - 45;
        zeichneBiene(spielfeld, bieneX, bieneY);
    	
    }

    // Einzelnen Block im Spielfeld setzen
    private void setBlock(Pixel[][] spielfeld, int x, int y, TextColor farbe) {
        if (x >= 0 && x < spielfeld.length && y >= 0 && y < spielfeld[0].length) {
            spielfeld[x][y].Text = '█';;
            spielfeld[x][y].textColor = farbe;
            spielfeld[x][y].backColor = farbe;
        }
    }

    // Schwierigkeitsgrad-MenÃ¼ mit Blut-Effekt zeichnen
    public void zeichneMenue(Pixel[][] spielfeld) {
        // Start-Y-Position unter OVER (OVER ist bei zeile2Y = 16, Buchstaben sind 9 Zeilen hoch)
        int startY = 16 + 9 + 3; // OVER Position + Höhe + Abstand = 28
        
        // Optionen untereinander zeichnen
        for (int i = 0; i < options.length; i++) {
            String text = options[i];
            
            // Menu pulsierender Blutfarbe
            TextColor farbe = BLOOD_COLORS[(bloodColorOffset + i * 5) % BLOOD_COLORS.length];
            
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
            
            // Zufällige Bluttropfen unter der ausgewählten Option
            if (i == selectedIndex && random.nextInt(10) == 0) {
                int dropX = startX + random.nextInt(text.length());
                if (dropX < spielfeld.length && y + 1 < spielfeld[0].length) {
                    spielfeld[dropX][y + 1].Text = '·';
                    spielfeld[dropX][y + 1].textColor = BLOOD_COLORS[bloodColorOffset % BLOOD_COLORS.length];
                }
            }
        }
    }
    public void zeichneFrog(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.FROGSAD;
        TextColor[] colors = AnimalColor.FROGFLY;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃ¼r quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    public void zeichneFly(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.FLY;
        TextColor[] colors = AnimalColor.FROGFLY;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃ¼r quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    public void zeichneFly2(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.FLY;
        TextColor[] colors = AnimalColor.FROGFLY;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃ¼r quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    public void zeichneFly3(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.FLY;
        TextColor[] colors = AnimalColor.FROGFLY;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃ¼r quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
    public void zeichneBiene(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.BEE;
        TextColor[] colors = AnimalColor.BEE;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃƒÂ¼r quadratischen Effekt
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
    }
}