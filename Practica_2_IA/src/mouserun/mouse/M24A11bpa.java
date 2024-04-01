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
    
    private HashMap<Pair<Integer, Integer>, Nodo> Nodoso;
    private Queue<Nodo> Cola_ = new ArrayDeque<>();

        
    
    public M24A11bpa(){
        super("M24A11_BreadthExplorer");
    }
    
    public int move(Grid currentGrid, Cheese cheese) {
        Nodoso.put();
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
    public void respawned(){}
    
    public void newCheese(){
        
    }
    
    
}
