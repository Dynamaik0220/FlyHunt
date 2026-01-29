package game;

import com.googlecode.lanterna.TextColor;

public class AnimalColor {
	public static final TextColor[] FROGFLY = {
	        new TextColor.RGB(18, 76, 235),    // hellblau
	        new TextColor.RGB(5, 42, 146),  // dunkelblau
	        new TextColor.RGB(199, 242, 67),  // Grün1
	        new TextColor.RGB(79, 200, 131),    // Grün2
	        new TextColor.RGB(28, 142, 142),    // Grün3
	        new TextColor.RGB(254, 225, 243),   // Pink
	        new TextColor.RGB(68,153,171),   // Tränen
	    };
	
	public static final TextColor[] BEE = {
			new TextColor.RGB(249, 249, 249),		// weiß
			new TextColor.RGB(235,235,235), 		// hellgrau
			new TextColor.RGB(250, 227, 116),		// gelb
			new TextColor.RGB(243, 218, 91),		// dunkleres gelb
			new TextColor.RGB(49, 51, 50),			// grauer
			new TextColor.RGB(0, 0, 0),  			// schwarze
	    };
	
	// Fliegen-Animation Variablen
	private static double flyAnimationOffset = 0;
	private static long lastFlyAnimationTime = 0;
	private static final int FLY_ANIMATION_DELAY_MS = 5;
	private static final int FLY_ANIMATION_AMPLITUDE = 1; // Wie weit auf und ab
	
	// Berechnet Y-Offset für Fliegen-Animation basierend auf Zeit
	public static int getFlyYOffset(int flyIndex) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastFlyAnimationTime > FLY_ANIMATION_DELAY_MS) {
			flyAnimationOffset += 0.15; // Kleinere Schritte für sanftere Animation
			lastFlyAnimationTime = currentTime;
		}
		
		// Goldener Winkel-Verteilung für maximale Gleichverteilung
		final double GOLDEN_ANGLE = Math.PI * (3 - Math.sqrt(5)); // ~137.5°
		double phase = flyAnimationOffset + (flyIndex * GOLDEN_ANGLE);
		return (int) Math.round(Math.sin(phase) * FLY_ANIMATION_AMPLITUDE);
	}
	// Frosch-Quack-Animation Variablen
		private static double frogQuackOffset = 0;
		private static long lastFrogQuackTime = 0;
		private static final int FROG_QUACK_DELAY_MS = 80;
		
		// Gibt zurück, ob der Frosch gerade "quakt" (Mund offen)
		public static boolean isFrogQuacking() {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastFrogQuackTime > FROG_QUACK_DELAY_MS) {
				frogQuackOffset += 0.15;
				lastFrogQuackTime = currentTime;
			}
			
			// Sinuswelle zwischen -1 und 1, quakt wenn > 0.5
			double quackCycle = Math.sin(frogQuackOffset);
			return quackCycle > 0.3;
		}
}