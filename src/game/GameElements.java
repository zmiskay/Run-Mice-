package game;
import java.awt.Graphics;
/**
* The interface for game elements that can be painted and moved.
*/
public interface GameElements {
	/**
	 * Paints the game element on the screen.
	 *
	 * @param brush The graphics object to paint with.
	 */
	public void paint(Graphics brush);
	/**
	 * Moves the game element.
	 */
	public void move();
}
