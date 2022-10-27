package gremlins;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

//for Map constructor (PApplet objects)
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
//import java.util.Random;

import java.io.*;

public class App extends PApplet {

    /**
    * The App class is responsible for displaying the sketches 
    */

    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_BLUE = "\u001B[34m";


    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int FPS = 60;

    //public static final Random randomGenerator = new Random();

    public int timer;
    public String configPath;

    public PImage stoneWall;
    public PImage brickWall;
    public PImage brickWall0;
    public PImage brickWall1;
    public PImage brickWall2;
    public PImage brickWall3;
    public PImage door;

    public PImage wizard0;
    public PImage wizard1;
    public PImage wizard2;
    public PImage wizard3;
    
    public PImage gremlin;

    public PImage fireBall;
    public PImage slime;

    public PImage wizardState;

    public PImage freeze;
    public PImage portalA;
    public PImage portalB;

    public PImage blank;

    public PImage winScreen;
    public PImage loseScreen;


    public int level = 0;
    public JSONArray levels;
    public Integer totalLives;
    public Integer lives;
    public String layout;
    public File layoutFile;
    public double wizardCooldown;
    public double enemyCooldown;

    public Map map;
    public Wizard wizard;
    
    protected int wizardSpeed = 2;
    protected int gremlinSpeed = 1;

    protected boolean won = false;
    protected boolean restart = false;
    protected boolean cooldown = false;

    public float freezeDuration; //progress of freeze
    public float coolDownPercent;
    protected int prevFrame;

    public PFont font;
    public Icons[] wizardLives;
    public Icons fireballIcon;
    public Icons freezeIcon;
    public Icons winIcon;
    public Icons loseIcon;

    public App() {
        this.configPath = "config.json";
    }


