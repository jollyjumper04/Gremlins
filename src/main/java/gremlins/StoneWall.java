package gremlins;

public class StoneWall extends Wall {
    /**
    * Constructor for StoneWall class, requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */


    public StoneWall(App app, int i, int j) {
        super(app, i, j);
    }
    
}
