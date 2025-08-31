package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/*
* Main Class: make cheese based on random values
* It extends polygon since it is a shape
*/

public class Cheese extends Polygon implements GameElements, KeyListener {
	private double x; // current x coordinate
	private double y; // current y coordinate
	
	private double rotationSpeed; // Rotation speed
	private boolean rotateLeftPressed; // Flag for left rotation key
	private boolean rotateRightPressed; // Flag for right rotation key
	
	/**
	 * Constructs a Cheese object with the specified shape, position, and rotation.
	 *
	 * @param inShape    The array of points defining the shape of the cheese.
	 * @param inPosition The position of the cheese.
	 * @param inRotation The rotation of the cheese.
	 */
 
	public Cheese(Point[] inShape, Point inPosition, double inRotation) {
		super(inShape, inPosition, inRotation);// Call the superclass constructor
		
		// Initialize x and y coordinates
		this.x = inPosition.getX();
		this.y = inPosition.getY();		
		
		this.rotationSpeed = 5; //rotation speed
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
	}
	
	/**
	    * Paints the cheese object onto the specified graphics context.
	    * @param brush The graphics context to paint onto.
	    */
 
	@Override
	public void paint(Graphics brush) {
		// set the color of the object
		brush.setColor(Color.yellow);
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
	    * Moves the cheese object continuously to the left.
	    */

	@Override
	public void move() {
		 // speed cheese is moving 
		 
		x -= 0.9; //moving the x-position to the left
		
		if (rotateLeftPressed) {
	        rotate(-5); // Rotate counter-clockwise
	    }
	    if (rotateRightPressed) {
	        rotate(5); // Rotate clockwise
	    }
	    
		position.setX(x); // update x coordinate
	}
	
	/**
	    * Checks if the cheese object is off the screen.
	    * @return true if the cheese is off the screen, false otherwise.
	    */

	public boolean offScreen() {
		boolean consumed = false; 
		
		if (x<=0) { 
			consumed = true; 
		}
		
		return consumed;
	}
	
	/**
	 * Updates the state of the right and left arrow keys.
	 * 
	 * @param e The key that is being pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    
	    // Handle rotation keys
	    switch (e.getKeyCode()) {
	        case KeyEvent.VK_LEFT:
	            rotateLeftPressed = true;
	            break;
	        case KeyEvent.VK_RIGHT:
	            rotateRightPressed = true;
	            break;
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
	    switch (e.getKeyCode()) {
	        case KeyEvent.VK_LEFT:
	            rotateLeftPressed = false;
	            break;
	        case KeyEvent.VK_RIGHT:
	            rotateRightPressed = false;
	            break;
	    }
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
