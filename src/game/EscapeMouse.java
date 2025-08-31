package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Extending Game, EscapeMouse is the controller for this game.
 */
class EscapeMouse extends Game {
	static int counter = 0; // Counter for keeping track of the score
	Mouse mouse; // Instance of the Mouse class
	static boolean pauseCounter = true; // Flag to control whether the game counter is paused
	static HighScore highScore; // Instance of HighScore class to track highest score
	static Level level; // Instance of Level class to track game level
	static int wallTime = 0; // Counter for tracking wall creation time
	Cheese cheese; // Instance of Cheese class for collecting points
	ArrayList<Cheese> cheeses; // List to hold Cheese objects
	ArrayList<Wall> walls; // List to hold Wall objects
	static boolean needsReset = false;// Boolean flag to indicate whether the game needs to be reset
	private static EscapeMouse instance;

	/**
	 * Constructor for EscapeMouse class. Initializes game window, creates player,
	 * cheese, and wall objects, and adds key listener for player controls.
	 */
	public EscapeMouse() {
		super("ESCAPE MOUSE!", 800, 600); // Call superclass constructor to set up game window
		this.setFocusable(true);
		this.requestFocus();

		// create the coordinates for the dimensions of the mouse
		Point[] mousePoints = { new Point(400, 500), // Bottom center
				new Point(400, 480), // Top left
				new Point(420, 490), // Top right
				new Point(400, 500) }; // Back to bottom center to close the shape };
		// MOUSE: create the mouse
		mouse = new Mouse(mousePoints, new Point(100, 300), 0, instance); // Initialize Mouse object
		// CHEESE: create a cheese
		cheeses = new ArrayList<>(); // Initialize ArrayList for storing Cheese objects
		makeCheese(); // Generate initial Cheese object
		// WAllS: create walls
		walls = new ArrayList<>(); // Initialize ArrayList for storing Wall objects
		makeWall(); // Generate initial set of walls
		
		// Add key listener for player controls
		this.addKeyListener(mouse); 
		this.addKeyListener(cheese);

	}

	/**
	 * Creating a cheese. Generates a new Cheese object at a random position within
	 * the game window.
	 */
	private void makeCheese() {
		Random random = new Random(); // Initialize random number generator
		int x = width - 30; // Set x-coordinate to be within game window width
		int y = random.nextInt(height - 50); // Set y-coordinate to be within game window height

		// Define the shape of the cheese
		Point[] cheesePoints = { new Point(0, 20), // Bottom center
				new Point(20, 0), // Top left
				new Point(25, 20), // Top right
				new Point(0, 20) };

		cheese = new Cheese(cheesePoints, new Point(x, y), 0); // Create new Cheese object
		cheeses.add(cheese); // Add Cheese object to the list of cheeses
	}

	/**
	 * Creating walls. Generates a new set of walls (top and bottom) at random
	 * positions within the game window.
	 */
	private void makeWall() {
		// Interface for generating wall height
		interface WallHeightGenerator {
			/**
			 * Generates the height for a wall.
			 * 
			 * @param random    The random number generator.
			 * @param maxHeight The maximum height for the wall.
			 * @return The generated height for the wall.
			 */
			int generateHeight(Random random, int maxHeight);
		}

		// Anonymous class implementing WallHeightGenerator interface
		WallHeightGenerator heightGenerator = new WallHeightGenerator() {
			/**
			 * Generates the height for a wall.
			 * 
			 * @param random    The random number generator.
			 * @param maxHeight The maximum height for the wall.
			 * @return The generated height for the wall.
			 */
			@Override
			public int generateHeight(Random random, int maxHeight) {
				return Wall.WALL_HEIGHT + random.nextInt(maxHeight - 50); // Generate random height for walls
			}
		};

		Random random = new Random(); // Initialize random number generator
		int gap = 170; // Gap between top and bottom walls

		int wallHeight = heightGenerator.generateHeight(random, height);

		// Make Top Wall
		int topWallYOffset = random.nextInt(80); // Introduce slight variation
		Point topPosition = new Point(width, topWallYOffset); // Position of top wall
		Wall topWall = new Wall(topPosition, true); // Create top wall object
		walls.add(topWall); // Add top wall to list of walls

		// Make Bottom Wall
		if (wallHeight + gap >= 600) {
			Point bottomPosition = new Point(width, 500); // Position of bottom wall
			Wall bottomWall = new Wall(bottomPosition, false); // Create bottom wall object
			walls.add(bottomWall); // Add bottom wall to list of walls
		} else {
			Point bottomPosition = new Point(width, gap + wallHeight); // Position of bottom wall
			Wall bottomWall = new Wall(bottomPosition, false); // Create bottom wall object
			walls.add(bottomWall); // Add bottom wall to list of walls
		}
	}

