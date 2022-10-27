package gremlins;

public class Projectile extends Movables {

    protected Boolean fireball; //if true, then projectile is a fireball
    protected Boolean collided = false; //triggers when hits anything (flag to remove projectile obj from creatures proj's array)


    /**
    * Constructor for Movables class child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}
    * requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param speed (int) the speed of which the object will move at (from Movables classes constructor aramter)
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * @param fireball (boolean) true if the projectile is of type fireball, false if the projectile is of type slimeball
    * all collidables children objects inherit this constructor
    */

    public Projectile(App app, int speed, int i, int j, Boolean fireball) {
        super(app, speed, i, j);
        this.fireball = fireball;
    }
    
    /**
     * Sets direction of firing the projectile from paramaters creature obj's current direction
     * @param creatureObj (Creatures) object
    */

    public void fireDir(Creatures creatureObj){
        this.curDir = creatureObj.curDir;
    }

    /**
     * Calls the {@link Map#objAtMapPos(int x, int y) objAtMapPos(int x, int y)} to check if the parameter x and y 
     * is of type BrickWall. 
     * @param X (int) relative x position
     * @param Y (int) relative y position
     * @return Boolean - Returns true if is, false if its not.
    */

    public Boolean checkPosBrickWall(int X, int Y){
        Object obj = app.map.objAtMapPos(X,Y);
        if (obj instanceof BrickWall){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Collision detection for projectiles 
     * <ul>
     * <li> If the projectile has impacted any wall (impact flag set to true)...
     * <li> and if the projectile is of type fireball (fireball flag set to true) it will check for if the impacted wall is of type brick using 
     * {@link #checkPosBrickWall(int X, int Y) checkPosBrickWall()} and destroy the wall using {@link Map#destroyBrickWall(int x, int y) destroyBrickWall()} with the 
     * stored impactPosX and impactPosY variables as the parameters
     * <ul>
     * <li> if it has not impacted a wall then it checks if it is a fireball
     * <ul> 
     * <li> if it is {@link #inCurrentPos(Collidables colObj) destroyBrickWall()} 
     * of any of the gremlin objects in the maps gremlinObjs arraylist. If this is the case then it will call {@link #randomSpawnPos(int radius) destroyBrickWall()} 
     * on the corresponding gremlin object
     * </ul>
     *  if it is a slimeball (fireball flag is false)...
     * <ul>
     * <li> checks if it hits any of the wizard fireballs in the array and removes both projectiles
     * <li> checks if it hits the wizard and if it does the apps instance of the wizards {@link Wizard#getsHurt() getsHurt()} is called
     * </ul>
     * </ul>
     * lastly in all cases where any projectile has hit anything (ie wall, wizard, gremlin, and other projectile object) it will set the collided 
     * flag to true in order to remove the projectile object from the {@link Creatures#getProjObjs() getProjObjs()} arraylist
     * </ul>
    */

    public void checkCollide(){
            if (impact){ //when any projectile hits any wall

                collided = true;

                if (fireball){ //if projectile is a fireball 
                    if (checkPosBrickWall(this.impactPosX, this.impactPosY)){ //and if it hits a brick wall
                        app.map.destroyBrickWall(this.impactPosX, this.impactPosY);
                    }
                }
            }

            else {
                if (fireball) { //if proj is fireball , checks if it hits any of the gremlins in the array
                    //System.out.print("F");
                    for (int counter = 0; counter < app.map.gremlinObjs.size(); counter++) { 
                        Gremlins curGremObj = (Gremlins) app.map.gremlinObjs.get(counter);
        
                        if (this.inCurrentPos(curGremObj)){
                            collided = true;
                            curGremObj.randomSpawnPos(10);
                        }
                    }

                    //System.out.print("F");
                } else { //if proj is slime , checks if it hits any of the wizard fireballs in the array and if it hits the wizard

                    for (int counter = 0; counter < app.wizard.getProjObjs().size(); counter++) {
                        Projectile curFireBall = (Projectile) app.wizard.getProjObjs().get(counter);

                        if (this.inCurrentPos(curFireBall)){
                            curFireBall.collided = true;
                            this.collided = true;
                            app.wizard.getProjObjs().remove(curFireBall);
                        }
                    }

                    if (this.inCurrentPos(app.wizard)){
                        app.wizard.getsHurt();
                        this.collided = true;
                    }

                    //System.out.print("S");

                }
                //System.out.print(app.wizard.getProjObjs().size());
            }

    }

    /**
     * tick() calls:
     * <ul>
     * <li>{@link #updatePos() updatePos()}
     * <li>{@link #checkCollide() checkCollide()}
     * <li>{@link #moveDir() moveDir()}
     * </ul>
    */

    public void tick(){
        updatePos();
        checkCollide();
        moveDir();
        
    }
}
