package gremlins;

public class BrickWall extends Wall{
    
    private boolean animation = false;
    private int prevFrame;
    private int count = 0;

    
    /**
    * Constructor for BrickWall class, child of {@link Icons#Icons (App app, int i, int j) Icons (App app, int i, int j)}, 
    * requires app instance, and absolute x and y pixel cordinates
    * @param app (App)
    * @param i (int) X
    * @param j (int) Y
    */

    public BrickWall(App app, int i, int j) {
        super(app, i, j);
    }

    public boolean getAnimation(){
        return animation;
    }

    public void setAnimation(boolean animation){
        this.animation = animation;
    }
    
    public int getPrevFrame(){
        return prevFrame;
    }

    public void setPrevFrame(int prevFrame){
        this.prevFrame = prevFrame;
    }

    public int getCount(){
        return count;
    }

    /**
     * for looping through current image sprite in the destroy animation cycle
    */
    
    public void incrementCount(){
        this.count++;
    }
}
