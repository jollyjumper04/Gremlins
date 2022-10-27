package gremlins;

public class Portal extends Collidables {
    
    protected int respawnRadius = 10;

    /**
    * Constructor for Portal class, requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */

    public Portal(App app, int i, int j) {
        super(app, i, j);
    }
    
    /**
     * calls the {@link Collidables#randomSpawnPos(int radius) randomSpawnPos(int radius)} method
    */
    public void relocatePortal(){
        randomSpawnPos(respawnRadius);
    }
}
