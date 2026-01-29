package game;

import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

//Handles rendering the game field to the terminal
public class Renderer {
    private Terminal terminal;

    public Renderer(Terminal terminal) throws IOException {
        this.terminal = terminal;
        terminal.setCursorVisible(false);
    }

    //Renders the entire spielfeld to the terminal
    public void render(Spielfeld spielfeld) throws IOException {
        Pixel[][] pixels = spielfeld.getPixels();

        for (int i = 0; i < spielfeld.getWidth(); i++) {
            for (int j = 0; j < spielfeld.getHeight(); j++) {
                terminal.setCursorPosition(i, j);
                terminal.setForegroundColor(pixels[i][j].textColor);
                terminal.setBackgroundColor(pixels[i][j].backColor);
                terminal.putCharacter(pixels[i][j].Text);
            }
        }
        terminal.flush();
    }

    public Terminal getTerminal() {
        return terminal;
    }
}