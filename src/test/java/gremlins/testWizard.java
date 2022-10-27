package gremlins;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class testWizard {
    App app = new App();
    Wizard wizard = new Wizard(app,10,0,0);
    
    @Test
    public void wizardNormalMovement(){
        wizard.resetDir();

        //moving right
        wizard.pressRight(true);
        assertTrue(wizard.moveRight);
        wizard.pressRight(false);
        assertFalse(wizard.moveRight);
        wizard.resetDir();

        //moving down
        wizard.pressDown(true);
        assertTrue(wizard.moveDown);
        wizard.pressDown(false);
        assertFalse(wizard.moveDown);
        wizard.resetDir();

        //moving up
        wizard.pressUp(true);
        assertTrue(wizard.moveUp);
        wizard.pressUp(false);
        assertFalse(wizard.moveUp);
        wizard.resetDir();

        //moving left
        wizard.pressLeft(true);
        assertTrue(wizard.moveLeft);
        wizard.pressLeft(false);
        assertFalse(wizard.moveLeft);
        wizard.resetDir();

        //moveing diagonal (not allowed)
        wizard.curDir = 1;
        int prevDir = wizard.curDir;
        wizard.pressRight(true);
        wizard.pressDown(true);
        assertEquals(prevDir, wizard.curDir);
        wizard.resetDir();

        //press spacebar
        wizard.pressSpace(true);
        assertTrue(wizard.pressSpace);
        wizard.pressSpace(false);
        assertFalse(wizard.pressSpace);
    }

    @Test
    public void wizardEraticMovement(){
        wizard.resetDir();
        wizard.pressLeft(true);
        wizard.pressRight(true);
        wizard.pressDown(true);
        wizard.pressUp(true);
    }
}
