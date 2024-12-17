import battleship.*;
import java.awt.*;
import java.util.Random;
import java.util.Stack;

/**
 * This is a Bot that strategically selects a location and shoots to sink all ships on a battleShip board.
 * Here, two strategies are being used 1) HuntMode 2) trackMode
 * 1) HuntMode : Randomly selects a location to shoot in a checkerBoard pattern.
 * 2) TrackMode : Selects a direction to shoot and keeps shooting till the next miss.
 * @author Hitarth Patel (000897988) & Aish Patel (00902820)
 */
public class PatelsBot implements BattleShipBot {
    private int gameSize;
    private BattleShip3 battleShip;
    private Random random;
    private String botMode;
    private boolean [][] shotTracker;
    private Stack<Target> potentialTargets = new Stack<>();
    //Target currentTarget;

    /**
     * Constructor keeps a copy of the BattleShip instance
     * Create instances of any Data Structures and initialize any variables here
     * @param b previously created battleship instance - should be a new game
     */

    @Override
    public void initialize(BattleShip3 b) {
        battleShip = b;
        gameSize = b.BOARD_SIZE;

        shotTracker = new boolean[gameSize][gameSize];
        botMode = "huntMode";

        // Need to use a Seed if you want the same results to occur from run to run
        // This is needed if you are trying to improve the performance of your code

        random = new Random();   // Needed for random shooter - not required for more systematic approaches
    }

    /**
     * This method is being called by BattleShip API to fire a shot per method Call.
     * It uses the shoot method in BattleShip API to shoot which returns true if it hits a ship.
     * This method decides which strategy to choose to take the next shot depending upon the last shot.
     */

    @Override
    public void fireShot() {
        if (botMode.equals("huntMode")) huntMode();
        else if (botMode.equals("trackMode")) trackMode();
    }

    /**
     * This method is for hunt Strategy which randomly (using checkBoard strategy) selects a location that has not been shot before and shoots.
     * If it gets a hit, and it switches to trackMode for the next shot and adds Potential targets in a stack .
     */
    public void huntMode(){

        int x, y;

        //this while loop will select a (x,y) location on a checkerboard which has never been shot before.
        do {
            x = (random.nextInt(gameSize));     // randomly selects x
            if (x % 2 == 0) y = ((random.nextInt(gameSize)/2) * 2) + 1;     //If x is even, randomly selects an odd number
            else y = (random.nextInt(gameSize)/2) * 2;      //If x is odd, randomly selects an even number
        }while (shotTracker[x][y]);

        markPoints(x, y);       //keeps track of the location

        boolean hit = battleShip.shoot(new Point(x,y));     // Will return true if we hit a ship
        if (hit) {
            botMode = "trackMode";
            Target currentTarget = new Target(x,y, "all");
            addPotentialTargets(currentTarget);     // adds 4 location to the stack (up, down, right, left).
        }
    }

    /**
     * This method uses a stack to keep track of the next location to hit.
     * Initially after getting a hit by huntMode. It shoots in one of the direction around the hit location
     * to figure which direction the ship is.(horizontal or vertical)
     * Once the direction is figured, this method will keep going in that direction till it gets a miss and then check the other side.
     * if we run out of Potential targets, and it will switch to huntMode.
     */
    public void trackMode(){
        if (!potentialTargets.empty()) {
            Target currentTarget = potentialTargets.pop();
            boolean hit = battleShip.shoot(new Point(currentTarget.x, currentTarget.y));
            markPoints(currentTarget.x, currentTarget.y);       //keeps track of the location

            if (hit) {
                addPotentialTargets(currentTarget);       // adds the next potential target to the stack (in the same direction).
            }
        }else {     //if no more Potential targets are left to check
            botMode = "huntMode";
            huntMode();     //switches back to huntMode
        }

    }

    /**
     * this method checks if a location is valid and assigns true if valid.
     * @param x     x-Location
     * @param y     y-Location
     */
    private void markPoints(int x, int y) {
        if (x >= 0 && x < gameSize && y >= 0 && y < gameSize) {
            shotTracker[x][y] = true;
        }
    }

    /**
     * This method check available Potential targets and their valid location before add them on the stack.
     * provides adding targets in all 4 direction or any particular direction.
     * @param target - current target
     */
    public void addPotentialTargets(Target target){

        if (target.lastDirection.equals("all")) {
            if ((target.y-1) >= 0 && !shotTracker[target.x][target.y-1])
                potentialTargets.push(new Target(target.x, target.y-1, "up"));

            if ((target.y+1) < gameSize && !shotTracker[target.x][target.y+1])
                potentialTargets.push(new Target(target.x, target.y+1, "down"));

            if ((target.x+1) < gameSize && !shotTracker[target.x+1][target.y])
                potentialTargets.push(new Target(target.x+1, target.y, "right"));

            if ((target.x-1) >= 0 && !shotTracker[target.x-1][target.y])
                potentialTargets.push(new Target(target.x-1, target.y, "left"));
        }
        else if (target.lastDirection.equals("right")){
            if ((target.x+1) < gameSize && !shotTracker[target.x+1][target.y])
                potentialTargets.push(new Target(target.x+1, target.y, "right"));
        }
        else if (target.lastDirection.equals("left")) {
            if ((target.x-1) >= 0 && !shotTracker[target.x-1][target.y])
                potentialTargets.push(new Target(target.x-1, target.y, "left"));
        }
        else if (target.lastDirection.equals("up")) {
            if ((target.y - 1) >= 0 && !shotTracker[target.x][target.y-1])
                potentialTargets.push(new Target(target.x, target.y - 1, "up"));
        }
        else if (target.lastDirection.equals("down")) {
            if ((target.y + 1) < gameSize && !shotTracker[target.x][target.y+1])
                potentialTargets.push(new Target(target.x, target.y + 1, "down"));
        }
        else throw new RuntimeException("lastDirection not defined!!!");
    }

    /**
     * Authorship of the solution - must return names of all students that contributed to
     * the solution
     * @return names of the authors of the solution
     */

    @Override
    public String getAuthors() {
        return "Hitarth Patel (000897988) \n Aish Patel (000902820)";
    }
}
