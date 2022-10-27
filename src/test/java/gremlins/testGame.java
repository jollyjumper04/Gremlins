package gremlins;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class testGame {

    App app;
    Map map;

    File testfile1 = new File("src/test/resources/testfile1.txt");
    File testfile2 = new File("src/test/resources/testfile2.txt");

    public void dummyAppSetup(){ //setting all PImages, Paths and Jsonarrays to null and initalting other values...
        app.timer = 0;
        app.freezeDuration = 10; 
        app.coolDownPercent = 0;
        app.configPath = null;
        app.stoneWall = null;
        app.brickWall = null;
        app.brickWall0 = null;
        app.brickWall1= null;
        app.brickWall2= null;
        app.brickWall3= null;
        app.door= null;
        app.wizard0= null;
        app.wizard1= null;
        app.wizard2= null;
        app.wizard3= null;
        app.gremlin= null;
        app.fireBall= null;
        app.slime= null;
        app.wizardState= null;
        app.freeze= null;
        app.portalA= null;
        app.portalB= null;
        app.blank= null;
        app.winScreen= null;
        app.loseScreen= null;
        app.level = 0;
        app.levels = null;
        app.totalLives = 3;
        app.lives = 3;
        app.layout = "";
        app.layoutFile = null;
        app.wizardCooldown = 0.3333;
        app.enemyCooldown = 3.0;
        app.map = null;
        app.wizard = null;
        app.prevFrame = 0;
        app.font = null;
        app.wizardLives = null;
        app.fireballIcon = null;
        app.freezeIcon = null;
        app.winIcon = null;
        app.loseIcon = null;
    }

    public void tickApp(int timeForTick){ //ticks dummyApp (running app without drawing) for a set amount of time
        int count = 0; 
        while (count < timeForTick){
            app.tick();
            count++;
        }
    }

    @BeforeEach
    public void setup(){ //intiate new app and map for every test
        app = new App();
        map = new Map(app);
    }

    public void initMap(File testfile){

        //setting all PImages to null and other values to not null
        dummyAppSetup();
        app.layoutFile = testfile;
        app.map = map;
        map.initMap();
    }
    
    @Test
    public void testSetup(){ //testing if setup() is being run properly
        map.setup();
        assertTrue(map.mapGrid instanceof Collidables[][]);
        assertTrue(map.mapObjs instanceof ArrayList);
        assertTrue(map.gremlinObjs instanceof ArrayList);
        assertTrue(map.brickWallObjs instanceof ArrayList);
    }

    @Test
    public void testReadLayout(){ //testing different cases where reading file layout is valid/invalid
        
        //if file is readable
        map.readLayout(testfile1);
        assertTrue(map.readLayout(testfile1) instanceof Character[]);
        assertNotNull(map.readLayout(testfile1));

        //if file is not readable (doesnt exist)
        File file2 = new File("src/test/resources/notafile.txt");
        app.layoutFile = file2;
        map.readLayout(file2);
        assertNull(map.readLayout(file2));

        //if file format is wrong, has invalid X (amount of characters)
        File file3 = new File("src/test/resources/invalidTestFile1.txt");
        app.layoutFile = file3;
        map.readLayout(file3);
        assertNull(map.readLayout(file3));

        //if file format is wrong, has invalid Y (amount of Lines)
        File file4 = new File("src/test/resources/invalidTestFile2.txt");
        app.layoutFile = file4;
        map.readLayout(file4);
        assertNull(map.readLayout(file4));
    }

    @Test
    public void testToCharacterArray(){

        //valid character array 
        String out = "XXXXXXXXXX"; //10 Char
        map.toCharacterArray(out);
        assertNotNull(map.toCharacterArray(out));
        assertEquals(map.toCharacterArray(out).length, 10);

        //invalid character array
        out = null;
        assertNull(map.toCharacterArray(out));
    }

    @Test
    public void testInitMap(){
        initMap(testfile1);
        assertTrue(map.initMap);
        initMap(testfile2);
        assertTrue(map.initMap);
    }

    @Test
    public void wizardDestroysWall(){
        initMap(testfile1);
        app.loadLevel();

        //System.out.println(app.wizard.x);
        app.wizard.pressSpace(true);
        tickApp(250);
        app.wizard.pressSpace(false);
        assertNull(app.map.mapGrid[app.map.wizSpawnX+3][app.map.wizSpawnY]); //check if brickWall obj actually set to null in mapGrid
    }

    @Test
    public void wizardHitsGremlinWithFireball(){
        initMap(testfile1);
        app.enemyCooldown = 1000; //long cooldown so gremlin wont cancel wizards fireball or hit the wizard
        app.loadLevel();

        app.wizard.setPos(app.map.wizSpawnX, app.map.wizSpawnY + 1); //this gremlin would be in index 0 as it is added first to the gremlin list (being read first on the map)
        Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);
        int gremOldX = gremObj.x;
        int gremOldY = gremObj.y;

        app.wizard.curDir = 4;
        app.wizard.fire(true);

        tickApp(1000);

        assertTrue((gremOldX != gremObj.x)||(gremOldY != gremObj.y)); //check if it has been moved from the position it was hit, 
        //either x or y or both are different

    }

    @Test
    public void wizardGetsHitByGremlinSlimeball(){
        initMap(testfile1);
        app.enemyCooldown = 0.3333; //to set a quick cooldown
        app.loadLevel();
        int prevLives = app.lives;

        app.wizard.setPos(app.map.wizSpawnX+7, app.map.wizSpawnY+1); //spawns a distance from where the gremlin is facing
        tickApp(100);
        assertEquals(app.lives, prevLives-1);

    }

    @Test
    public void wizardGremlinProjectilesHit(){
        initMap(testfile1);
        app.wizardCooldown = 1; //same cooldown, both projectiles will be fired at eachother at the same time
        app.enemyCooldown = 1;

        app.loadLevel();

        tickApp(100);
        Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);

        app.wizard.setPos(app.map.wizSpawnX+7, app.map.wizSpawnY+1);
        app.wizard.curDir = 4;
        app.wizard.fire(true);
        tickApp(5);
        //System.out.println((gremObj.getProjObjs().size()));
        assertEquals(gremObj.getProjObjs().size(), 0);
        assertEquals(app.wizard.getProjObjs().size(), 0);
    }

    

    @Test
    public void wizardTouchesGremlin() throws InterruptedException{
        initMap(testfile1);
        app.enemyCooldown = 100;
        app.loadLevel();

        tickApp(100);
        Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);
        app.wizard.setPos(gremObj.x-1, gremObj.y);
        tickApp(100);
        assertEquals(app.lives, app.totalLives-1);
    }

    @Test
    public void wizardLooseGame() throws InterruptedException{
        initMap(testfile1);
        app.loadLevel();

        while (!(app.map.gameOver)){
            app.wizard.getsHurt();
            tickApp(1);
        }

        assertFalse(app.won);
        assertTrue(app.map.gameOver);
    }


    @Test
    public void wizardExits(){
        initMap(testfile1);
        app.loadLevel();

        app.wizard.setPos(app.map.exit.x, app.map.exit.y);
        
        app.map.tick();
        assertTrue(map.getWizardExit());
    }

    @Test
    public void wizardActivatesFreeze(){
        initMap(testfile1);
        app.loadLevel();
        app.map.freeze.freezeTime = 1; //testing for 1 second (default value is 5 seconds)
        app.wizard.setPos(app.map.freeze.x, app.map.freeze.y);
        tickApp(1);
        assertTrue(app.map.freeze.active);
        tickApp(1000);

        assertFalse(app.map.freeze.active); //reset after freeze duration finishes

        //activating freeze again...
        app.map.freeze.freezeTime = 5; //testing for 5 seconds
        app.wizard.setPos(app.map.freeze.x, app.map.freeze.y); //
        tickApp(5);
        assertTrue(app.map.freeze.active);
        tickApp(1000);

        assertFalse(app.map.freeze.active); //reset after freeze duration finishes
    }

    @Test
    public void wizardGoesThroughPortal(){
        initMap(testfile1);
        app.loadLevel();
        tickApp(1);

        int prevPortX;
        int prevPortY;

        //going through portalA

        prevPortX = app.map.portalB.x;
        prevPortY = app.map.portalB.y;
        //System.out.println(prevPortX);
        app.wizard.setPos(app.map.portalA.x, app.map.portalA.y);
        tickApp(1);
        assertEquals(prevPortX,app.wizard.x);
        assertEquals(prevPortY,app.wizard.y);
        tickApp(1);

        //going through portalB

        prevPortX = app.map.portalA.x;
        prevPortY = app.map.portalA.y;
        //System.out.println(prevPortX);
        app.wizard.setPos(app.map.portalB.x, app.map.portalB.y);
        tickApp(1);
        assertEquals(prevPortX,app.wizard.x);
        assertEquals(prevPortY,app.wizard.y);
        tickApp(1);
    }

    @Test
    public void testRandomSpawnPos(){ //only valid respawn will be at (1,1)
        initMap(testfile2);
        app.loadLevel();

        Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);
        while ((gremObj.x != 1)&&(gremObj.y != 1)){
            gremObj.randomSpawnPos(10);
            tickApp(1);
        }

        //System.out.println(gremObj.x);
        //System.out.println(gremObj.y);
        assertEquals(1, gremObj.x);
        assertEquals(1, gremObj.y);

    }

    
}
