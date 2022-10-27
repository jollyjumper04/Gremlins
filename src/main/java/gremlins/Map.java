package gremlins;
import java.util.*;

import javax.sound.sampled.Port;

import org.checkerframework.checker.units.qual.Current;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import processing.core.PImage;

public class Map {

    protected App app; 

    protected int wizSpawnX;
    protected int wizSpawnY;
    protected boolean gameOver = false;
    protected Freeze freeze;
    protected Collidables[][] mapGrid;
    protected ArrayList<Collidables> gremlinObjs;

    protected boolean freezeGremlins = false;
    protected Door exit;
    protected Portal portalA;
    protected Portal portalB;
    protected ArrayList<Collidables> mapObjs; //for all non movable interactable things on the map, ie: brickwalls and exit door
    protected ArrayList<BrickWall> brickWallObjs; //add brickwalls to be destroyed into this array
    protected PImage[] brickWallAnimation;
    protected Boolean initMap = false;
    protected BrickWall wallToDestroy;

    /**
     * Constructor for Map
     * @param app instance
     * initialises an array with the 4 possible images for the brickwall being destroyed animation
    */

    public Map(App app){

        this.app = app;
        brickWallAnimation = new PImage[] {app.brickWall0, app.brickWall1, app.brickWall2, app.brickWall3};
    }

    /**
     * Setup:
     * <ul>
     * <li> initalises map grid (2d array) of 36 by 33 elements... <ul>
     * <li> map is divided to into a grid of elements with each position taking up 20x20 pixels
     * <li> it stores all the wall, door and powerups objects (all of which are elements that can be interactable) 
     * <li> map grid does not inlcuding the wizard and gremlins objects (and their subsequent fireball and slime objects)
     * <li> entries with Null entries are empty spaces on the map where the gremlins/wizard objects (and their subsequent fireball and slime objects) can move to
     * </ul>
     * 
     * <li> initalises an arraylist of all interactable objects in the map grid... <ul>
     * <li> so it can be looped through to update and draw each object while the game runs
     * </ul>
     * 
     * <li> initalises an arraylist of all the gremlin objects.. <ul>
     * <li> so it can be looped through to update and draw each gremlin object while the game runs
     * </ul>
     * 
     * <li> initalises an arraylist of all the brickwall objects that need to be destroyed.. <ul>
     * <li> so it can be looped through to alow the destroyed brickwall objects to be animated and then removed from the mapObj arraylist
     * </ul>
     * 
     * </ul>
    */

    public void setup(){
        this.mapGrid = new Collidables[36][33];
        this.mapObjs = new ArrayList<Collidables>(); //for all non movable interactable things on the map, ie: brickwalls and exit door
        this.gremlinObjs = new ArrayList<Collidables>();
        this.brickWallObjs = new ArrayList<BrickWall>(); //add brickwalls to be destroyed into this array
    }


    /**
     * reads layout of level file and appends it it to string variable out (which contains all the characters it has read)
     * @param file (File) app.layoutFile passed into argument 
     * @return {@link #toCharacterArray(String out) toCharacterArray(String out)} with the string out as its parameter or null if the file cannot be read (when app.layoutFile is invalid).
     * 
    */

    public Character[] readLayout(File layoutFile){

        BufferedReader reader;
        File file = layoutFile;
 
        try {
            reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
            String out = "";
            int y = 0;

			while (line != null) {
                if (line.length() != 36){
                    //System.out.println("invlaid file (to many x)");
                    return null;
                }

                out = out+line;
				// read next line
				line = reader.readLine();
                y++;
			}
            if (y != 33){
                //System.out.println("invlaid file (to many y)");
                return null;
            }
			reader.close();

            //System.out.println(out);
            return toCharacterArray(out);
            
        } catch (IOException e) {
            //System.out.println("layout file doesnt exist... exiting program");
            System.setErr(null); //hides long ass error message when file cannot be read, just need to know it returns null if file is invalid
            return null;
        }

    }
    
    /**
     * takes string out and returns a character array
     * @param out (string)
     * @return array (char array)
    */

    public Character[] toCharacterArray(String out) {

        if ( out == null ) {
            return null;
        } 
     
        int len = out.length();
        Character[] array = new Character[len];
        for (int i = 0; i < len ; i++) {
           array[i] = out.charAt(i);
        }
     
        return array;
     }
     
    /**
     * calls {@link #setup() setup()} and reads char list from {@link #readLayout(File layoutFile) readLayout(File layoutFile)} with 
     * layoutFile being the current level file in app.layoutFile. It then maps each position of the characters read to its equivalent object 
     * for each object made it...
     * <ul>
     * <li> creates obj with its constructor, and sets the obj sprite images (if the current char is not blank)
     * <li> adds objects to mabjObj arraylist (unless it is either a gremlin or wizard, which in this case stores and initlises each spawn position inside their object)
     * <li> if the char is blank sets the mapGrid index it is on to null, or if it is not blank sets it to the corrosponding object position
     * </ul>
     * 
     * After initlaising the arrays, initMap flag is set to true
     * 
    */

