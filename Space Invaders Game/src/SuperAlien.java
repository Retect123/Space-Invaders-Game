import java.awt.Color;
import java.awt.Point;
import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

/**
 * The representation and display of a Super Alien
 */

public class SuperAlien extends Alien {
	
    // HP of the super alien
	private int lives;
	
	/**
     * Creates a super alien in the graphics window
     * 
     * @param window the GWindow this SuperAlien belongs to
     * @param center the center Point of this SuperAlien
     */
	
    public SuperAlien(GWindow window, Point center) {
        super(window, center);
        // Set initial HP of the super alien
        this.lives = 30;
        
        // Display the super alien
        this.draw();
    }
    
    /**
     * Decreases the HP of the super alien when it's shot
     */
    public void isShot() {
		this.lives --;
	}
	

	/**
	 * Is this SuperAlien dead?
	 */
	public boolean isDead() {
		// Check if the super alien is dead
		return this.lives == 0;
	}

	/**
	 * Returns the location of this SuperAlien
	 */
	public Point getLocation() {
		return this.center;
	}
    
	/**
     * Moves this SuperAlien
     * The super alien moves downward and randomly left or right
     * If it reaches the bottom of the screen, it reappears at the top
     */
    public void move() {
		// Erase current drawing
		this.erase();
		// Update y-coordinate of super alien (Moving x2 speed of regular alien)
		this.center.y += 2 * RADIUS;
        // If the super alien reaches the bottom, wrap it around to the top
        if (this.center.y > this.window.getWindowHeight()) {
            this.center.y = 10 * RADIUS;
        }
        
        // Randomly choose left or right direction
	    int direction = Math.random() < 0.5 ? MovingObject.LEFT : MovingObject.RIGHT;
	    
	    // Calculate new x-coordinate based on direction
	    int newX = this.center.x + (direction == MovingObject.LEFT ? -RADIUS : RADIUS);
	    
	    // Check if the new x-coordinate is within window bounds
	    if (newX >= RADIUS && newX <= this.window.getWindowWidth() - RADIUS) {
	        // Update the center point
	        this.center.x = newX;
	        
	    }
	 // Redraw the super alien at its new position
        this.draw();
    }
    
    /**
     * Displays this SuperAlien in the graphics window
     */
    protected void draw() {
    	// Determine the color based on the number of lives left
        Color color;
        if (this.lives <= 10) {
            color = Color.RED;
        } else if (this.lives <= 20) {
            color = Color.BLUE;
        } else if (this.lives <= 30) {
            color = Color.GREEN;
        } else {
            // Default color (if lives > 30)
            color = Color.BLACK;
        }

        // Graphics elements for the display of this SuperAlien
        this.shapes = new Shape[3];
        // Increase the size of the shapes by 7 compared to regular alien
        int enlargedRadius = 7 * RADIUS;
        this.shapes[0] = new Line(this.center.x - 2 * enlargedRadius, this.center.y - 2 * enlargedRadius, this.center.x + 2 * enlargedRadius,
                this.center.y + 2 * enlargedRadius, color);
        this.shapes[1] = new Line(this.center.x + 2 * enlargedRadius, this.center.y - 2 * enlargedRadius, this.center.x - 2 * enlargedRadius,
                this.center.y + 2 * enlargedRadius, color);
        this.shapes[2] = new Oval(this.center.x - enlargedRadius, this.center.y - enlargedRadius, 2 * enlargedRadius, 2 * enlargedRadius, color, true);

        // Add shapes to the graphics window
        for (int i = 0; i < this.shapes.length; i++)
            this.window.add(this.shapes[i]);

        // Bounding box of this Alien
        this.boundingBox = new Rectangle(this.center.x - 2 * enlargedRadius, this.center.y - 2 * enlargedRadius, 4 * enlargedRadius,
                4 * enlargedRadius);

        // Update the graphics window
        this.window.doRepaint();
    }
}
	