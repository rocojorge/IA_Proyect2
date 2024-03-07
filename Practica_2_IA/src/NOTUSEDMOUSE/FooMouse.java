package NOTUSEDMOUSE;		
import mouserun.game.*;		
import java.util.*;
import java.util.ArrayList;

public class FooMouse
    extends Mouse				
{
    private ArrayList<Grid> lastGrid;
    private int cont;
    private ArrayList<Integer> acumulador;

    public FooMouse() {
	super("Puto1");	
        acumulador = new ArrayList<>();
        lastGrid = new ArrayList<>();
        cont =0;
        
    }
	
    public int move(Grid currentGrid, Cheese cheese) {
        cont++;
        Random random = new Random();
        ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
            
        if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
        if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
        if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
        if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
        System.out.println("ADDTOGRID1");
        acumulador.add(possibleMoves.size());
        System.out.println("ADDTOGRID2");
	
        if (possibleMoves.size() == 1) {
            System.out.println("ADDTOGRID");
            lastGrid.add(currentGrid);
            return possibleMoves.get(0);
        } else {
            //elimina el movimiento anterior
            if (!testGrid(Mouse.UP, currentGrid)) possibleMoves.remove((Integer)Mouse.DOWN);
            if (!testGrid(Mouse.DOWN, currentGrid)) possibleMoves.remove((Integer)Mouse.UP);
            if (!testGrid(Mouse.LEFT, currentGrid)) possibleMoves.remove((Integer)Mouse.RIGHT);
            if (!testGrid(Mouse.RIGHT, currentGrid)) possibleMoves.remove((Integer)Mouse.LEFT);
            //buscamos la posicion con mas movimientos si no nos podemos mover
            if (possibleMoves.size() == 0) {
                int moreMovesX = 0;
                int moreMovesY = 0;
                for(int i=cont-1; i==0; i--){
                    if(acumulador.get(i)!=1 && acumulador.get(i)!=0){
                        System.out.println("GETTOGRID");
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
                    System.out.println("ADDTOGRID");
                    lastGrid.add(currentGrid);
                    toMove(possibleMoves.get(random.nextInt(possibleMoves.size())));
                }
                
                if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
                if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
                if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
                if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
		System.out.println("ADDTOGRID");		
                lastGrid.add(currentGrid);
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            } else {
                System.out.println("ADDTOGRID");
                lastGrid.add(currentGrid);
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            }
        }
    }
    
    public int toMove(int move){
        System.out.println("TOMOVE");
        return move;
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