    public void initMap(){

        this.setup();

        Character id;

        List charOut = Arrays.asList(readLayout(app.layoutFile));

        //System.out.println(charOut.size()); should be 1188
        int i;
        int j;

        boolean pad = false;

        int counter = 0;
        for(int y = 0; y < 33; y++){
            j = y*20;
            for(int x = 0; x < 36; x++){
                i = x*20;
                
                id = (Character) charOut.get(counter);
                //System.out.print(id);
    
                //Object obj;
    
                if (id == 'X'){ //stonewall
                    Wall obj = new StoneWall (app, i, j);
                    mapObjs.add(obj);
                    obj.setSprite(app.stoneWall);
                    mapGrid[x][y] = obj;
                }
                else if (id == 'B'){ //brickwall
                    Wall obj = new BrickWall (app, i, j);
                    mapObjs.add(obj);
                    obj.setSprite(app.brickWall);
                    mapGrid[x][y] = obj;
                }
                else if (id == 'E'){
                    Door obj = new Door (app, i, j);
                    this.exit = obj;
                    mapObjs.add(obj);
                    obj.setSprite(app.door);

                    mapGrid[x][y] = obj;
                }
                else if (id == 'W'){
                    this.wizSpawnX = x;
                    this.wizSpawnY = y;
                    mapGrid[x][y] = null;
                }
                else if (id == 'G'){ 
                    Gremlins obj = new Gremlins(app, app.gremlinSpeed, i, j);
                    obj.setSprite(app.gremlin);
                    obj.setCooldown(app.enemyCooldown);
                    gremlinObjs.add(obj);
                    mapGrid[x][y] = null;
                }
                else if (id == 'F'){ 
                    Freeze obj = new Freeze(app, i, j);
                    obj.setSprite(app.freeze);
                    freeze = obj;
                    mapObjs.add(obj);
                    mapGrid[x][y] = null;
                }

                else if (id == 'P'){ 
                    Portal obj = new Portal(app, i, j);
                    if (!pad){
                        obj.setSprite(app.portalA);
                        portalA = obj;
                        pad = true;
                    } else {
                        obj.setSprite(app.portalB);
                        portalB = obj;
                    }
                    mapObjs.add(obj);
                    mapGrid[x][y] = null;
                }
                //System.out.print(id);
                //counter = (counter + 1 ) % charOut.size();
                counter++;
            }
        }

        //System.out.println(counter);
        //System.out.println(mapGrid[4][1].getClass().getName());
        //System.out.println(mapObjs.size());
        initMap = true;
    }

    /**
     * Getter for returing object in a relative x and y position on the grid
     * @param x (int)
     * @param y (int)
     * @return object in mapGrid[x][y]
    */
    public Collidables objAtMapPos(int x, int y){
        return mapGrid[x][y];
    }
    
    /**
     * draws/calls each {@link #draw() draw(PApplet app)} draw function of each obj in the objArray parameter
     * @param objArray, ArrayList (Collidables)
    */

    public void drawObjs(ArrayList<Collidables> objArray){
        for (int counter = 0; counter < objArray.size(); counter++) {
            objArray.get(counter).draw(app);
            }
    }


    /**
     * checks if the freeze powerup on the map has been activated and if it has not then it updates 
     * each gremlins {@link Gremlins#tick() tick()}, allowing their positions to be updated
     * <ul> 
     * <li> Uses a type cast from Collidable obj in arraylist (parent obj) to a Gremlin obj becuase it allows 
     * the {@link #drawObjs(ArrayList objArray) drawObjs(ArrayList objArray)} method to be used with multiple different objects ie: Gremlins and Projectiles
     * </ul>
    */

    public void updateGremlins(){
        if (!(freeze.active)){
            for (int counter = 0; counter < gremlinObjs.size(); counter++) { 
                Gremlins curGremObj = (Gremlins) gremlinObjs.get(counter); //sets current gremlinObj in array to gremlin
                //type cast from Collidable obj in arraylist (parent obj) to a Gremlin obj
                //reason to use type casting: want to use drawObjs methods with multiple different objects ie: Gremlins and Projectiles
    
                curGremObj.tick(); //gremlin movements
            }
        }
    }

    /**
     * calls {@link #drawObjs(ArrayList objArray) drawObjs(ArrayList objArray)} with gremlinObjs arraylist to draw all the gremlins on the map, then for each of the 
     * gremlinObjs on the maps (in gremlinObjs arryalist) it calls {@link #drawObjs(ArrayList objArray) drawObjs(ArrayList objArray)} again to draw each gremlins fired slime projectiles
    */

    public void drawGremlins(){
        drawObjs(gremlinObjs);
        for (int counter = 0; counter < gremlinObjs.size(); counter++) {
            Creatures gremlinObj = (Creatures) gremlinObjs.get(counter);
            drawObjs(gremlinObj.getProjObjs());
        }
    }

    /**
     * calls {@link #drawObjs(ArrayList objArray) drawObjs(ArrayList objArray)} to draw the wizard fired fireball projectiles
    */

    public void drawWizardFireballs(){
        drawObjs(app.wizard.getProjObjs());
    }

