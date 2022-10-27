package gremlins;

class Collidables extends Icons{

    private int timer = 0;

    protected int x;
    protected int y;

    protected int impactPosX;
    protected int impactPosY; 

    protected Boolean impact; //if the collidable gets hit/hurt by another collidable eg: fireball on brick wall

    /**
    * Constructor for Collidables class child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}, 
    * requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */

    public Collidables(App app, int i, int j) {
        super(app, i, j);
        this.x = i/20;
        this.y = j/20;
    }

    /**
     * Sets the position of the object it is called from to the realtive X and Y cordinates
     * this is changed to the absolute (pixel) cordinates by multiplying both X and Y by 20 
     * (each block in the grid takes up 20x20 pixels)
     * @param X (int) X
     * @param Y (int) Y
    */

    public void setPos(int X, int Y){
        this.x = X; //set actual x pos to X
        this.y = Y;
        this.i = X*20; //set visuals to x pos X
        this.j = Y*20;
    }

    /**
     * gets random X and Y values using {@link #randomInt(int min, int max) randomInt(int min, int max)} 
     * (with the boundries being the map size minus the 1 block border)...
     * <ul>
     * <li> with these boundries it calcuates if the absoute distance of the wizard is more than a set radius from the point 
     * and checks if its the calculated position is not empty.
     * finally if all the conditions are met itsets the position of the object it is called from using {@link #setPos(int X, int Y) setPos(int X, int Y)}
     * </ul>
     * @param radius (int) from wizard obj
    */

    public void randomSpawnPos(int radius){ //set object position a given radius from wizard
        int xBound = 0;
        int yBound = 0;

        
        while (true) {
            xBound = randomInt(1, 34); //cant spawn in 1 block border
            yBound = randomInt(1, 31);
            if (!((Math.abs(xBound-app.wizard.x) < radius) && (Math.abs(yBound-app.wizard.y) < radius))){
                if (app.map.mapGrid[xBound][yBound] == null){
                    break;
                }
            }
        }

        this.setPos(xBound,yBound);
    }

    /**
     * checks if the object this function is called from is in the same position as the parameter object
     * return True if yes, False if no
     * @param colObj (Collidables) object
     * @return boolean
    */

    public Boolean inCurrentPos(Collidables colObj){ //checks if X and Y args match objects own x and y
        if ((this.x == colObj.x) && (this.y == colObj.y)){
            return true;
        }
        return false;
    }

    /**
     * Generates random integer between (including boundries) min and max
     * @param min minimum int value
     * @param max maximum int value
     * @return integer
    */

    public int randomInt(int min, int max){ //returns random int between range
        return (min + (int)(Math.random() * ((max - min) + 1)));
    }


    public int getTimer(){
        return timer;
    }

    public void setTimer(int timer){
        this.timer = timer;
    }
    
    public void incrementTimer(){
        this.timer++;
    }


    
}
