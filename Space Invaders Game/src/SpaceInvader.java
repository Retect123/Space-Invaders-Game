
// Write your compliance statement here:
// What are your 4 extra features?
// How is your new alien different from the one described by the Alien class?

/**
 * Our program meets the requirements because we have 4 extra features.
 * Our first feature was making the space ship move in every direction (left, right, up, and down).
 * Our second feature was making the aliens move left and right randomly, making them harder to shoot.
 * Our third feature was increasing the difficulty of the game. For example, once the player kills all
 * of the regular aliens, they move onto the boss level which features a bigger and faster alien with more health.
 * Our fourth feature was making a life system. The player only has one life, so if they get touched by
 * an alien in any way they lose.
 * We also created a new kind of alien called the SuperAlien, which passes the requirement of making
 * a new kind of alien. The super alien is way bigger than the regular aliens, two times as fast and has way more health.
 * We also meet the basic requirements because every time an alien is shot, it's color changes. The aliens
 * also spawn at different heights and gradually move down the screen. When an alien is dead, it is no longer
 * on the screen, and if there are no more aliens on the screen, a prompt window comes up indicating that the
 * user has won. We also have a prompt window at the beginning to make sure our user knows the controls.
 * 
 * @author JORDAN CHAU
 */

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import uwcse.graphics.GWindow;
import uwcse.graphics.GWindowEvent;
import uwcse.graphics.GWindowEventAdapter;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;

/**
 * A SpaceInvader displays a fleet of alien ships and a space ship. The player
 * directs the moves of the spaceship and can shoot at the aliens.
 */

public class SpaceInvader extends GWindowEventAdapter {
	// Possible actions from the keyboard
	/** No action */
	public static final int DO_NOTHING = 0;

	/** Steer the space ship */
	public static final int SET_SPACESHIP_DIRECTION = 1;

	/** To shoot at the aliens */
	public static final int SHOOT = 2;

	// Period of the animation (in ms)
	// (the smaller the value, the faster the animation)
	private int animationPeriod = 100;

	// Current action from the keyboard
	private int action;

	// Game window
	private GWindow window;

	// The space ship
	private SpaceShip spaceShip;

	// Direction of motion given by the player
	private int dirFromKeyboard = MovingObject.LEFT;

	// The aliens
	private ArrayList<Alien> aliens;
	
	// The SuperAlien
	private boolean superAlienSpawned = false;
	
	// Number of times the spaceship is hit
    private int spaceshipHitCount = 0;

	// Is the current game over?
	private String messageGameOver = "Game Over! You lost!";
	
	// Flag to track if the special message has been displayed
    private boolean specialMessageDisplayed = false;

	/**
	 * Constructs a space invader game
	 */
	public SpaceInvader() {
		this.window = new GWindow("Space invaders", 500, 500);
		this.window.setExitOnClose();
		this.window.addEventHandler(this); // this SpaceInvader handles all of
		// the events fired by the graphics
		// window

		// Display the game rules
		String rulesOfTheGame = "Save the Earth! Destroy all of the aliens ships.\n" + "To move left, press 'a'.\n"
				+ "To move right, press 'd'.\n" + "To move forward, press 'w'.\n" + "To move backwards, press 's'.\n"
				+ "To shoot, press the space bar.\n" + "To quit, press 'Q'.\n" + "Try not to touch the alien or you will perish.";
		JOptionPane.showMessageDialog(null, rulesOfTheGame, "Space invaders", JOptionPane.INFORMATION_MESSAGE);
		this.initializeGame();
	}