    /**
     * this method called by wizard obj (which manages each fireball obj fired)
     * @param x (int)
     * @param y (int)
     * <ul>
     * <li> sets current brickwall object to be destroyed to matching x and y params int mapGrid
     * <li> sets the obj in mapGrid to be null ie brickwall in that position doesnt exist anymore
     * <li> sets the brickwalls animation flag to be true 
     * <li> adds the brickwall object to the arraylist of brickwall objects to be destroyed
     * <li> {@link #updateBrickWalls() updateBrickWalls()} is called
     * </ul>
    */
    
    public void destroyBrickWall(int x, int y){ //called by wizard obj, which manages each fireball obj fired
        wallToDestroy = (BrickWall) mapGrid[x][y];
        mapGrid[x][y] = null;
        wallToDestroy.setAnimation(true);
        wallToDestroy.setPrevFrame(app.frameCount);
        brickWallObjs.add(wallToDestroy);
        updateBrickWalls();
    }

    /**
     * this method consistantly checks for walls that have been hit by the wizards fireball (animation flag is set true)
     * <ul>
     * <li> if the current obj it is checking (from brickWalObj arraylist) has the animation flag set to true...
     * <ul>
     * <li> sets the obj to the animation frame it is currently on
     * <li> animation frame is decided by counting the app frames, if 4 frames have passed it updates the animation frame to the next
     * <li> wall obj count is incremented and stored
     * </ul>
     * <li> If the wall objs count variable is equal to 4, the animation is over so it can be removed from the mapObjs arraylist (no longer needs to be updated and drawn)
     * </ul>
    */

    public void updateBrickWalls(){ //called in map.tick, consistantly checks for walls that have been hit by fireball (animation flag is set true)

        for (int counter = 0; counter < brickWallObjs.size(); counter++) {
            BrickWall currentWall = brickWallObjs.get(counter);
            if ((currentWall.getAnimation())) {
                currentWall.setSprite(brickWallAnimation[currentWall.getCount()]);
                //System.out.println(brickWallAnimation[count].getClass().getName())
                //System.out.print(count);
                if  (app.frameCount >= currentWall.getPrevFrame()+4) {
                    currentWall.incrementCount();; //currentWalls frame of its animation
                    currentWall.setPrevFrame(app.frameCount);
                }
            }
            if (currentWall.getCount() >= 4){
                mapObjs.remove(currentWall); //remove from being displayed
                brickWallObjs.remove(currentWall);  //removes from array of brickwalls that need the destroy animation be played
                currentWall.setAnimation(false);
            }
        }
        //System.out.println(brickWallObjs.size());
    }
    /**
     * Checks if wizard instance is in the positon of exit object...
     * @return boolean true - if reached the exit door, false - if not
    */
    public boolean getWizardExit(){ 
        if (app.wizard.inCurrentPos(exit)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if wizard instance is in the positon of the freeze object...
     * If true set the active flag of freeze boject to true
     * then calls {@link Freeze#tick() tick()} to update freeze machanics
    */
    public void checkFreezeActivate(){ 
        if (app.wizard.inCurrentPos(freeze)){
            freeze.active = true;
            }
        freeze.tick();
        }


    /**
     * Checks if wizard instance is in the positon of either portals...
     * If in position of portalA sets wizard position to portalB and 
     * randomly spawn both portals with {@link Portal#relocatePortal() relocatePortal()} and vice versa 
     * updates new locations of both portals to mapGrid[x][y]
    */
    public void checkWizardPortals(){
        if (app.wizard.inCurrentPos(portalA)){
            app.wizard.setPos(portalB.x, portalB.y);
            portalA.relocatePortal();
            mapGrid[portalA.x][portalA.y] = portalA;
            portalB.relocatePortal();
            mapGrid[portalB.x][portalB.y] = portalB;

        } else if (app.wizard.inCurrentPos(portalB)){
            app.wizard.setPos(portalA.x, portalA.y);
            portalA.relocatePortal();
            mapGrid[portalA.x][portalA.y] = portalA;
            portalB.relocatePortal();
            mapGrid[portalB.x][portalB.y] = portalB;
        }

    }

   /**
     * draw calls {@link #drawObjs(ArrayList objArray) drawObjs(ArrayList objArray)} and {@link #drawGremlins() drawGremlins()} and {@link #drawWizardFireballs() drawWizardFireballs()}
    */
    public void draw(){
        this.drawObjs(mapObjs);; //draws all map objs
        this.drawWizardFireballs();
        this.drawGremlins();
    }
    
    /**
     * tick() calls:
     * <ul>
     * <li>{@link #updateGremlins() updateGremlins()}
     * <li>{@link #updateBrickWalls() updateBrickWalls()}
     * <li>{@link #checkWizardExit() checkWizardExit()}
     * <li>{@link #checkFreezeActivate() checkFreezeActivate()}
     * <li>{@link #checkWizardPortals() checkWizardPortals()}
     * </ul>
    */
    public void tick(){
        this.updateGremlins();
        this.updateBrickWalls();
        this.checkFreezeActivate();
        this.checkWizardPortals();

    }

}
