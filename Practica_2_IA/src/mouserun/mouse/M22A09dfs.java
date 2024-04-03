/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;

/**
 *
 * @author Bader
 */
public class M22A09dfs extends Mouse {
    
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    private final HashMap<Pair<Integer, Integer>, Grid> celdasExploradas;
    private final Stack<Grid> pilaMovimientos;
    
    private Stack<Grid> caminoQueso; //camino desde la posicion del queso hasta la posicion actual
    private boolean bomba; //un logico para saber si el raton ha tirado una bomba o no
    private boolean caminoQE; //camino queso encontrado
    
    /**
     * Constructor
     */
    public M22A09dfs() {
        super("DFS");
        celdasVisitadas = new HashMap<>();
        celdasExploradas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        
        caminoQueso = new Stack<>();
        bomba=false;
        caminoQE=false;
    }
    
    private class Pair<A, B> {

        public A first;
        public B second;

        public Pair() {
        }

        public Pair(A _first, B _second) {
            first = _first;
            second = _second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Pair)) {
                return false;
            }
            Pair key = (Pair) o;
            return first == key.first && second == key.second;
        }

        @Override
        public int hashCode() {
            if (first instanceof Integer && second instanceof Integer) {
                Integer result = (Integer) first;
                Integer sec = (Integer) second;
                return result * 1000 + sec;
            }

            return 0;
        }

        @Override
        public String toString() {
            return "X: " + first + " Y: " + second;
        }
    }


    /**
     * Un metodo para devolver los posibles movimientos a partir de la posion actual
     * Se añade el movimiento si no hay pared y si la celda todavia no ha sido visitada anteriormente
     * @param currentGrid La celda actual 
     * @return Se devuelve un ArrayList con los posibles movimientos
     */
    private ArrayList<Integer> posiblesMovimientos(Grid currentGrid){
        ArrayList<Integer> salida = new ArrayList<>();
        if(currentGrid.canGoLeft() && !celdasVisitadas.containsKey(new Pair(currentGrid.getX()-1,currentGrid.getY()))){
            salida.add(Mouse.LEFT);
        }
        if(currentGrid.canGoRight() && !celdasVisitadas.containsKey(new Pair(currentGrid.getX()+1,currentGrid.getY()))){
            salida.add(Mouse.RIGHT);
        }
        if(currentGrid.canGoUp() && !celdasVisitadas.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()+1))){
            salida.add(Mouse.UP);
        }
        if(currentGrid.canGoDown() && !celdasVisitadas.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()-1))){
            salida.add(Mouse.DOWN);
        }
        return salida;
    }
    
    
    /**
     * Una funcion que devuelve el ultimo paso para poder volver a tras en caso de haber un camino cerrado
     * @param currentGrid La casilla actual
     * @return Devolver el ultimo paso, la ultima casilla añadida a la pila
     */
    private int vueltaAtras(Grid currentGrid) {
        int movimientoHaciaAtras = -1;
        Grid celdaAnterior = pilaMovimientos.pop();
        if (celdaAnterior.getX() == currentGrid.getX() && celdaAnterior.getY() == currentGrid.getY() - 1) {
            movimientoHaciaAtras = Mouse.DOWN;
        }
        if (celdaAnterior.getX() == currentGrid.getX() && celdaAnterior.getY() == currentGrid.getY() + 1) {
            movimientoHaciaAtras = Mouse.UP;
        }
        if (celdaAnterior.getX() == currentGrid.getX() - 1 && celdaAnterior.getY() == currentGrid.getY()) {
            movimientoHaciaAtras = Mouse.LEFT;
        }
        if (celdaAnterior.getX() == currentGrid.getX() + 1 && celdaAnterior.getY() == currentGrid.getY()) {
            movimientoHaciaAtras = Mouse.RIGHT;
        }
        return movimientoHaciaAtras;
    }
    
    /**
     * Se mira si tenemos que volver atras o añadir la casilla 
     * a la pila y seguir adelante.
     * @param currentGrid La casilla actual
     * @return Devolver el siguiente movimiento
     */
    private int explorar(Grid currentGrid, ArrayList<Integer> posiblesMovimientos) {
        
        if (posiblesMovimientos.isEmpty()) {
            return vueltaAtras(currentGrid);
        } else {
            Random random = new Random();
            int ran = random.nextInt(posiblesMovimientos.size())%4;
            pilaMovimientos.add(currentGrid);
            return posiblesMovimientos.get(ran);
        }
    }
    
    
    /**
     * Un metodo que busca un camino desde la pocicion del queso hasta la posicion actual.
     * Este metodo se llama cuando el queso aparece en una posicion explorada anteriormente.
     * Se limpia el camino anterior para crear uno nuevo, se añade la posicion del queso en una cola nueva 
     * y se va llamando a una funcion que bueca el camino desde la posicion del queso hasta 
     * la posicion actual.
     * Se obtienen los posibles movimientos de los que se ha visitado anteriomente llamando al metodo
     * obtenerHijos.
     * Para cada uno de estos hijos se mira cual lleva hasta la posicion actual.
     * @param queso la posicion del queso
     * @param currentGrid la posicion actual
     * @return el metodo devuelve un logico que indica si tenemos un camino hasta el queso o no
     */
    private boolean buscarCaminoQueso(Grid queso, Grid currentGrid){
        caminoQE=false;
        caminoQueso.clear();
        Stack<Grid> camino = new Stack<>();
        camino.add(queso);
        
        celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()), currentGrid);
        ArrayList<Grid> hijos = obtenerHijos(new Grid(queso.getX(), queso.getY()));
        for(int i=0; i<hijos.size(); i++){
            buscarCaminoR(hijos.get(i), currentGrid, camino);
            if(caminoQE){
                return caminoQueso.isEmpty();
            }
        }
        return caminoQueso.isEmpty();
    }
    
    /**
     * Un metodo auxiliar de buscarCaminoQueso que se llama recursivamente para construir un camino
     * que lleva desde la posicion del queso hasta mi posicion actual.
     * @param hijoActual el posibles hijo que lleva al destino
     * @param currentGrid la celda actual
     * @param caminoAcumulado el camino construido para llegar al queso
     */
    private void buscarCaminoR(Grid hijoActual, Grid currentGrid, Stack<Grid> caminoAcumulado){
        if(!caminoQE){
            if(hijoActual.getX()==currentGrid.getX() && hijoActual.getY()==currentGrid.getY()){
                caminoQE=true;
                caminoQueso = caminoAcumulado;
            }else{
                Stack<Grid> camino = new Stack<>();
                camino=caminoAcumulado;
                camino.add(hijoActual);
                ArrayList<Grid> hijos = obtenerHijos(hijoActual);
                for(int i=0; i<hijos.size(); i++){
                    if(!camino.contains(hijos.get(i))){
                        buscarCaminoR(hijos.get(i), currentGrid, camino);
                        caminoAcumulado=camino;
                    }
                }
                if(!caminoQE){
                    camino.pop();
                    caminoAcumulado=camino;
                }
            }
        }
    }
    
    /**
     * Un metodo para obtener todas las celdas posibles para construir el camino del queso
     * @param hijoActual La celda actual 
     * @return El metodo devuevle un array con los posibles movimientos
     */
    private ArrayList<Grid> obtenerHijos(Grid hijoActual){
        ArrayList<Grid> salida = new ArrayList<>();
        Pair<Integer, Integer> actual = new Pair(hijoActual.getX(), hijoActual.getY());
        
        //si la celda de arriba esta visitada y puede ir arriba
        Pair<Integer,Integer> arriba = new Pair(hijoActual.getX(), hijoActual.getY()+1);
        if(celdasVisitadas.containsKey(arriba) && celdasVisitadas.get(actual).canGoUp()){
            salida.add(celdasVisitadas.get(arriba));
        }
        //si la celda de abajo esta visitada y puede ir abajo
        Pair<Integer,Integer> abajo = new Pair(hijoActual.getX(), hijoActual.getY()-1);
        if(celdasVisitadas.containsKey(abajo) && celdasVisitadas.get(actual).canGoDown()){
            salida.add(celdasVisitadas.get(abajo));
        }
        //si la celda de izquierda esta visitada y puede ir izquierda
        Pair<Integer,Integer> izquierda = new Pair(hijoActual.getX()-1, hijoActual.getY());
        if(celdasVisitadas.containsKey(izquierda) && celdasVisitadas.get(actual).canGoLeft()){
            salida.add(celdasVisitadas.get(izquierda));
        }
        //si la celda de derecha esta visitada y puede ir derecha
        Pair<Integer,Integer> derecha = new Pair(hijoActual.getX()+1, hijoActual.getY());
        if(celdasVisitadas.containsKey(derecha) && celdasVisitadas.get(actual).canGoRight()){
            salida.add(celdasVisitadas.get(derecha));
        }
        
        return salida;
    }
    
    /**
     * Un metodo para mover el raton en el camino del queso
     * @param currentGrid La celda actual 
     * @return El metodo devuelve el siguiente movimiento
     */
    private int moverQueso(Grid currentGrid){
        int mov=-1;
        Grid sigMov = caminoQueso.pop();
        if(sigMov.getX() == currentGrid.getX() && sigMov.getY() == currentGrid.getY()+1){
            mov = Mouse.UP;
        }
        if(sigMov.getX() == currentGrid.getX() && sigMov.getY() == currentGrid.getY()-1){
            mov = Mouse.DOWN;
        }
        if(sigMov.getX() == currentGrid.getX()+1 && sigMov.getY() == currentGrid.getY()){
            mov = Mouse.RIGHT;
        }
        if(sigMov.getX() == currentGrid.getX()-1 && sigMov.getY() == currentGrid.getY()){
            mov = Mouse.LEFT;
        }
        return mov;
    }
    
    /**
     * Un metodo para tirar bombas
     * Se calcula un numero aleatorio, si es menor de 10 tira bomba
     * Si el agente se encuentra en un cruce tira bomba
     * @param currentGrid Grid la posicion actual del agente
     * @return Devuelve un logico que indica que se puede tirar bomba o no
     */
    private boolean tirarBomba(Grid currentGrid){
        boolean tirar=false;
        Random r = new Random();
        int i = r.nextInt(100);
        
        if((currentGrid.canGoUp() && currentGrid.canGoDown() && currentGrid.canGoLeft() && currentGrid.canGoRight()) ||
                i<5){
            tirar=true;
        }
        return tirar;
    }
    
    /**
     * El metodo principal del raton
     * @param currentGrid La casilla actual
     * @param cheese La casilla del queso
     * @return Aplica el movimiento
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        //añadir la casilla a los mapas si no esta añadido
        Pair<Integer, Integer> actual = new Pair<>(currentGrid.getX(), currentGrid.getY());
        if (!celdasVisitadas.containsKey(actual)) {
            celdasVisitadas.put(actual, currentGrid);
        }
        if (!celdasExploradas.containsKey(actual)) {
            //incrementar el contador 
            incExploredGrids();
            celdasExploradas.put(actual, currentGrid);
        }
        
        ArrayList<Integer> posiblesMovimientos = posiblesMovimientos(currentGrid);
        
        if(!bomba){ //si el raton no ha puesto una bomba
            //intentar tiro bomba
            if(tirarBomba(currentGrid)){
                bomba=true;
                return Mouse.BOMB;
            }else{
                //si el queso esta en una celda explorada anteriormente
                if(celdasVisitadas.containsKey(new Pair(cheese.getX(), cheese.getY()))){
                    //si no tengo camino hasta el queso
                    if(buscarCaminoQueso(new Grid(cheese.getX(), cheese.getY()), currentGrid)){
                        pilaMovimientos.add(currentGrid);
                        return moverQueso(currentGrid);
                    }else{ //si tengo camino para el queso
                        pilaMovimientos.add(currentGrid);
                        return moverQueso(currentGrid);
                    }
                }else{ //si el queso no esta en una celda explorada sigue explorando
                    return explorar(currentGrid, posiblesMovimientos);
                }
            }
        }else{
            bomba=false;
            //si el queso esta en una celda explorada
            if(celdasVisitadas.containsKey(new Pair(cheese.getX(), cheese.getY()))){
                //si no tengo camino para el queso
                if(buscarCaminoQueso(new Grid(cheese.getX(), cheese.getY()), currentGrid)){
                    pilaMovimientos.add(currentGrid);
                    return moverQueso(currentGrid);
                }else{ //si tengo camino para el queso
                    pilaMovimientos.add(currentGrid);
                    return moverQueso(currentGrid);
                }
            }else{ //si el queso esta en una celda no explorada
                return explorar(currentGrid, posiblesMovimientos);
            }
        }
    }

    @Override
    public void newCheese() {
        caminoQE=false;
        caminoQueso.clear();
    }
    
    @Override
    public void respawned() {
        pilaMovimientos.clear();
        celdasVisitadas.clear();
        caminoQueso.clear();
    }
}
