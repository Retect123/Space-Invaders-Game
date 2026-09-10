import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

/**
 * The representation and display of an Alien
 */

public class Alien extends MovingObject {
	// Size of an Alien
	public static final int RADIUS = 5;

	// Number of lives in this Alien
	// When 0, this Alien is dead
	private int lives;

	/**
	 * Creates an alien in the graphics window
	 * 
	 * @param window the GWindow this Alien belongs to
	 * @param center the center Point of this Alien
	 */
	public Alien(GWindow window, Point center) {
		super(window, center);
		// Randomly assigns number of lives between 1-3
		this.lives = (int) (Math.random() * 3 + 1);

		// Display this Alien
		this.draw();
	}

	/**
	 * The alien is being shot. Decrement its number of lives and erase it from the
	 * graphics window if it is dead.
	 */
	public void isShot() {
		// Decrement its number of lives when alien is shot
		this.lives --;
	}
	

	/**
	 * Is this Alien dead?
	 */
	public boolean isDead() {
		// Check if alien is dead
		return this.lives == 0;
	}

	/**
	 * Returns the location of this Alien
	 */
	public Point getLocation() {
		// Return the center point of the alien
		return this.center;
	}
	

	/**
     * Moves this Alien As a start make all of the aliens move downward. If an alien
     * reaches the bottom of the screen, it reappears at the top. Also, randomly
     * moves the alien left or right, while preventing it from going out of the
     * window horizontally.
     */
	public void move() {
        // erase current drawing
        this.erase();
        
        // update coordinates of alien
        this.center.y += RADIUS;

        // If the alien reaches the bottom, wrap it around to the top
        if (this.center.y > this.window.getWindowHeight()) {
            this.center.y = 10 * RADIUS;
        }
        
        // Randomly choose left or right direction
        int direction = Math.random() < 0.5 ? MovingObject.LEFT : MovingObject.RIGHT;
        
        // Calculate new x-coordinate based on direction
        int newX = this.center.x + (direction == MovingObject.LEFT ? -RADIUS : RADIUS);
        
        // Check if the new x-coordinate is within window bounds
        if (newX >= RADIUS && newX <= this.window.getWindowWidth() - RADIUS) {
            // Update the center point if within bounds
            this.center.x = newX;
        }

        // Redraw the alien at its new position
        this.draw();
    }

	/**
	 * Displays this Alien in the graphics window
	 */
	protected void draw() {
		// Pick the color (according to the number of lives left)
		Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
		// lives is 1 -> RED, 2 -> BLUE, 3 -> Green
		Color color = colors[this.lives - 1];

		// Graphics elements for the display of this Alien
		// A circle on top of an X
		this.shapes = new Shape[3];
		this.shapes[0] = new Line(this.center.x - 2 * RADIUS, this.center.y - 2 * RADIUS, this.center.x + 2 * RADIUS,
				this.center.y + 2 * RADIUS, color);
		this.shapes[1] = new Line(this.center.x + 2 * RADIUS, this.center.y - 2 * RADIUS, this.center.x - 2 * RADIUS,
				this.center.y + 2 * RADIUS, color);
		this.shapes[2] = new Oval(this.center.x - RADIUS, this.center.y - RADIUS, 2 * RADIUS, 2 * RADIUS, color, true);

		// Add shapes to the graphics window
		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);

		// Bounding box of this Alien
		this.boundingBox = new Rectangle(this.center.x - 2 * RADIUS, this.center.y - 2 * RADIUS, 4 * RADIUS,
				4 * RADIUS);

		// Updates the graphics window
		this.window.doRepaint();
	}
}
