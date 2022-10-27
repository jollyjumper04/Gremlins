package gremlins;

import java.util.ArrayList;

public class Gremlins extends Creatures{

    protected Boolean initFlag = false;
    protected int randomDir;

    /**
     * Constructor for a Gremlins, child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)},
     * requires the app instance, speed value and pixel position on the window
     * @param app app instance
     * @param speed int speed value
     * @param i actual pixel X cordinate 
     * @param j actual pixel Y cordinate 
    */

    public Gremlins(App app, int speed, int i ,int j) {
        super(app, speed, i, j);
    }

    /**
     * gets random index from 0-size of the param array...
     * the array is already pre checked for having elements (not null) before passed into this fuction
     * @param okDirRand (of type Integer) 
     * @return random int element from the paramter arraylist
    */

    public int getRandElement(ArrayList<Integer> okDirRand){
        int randomInt = (int) ((Math.random() * (okDirRand.size() - 0)) + 0); //gets random index from 0-size of array (array is already checked for having elemnts before passed into this fuction)
        return okDirRand.get(randomInt);

    }

    /**
     * Checks if the {@link #checkPosEmpty(int checkX, int checkY) checkPosEmpty(int checkX, int checkY)()} function 
     * is not empty (ie it has hit a wall)... if so it checks all the directions it can move (up down left right) and appends only the 
     * valid directions it can go to into the okDir arraylist. 
     * <ul>
     * <li> if there is no direction it can go it will do nothing (ie: if stuck in 1x1 hole) 
     * <li> if it has only 1 direction it can go, the current direction will be set to that. 
     * <li> if it has multiple directions it can go, the {@link #getRandElement(ArrayList okDirRand) getRandElement(ArrayList okDirRand)} 
     * <li> will be called to select a random element in the array and the return value will be set to the Gremlins current direction
     * </ul>
    */

    public void changeDir(){
        if (checkPosEmpty(checkX, checkY) == false){ //has hit a wall

            ArrayList<Integer> okDir = new ArrayList<Integer>();
            //System.out.println(okDir.size());

            this.checkX = x;
            this.checkY = y;

            this.checkY = checkY-1;
            if (checkPosEmpty(checkX,checkY)){
                okDir.add(1);
            } checkY = y;

            this.checkY = checkY+1;
            if (checkPosEmpty(checkX,checkY)){
                okDir.add(2);
            } checkY = y;

            this.checkX = checkX-1;
            if (checkPosEmpty(checkX,checkY)){
                okDir.add(3);
            } checkX = x;

            this.checkX = checkX+1 ;
            if (checkPosEmpty(checkX,checkY)){
                okDir.add(4);
            } checkX = x;

            if (okDir.size() == 0){
                ; //if stuck in 1x1 hole with no possible direction to go, do nothing (from chance of random respawn of gremlin)
            } else if (okDir.size() == 1){ //only direction possible direction to go is the direction came from, ie dead end
                this.curDir = oppDir;
            } else {
                okDir.remove(Integer.valueOf(oppDir)); //removes direction it came from in possible directions it can move to array
                this.curDir = getRandElement(okDir);
            }
            moveDir();
        } 
    }

    /**
     * Similar to {@link Wizard#checkCooldown() checkCooldown()}... When called, checks if the 
     * {@link Creatures#checkCooldown() checkCooldown()} return boolean is true. If so, calls:
     * calls {@link Creatures#checkCooldown() checkCooldown()}
     * resets timer with the setter {@link Creatures#setTimer(int time) setTimer(int time)}
    */

    public void checkFire(){
        if (checkCooldown()){
            fire(false);
            this.setTimer(0);
        }
    }

    /**
     * If the gremlin in in the Similar to current position of the wizard 
     * {@link Collidables#inCurrentPos(Collidables obj) inCurrentPos(Collidables obj)}... 
     * it will hurt the wizard... calls {@link Wizard#getsHurt() getsHurt()}
    */
    public void checkSelfHitWizard() { //checks if itself has touched the wizard
        if (this.inCurrentPos(app.wizard)){
            app.wizard.getsHurt();
        }
    }

    /**
     * tick() calls:
     * <ul>
     * <li>{@link #incrementTimer() incrementTimer()}
     * <li>{@link #changeDir() changeDir()}
     * <li>{@link #updatePos() updatePos()}
     * <li>{@link #checkFire() checkFire()}
     * <li>{@link #checkProjCollide() checkProjCollide()}
     * <li>{@link #checkSelfHitWizard() checkSelfHitWizard()}
     * </ul>
    */

    public void tick(){
        incrementTimer();
        changeDir();
        updatePos();
        checkFire();
        checkProjCollide();
        checkSelfHitWizard();
    }

}