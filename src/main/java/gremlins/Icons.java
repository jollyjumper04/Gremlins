package gremlins;
import processing.core.PImage;
import processing.core.PApplet;

class Icons {

    protected App app;

    protected int i; //raw x cordinates
    protected int j; //raw y cordinates
    
    protected PImage sprite;

    /**
    * Constructor for Icons class (parent of all visiable elements in the game) requires app instance, and absolute x and y pixel cordinates
    * @param app (App) app instance 
    * @param i (int) absolute X 
    * @param j (int) absolute Y
    * all collidables children objects inherit this constructor
    */


    public Icons (App app, int i, int j){
        this.app = app;
        this.i = i;
        this.j = j;
    }

    /**
     * Setter for the image (sprite) of the current object
     * @param sprite (PImage) sprite image
    */

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Draws the set sprite image to the absolute locations i and j with {@link #draw(PApplet app) draw(PApplet app)}
     * @param app (PApplet) instance of app
    */

    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates
        app.image(this.sprite, this.i, this.j);
    }
}
