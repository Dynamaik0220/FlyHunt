package game;


import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;

public class MainGame {

    public static int spielfeldHoehe = 75;
    public static int spielfeldBreite = 150;
    public static TextColor DefaultBackColor = TextColor.ANSI.WHITE;
    public static TextColor DefaultTextColor = TextColor.ANSI.WHITE;
    public static Frog frog;
    public static Frog aim;
    private static char lastKey = ' ';


    public static void main(String[] args) throws IOException, InterruptedException {

        // Create list of targets
        ArrayList<Target> targets = new ArrayList<>();
        int maxCycleCount = 20;             //counter used in rendering loop, important for sinus movement
        int cycleCount = 1;                 //

        // Initialize pixel art library
        PixelArtLibrary pixelArtLibrary = new PixelArtLibrary();

        // Create spielfeld
        Spielfeld spielfeld = new Spielfeld(spielfeldBreite, spielfeldHoehe,
                DefaultBackColor, DefaultTextColor);

        // Create Background 
        Background background =new Background (pixelArtLibrary,0,0);

        Score scoreDisplay = new Score(105, 5, 8, pixelArtLibrary);
        Health healthDisplay = new Health(6, 5, 12, 2, pixelArtLibrary);

        // Reflecting fly
        targets.add(new Target(1, 30, 2, 0, 0, 0, 5, 1, 3, pixelArtLibrary, false));

        // Teleporting fly
        targets.add(new Target(50, 20, -1, 0.5, 0, 0, 5, 1, 3, pixelArtLibrary, false));

        // Accelerating fly
        targets.add(new Target(0, 10, 2, 0, 0, 0, 5, 1, 5, pixelArtLibrary, false));

        // Bee
        targets.add(new Target(80, 40, 1.5, 0, 0, 0, 5, 1, 3, pixelArtLibrary, true)); 

        // Create terminal and renderer
        TerminalFactory factory = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(spielfeldBreite, spielfeldHoehe));

