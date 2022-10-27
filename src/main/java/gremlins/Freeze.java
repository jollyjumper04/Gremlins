package gremlins;

public class Freeze extends Collidables{

    protected int respawnRadius = 10;
    protected boolean active = false;
    protected int freezeTime = 5; //seconds

    /**
    * Constructor for Freeze class, child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)},
    * requires app instance, and absolute x and y pixel cordinates
    * @param app (App)
    * @param i (int) X
    * @param j (int) Y
    */

    public Freeze(App app, int i, int j) {
        super(app, i, j);
    }
    
    /**
     * If the freeze objects is active, sets sprite to a blank image (appear to the user to have dissapeared)
     * calls {@link #incrementTimer() incrementTimer()}. 
     * Tf the timer of this object is greater than the duration of the freeze active time, then reset the active flag, timer,
     * spawn the freeze timer a random x and y cordinate a set radius away from the wizard. After this it
     * update the map grid element to have the new position of the obj and finally set the sprite of the obj back to its original icon.
     * 
    */

    public void checkActive(){
        if (active){
            this.setSprite(app.blank);
            incrementTimer();
            if (getTimer() >= freezeTime*App.FPS){
                active = false;
                setTimer(0);
                randomSpawnPos(respawnRadius);
                app.map.mapGrid[this.x][this.y] = this;
                this.setSprite(app.freeze);
            }
        }
    }
    /**
     * tick() calls:
     * <ul>
     * <li>{@link #checkActive() checkActive()}
     * </ul>
    */
    public void tick(){
        checkActive();
    }
    
}
