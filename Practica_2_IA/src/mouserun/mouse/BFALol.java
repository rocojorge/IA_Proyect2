/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mouserun.mouse;

import mouserun.game.*;

import java.util.*;

public class BFALol extends Mouse {

    private HashMap<Pair<Integer, Integer>, mouseNode> maze;
    private Queue<Pair<Integer, Integer>> queue;
    private HashMap<Pair<Integer, Integer>, Pair<Integer, Integer>> parents;
    private final int NO_MOVE =0;
    
    private class Pair<A, B> {
        public A first;
        public B second;

        public Pair(A _first, B _second) {
            first = _first;
            second = _second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    private class mouseNode {
        public int x;
        public int y;

        public boolean up;
        public boolean down;
        public boolean left;
        public boolean right;

        public mouseNode(int _x, int _y, boolean _up, boolean _down, boolean _left, boolean _right) {
            x = _x;
            y = _y;
            up = _up;
            down = _down;
            left = _left;
            right = _right;
        }
    }

    public BFALol() {
        super("loooooooooooooool");
        maze = new HashMap<>();
        queue = new LinkedList<>();
        parents = new HashMap<>();
    }

    
   @Override
public int move(Grid currentGrid, Cheese cheese) {
    Pair<Integer, Integer> currentPosition = new Pair<>(currentGrid.getX(), currentGrid.getY());

    // Verificar si la posición actual ya está registrada en el mapa maze
    if (!maze.containsKey(currentPosition)) {
        mouseNode newNode = new mouseNode(
                currentGrid.getX(), currentGrid.getY(),
                currentGrid.canGoUp(), currentGrid.canGoDown(),
                currentGrid.canGoLeft(), currentGrid.canGoRight()
        );
        maze.put(currentPosition, newNode);
    }

    // Obtener el nodo correspondiente a la posición actual
    mouseNode currentNode = maze.get(currentPosition);

    // Verificar si currentNode es nulo
    if (currentNode == null) {
        // Si currentNode es nulo, imprimir un mensaje de error y devolver un movimiento predeterminado
        System.err.println("Error: No se pudo obtener el nodo para la posición actual.");
        return NO_MOVE;
    }

    // Continuar con la lógica del BFS si la cola está vacía
    if (queue.isEmpty()) {
        bfs(currentPosition);
    }

    // Obtener el siguiente movimiento de la cola
    Pair<Integer, Integer> nextMove = queue.poll();
    if (nextMove == null) {
        return NO_MOVE;
    }

    // Devolver la dirección del próximo movimiento
    return getDirection(currentPosition, nextMove);
}



    @Override
    public void newCheese() {
        queue.clear();
        parents.clear();
    }

    @Override
    public void respawned() {
        queue.clear();
        parents.clear();
    }

    void bfs(Pair<Integer, Integer> start) {
        queue.clear();
        parents.clear();

        queue.add(start);
        parents.put(start, null);

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> current = queue.poll();

            mouseNode currentNode = maze.get(current);

            if (currentNode.up && !maze.containsKey(new Pair<>(current.first, current.second + 1))) {
                Pair<Integer, Integer> nextPosition = new Pair<>(current.first, current.second + 1);
                queue.add(nextPosition);
                parents.put(nextPosition, current);
            }

            if (currentNode.down && !maze.containsKey(new Pair<>(current.first, current.second - 1))) {
                Pair<Integer, Integer> nextPosition = new Pair<>(current.first, current.second - 1);
                queue.add(nextPosition);
                parents.put(nextPosition, current);
            }

            if (currentNode.left && !maze.containsKey(new Pair<>(current.first - 1, current.second))) {
                Pair<Integer, Integer> nextPosition = new Pair<>(current.first - 1, current.second);
                queue.add(nextPosition);
                parents.put(nextPosition, current);
            }

            if (currentNode.right && !maze.containsKey(new Pair<>(current.first + 1, current.second))) {
                Pair<Integer, Integer> nextPosition = new Pair<>(current.first + 1, current.second);
                queue.add(nextPosition);
                parents.put(nextPosition, current);
            }
        }
    }

    

    private int getDirection(Pair<Integer, Integer> init, Pair<Integer, Integer> target) {
        if (target.second - 1 == init.second) {
            return Mouse.UP;
        } else if (target.second + 1 == init.second) {
            return Mouse.DOWN;
        } else if (target.first - 1 == init.first) {
            return Mouse.LEFT;
        } else if (target.first + 1 == init.first) {
            return Mouse.RIGHT;
        }
        return NO_MOVE;
    }
}