        ((DefaultTerminalFactory) factory).setTerminalEmulatorFontConfiguration(
                com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration.newInstance(
                        new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 9)  // <-- Change terminal size
                )
        );
        Terminal terminal = factory.createTerminal();
        Renderer renderer = new Renderer(terminal);

        // Initialize frog
        frog = new Frog(spielfeldBreite, spielfeldHoehe, pixelArtLibrary);

        // *** Start Screen Loop *** courtesy of Maik Kiehlmann
        Startscreen startscreen = new Startscreen(pixelArtLibrary);

        boolean gameStarted = false;
        while (!gameStarted){
            // clear screen and create startscreen
            spielfeld.clear();
            background.drawBackground(spielfeld, 0, 0);
            startscreen.zeichneLogo(spielfeld.getPixels());
            startscreen.zeichneMenue(spielfeld.getPixels());
            renderer.render(spielfeld);

            //inout handling
            KeyStroke key = terminal.pollInput();
            if (key != null){
                int result = startscreen.handleInput(key);
                if (result == -1){  // ESC key -> exit game
                    terminal.clearScreen();
                    terminal.flush();
                    terminal.close();
                    return;
                } else if (result == 2){    // ENTER -> start game
                    gameStarted = true;
                }
            }
            Thread.sleep(10);
        }

        // *** GAME START ***

        // Clear spielfeld to fix visual bugs during the first few frames
        spielfeld.clear();
        renderer.render(spielfeld);
        spielfeld.clear();

        // Build initial spielfeld
        healthDisplay.draw(spielfeld);
        scoreDisplay.draw(spielfeld);

        // Display the spielfeld
        renderer.render(spielfeld);

        // Game loop
        while (true) {
            // Clear spielfeld
            spielfeld.clear();

            // Draw Background
            background.drawBackground(spielfeld,0 ,0);

            if (healthDisplay.getCurrentHealth() > 0){

                // Draw score
                scoreDisplay.draw(spielfeld);

                // Update all targets
                for (Target target : targets) {
                target.performAction(1, spielfeldBreite - 15, 20, spielfeldHoehe - 35, cycleCount, maxCycleCount);
                
                // KORREKTUR: Zeichne das Insekt passend zu seinem Typ
                    if (target.isBee()) {
                        target.drawBee(spielfeld);
                    } 
                    else {
                        target.drawFly(spielfeld);
                    }
                }
                // Draw Frog + Aim
                frog.draw(spielfeld);

            } 
            else {
                // Game over - Endscreen Loop
                Endscreen2 endscreen = new Endscreen2();
                
                while (true) {
                    spielfeld.clear();
                    background.drawEndscreen(spielfeld, 0, 0);
                    endscreen.updateAnimation();  
                    endscreen.zeichneLogo(spielfeld.getPixels());
                    endscreen.zeichneMenue(spielfeld.getPixels());
                    scoreDisplay.setPosition(55, 35);
                    scoreDisplay.draw(spielfeld);
                    renderer.render(spielfeld);
                    
                    // input handling
                    KeyStroke endInput = terminal.pollInput();
                    if (endInput != null) {
                        int endResult = endscreen.handleInput(endInput);
                        if (endResult == -1) {  // ESC --> close game
                            terminal.clearScreen();
                            terminal.flush();
                            terminal.close();
                            return;
                        } else if (endResult == 1) {    // ENTER -> restart game
                            main(args);
                            return;
                        }
                    }
                    Thread.sleep(10);
                }
            }
            // Check for input
            // Check for input
            KeyStroke input = terminal.pollInput();

            // Handle input and movement state
            if (input == null) {
                // No key pressed: reset lastKey to neutral to stop continuous movement
                lastKey = 'n';
            } 
            else if (input.getKeyType().equals(KeyType.Escape)) {
                break; // Exit game loop
            } 
            else if (input.getKeyType().equals(KeyType.Character)) {
                // Capture the character pressed
                lastKey = input.getCharacter();
            }
            if (lastKey == ' ') {
                int[] aimPos = frog.getAimLocation();

                //Trefferprüfung und Effekt (Rückwärts durchlaufen zum Löschen)
                for (int i = targets.size() - 1; i >= 0; i--) {
                    Target target = targets.get(i);

                    if (target.isHit(aimPos[0], aimPos[1])) {
                        // Effekt berechnen
                        if (target.isBee()) { 
                            healthDisplay.decrease(1);
                        } else {
                            scoreDisplay.addScore(100);
                        }

                        // Das getroffene Objekt entfernen
                        targets.remove(i); 

                        // Zählen, wie viele Fliegen (keine Bienen) noch da sind
                        int currentFlyCount = 0;
                        for (Target t : targets) {
                            if (!t.isBee()) {
                                currentFlyCount++;
                            }
                        }

                        //Wenn weniger als 3 Fliegen da sind, fülle gezielt mit Fliegen auf
                        while (currentFlyCount < 3) {
                            int randomX = (int) (Math.random() * (spielfeldBreite - 30) + 10);
                            int randomY = (int) (Math.random() * (spielfeldHoehe - 40) + 10);
                            double randomSpeed = 1.0 + Math.random();
                            int randomMovement = (Math.random() < 0.5) ? 3 : 5; // Abprallen oder Sinus

                            // Neue Fliege hinzufügen (isBee = false)
                            targets.add(new Target(randomX, randomY, randomSpeed, 0, 0, 0, 5, 1, randomMovement, pixelArtLibrary, false));
                            currentFlyCount++;
                        }

                        //Falls eine Biene getroffen wurde, spawne eine neue Biene
                        if (target.isBee()) {
                            int randomX = (int) (Math.random() * (spielfeldBreite - 30) + 10);
                            int randomY = (int) (Math.random() * (spielfeldHoehe - 40) + 10);
                            targets.add(new Target(randomX, randomY, 1.2, 0, 0, 0, 5, 1, 3, pixelArtLibrary, true));
                        }

                        break;
                    }
                }
                lastKey = 'n'; 
            }
                
            // Execute movement only if a valid direction key is active
            if (lastKey == 'w' || lastKey == 'a' || lastKey == 's' || lastKey == 'd') {
                frog.move(lastKey);
            }

            healthDisplay.update();
            // Draw 3 hearts based on current health
            healthDisplay.draw(spielfeld);

            // Render everything
            renderer.render(spielfeld);


            // Small delay to control game speed
            Thread.sleep(10);

            cycleCount++;                        //increase Time Counter
            if(cycleCount > maxCycleCount){cycleCount = 1;}  //when counter reaches limit, reset to 0
        }
        // Clear the screen before closing
        terminal.clearScreen();
        terminal.flush();

        // Clean up
        terminal.close();

     }
    }