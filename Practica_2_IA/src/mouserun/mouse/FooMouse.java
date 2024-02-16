package mouserun.mouse;

import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;

public class FooMouse extends Mouse
{
    public FooMouse(){
        super("FooMouse");
    }
    public int move(Grid currentGrid, Cheese cheese){
        
        return Mouse.UP;
        return Mouse.DOWN;
        
    }
    public void newCheese(){
        
    }
    public void respawned(){
        
    }
}

