package game;

import com.googlecode.lanterna.TextColor;
public class Pixel {

    public char Text;
    public TextColor textColor;
    public TextColor backColor;

    // Der Konstruktor: Er legt fest, wie ein Pixel aussieht, wenn er neu erstellt wird
    public Pixel() {
        this.Text = ' '; // Standardmäßig leer
        this.backColor = new TextColor.Indexed(022); // hintergrund
        this.textColor = TextColor.ANSI.WHITE;
    }
}
