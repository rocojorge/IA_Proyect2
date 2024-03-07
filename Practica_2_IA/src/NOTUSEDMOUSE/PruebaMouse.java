package NOTUSEDMOUSE;		
import mouserun.game.*;		
import java.util.*;			

public class PruebaMouse
    extends Mouse				
{
    private ArrayList<Grid> lastGrid = new ArrayList<Grid>();
    private int cont=-1;
    private ArrayList<Integer> acumulador = new ArrayList<Integer>();

    public PruebaMouse() {
	super("PruebaMouse");		
    }
    
    public int return_incremento(int mov){
        cont++;
        return mov;
    }
    
    public int move(Grid currentGrid, Cheese cheese) {
        Random random = new Random();
        ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
        if(lastGrid.size()==0){
            if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
            if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
            if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
            if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
            lastGrid.add(currentGrid);
            return return_incremento(possibleMoves.get(random.nextInt(possibleMoves.size())));
        }else{    
            if (currentGrid.canGoUp()) {
                possibleMoves.add(Mouse.UP);
                for(int i=0; i<lastGrid.size(); i++){
                    if(lastGrid.get(i).getY()==lastGrid.get(cont).getY()+1){
                        possibleMoves.remove((Integer)Mouse.UP);
                        break;
                    }
                }
            }
            if (currentGrid.canGoDown()){
                possibleMoves.add(Mouse.DOWN);
                for(int i=0; i<lastGrid.size(); i++){
                    if(lastGrid.get(i).getY()==lastGrid.get(cont).getY()-1){
                        possibleMoves.remove((Integer)Mouse.DOWN);
                        break;
                    }
                }
            }
            if (currentGrid.canGoLeft()){
                possibleMoves.add(Mouse.LEFT);
                for(int i=0; i<lastGrid.size(); i++){
                    if(lastGrid.get(i).getX()==lastGrid.get(cont).getX()-1){
                        possibleMoves.remove((Integer)Mouse.LEFT);
                        break;
                    }
                }
            }
            if (currentGrid.canGoRight()){
                possibleMoves.add(Mouse.RIGHT);
                for(int i=0; i<lastGrid.size(); i++){
                    if(lastGrid.get(i).getX()==lastGrid.get(cont).getX()+1){
                        possibleMoves.remove((Integer)Mouse.RIGHT);
                        break;
                    }
                }
            }
        }
        
        if (possibleMoves.size() == 1) {
            lastGrid.add(currentGrid);
            return possibleMoves.get(0);
        }
            //si nos quedamos sin movimientos tenemos que repetir coordenadas
            if (possibleMoves.size() == 0) {
                if (currentGrid.canGoUp()) {
                    for(int i=0; i<lastGrid.size(); i++){
                        if(lastGrid.get(i).getY()==lastGrid.get(cont).getY()+1){
                            possibleMoves.add(Mouse.UP);
                            break;
                        }
                    }
                }
                if (currentGrid.canGoDown()){
                    for(int i=0; i<lastGrid.size(); i++){
                        if(lastGrid.get(i).getY()==lastGrid.get(cont).getY()-1){
                            possibleMoves.add(Mouse.DOWN);
                            break;
                        }
                    }
                }
                if (currentGrid.canGoLeft()){
                    for(int i=0; i<lastGrid.size(); i++){
                        if(lastGrid.get(i).getX()==lastGrid.get(cont).getX()-1){
                            possibleMoves.add(Mouse.LEFT);
                            break;
                        }
                    }
                }
                if (currentGrid.canGoRight()){
                    for(int i=0; i<lastGrid.size(); i++){
                        if(lastGrid.get(i).getX()==lastGrid.get(cont).getX()+1){
                            possibleMoves.add(Mouse.RIGHT);
                            break;
                        }
                    }
                }
                lastGrid.add(currentGrid);
                return return_incremento(possibleMoves.get(random.nextInt(possibleMoves.size())));
            } else {
                lastGrid.add(currentGrid);
                return return_incremento(possibleMoves.get(random.nextInt(possibleMoves.size())));
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
		
        return !(lastGrid.get(cont).getX() == x && lastGrid.get(cont).getY() == y);
    }
}