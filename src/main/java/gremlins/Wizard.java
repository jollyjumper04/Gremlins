package gremlins;


public class Wizard extends Creatures{
    /**
     * The state ofnthe space bar (is it being pressed)
    */
    protected Boolean pressSpace = false;

    /**
     * Constructor for a Wizard, child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}, 
     * requires the app instance, speed value and pixel position on the window
     * @param app app instance
     * @param speed int speed value
     * @param i actual pixel X cordinate 
     * @param j actual pixel Y cordinate 
    */

    public Wizard(App app, int speed, int i ,int j) {
        super(app, speed, i, j);
    }

    /**
     * If the wizard gets hurt, decrement the lives remaining and sets the wizard position to its inital spawn position
     * If there are no more lives left the game is over. (sets maps.gameOver variable to true)
    */

    public void getsHurt(){
        app.lives--;
        if (app.lives == 0){
            app.map.gameOver = true;
        }
        this.setPos(app.map.wizSpawnX, app.map.wizSpawnY); //reset to spawn after hit
    }
    
    /**
     * setter for moveUp direction state 
     * @param state (boolean)
    */

    public void pressUp(boolean state) {
        this.moveUp = state;
    }

    /**
     * setter for moveDown direction state 
     * @param state (boolean)
    */

    public void pressDown(boolean state) {
        this.moveDown = state;
    }

    /**
     * setter for moveLeft direction state 
     * @param state (boolean)
    */

    public void pressLeft(boolean state) {
        this.moveLeft = state;
    }

    /**
     * setter for moveRight direction state 
     * @param state (boolean)
    */

    public void pressRight(boolean state) {
        this.moveRight = state;
    }

    /**
     * setter for Space button state 
     * @param state (boolean)
    */

    public void pressSpace(boolean state){
        this.pressSpace = state;
    }

    /**
     * When called, checks if the Space Button has been pressed and if the 
     * {@link Creatures#checkCooldown() checkCooldown()} return boolean is true. If so, calls:
     * calls {@link Creatures#checkCooldown() checkCooldown()}
     * resets timer with the setter {@link Creatures#setTimer(int time) setTimer(int time)()}
    */

    public void checkFire(){
        if (pressSpace && checkCooldown()){
            fire(true);
            setTimer(0);
        }
    }

    /**
     * Tick() calls
     * {@link #incrementTimer()}
     * {@link #updatePos()}
     * {@link #checkFire()}
     * {@link #checkProjCollide()}
    */

    public void tick(){
        incrementTimer();
        updatePos();
        checkFire();
        checkProjCollide();
    }

    


}
