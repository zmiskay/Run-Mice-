package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Mouse class represents the player character (mouse) in the game.
 */
public class Mouse extends Polygon implements KeyListener {
	private double y; // current y coordinate
	private boolean spaceKey; // variable for forward key
	private boolean spaceKeyReleased; // keeps track of when the space key is not pressed down
	private double velocity; // current velocity
	private static boolean firstSpaceBar; // makes it so the first space bar does not move the mouse and starts game
	private int spaceCounter; // count the number of times the space bar has been pressed
	private EscapeMouse game;

	/**
	 * Constructs a Mouse object with the specified shape, position, and rotation.
	 * 
	 * @param inShape    The array of points defining the shape of the mouse.
	 * @param inPosition The position of the mouse.
	 * @param inRotation The initial rotation of the mouse.
	 */
	public Mouse(Point[] inShape, Point inPosition, double inRotation, EscapeMouse gameInstance) {
		super(inShape, inPosition, inRotation);

		this.y = inPosition.getY();
		this.spaceKey = false; // set the forward key variable to true
		this.spaceKeyReleased = true; // initialize to true
		this.velocity = 0; // start with no velocity;
		firstSpaceBar = true; // the space bar has not yet been touched
		this.spaceCounter = 0; // start with no spaces
		EscapeMouse.pauseCounter(true); // don't start the counter until the game has started
		this.game = gameInstance;

	}

	/**
	 * Paints the mouse object on the screen.
	 * 
	 * @param brush The graphics context to paint onto.
	 */
	public void paint(Graphics brush) {
		// set the color of the object
		brush.setColor(Color.pink);
		// get the dimensions/points of the object
		Point[] points = getPoints();

		// extract x and y coordinates from points array
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			xPoints[i] = (int) points[i].getX();
			yPoints[i] = (int) points[i].getY();
		}

		// use fillPolygon method to create the shape
		brush.fillPolygon(xPoints, yPoints, points.length);
	}

	/**
	 * Moves the mouse object based on user input
	 */
	public void move() {
		// doesn't start the game until the space bar has been pressed once
		if (!firstSpaceBar) {
			velocity += 0.5; // makes the mouse continuously fall (ie gravity)

			// moves the mouse up if the space key has been pressed
			if (spaceKey) {
				spaceCounter++;
				velocity = -9; // this will make the mouse jump
				setSpaceKey(false); // sets spaceKey back to false until pressed again
			}

			y += velocity; // add how much the mouse falls by + if it jumped at all to the current y
							// coordinate

			// if the mouse hits the top or bottom then it will restart the game
			if (y <= 0 || y >= 600) {
				EscapeMouse.needsReset = true;
				// resetMouse();
			}

			// update y coordinate
			position.setY(y);
		}

	}

	/**
	 * Method to reset the game when the mouse hits a wall or edge
	 */
	void resetMouse() {
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
				// Restart the game by resetting the position and velocity of the mouse
				y = 300; // Reset y position to the middle of the screen
				velocity = 0; // Reset velocity to 0
				firstSpaceBar = true; // Make user hit space bar again to start the game
				EscapeMouse.pauseCounter(true); // Stop the counter while game is stopped
				EscapeMouse.resetCounter(); // reset the counter
				setSpaceCounter(0); // reset the number of times the space bar has been pressed
			}
		};

		// Call the reset method of the anonymous class
		gameReset.reset();
	}

	/**
	 * Sets the space counter to the specified value.
	 * 
	 * @param value The value to set the space counter to.
	 */
	public void setSpaceCounter(int value) {
		if (value == 0) {
			spaceCounter = 0;
		} else {
			spaceCounter += value;
		}

	}

	/**
	 * Sets the value of the first space bar flag.
	 * 
	 * @param value The value to set the first space bar flag to.
	 */
	public static void setFirstSpaceBar(boolean value) {
		firstSpaceBar = value;
	}

	/**
	 * Retrieves the number of times the space bar has been pressed.
	 * 
	 * @return The number of times the space bar has been pressed.
	 */
	public int getSpaceCounter() {
		return spaceCounter;
	}

	/**
	 * Updates the state of the space key.
	 * 
	 * @param pressed True if the space key is pressed, false otherwise.
	 */
	public void setSpaceKey(boolean pressed) {
		this.spaceKey = pressed;
	}
	
	/**
	 * Updates the state of the right and left arrow keys.
	 * 
	 * @param e The key that is being pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_SPACE && EscapeMouse.needsReset) {
			EscapeMouse.needsReset = false;
		}
		if (keyCode == KeyEvent.VK_SPACE && firstSpaceBar == true) {
			firstSpaceBar = false; // start the game since the user has hit the space bar

			EscapeMouse.pauseCounter(false); // start the counter again
		} else if (keyCode == KeyEvent.VK_SPACE && spaceKeyReleased) {
			setSpaceKey(true);
			spaceKeyReleased = false; // space bar is no longer released
		}
	}
	
	/**
	 * Updates the state of the right and left arrow keys.
	 * 
	 * @param e The key that is being released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			setSpaceKey(false);
			spaceKeyReleased = true; // space bar is released
		}
	}

	@Override
	// add code to this if we want to include when a key has been typed
	public void keyTyped(KeyEvent e) {
		// invoked when a key pas been pressed
	}
}
