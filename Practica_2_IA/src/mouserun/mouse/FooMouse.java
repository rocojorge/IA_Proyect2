package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;
public class FooMouse
	extends Mouse				
{

	private Grid lastGrid;
        private HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;

	public FooMouse()
	{
		super("FooMOUSE");		
	}
	
	public int move(Grid currentGrid, Cheese cheese)
	{
		Random random = new Random();
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
		if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
		if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
		if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
		possibleMoves.add(Mouse.BOMB);
				
		if (possibleMoves.size() == 1)
		{
			lastGrid = currentGrid;
			return possibleMoves.get(0);
		}
		else
		{
			if (!testGrid(Mouse.UP, currentGrid)) possibleMoves.remove((Integer)Mouse.UP);
			if (!testGrid(Mouse.DOWN, currentGrid)) possibleMoves.remove((Integer)Mouse.DOWN);
			if (!testGrid(Mouse.LEFT, currentGrid)) possibleMoves.remove((Integer)Mouse.LEFT);
			if (!testGrid(Mouse.RIGHT, currentGrid)) possibleMoves.remove((Integer)Mouse.RIGHT);
		
			if (possibleMoves.size() == 0)
			{
				if (currentGrid.canGoUp()) possibleMoves.add(Mouse.UP);
				if (currentGrid.canGoDown()) possibleMoves.add(Mouse.DOWN);
				if (currentGrid.canGoLeft()) possibleMoves.add(Mouse.LEFT);
				if (currentGrid.canGoRight()) possibleMoves.add(Mouse.RIGHT);
				possibleMoves.add(Mouse.BOMB);
				
				lastGrid = currentGrid;
				return possibleMoves.get(random.nextInt(possibleMoves.size()));
			}
			else
			{
				lastGrid = currentGrid;
				return possibleMoves.get(random.nextInt(possibleMoves.size()));
			}
		}
		
	}
	
	public void newCheese()
	{
	
	}
	
	public void respawned()
	{
	
	}
	
	private boolean testGrid(int direction, Grid currentGrid)
	{
		if (lastGrid == null)
		{
			return true;
		}	
	
		int x = currentGrid.getX();
		int y = currentGrid.getY();
		
		switch (direction)
		{
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
		
		return !(lastGrid.getX() == x && lastGrid.getY() == y);
		
	}
	

        class Pair<U, V> {

        public final U first;       // el primer campo de un par
        public final V second;      // el segundo campo de un par

        // Construye un nuevo par con valores especificados
        private Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }
        
        @Override
        // Verifica que el objeto especificado sea "igual a" el objeto actual o no
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            
            Pair<?, ?> pair = (Pair<?, ?>) o;

            // llamar al método `equals()` de los objetos subyacentes
            if (!first.equals(pair.first)) {
                return false;
            }
            return second.equals(pair.second);
        }
        
        @Override
        // Calcula el código hash de un objeto para admitir tablas hash
        public int hashCode() {
            // usa códigos hash de los objetos subyacentes
            return 31 * first.hashCode() + second.hashCode();
        }
        
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
        
    }
    
}