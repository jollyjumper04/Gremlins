package gremlins;

public class Wall extends Collidables {


    /**
    * Constructor for Wall class, requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */


    public Wall(App app, int i, int j) {
        super(app, i, j);
    }

}