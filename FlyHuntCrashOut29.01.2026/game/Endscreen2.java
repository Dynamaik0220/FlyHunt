package game;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.Random;

public class Endscreen2 {
    private int selectedIndex = 0;
    private final String[] options = {"[ ENTER – Den Kreislauf wiederholen ]", "[ ESC – Im Schlick versinken ]"};
    
    // Animation
    private int bloodColorOffset = 0;
    private long lastAnimationTime = 0;
    private static final int ANIMATION_DELAY_MS = 150;
    private final Random random = new Random();
    
    // Farbpaletten
    private static final TextColor BG_GREEN = new TextColor.RGB(162, 5, 51);
    
    private static final TextColor[] BLOOD_COLORS = {
        new TextColor.RGB(82, 13, 1),    // Dunkelrot
        new TextColor.RGB(120, 20, 2),
        new TextColor.RGB(160, 27, 3),
        new TextColor.RGB(200, 34, 4),   // Hellrot
        new TextColor.RGB(140, 23, 3),
        new TextColor.RGB(100, 16, 2)
    };
    
    // Animation aktualisieren - MUSS vor zeichneLogo() und zeichneMenue() aufgerufen werden!
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnimationTime > ANIMATION_DELAY_MS) {
            bloodColorOffset++;
            lastAnimationTime = currentTime;
        }
    }
    
    // Buchstabe mit Blut-Effekt zeichnen (pulsierende Farbe + Tropfen)
    private void maleBuchstabeBlut(Pixel[][] spielfeld, int[][] pattern, int startX, int startY, boolean isOver) {
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                if (pattern[y][x] > 0) {
                    // Farbe basierend auf Position und Animation
                    int colorPos = (bloodColorOffset + (isOver ? x*2 + y*3 : x + y)) % BLOOD_COLORS.length;
                    TextColor farbe = BLOOD_COLORS[colorPos];
                    
                    // ZufÃƒÂ¤llige leichte Position-Variation fÃƒÂ¼r "zittrigen" Effekt
                    int drawX = startX + (x * 2);
                    int drawY = startY + y;
                    if (random.nextInt(isOver ? 8 : 10) == 0) {
                        drawX += random.nextInt(3) - 1;
                        if (isOver) drawY += random.nextInt(2);
                    }
                    
                    // Doppelte Breite fÃƒÂ¼r quadratischen Effekt
                    setBlock(spielfeld, drawX, drawY, farbe);
                    setBlock(spielfeld, drawX + 1, drawY, farbe);
                    
                    // ZufÃƒÂ¤llige Bluttropfen
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
        if (key.getKeyType() == KeyType.ArrowDown || key.getKeyType() == KeyType.ArrowRight) {
            selectedIndex = (selectedIndex + 1) % options.length;
        } else if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowLeft) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        } else if (key.getKeyType() == KeyType.Escape) {
            return -1; // Signal zum Beenden
        } else if (key.getKeyType() == KeyType.Enter) {
            return 1; // Signal fÃƒÂ¼r zurÃƒÂ¼ck zum Startscreen
        }
        return 0; // Kein besonderes Event
    }

    // Logo "GAME OVER" mit Blut-Animation zeichnen
    public void zeichneLogo(Pixel[][] spielfeld) {
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
        int frogX = spielfeld.length - 145;
        int frogY = spielfeld[0].length - 18;
        zeichneFrogWithTears(spielfeld, frogX, frogY);
        
        //Fliegen mit Animation zeichnen - jede bekommt einen Index fÃƒÂ¼r Phasenverschiebung
        int flyX = spielfeld.length - 120;
        int flyY = spielfeld[0].length - 23;
        zeichneFlyAnimated(spielfeld, flyX, flyY, 0);
        
        int fly2X = spielfeld.length - 140;
        int fly2Y = spielfeld[0].length - 28;
        zeichneFlyAnimated(spielfeld, fly2X, fly2Y, 1);
        
        int fly3X = spielfeld.length - 130;
        int fly3Y = spielfeld[0].length - 35;
        zeichneFlyAnimated(spielfeld, fly3X, fly3Y, 2);
        
        
        //Biene unten rechts zeichnen  
        int bieneX = spielfeld.length - 45;
        int bieneY = spielfeld[0].length - 45;
        zeichneBiene(spielfeld, bieneX, bieneY);
        
    }

    // Einzelnen Block im Spielfeld setzen
    private void setBlock(Pixel[][] spielfeld, int x, int y, TextColor farbe) {
        if (x >= 0 && x < spielfeld.length && y >= 0 && y < spielfeld[0].length) {
            spielfeld[x][y].Text = '█';
            spielfeld[x][y].textColor = farbe;
            spielfeld[x][y].backColor = farbe;
        }
    }

    // Schwierigkeitsgrad-MenÃƒÂ¼ mit pulsierendem Blut-Effekt zeichnen
    public void zeichneMenue(Pixel[][] spielfeld) {
        // Start-Y-Position unter OVER (OVER ist bei zeile2Y = 16, Buchstaben sind 9 Zeilen hoch)
        int startY = 16 + 9 + 3; // OVER Position + HÃƒÂ¶he + Abstand = 28
        
        // Optionen untereinander zeichnen
        for (int i = 0; i < options.length; i++) {
            String text = options[i];
            
            // Pulsierender Blutfarbe-Effekt 
            TextColor farbe;
            if (i == selectedIndex) {
                // AusgewÃƒÂ¤hlte Option: intensiver pulsierender Effekt
                int pulseIndex = (bloodColorOffset * 2) % BLOOD_COLORS.length;
                farbe = BLOOD_COLORS[pulseIndex];
            } else {
                // Nicht ausgewÃƒÂ¤hlte Option: langsamerer Puls
                int pulseIndex = (bloodColorOffset + i * 3) % BLOOD_COLORS.length;
                farbe = BLOOD_COLORS[pulseIndex];
            }
            
            // Jede Option zentrieren
            int startX = (spielfeld.length - text.length()) / 2;
            int y = startY + (i * 2); // 2 Zeilen Abstand zwischen den Optionen
            
            // Text zeichnen - backColor wird NICHT gesetzt (bleibt transparent)
            for (int j = 0; j < text.length(); j++) {
                if (startX + j < spielfeld.length && y < spielfeld[0].length) {
                    spielfeld[startX + j][y].Text = text.charAt(j);
                    spielfeld[startX + j][y].textColor = farbe;
                }
            }
            
            // ZufÃƒÂ¤llige Bluttropfen unter der ausgewÃƒÂ¤hlten Option
            if (i == selectedIndex && random.nextInt(8) == 0) {
                int dropX = startX + random.nextInt(text.length());
                if (dropX < spielfeld.length && y + 1 < spielfeld[0].length) {
                    spielfeld[dropX][y + 1].Text = '█';
                    spielfeld[dropX][y + 1].textColor = BLOOD_COLORS[bloodColorOffset % BLOOD_COLORS.length];
                }
            }
        }
    }
    
    // Frosch mit animierten TrÃ¤nen zeichnen - PROGRESSIV FLIESSENDE TRÃƒNEN
    public void zeichneFrogWithTears(Pixel[][] spielfeld, int startX, int startY) {
        int[][] pattern = Animals.FROGCRYING;  // Weinender Frosch
        TextColor[] colors = AnimalColor.FROGFLY;
        
        // Kontinuierlicher Flow-Offset fÃƒÂ¼r flieÃƒÅ¸ende TrÃƒÂ¤nen
        double tearFlowOffset = AnimalColor.getTearFlowOffset();
       
        // Zeichne den KOMPLETTEN Frosch INKLUSIVE 7-Positionen (mit Froschfarbe)
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {  // Alle Positionen zeichnen
                    TextColor farbe;
                    if (colorIndex == 7) {
                        // TrÃ¤nen-Positionen erstmal mit FroschkÃ¶rperfarbe fÃ¼llen (GrÃ¼n)
                        farbe = colors[3]; // GrÃ¼n vom FroschkÃ¶rper
                    } else {
                        farbe = colors[colorIndex - 1];
                    }
                    setBlock(spielfeld, startX + (x * 2), startY + y, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y, farbe);
                }
            }
        }
        
        // Definiere TrÃƒÂ¤nenfarben fÃƒÂ¼r flieÃƒÅ¸enden ÃƒÅ“bergang
        TextColor traenenFarbeHell = new TextColor.RGB(211, 250, 252);   // Hellstes TÃ¼rkis (Kopf)
        TextColor traenenFarbe = new TextColor.RGB(165, 237, 242);        // Original TÃ¼rkis (Mitte)
        TextColor traenenFarbeDunkel = new TextColor.RGB(136, 227, 239);  // Dunkelstes TÃ¼rkis (Ende)
        
        // Jetzt ÃƒÂ¼berschreibe die 7-Positionen mit flieÃƒÅ¸enden TrÃƒÂ¤nen
        for (int x = 0; x < pattern[0].length; x++) {
            // Finde die oberste und unterste TrÃƒÂ¤nenposition in dieser Spalte
            int eyeY = -1;        // Start (Auge)
            int patternEndY = -1; // Ende im Pattern
            
            for (int y = 0; y < pattern.length; y++) {
                if (pattern[y][x] == 7) {
                    if (eyeY == -1) eyeY = y; // Erstes Vorkommen = Auge
                    patternEndY = y; // Letztes Vorkommen = Ende
                }
            }
            
            // Wenn TrÃ¤nen in dieser Spalte gefunden wurden
            if (eyeY != -1 && patternEndY != -1) {
                // Berechne die GesamtlÃ¤nge der TrÃ¤ne in dieser Spalte
                int totalTearLength = patternEndY - eyeY + 1;
                
                // Berechne wie weit die TrÃ¤ne schon gefÃ¼llt ist
                int maxLength = totalTearLength + 5; // Extra LÃ¤nge fÃ¼r Tropfen
                final double GLOBAL_CYCLE_LENGTH = 15.0; // Feste Zyklus-LÃ¤nge fÃ¼r alle Spalten
                
                
                // Zeitversatz zwischen linkem und rechtem Auge
                // Linkes Auge ist bei x <= 8, rechtes Auge bei x > 8
                double eyeOffset = (x <= 8) ? 0 : 3.0; // Rechtes Auge startet 3 Einheiten später
                
                // Beide Augen weinen mit leichtem Versatz
                double adjustedOffset = (tearFlowOffset + eyeOffset) % GLOBAL_CYCLE_LENGTH;
                
                int currentFillLength;
                
                // Wachstumsphase nur wenn adjustedOffset klein genug ist
                if (adjustedOffset < maxLength) {
                    // TrÃ¤ne wÃ¤chst von 0 bis maxLength
                    currentFillLength = (int)adjustedOffset;
                } else {
                    // Pause/Reset-Phase - keine TrÃ¤ne sichtbar
                    currentFillLength = -1;
                }
                
                // Nur zeichnen wenn wir in der Wachstumsphase sind
                if (currentFillLength >= 0) {
                    // Zeichne kontinuierliche TrÃ¤ne vom Auge bis zur aktuellen LÃ¤nge
                    int tearEndY = eyeY + currentFillLength;
                    
                    for (int tearY = eyeY; tearY <= Math.min(tearEndY, patternEndY + 5); tearY++) {
                        int distanceFromEye = tearY - eyeY;
                        
                        if (distanceFromEye <= currentFillLength) {
                            // Berechne FarbintensitÃ¤t basierend auf Position
                            float colorProgress = (float)distanceFromEye / (float)Math.max(totalTearLength, 1);
                            
                            TextColor currentColor;
                            
                            // Farbverlauf von hell (oben/frisch) zu dunkel (unten/alt)
                            if (colorProgress < 0.3f) {
                                // Oberer Bereich (am Auge)
                                currentColor = traenenFarbeHell;
                            } else if (colorProgress < 0.6f) {
                                // Mittlerer Bereich
                                currentColor = traenenFarbe;
                            } else {
                                // Unterer Bereich
                                currentColor = traenenFarbeDunkel;
                            }
                            
                            int screenY = startY + tearY;
                            if (screenY >= 0 && screenY < spielfeld[0].length) {
                                setBlock(spielfeld, startX + (x * 2), screenY, currentColor);
                                setBlock(spielfeld, startX + (x * 2) + 1, screenY, currentColor);
                            }
                        }
                    }
                }

                
                // ZusÃ¤tzliche Tropfen die Ã¼ber das Pattern hinaus fallen
                if (currentFillLength > totalTearLength) {
                    int extraDrops = currentFillLength - totalTearLength;
                    for (int drop = 0; drop < extraDrops && drop < 3; drop++) {
                        int dropY = startY + patternEndY + 1 + drop;
                        if (dropY >= 0 && dropY < spielfeld[0].length) {
                            // Fade out der Extratropfen
                            if (drop < 2 || random.nextInt(2) == 0) {
                                setBlock(spielfeld, startX + (x * 2), dropY, traenenFarbeDunkel);
                                setBlock(spielfeld, startX + (x * 2) + 1, dropY, traenenFarbeDunkel);
                            }
                        }
                    }
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
                    // Doppelte Breite fÃƒÂ¼r quadratischen Effekt + Y-Offset fÃƒÂ¼r Animation
                    setBlock(spielfeld, startX + (x * 2), startY + y + yOffset, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y + yOffset, farbe);
                }
            }
        }
    }
    
    public void zeichneBiene(Pixel[][] spielfeld, int startX, int startY) {
        // WÃƒÂ¤hle Pattern basierend auf Augen-Animation
        int[][] pattern = AnimalColor.hasBeeRedEyes() ? Animals.BEE_RED_EYES : Animals.BEE;
        
        // Hole den Y-Offset von der Animation in AnimalColor
        int yOffset = AnimalColor.getBeeYOffset();
        TextColor[] colors = AnimalColor.BEE;
       
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int colorIndex = pattern[y][x];
                if (colorIndex > 0) {
                    TextColor farbe = colors[colorIndex - 1];
                    // Doppelte Breite fÃƒÂ¼r quadratischen Effekt + Y-Offset fÃƒÂ¼r Animation
                    setBlock(spielfeld, startX + (x * 2), startY + y + yOffset, farbe);
                    setBlock(spielfeld, startX + (x * 2) + 1, startY + y + yOffset, farbe);
                }
            }
        }
    }
}