	/**
	 * Initializes the game (draw the background, aliens, and space ship)
	 */
	private void initializeGame() {
		// Clear the window
		this.window.erase();
		
		// Reset flags
	    this.superAlienSpawned = false;

		// Background (starry universe)
		Rectangle background = new Rectangle(0, 0, this.window.getWindowWidth(), this.window.getWindowHeight(),
				Color.black, true);
		this.window.add(background);
		// Add 50 stars here and there (as small circles)
		Random rnd = new Random();
		for (int i = 0; i < 50; i++) {
			// Random radius between 1 and 3
			int radius = rnd.nextInt(3) + 1;
			// Random location (within the window)
			// Make sure that the full circle is visible in the window
			int x = rnd.nextInt(this.window.getWindowWidth() - 2 * radius);
			int y = rnd.nextInt(this.window.getWindowHeight() - 2 * radius);
			this.window.add(new Oval(x, y, 2 * radius, 2 * radius, Color.WHITE, true));
		}

		// ArrayList of aliens
		this.aliens = new ArrayList<Alien>();

		// Create 12 aliens
		// Spawn aliens at random location
		// (Make sure that the space ship can fire at them)
		int min_X = 10 * Alien.RADIUS; // Minimum X position offset
		int max_X = this.window.getWindowWidth() - 10 * Alien.RADIUS; // Maximum X position

		for (int i = 0; i < 12; i++) {
		    int x = rnd.nextInt(max_X - min_X) + min_X; // Random x-coordinate within the allowed range with minimum offset
		    int y = rnd.nextInt(this.window.getWindowHeight() / 2); // Random y-coordinate above halfway mark
		    this.aliens.add(new Alien(this.window, new Point(x, y)));
		}
		
		// Create the space ship at the bottom of the window
		int x = this.window.getWindowWidth() / 2;
		int y = this.window.getWindowHeight() - SpaceShip.HEIGHT / 2;
		this.spaceShip = new SpaceShip(this.window, new Point(x, y));

		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	/**
	 * Moves the objects within the graphics window every time the timer fires an
	 * event
	 */
	public void timerExpired(GWindowEvent we) {
		// Perform the action requested by the user?
		switch (this.action) {
		case SpaceInvader.SET_SPACESHIP_DIRECTION:
			this.spaceShip.setDirection(this.dirFromKeyboard);
			break;
		case SpaceInvader.SHOOT:
			this.spaceShip.shoot(this.aliens);
			break;
		}

		this.action = SpaceInvader.DO_NOTHING; // Don't do the same action
		// twice

		// Show the new locations of the objects
		this.updateGame();
	}
	

	/**
	 * Selects the action requested by the pressed key
	 */
	public void keyPressed(GWindowEvent e) {
		// Don't perform the actions (such as shoot) directly in this method.
		// Do the actions in timerExpired, so that the alien ArrayList can't be
		// modified at the same time by two methods (keyPressed and timerExpired
		// run in different threads).

		switch (Character.toLowerCase(e.getKey())) // not case sensitive
		{
		// Code to move the space ship using keyboard
		case 'a': // press 'a' to move the space ship left
            this.dirFromKeyboard = MovingObject.LEFT;
            this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
            break;

        case 'd': // press 'd' to move the space ship right
            this.dirFromKeyboard = MovingObject.RIGHT;
            this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
            break;
            
        case 'w': // press 'w' to move the space ship forward
            this.dirFromKeyboard = MovingObject.UP;
            this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
            break;

        case 's': // press 's' to move the space ship backward
            this.dirFromKeyboard = MovingObject.DOWN;
            this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
            break;

		case ' ': // press space to shoot at the aliens
			this.action = SpaceInvader.SHOOT;
			break;

		case 'q': // press 'q' to quit the game
			System.exit(0);

		default: // no new action
			this.action = SpaceInvader.DO_NOTHING;
			break;
		}
	}

	
	/**
	 * Updates the game (Move aliens + space ship)
	 */
	private void updateGame() {
	    // Move the aliens vertically
	    for (Alien a : aliens) {
	        a.move();
	    }

	    // Move the space ship
	    this.spaceShip.move();

	    // Check for collisions between the space ship and aliens
	    for (Alien alien : this.aliens) {
	        if (this.spaceShip.getBoundingBox().intersects(alien.getBoundingBox())) {
	            this.spaceshipHitCount++;
	            String s = messageGameOver;
	            if (this.spaceshipHitCount >= 2) {
	                this.window.stopTimerEvents();
	                // Prompt the user to play another game
	                if (!this.anotherGame(s)) {
	                    // If the user chooses not to play again, exit the game
	                    System.exit(0);
	                } else {
	                    // If the user chooses to play again, restart the game
	                    this.initializeGame();
	                    return;
	                }
	            }
	        }
	    }

	 // Check if all regular aliens are dead and prompt the user for boss level
	    if (allAliensDead() && !superAlienSpawned) {
	        if (!promptBossLevel()) {
	            // If the user chooses not to go to the boss level, exit the game
	            System.exit(0);
	        } else {
	            // Add super alien
	            createSuperAlien();
	            // Redraw background
	            redrawBackground();
	            this.window.doRepaint();
	            // Set superAlienSpawned to true to prevent further prompts
	            superAlienSpawned = true;
	            return; // Exit updateGame to prevent further processing
	        }
	    }

	    // Check if the SuperAlien is dead
	    if (superAlienSpawned && allAliensDead() && allSuperAliensDead() && !specialMessageDisplayed) {
	    	String s = "Congratulations, you saved the Earth!";
	        // Prompt for another game
	        if (!this.anotherGame(s)) {
	            // If the user chooses not to play again, exit the game
	            System.exit(0);
	        } else {
	            // If the user chooses to play again, restart the game
	            this.initializeGame();
	            return;
	        }
	    }
	}
    

	/**
	 * Check if all regular aliens are dead
	 */
	private boolean allAliensDead() {
		
	    for (Alien a : aliens) {
	        // Check if the alien is not an instance of SuperAlien and is not dead
	        if (!(a instanceof SuperAlien) && !a.isDead()) {
	            return false;
	        }
	    }
	    return true;
	}
	/**
	 * Check if all super aliens are dead
	 */
	private boolean allSuperAliensDead() {
	    for (Alien a : aliens) {
	    	// Check if the alien is an instance of SuperAlien and is not dead
	        if (a instanceof SuperAlien && !a.isDead()) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Creates a SuperAlien and adds it if the user wants to go to boss level
	 */
	private void createSuperAlien() {
	    // Define the size of the SuperAlien (width and height)
	    int superAlienWidth = 50; 
	    int superAlienHeight = 50; 

	    // Define the maximum x and y coordinates to ensure the SuperAlien fits within the screen
	    int maxX = window.getWindowWidth() - superAlienWidth;
	    int maxY = window.getWindowHeight() / 2 - superAlienHeight; // Upper half of the screen

	    // Place the big alien at a random location within the screen bounds
	    int x = (int) (Math.random() * maxX);
	    int y = (int) (Math.random() * maxY);
	    
	    Point superAlienLocation = new Point(x, y);

	    // Create the big alien
	    SuperAlien superAlien = new SuperAlien(window, superAlienLocation); // Create SuperAlien instead of Alien
	    aliens.add(superAlien);
	}

	/**
	 * Redraws background when if the user wants to go to boss level
	 */
	private void redrawBackground() {
	    // Clear the window
	    this.window.erase();
	    // Background (starry universe)
	    Rectangle background = new Rectangle(0, 0, this.window.getWindowWidth(), this.window.getWindowHeight(),
	            Color.black, true);
	    this.window.add(background);
	    // Add 50 stars here and there (as small circles)
	    Random rnd = new Random();
	    for (int i = 0; i < 50; i++) {
	        // Random radius between 1 and 3
	        int radius = rnd.nextInt(3) + 1;
	        // Random location (within the window)
	        // Make sure that the full circle is visible in the window
	        int x = rnd.nextInt(this.window.getWindowWidth() - 2 * radius);
	        int y = rnd.nextInt(this.window.getWindowHeight() - 2 * radius);
	        this.window.add(new Oval(x, y, 2 * radius, 2 * radius, Color.WHITE, true));
	    }
	}
	
	/**
	 * Prompt the user to go to the boss level
	 * @return true if the user wants to go to the boss level, false otherwise
	 */
	private boolean promptBossLevel() {
	    int choice = JOptionPane.showConfirmDialog(null, "All regular aliens are dead. Do you want to go to the boss level?",
	            "Boss Level", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    return (choice == JOptionPane.YES_OPTION);
	    
	}
	
	/**
     * Does the player want to play again?
     */
    public boolean anotherGame(String s) {
        // this method is useful at the end of a game if you want to prompt the
        // user
        // for another game (s would be a String describing the outcome of the
        // game
        // that just ended, e.g. "Congratulations, you saved the Earth!")
        int choice;
        if (s.contains("Congratulations, you saved the Earth")) {
            // Special message if SuperAlien is killed
            choice = JOptionPane.showConfirmDialog(null, s + "\nDo you want to play again?", "Congratulations!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        } else {
            choice = JOptionPane.showConfirmDialog(null, s + "\nDo you want to play again?", "Game over!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }

        return (choice == JOptionPane.YES_OPTION);
    }
    
	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new SpaceInvader();
	}
}
