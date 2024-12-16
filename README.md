# BattleShip-Solver
It uses a BattleShip API to generate a board of 12 by 12 size with 6 ships (of size 5,5,4,4,2,2)

**Rules with the API:**
1. the ships can be vertical or horizontal
2. Two ships will not be touching each other perpendicularly 
3. Two ships can be touching each other diagonally
4. The API should be provided a Bot class with atleast three methods.
   - void initialize(BattleShip3 battleship)	=> constructor
   - fireShot		=> selects a x,y coordinate and uses Shoot method from BattleShip API to take a shot.
   - getAuthors		=> prints authors name
                  
**I have created a PatelsBot class that solves the the board in arround 100 shots and takes less than 1ms to solve it**

- **Current Shooting Algorithm**
	- generates Random x & y (uses CheckBoard method to remove 1/2 locations).
	- On hit, Uses stack to store Potential Targets.
	- On another hit, it will keep shooting in the same direction (Horizontal or vertical) until next miss and then shoots the other side (left-right or up-down) of the direction.
	- when all potential targets are shot, switches to random shooting.

- **Goal:**  to reduce the number of shots to finish the game.
- **Potential Solution**
	- If it is not possible to have ships touching perpendicular, then we can mark the other sides of a hit location as marked (if going horizontal then verticals can be marked as no ship found).
    - when a ship is shunk, we can check diagonals.

