package gremlins;

import java.util.ArrayList;

public class Creatures extends Movables {

    private double cooldown;
    private int timer = 0;
    private ArrayList<Collidables> projObjs = new ArrayList<Collidables>(); //all casted projectiles
    private Projectile removeProj;

    /**
    * Constructor for Creature class, child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}, 
    * requires app instance, speed, and absolute x and y pixel cordinates
    * @param app (App)
    * @param Speed (int)
    * @param i (int) X
    * @param j (int) Y
    */

    public Creatures(App app, int Speed, int i, int j) {
        super(app, Speed, i, j);
    }
    /**
     * Getter for the projObjs arraylist...
     * [wizard - grewlins] has projectile type [fireball - slime] 
     * @return projObjs
    */

    public ArrayList<Collidables> getProjObjs(){ //move this to map?
        return (projObjs);
    }

    /**
     * if the timer for this instance (how long the creature has existed) is greater than the amount of frames it takes for the cooldown to expire,
     * then return true (there is no more cooldown needed), else return false 
     * @return Boolean
    */

    public Boolean checkCooldown(){
        if (this.getTimer() > Math.round(cooldown*App.FPS)){
            return true;
        }
        //cooldownPercent = timer / Math.round(cooldown*App.FPS);
        return false;

    }

    /**
     * sets image of fireball/slimeball and appends it to the projObs arraylist depending on the paramter...
     * @param fireball (boolean) - if true then it is a fireball if false then it is a slimeball
    */


    public void fire(boolean fireball){
        Projectile projectile = new Projectile (this.app, 4, this.x*20, this.y*20, fireball);
        projectile.fireDir(this);
        if (fireball){
            projectile.setSprite(app.fireBall);
        } else{
            projectile.setSprite(app.slime);
        }
        projObjs.add(projectile);
        //System.out.println(projObjs.size());
    }

    /**
     * updates/{@link Projectile#tick() tick()} each projectile and checks if it has collided with any walls
     * if the collided flag is true (hits any wall) then remove the object...
    */
    
    public void checkProjCollide(){ //checks if wizard/gremlin projectile has collided with walls, brickwalls, or other proj, 
        for (int counter = 0; counter < projObjs.size(); counter++) { 
            Projectile projectile = (Projectile) projObjs.get(counter);

            projectile.tick();

            if (projectile.collided){ // if projectile hits any wall 
                removeProj = projectile;
            }
        }
        if (removeProj != null){ //if indexToRemove has been updated with a counter value
            this.projObjs.remove(removeProj);
            
        }
    }

    public void setCooldown(double cooldown){
        this.cooldown = cooldown;
    }
}
