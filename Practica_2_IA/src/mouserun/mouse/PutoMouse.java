package mouserun.mouse;		
import mouserun.game.*;		
import java.util.*;			

public class PutoMouse
    extends Mouse				
{
    private ArrayList<Grid> lastGrid = new ArrayList<Grid>();
    private int cont=0;
    private ArrayList<Integer> acumulador = new ArrayList<Integer>();

    public PutoMouse() {
	super("Puto");		
    }
	
    public int move(Grid currentGrid, Cheese cheese) {
        cont++;
        Random random = new Random();
        ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
            
        if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
        if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
        if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
        if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
        acumulador.add(possibleMoves.size());
				
        if (possibleMoves.size() == 1) {
            lastGrid.add(currentGrid);
            return possibleMoves.get(0);
        } else {
            //elimina el movimiento anterior
            if (!testGrid(Mouse.UP, currentGrid)) possibleMoves.remove((Integer)Mouse.DOWN);
            if (!testGrid(Mouse.DOWN, currentGrid)) possibleMoves.remove((Integer)Mouse.UP);
            if (!testGrid(Mouse.LEFT, currentGrid)) possibleMoves.remove((Integer)Mouse.RIGHT);
            if (!testGrid(Mouse.RIGHT, currentGrid)) possibleMoves.remove((Integer)Mouse.LEFT);
            //buscamos la posicion con mas movimientos
            if (possibleMoves.size() == 0) {
                int moreMovesX = 0;
                int moreMovesY = 0;
                for(int i=cont-1; i==0; i--){
                    if(acumulador.get(i)!=1 && acumulador.get(i)!=0){
                        moreMovesX = lastGrid.get(i).getX();
                        moreMovesY = lastGrid.get(i).getY();
                        break;
                    }
                }
                
            while(lastGrid.get(cont-1).getX() == moreMovesX && lastGrid.get(cont-1).getY() == moreMovesY){
                    if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
                    if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
                    if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
                    if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
                    
                    lastGrid.add(currentGrid);
                    return possibleMoves.get(random.nextInt(possibleMoves.size()));
                }
                
                if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
                if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
                if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
                if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
				
                lastGrid.add(currentGrid);
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            } else {
                lastGrid.add(currentGrid);
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            }
        }
    }
	
    public void newCheese() {
    }
	
    public void respawned() {
    }
	
    private boolean testGrid(int direction, Grid currentGrid) {
        if (lastGrid == null) {
            return true;
        }	
	
        int x = currentGrid.getX();
        int y = currentGrid.getY();
		
        switch (direction) {
            case Mouse.UP: 
                y += 1;
                break;
				
            case Mouse.DOWN:
                y -= 1;
                break;
				
            case Mouse.LEFT:
                x -= 1;
                break;
				
            case Mouse.RIGHT:
                x += 1;
                break;
            }
		
        return !(lastGrid.get(cont-1).getX() == x && lastGrid.get(cont-1).getY() == y);
    }
}