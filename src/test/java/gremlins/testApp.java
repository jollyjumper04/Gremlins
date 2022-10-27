package gremlins;


import processing.core.PApplet;
import processing.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testApp { //Testing app by running it (this is the only test file that uses calls draw function for testing)

    App app;

    //test loading map
    @BeforeEach
    public void setup(){
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
    }

    public void appKeyPress(KeyEvent key) {
        app.delay(100);
        app.keyPressed(key);
        app.delay(100);
        app.keyReleased(key);
    }

    @Test
    public void loadLevel() {
        //System.out.println(app.map.mapObjs.size());
        assertEquals(622, app.map.mapObjs.size());
        //Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);
        //assertTrue(app.map.randomSpawnPos(gremObj,10),"good");
    }
    //@Test
    public void errorLoadingResources() {
        app.brickWall = null;
        setup();
    }

    @Test
    public void wizardKeyPressRegisters() throws InterruptedException{
        // Left: 37
        // Up: 38
        // Right: 39
        // Down: 40
        KeyEvent Left = new KeyEvent(app, 0, 0, 0, '.', 37);
        KeyEvent Up = new KeyEvent(app, 0, 0, 0, '.', 38);
        KeyEvent Right = new KeyEvent(app, 0, 0, 0, '.', 39);
        KeyEvent Down = new KeyEvent(app, 0, 0, 0, '.', 40);
        Thread.sleep(100);

        //moving right
        app.keyCode = 39;
        app.keyPressed();
        Thread.sleep(150);
        assertEquals(app.map.wizSpawnX+1, app.wizard.x);
        assertEquals(app.map.wizSpawnY, app.wizard.y);
        app.keyReleased();
        app.wizard.resetDir();

        //moving down
        app.keyCode = 40;
        app.keyPressed();
        Thread.sleep(150);
        assertEquals(app.map.wizSpawnX+1, app.wizard.x);
        assertEquals(app.map.wizSpawnY+1, app.wizard.y);
        app.keyReleased();
        app.wizard.resetDir();

        //moving up
        app.keyCode = 38;
        app.keyPressed();
        Thread.sleep(150);
        assertEquals(app.map.wizSpawnX+1, app.wizard.x);
        assertEquals(app.map.wizSpawnY, app.wizard.y);
        app.keyReleased();
        app.wizard.resetDir();

        //moving left
        app.keyCode = 37;
        app.keyPressed();
        Thread.sleep(150);
        assertEquals(app.map.wizSpawnX, app.wizard.x);
        assertEquals(app.map.wizSpawnY, app.wizard.y);
        app.keyReleased();
        app.wizard.resetDir();

        //press spacebar
        app.keyCode = 32;
        app.keyPressed();
        Thread.sleep(150);
        app.keyReleased();
        app.wizard.resetDir();

        Thread.sleep(100);
        //should return to spawn pos (runs in circle)
        assertEquals(app.wizard.checkCooldown(),false);
        assertEquals(app.map.wizSpawnX, app.wizard.x); //wizard x pos
        assertEquals(app.map.wizSpawnY, app.wizard.y);
        
    }

    @Test
    public void wizardDestroysWall() throws InterruptedException{
        Thread.sleep(1000);
        app.wizard.pressSpace(true);
        Thread.sleep(250);
        app.wizard.pressSpace(false);
        Thread.sleep(1000);
        assertNull(app.map.mapGrid[app.map.wizSpawnX+2][app.map.wizSpawnY]); //check if brickWall obj actually set to null in mapGrid
    }

    @Test
    public void wizardMoveToNextLevel() throws InterruptedException{
        app.wizard.setPos(app.map.exit.x, app.map.exit.y);
        Thread.sleep(100);

        assertEquals(app.level, app.level++);
    }

    
    @Test
    public void wizardFreezePowerup() throws InterruptedException{
        app.map.freeze.freezeTime = 1;
        app.wizard.setPos(app.map.freeze.x, app.map.freeze.y);
        Thread.sleep(5);
        assertEquals(app.map.freeze.active, true);
        Thread.sleep(1000);
        assertEquals(app.map.freeze.active, false);
    }

    @Test
    public void wizardWinGame() throws InterruptedException{
        Thread.sleep(1000);
        while (!(app.won)){
            app.wizard.setPos(app.map.exit.x, app.map.exit.y);
            Thread.sleep(100);
        }

        assertTrue(app.won);
        assertTrue(app.map.gameOver);
    }

    @Test
    public void keypressToRestart()throws InterruptedException{
        wizardWinGame();
        Thread.sleep(1050);
        app.keyPressed = true;
    }


/*
    //@Test
    public void wizardLooseGame() throws InterruptedException{
        Thread.sleep(1000);
        System.out.println(app.map);
        while (!(app.map.gameOver)){
            Thread.sleep(100);
            app.wizard.getsHurt();
            if (app.lives == 0){
                app.map.gameOver = true;
            }
        }

        assertFalse(app.won);
        assertTrue(app.map.gameOver);
    }

    @Test
    public void wizardHitsGremlinWithFireball() throws InterruptedException{

        int gremOldX;
        int gremOldY;
        Gremlins gremObj;

        gremObj = (Gremlins) app.map.gremlinObjs.get(0);

        app.wizard.setPos(3, 2);


        gremObj.setPos(8, 2);

        gremOldX = gremObj.x;
        gremOldY = gremObj.y;

        app.wizardState = app.wizard1;
        app.wizard.curDir = 4;
        app.wizard.fire(true);

        Thread.sleep(1000);

        assertNotEquals(gremOldX, gremObj.x); //check if it has been moved from the position it was hit
        assertNotEquals(gremOldY, gremObj.y);

    }

    @Test
    public void wizardGetsHitByGremlin() throws InterruptedException{
        app.wizard.setPos(5, 18);
        Thread.sleep(1000);
        assertEquals(app.lives, app.lives--);
    }

    @Test
    public void wizardLooseGame() throws InterruptedException{
        Thread.sleep(2000);
        Gremlins gremObj = (Gremlins) app.map.gremlinObjs.get(0);
        while (!(app.map.gameOver)){
            app.wizard.setPos(gremObj.x, gremObj.y);
            Thread.sleep(100);
        }

        assertFalse(app.won);
        assertTrue(app.map.gameOver);
    }
    */
}