    /**
     * Initialise the setting of the window size.
    */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Initialises config file to have its configeration be fetched/read then calls {@link #loadConfig() loadConfig()}. 
     * After calls
    */
    @Override
    public void setup() {
        frameRate(FPS);

        JSONObject conf = loadJSONObject(new File(this.configPath));
        totalLives = conf.getInt("lives");
        lives = totalLives;
        levels = conf.getJSONArray("levels");// Levels array
        
        loadConfig();

        // Load images during setup
        
        loadImages();

        this.map = new Map(this);

        loadLevel();
        
        //amount of lives remaining icons [not hard coded ;)]
        this.wizardLives = new Icons[totalLives];
        int spacing = 80;

        for (int i=0; i < lives ;i++){
            Icons wizardIcon = new Icons(this,spacing,680);
            wizardIcon.setSprite(wizard0);
            wizardLives[i] = wizardIcon;
            spacing += 20;
        }

        fireballIcon = new Icons(this,570,670);
        fireballIcon.setSprite(fireBall);
        freezeIcon = new Icons(this,570,695);
        freezeIcon.setSprite(freeze);

        winIcon = new Icons(this,150,320);
        winIcon.setSprite(winScreen);
        loseIcon = new Icons(this,255,320);
        loseIcon.setSprite(loseScreen);

        font = createFont("LiHei Pro", 20);
        textFont(font);
    }

    
    /**
     * Load all resources/images. 
    */
    public void loadImages() {
        try{

            this.brickWall = loadImage(URLDecoder.decode(this.getClass().getResource("brickwall.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.brickWall0 = loadImage(URLDecoder.decode(this.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.brickWall1 = loadImage(URLDecoder.decode(this.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.brickWall2 = loadImage(URLDecoder.decode(this.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.brickWall3 = loadImage(URLDecoder.decode(this.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.stoneWall = loadImage(URLDecoder.decode(this.getClass().getResource("stonewall.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.door = loadImage(URLDecoder.decode(this.getClass().getResource("door.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));

            this.wizard0 = loadImage(URLDecoder.decode(this.getClass().getResource("wizard0.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.wizard1 = loadImage(URLDecoder.decode(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.wizard2 = loadImage(URLDecoder.decode(this.getClass().getResource("wizard2.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.wizard3 = loadImage(URLDecoder.decode(this.getClass().getResource("wizard3.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));

            this.fireBall = loadImage(URLDecoder.decode(this.getClass().getResource("fireball.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.slime = loadImage(URLDecoder.decode(this.getClass().getResource("slime.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            
            this.gremlin = loadImage(URLDecoder.decode(this.getClass().getResource("gremlin.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            
            this.fireBall = loadImage(URLDecoder.decode(this.getClass().getResource("fireball.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));

            this.freeze = loadImage(URLDecoder.decode(this.getClass().getResource("freeze.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.portalA = loadImage(URLDecoder.decode(this.getClass().getResource("portalA.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.portalB = loadImage(URLDecoder.decode(this.getClass().getResource("portalB.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));

            this.blank = loadImage(URLDecoder.decode(this.getClass().getResource("blank.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.loseScreen = loadImage(URLDecoder.decode(this.getClass().getResource("loseScreen.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));
            this.winScreen = loadImage(URLDecoder.decode(this.getClass().getResource("winScreen.png").getPath().replace("%20", " "), StandardCharsets.UTF_8.toString()));

        }
        catch(Exception e) {
            System.out.println("Error in reading items in resources");
            System.exit(1);
        }
    }

    /**
     * Load information from config.json file and saves it to the apps public variables
    */

    public void loadConfig(){
        // level set to 0 by default
        //lives = totalLives;
        JSONObject current = levels.getJSONObject(level);
        this.layout = (String) current.get("layout");
        layoutFile = new File(this.layout);
        System.out.println(layoutFile.isFile());
        System.out.println(layoutFile);
        this.wizardCooldown = (Double) current.get("wizard_cooldown");
        this.enemyCooldown = (Double) current.get("enemy_cooldown");
        System.out.println(wizardCooldown);
        System.out.println(enemyCooldown);

    }

    /**
     * calls the {@link Map#initMap() initMap()} method and
     * creates new wizard object with the {@link Wizard#Wizard(App,int,int,int) wizards constructor} and stores it to apps public variable
     * sets wizards fireball cooldown value (from config.json), and its first sprite image from its initial state
    */
    public void loadLevel(){
        
        this.map.initMap();
        this.wizard = new Wizard(this, wizardSpeed, this.map.wizSpawnX*20, this.map.wizSpawnY*20);
        this.wizardState = wizard3;
        this.wizard.setSprite(this.wizardState);
        this.wizard.setCooldown(wizardCooldown);

    }

    /**
     * Receive key pressed signal from the keyboard.
     * 
     * keycodes:
     *      Left: 37
     *      Up: 38
     *      Right: 39
     *      Down: 40
     * 
     * If any directional key is pressed ie: (Up,Down,Left,Right)...
     *      the wizard objects direction is reset and set to the most recent key press and
     *      the image and current direction it is facing is also updated accordingly
     * 
     * If the spacebar key is pressed, the spacebar state is set to true
    */

    @Override
    public void keyPressed() {


        if (this.keyCode == 38) {
            this.wizard.resetDir();
            this.wizard.pressUp(true);
            this.wizardState = wizard2;
            this.wizard.curDir = 1;

        }else if (this.keyCode == 40) {
            this.wizard.resetDir();
            this.wizard.pressDown(true);
            this.wizardState = wizard3;
            this.wizard.curDir = 2; 

        }else if (this.keyCode == 37) {
            this.wizard.resetDir();
            this.wizard.pressLeft(true);
            this.wizardState = wizard0;
            this.wizard.curDir = 3;

        }else if (this.keyCode == 39) {
            this.wizard.resetDir();
            this.wizard.pressRight(true);
            this.wizardState = wizard1;
            this.wizard.curDir = 4;

        }else if (this.keyCode == 32) {
            this.wizard.pressSpace(true);
        }

        this.wizard.setSprite(this.wizardState);
    }

    
    /**
     * Receive key released signal from the keyboard.
     * Anytime a key is released the state of the corrosponding key is set to false
    */
    
    @Override
    public void keyReleased(){

        if (this.keyCode == 38) {
            this.wizard.pressUp(false);
            //this.wizardState = wizard2;
        }else if (this.keyCode == 40) {
            this.wizard.pressDown(false);
            //this.wizardState = wizard3;
        }else if (this.keyCode == 37) {
            this.wizard.pressLeft(false);
            //this.wizardState = wizard0;
        }else if (this.keyCode == 39) {
            this.wizard.pressRight(false);
            //this.wizardState = wizard1;
        }else if (this.keyCode == 32) {
            this.wizard.pressSpace(false);
        }
    }

    /**
     * Displays the amount of lives remaining with wizard images for the player according to the 
     * value saved in the variable lives (which is set by totalLives which is set by the config.json file "lives" variable)
    */

    public void displayLives(){
        for (int i=0; i< lives ;i++){
            wizardLives[i].draw(this);
        }
        fill(255,255,255);
        text("Lives:", 20, 695);

    }

    /**
     * Displays the current level the player is on according to the
     * value saved in the variable level (which is set by the lenth of config.json file's array)
    */

    public void displayLevel(){
        fill(255,255,255);
        text("Level "+(level+1)+"/2", 180, 695);

    }

    /**
     * Displays the wizards fireball cooldown. The logic works by checking if the wizard timer has been reset to 0
     * from the wizards {@link Wizard#checkFire() shoot method} if it is less or equal to the time this means a fireball has been shot
     * and every increment of the timer until it is greater than the cooldown time will be in the cooldown period
     * this is then turned to a percentage of the maximum width of the slider, starting from 0 to 100
    */

    public void displayCooldown(){
        fireballIcon.draw(this);
        strokeWeight(2);
        fill(255,255,255);
        rect(600, 675, 100, 10);

        if (wizard.getTimer() <= Math.round(wizardCooldown*App.FPS)){
            coolDownPercent = (int) ((wizard.getTimer() * 100.0f) / Math.round(wizardCooldown*App.FPS));
        }
        if (coolDownPercent != 100){ //max width of the box is 100
            fill(102, 204, 0);
            rect(600, 675, coolDownPercent, 10);
        }
    }

    /**
     * Displays the freeze powerup cooldown. The logic works the same as the {@link #displayCooldown() displayCooldown()} above
     * but instead using the freeze objects timer which only gets triggered when the wizard interacts/collides with it
    */

    public void displayFreezeCooldown(){
        freezeIcon.draw(this);
        strokeWeight(2);
        fill(255,255,255);
        rect(600, 700, 100, 10);
        if (map.freeze.getTimer() <= Math.round(map.freeze.freezeTime*App.FPS)){
            freezeDuration = (int) ((map.freeze.getTimer() * 100.0f) / Math.round(map.freeze.freezeTime*App.FPS));
        }
        if (freezeDuration != 100){ //max width of the box is 100

            fill(66, 111, 245);
            rect(600, 700, freezeDuration, 10);
        }
    }

    /**
     * When called sets restart flag to true , saves the previous frame number and awaits for a key input 
     * (only accepting it after a one second delay to prevent accidental instant restart from the lose/win screen)
     * After key input, resets restart flag, map.gameOver flag, sets level to 0 and calls the apps
     * {@link #loadLevel() loadLevel()} method.
    */

    /**
     * If wizard instance is in the positon of exit object 
     * If true increment level and check if level is the last level. If it is, app instances won and maps gameOver flag is set to true.
     * If it is not then load the next level and set the position of the wizard to the new spawn point of the next map.
    */
    public void checkWizardExit(){ 
        if (map.getWizardExit()){
            level++;
            if (level == levels.size()){
                won = true;
                map.gameOver = true;
            } else {
                map.initMap = false;
                loadNextLevel(); //loads next level
                this.wizard.setPos(map.wizSpawnX, map.wizSpawnY);
            }
        }
    }

    public void restart(){
        if (!(restart)){
            prevFrame = frameCount;
            restart = true;
        }
        if (this.keyPressed){
            if (prevFrame + 1*FPS < frameCount){ //after 1 second delay button press will trigger restart
                restart = false;
                map.gameOver = false;
                lives = totalLives;
                level = 0;
                this.loadNextLevel();
            }
        }
    }

    /*
     * for loading next level from config file. Calls {@link #loadConfig() loadConfig()} and {@link #loadLevel() loadLevel()}
     */

    public void loadNextLevel() {
        loadConfig();
        loadLevel();
    }
    /*
    * <li>{@link Map#tick() map.tick()} updates map elements
    * <li>{@link Wizard#tick() wizard.tick()} updates the player (wizard)
    * <li>{@link #checkWizardExit() checkWizardExit()}
    */
    public void tick(){
        this.map.tick(); //for map logics and gremlin drawings
        this.wizard.tick();
        this.checkWizardExit();
    }

    /**
     * Draw all elements in the game by current frame. (called every frame)
     * <ul>
     * <li>While the game has not ended, call:
     * <ul>
     * <li>{@link #displayCooldown() displayCooldown()}
     * <li>{@link #displayFreezeCooldown() displayFreezeCooldown()}
     * <li>{@link #displayLives() displayLives()}
     * <li>{@link #displayLevel() displayLevel()}
     * 
     * <li>{@link #tick() map.tick()}
     * 
     * <li>{@link Map#draw() map.draw()} draws map elements onto screen
     * <li>{@link Icons#draw(PApplet) wizard.draw()} draws the player (wizard) onto screen 
     * </ul>
     * 
     * <li>If the game has ended... i.e: either player has won or has lost
     * 
     * <li>If the player has won, display "YOU WIN" text and winning screen picture 
     * <li>If the player has not won, display "YOU LOOSE :( " text and loss screen picture
     * </ul>
     * Then the {@link #restart() restart()} method is called.
     * 
     * 
     * 
     * 
     * 
	 */

    @Override
    public void draw() {
        background(191, 153, 114);
        timer++;
        
        if (!(map.gameOver)){
            displayCooldown();
            displayFreezeCooldown();
            displayLives();
            displayLevel();

            this.tick();

            
            this.map.draw();
            this.wizard.draw(this);

        } else {
            if (won){
                text("YOU WIN! ", 290, 320);
                winIcon.draw(this);
            } else {
                text("YOU LOOSE :( ", 290, 320);
                loseIcon.draw(this);
            }
            restart();
        }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");

    }
}
