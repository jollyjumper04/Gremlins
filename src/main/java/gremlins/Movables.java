package gremlins;

public class Movables extends Collidables{

    protected Boolean moveUp = false;
    protected Boolean moveDown = false;
    protected Boolean moveLeft = false;
    protected Boolean moveRight = false;
    protected int curDir = 4;
    protected int oppDir = 3;
    protected int checkX;
    protected int checkY;

    private int speed; // eg: wizard moves 2 pixels per frame
    private boolean movingFlag = false;

    /**
    * Constructor for Movables class child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}
    * requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param speed (int) the speed of which the object will move at
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */


    public Movables(App app, int speed, int i, int j) {
        super(app, i, j);
        this.speed = speed;
    }

    /**
     * sets all direction variables to false 
    */

    public void resetDir(){
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
    }

    /**
     * Firsly calls {@link #resetDir() resetDir()} then from the curDir variable (current direction it is going), sets
     * the move variable and its oppsite direction variable corrospondly.
    */


    public void moveDir() {

        resetDir();
        // 1:up 2:down 3:left 4:right

        switch (curDir) {
            case 1:
              moveUp = true;
              oppDir = 2;
              break;
            case 2:
              moveDown = true;
              oppDir = 1;
              break;
            case 3:
              moveLeft = true;
              oppDir = 4;
              break;
            case 4:
              moveRight = true;
              oppDir = 3;
              break;
          }
    }

    /**
     * Calls the {@link Map#objAtMapPos(int x, int y) objAtMapPos(int x, int y)} to check if the parameter x and y 
     * is any kind of wall (instance of the wall object). If it is it will store these cordinates to variables 
     * impactPosX and impactPosY for later refrence. 
     * @param X (int) relative x cordinates
     * @param Y (int) relative y cordinates
     * @return Boolean ... true if empty false if not empty
     * the move variable and its oppsite direction variable corrospondly.
    */

    public Boolean checkPosEmpty(int X, int Y){ //checks if X and Y args are null at the pos (empty tile)
        if (app.map.objAtMapPos(X,Y) instanceof Wall){
            //System.out.println("BLOCK");
            impactPosX = X;
            impactPosY = Y;
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * check if creature has fully enterd a block before moving on to the next
     * @return Boolean ... true if the both i and j pixel position is a multiple of 20, false if not
    */

    public Boolean preCheckPos(){ //check if creature has fully enterd a block before moving on to the next

        if ((this.i % 20 == 0)&(this.j % 20 == 0)){
            return true;
        } else {
            return false;
        }
    } 

    /**
     * Controls the smooth animation for all movable objects. If the movingFlag is true, 
     * then it increments the pixel cordinates of both i and j by blocks of the objects set speed. 
     * It stops when the absolute values if i and j arrive at its set relative x and y positions 
    */


    public void moveToPos(){

        movingFlag = true;

        if (this.i < this.x*20){
            i += this.speed;
        } else if (this.i > this.x*20){
            i -= this.speed;
        } else { // when this.i = this.x*20
            this.i = this.x*20;
            movingFlag = false;
        }

        if (this.j < this.y*20){
            j += this.speed;
        } else if (this.j > this.y*20){
            j -= this.speed;
        }  else {
            this.j = this.y*20;
            movingFlag = false;
        }
    }

    /**
     * This function Ensures movment direction is linear. 
     * If the object is not already in the process of moving to another square (movingFlag) it will
     * <ul>
     * <li> check for direction its moving doesnt have any obsticals (walls)
     * <li> if this is the case then the relative x and y cordinates are updated
     * </ul>
     * after position is ok to move to it will call the {@link #moveToPos() moveToPos()} to begin the movment animation
    */

    public void updatePos(){
        this.checkX = x;
        this.checkY = y;
        impact = false;


        if ((moveUp || moveDown) & preCheckPos()) {
            if (moveUp) {
                this.checkY = checkY-1;
                if (checkPosEmpty(checkX,checkY)){
                    this.y = y-1;
                } else {
                    impact = true;
                }
            } else {
                this.checkY = checkY+1;
                if (checkPosEmpty(checkX,checkY)){
                    this.y = y+1;
                } else {
                    impact = true;
                }
            }
        } 
    
        if ((moveLeft || moveRight)& preCheckPos()) {
            if (moveRight) {
                this.checkX = checkX+1 ;
                if (checkPosEmpty(checkX,checkY)){
                    this.x = x+1;
                } else {
                    impact = true;
                }
            } else {
                this.checkX = checkX-1;
                if (checkPosEmpty(checkX,checkY)){
                    this.x = x-1;
                } else {
                    impact = true;
                }
            }
        }
        moveToPos();
    }
}