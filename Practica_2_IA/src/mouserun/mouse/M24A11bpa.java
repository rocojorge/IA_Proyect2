/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package mouserun.mouse;

import mouserun.game.*;

import java.util.*;

/**
 *
 * @author gabri
 */
public class M24A11bpa extends Mouse {
    
    private final Integer SIN_MOVIMIENTOS = 0;
    
    private class Pair<A, B> {

        public A primero;
        public B segundo;

        public Pair() {
        }

        public Pair(A _first, B _second) {
            primero = _first;
            segundo = _second;
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
            return primero == key.primero && segundo == key.segundo;
        }

        @Override
        public int hashCode() {
            if (primero instanceof Integer && segundo instanceof Integer) {
                Integer result = (Integer) primero;
                Integer sec = (Integer) segundo;
                return result * 999 + sec;
            }

            return 0;
        }

        @Override
        public String toString() {
            return "X: " + primero + " Y: " + segundo;
        }
    }

    /**
     * Almacena una posición(x,y) y las direcciones accesibles desde la misma.
     * Esto último, solo será válido si el nodo está marcado como explored.
     */
    private class Nodo {

        public int x,y;
        public boolean up,down;
        public boolean left;
        public boolean right;
        public boolean explored;
        
        //#1
        public Nodo(int _x, int _y, boolean _up, boolean _down, boolean _left, boolean _right) {
            x= _x;
            y= _y;
            up= _up;
            down= _down;
            left= _left;
            right= _right;
            explored=true;
        }
        //#2
        public Nodo(Pair<Integer, Integer> pos, boolean _up, boolean _down, boolean _left, boolean _right) {
            this(pos.primero, pos.segundo, _up, _down, _left, _right);
        }
        //#3
        public Nodo(int _x, int _y){
            x=_x;
            y=_y;
            explored = false;
        }
        //#4
        public Nodo(Pair<Integer, Integer> pos) {
            this(pos.primero, pos.segundo);
        }

        public Pair<Integer, Integer> getPos() {
            return new Pair(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Nodo)) {
                return false;
            }
            Nodo node = (Nodo) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return x * 999 + y;
        }

        @Override
        public String toString() {
            return "X: " + x + " Y: " + y;
        }
    }
    
    
    private HashMap<Pair<Integer, Integer>, Nodo> Nodo_so =  new HashMap<>();
    private HashMap<Pair<Integer,Integer>,Nodo> Nodos_corto;
    private Nodo Nodo_del_principio;
    private LinkedList<Integer> movimientos_devuelta;
    private Queue<Pair<Integer,Integer>> Cola_ = new ArrayDeque<>();//Cola de nodos a visitar en una vuelta

        
    
    public M24A11bpa(){
        super("M24A11_BreadthExplorer");
        this.Nodo_so = new HashMap<>();
        this.Cola_=new ArrayDeque<>();
    }
    
    public int move(Grid currentGrid, Cheese cheese) {
        //Primero me fijo en la cola y hago la cola
        //Escribir aquí
        if (Cola_.isEmpty()){return 0;//Vuelvo al inicio con la pila
        }
        else{
            ArrayList<Integer> movimientos = posibles_movimientos(currentGrid);
            
            
        }
        //Evalúo mis alrededores y listo casillas.
        
        return 1;
    }
    
    private ArrayList<Integer> posibles_movimientos(Grid celda_actual)
    {
        ArrayList<Integer> posibles = new ArrayList<>();
        if (celda_actual.canGoDown()) posibles.add(Mouse.DOWN);
        if (celda_actual.canGoUp()) posibles.add(Mouse.UP);
        if (celda_actual.canGoLeft()) posibles.add(Mouse.LEFT);
        if (celda_actual.canGoRight()) posibles.add(Mouse.RIGHT);
        
        return posibles; 
            
    }
    
    private void casillas_visitar(Grid celda_actual){
        ArrayList<Integer> posibles;
        posibles = posibles_movimientos(celda_actual);
        
        for(int i =0; i<posibles.size();i++){
            if (posibles.get(i)==1){
                Pair<Integer,Integer> root_up = new Pair(celda_actual.getX(),celda_actual.getY()+1);
                Nodo celda_root_up= new Nodo(celda_actual.getX(),celda_actual.getY());
                Nodo_so.putIfAbsent(root_up,celda_root_up);
            }
        }
        
        
    }
    public void respawned(){}
    
    public void newCheese(){
        
    }
    
    
}
