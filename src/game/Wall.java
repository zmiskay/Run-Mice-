package game;
import java.awt.Color;
import java.awt.Graphics;
/**
* The Wall class represents a wall object in the game. It extends the Polygon
* class
*/
public class Wall extends Polygon implements GameElements{
	private static final int WALL_WIDTH = 30; // Width of the wall
	public static final int WALL_HEIGHT = 300; // Height of the wall
	private double x; // current x
	private double y; // current y
	/**
	 * Constructs a Wall object with the specified position and height.
	 *
	 * @param inPosition The position of the wall.
	 * @param isTop      Indicates whether the wall is at the top or bottom of the
	 *                   screen.
	 */
	public Wall(Point inPosition, boolean isTop) {
		super(createShape(inPosition, isTop), inPosition, 0); // Call the superclass constructor
		// Initialize x and y coordinates
		this.x = inPosition.getX();
		this.y = inPosition.getY();
	}
	/**
	 * Creates the shape of the wall based on its position and whether it's a top or
	 * bottom wall.
	 *
	 * @param inPosition The position of the wall.
	 * @param isTop      Indicates whether the wall is at the top or bottom of the
	 *                   screen.
	 * @return An array of points defining the shape of the wall.
	 */
	private static Point[] createShape(Point inPosition, boolean isTop) {
		// Determine the y-coordinate based on whether it's a top or bottom wall
		double baseY;
		if (isTop) {
			baseY = inPosition.getY() - WALL_HEIGHT;
		} else {
			baseY = inPosition.getY();
		}
		// Define the shape of the wall
		Point[] shape = { new Point(0, 0), new Point(0, WALL_HEIGHT), new Point(WALL_WIDTH, WALL_HEIGHT),
				new Point(WALL_WIDTH, 0) };
		// Adjust all points based on the initial position and whether it's a top or
		// bottom wall
		for (Point point : shape) {
			point.x += inPosition.getX();
			point.y += baseY;
		}
		return shape; // Return the shape of the wall
	}
	/**
	 * Paints the wall object onto the specified graphics context.
	 *
	 * @param brush The graphics context to paint onto.
	 */
	public void paint(Graphics brush) {
		brush.setColor(Color.cyan); // Set the color of the wall to gray
		Point[] points = getPoints(); // Get the points of the wall object
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		// Extract x and y coordinates from the points array
		for (int i = 0; i < points.length; i++) {
			xPoints[i] = (int) points[i].getX();
			yPoints[i] = (int) points[i].getY();
		}
		// Use fillPolygon method to create the shape of the wall
		brush.fillPolygon(xPoints, yPoints, points.length);
	}
	/**
	 * Moves the wall object continuously to the left.
	 */
	public void move() {
		x -= 0.9; // Move the x-position to the left
		position.setX(x); // Update x coordinate in the position object
	}
	/**
	 * Checks if the wall object is off the screen.
	 *
	 * @param screenWidth The width of the screen.
	 * @return true if the wall is off the screen, false otherwise.
	 */
	public boolean offScreen(int screenWidth) {
		return x + WALL_WIDTH < 0; // Check if the right edge of the wall is off the screen
	}
}