	/**
	 * Paint method for drawing the game. Renders all game elements including
	 * player, cheese, walls, and score. Updates game state based on player input
	 * and game logic.
	 * 
	 * @param brush The graphics context to paint onto.
	 */
	public void paint(Graphics brush) {
		brush.setColor(Color.black); // Set brush color to black
		brush.fillRect(0, 0, width, height); // Fill the entire window with black color
		brush.setColor(Color.white); // Set brush color to white
		// Display current score
		brush.drawString("Score is " + (int) counter, 10, 10); // Render score at specified position
		// Display highest score
		brush.drawString("High Score is " + HighScore.getHighestScore(), 10, 30); // Render highest score at specified
																					// position
		// Display current level
		brush.drawString("Level " + Level.getCurrentLevel(), 10, 50); // Render current level at specified position
		// Check if the game needs to be reset
		if (needsReset) {
			brush.drawString("Press 'SPACE' to Start", 300, 300);
			resetGame(); // Reset the game
		}else {

		// Counter only runs when the game is active
		if (!pauseCounter) {
			counter = mouse.getSpaceCounter(); // Update game counter with player score
			Level.updateLevel(counter); // Update game level based on score
		}
		
		// Move the player
		mouse.move(); // Update player position based on input
		// Draw the player
		mouse.paint(brush); // Render player at updated position
		// CHEESE: move & repaint cheese
		if (!cheeses.isEmpty()) { // Check if the cheeses list is not empty
			this.addKeyListener(cheese);
			Cheese currentCheese = cheeses.get(0); // Get current cheese object
			currentCheese.move(); // Move cheese object
			currentCheese.paint(brush); // Render cheese object

			// Remove consumed cheese & generate new one
			if (currentCheese.offScreen() || currentCheese.collides(mouse)) {
				cheeses.remove(0); // Remove consumed cheese from list
				makeCheese(); // Generate new cheese object
				if (currentCheese.collides(mouse)) {
					mouse.setSpaceCounter(5);

				}
			}
		}
		// WALLS
		wallTime++; // Increment wall creation timer

		// Generate new wall every few seconds
		if (wallTime % 300 == 0) {
			makeWall(); // Generate new set of walls
		}

		// Move & paint walls using lambda expression
		walls.removeIf(wall -> {
			wall.move(); // Move wall object
			if (wall.offScreen(width)) {
				return true; // Remove wall from list if it's offscreen
			} else {
				wall.paint(brush); // Render wall object
				return false;
			}
		});

		// WALLS: check for collision with walls
		for (Wall wall : walls) {
			if (wall.collides(mouse)) {
				needsReset = true;
				break; // Exit the loop if collision detected to avoid unnecessary checks
			}
		}
		}
	}

	/**
	 * Resets the game by clearing all cheeses and walls, resetting the mouse
	 * position, and resetting the game counter and other game parameters.
	 */
	private void resetGame() {
		// Interface for resetting the game state
		interface GameReset {
			void reset(); // Method to reset the game state
		}
		// Anonymous class implementing GameReset interface
		GameReset gameReset = new GameReset() {
			/**
			 * Resets the game state.
			 */
			@Override
			public void reset() {
				// Clear all cheeses
				cheeses.clear();
				// Clear all walls
				walls.clear();
				// Reset mouse position
				mouse.resetMouse();
				// Reset game counter
				resetCounter();
				makeCheese();
			}
		};
		// Call the reset method of the anonymous class
		gameReset.reset();
	}

	/**
	 * Resets the game counter and updates the highest score and current level.
	 * Called when the game is reset.
	 */
	public static void resetCounter() {
		HighScore.updateHighScore(counter); // Update highest score with current counter value
		Level.resetLevel(); // Reset game level to initial value
		counter = 0; // Reset game counter to zero
	}

	/**
	 * Pauses or resumes the game counter based on the given boolean flag.
	 * 
	 * @param pause True to pause the game counter, false to resume.
	 */
	public static void pauseCounter(boolean pause) {
		pauseCounter = pause; // Set pause flag to the specified value
	}

	/**
	 * Main method to start the game. Creates an instance of EscapeMouse and starts
	 * the game loop.
	 * 
	 * @param args Command-line arguments (unused).
	 */
	public static void main(String[] args) {
		EscapeMouse a = new EscapeMouse(); // Create new instance of EscapeMouse
		a.repaint(); // Start the game loop
	}

	/**
	 * Inner class to keep track of the highest score.
	 */
	class HighScore {
		private static int highestScore; // Variable to store the highest score

		/**
		 * Constructor for HighScore class. Initializes the highest score to zero.
		 */
		public HighScore() {
			highestScore = 0; // Initialize highest score to zero
		}

		/**
		 * Updates the highest score if the current score is greater.
		 * 
		 * @param currentScore The current score to compare.
		 */
		public static void updateHighScore(int currentScore) {
			if (currentScore > highestScore) {
				highestScore = currentScore; // Update highest score if current score is greater
			}
		}

		/**
		 * Retrieves the highest score.
		 * 
		 * @return The highest score.
		 */
		public static int getHighestScore() {
			return highestScore; // Return the highest score
		}
	}

	/**
	 * Inner class to keep track of the current level.
	 */
	static class Level {
		private static int currentLevel; // Variable to store the current level

		/**
		 * Constructor for Level class. Initializes the current level to 1.
		 */
		public Level() {
			currentLevel = 1; // Start at level 1
		}

		/**
		 * Updates the current level based on the current score.
		 * 
		 * @param currentScore The current score to determine the level.
		 */
		public static void updateLevel(int currentScore) {
			// Check if the score reaches a multiple of ten and a new level is reached
			if (currentScore % 10 == 0 && currentScore != 0 && currentScore / 10 != currentLevel) {
				currentLevel += 1; // Increment the current level
			}
		}

		/**
		 * Retrieves the current level.
		 * 
		 * @return The current level.
		 */
		public static int getCurrentLevel() {
			return currentLevel; // Return the current level
		}

		/**
		 * Resets the current level to 0.
		 */
		public static void resetLevel() {
			currentLevel = 0; // Reset the current level to 0
		}
	}
}
