package mouserun.mouse;

import mouserun.game.*;
import java.util.*;

public class M24A11bae extends Mouse {

    //Clases de Soporte

    /* 
     Las clases definidas sobrecargan los metodos equals y hashcode.   
     Esto es así dado que ambos son usados como claves en estructuras HashMap.
     De no sobrecargarse, no se comportarían adecuadamente.
     */
    
    /**
     * TDA que permite almacenar dos valores, del mismo o distinto tipo.
     * @param <A> Tipo del atributo first
     * @param <B> Tipo del atributo second
     */
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
     * Almacena una posición(x,y) y las direcciones accesibles desde la misma. Esto último, solo será 
     * válido si el nodo está marcado como explorado.
     */
    private class nodo_raton {

        public int x;
        public int y;
        public boolean up;
        public boolean down;
        public boolean left;
        public boolean right;
        public boolean explored;

        public nodo_raton(int _x, int _y, boolean _up, boolean _down, boolean _left, boolean _right) {
            x = _x;
            y = _y;
            up = _up;
            down = _down;
            left = _left;
            right = _right;
            explored = true;
        }

        public nodo_raton(Pair<Integer, Integer> pos, boolean _up, boolean _down, boolean _left, boolean _right) {
            this(pos.first, pos.second, _up, _down, _left, _right);
        }

        public nodo_raton(int _x, int _y) {
            x = _x;
            y = _y;
            explored = false;
        }

        public nodo_raton(Pair<Integer, Integer> pos) {
            this(pos.first, pos.second);
        }

        public Pair<Integer, Integer> get_pos() {
            return new Pair(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof nodo_raton)) {
                return false;
            }
            nodo_raton node = (nodo_raton) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return x * 10000 + y;
        }

        @Override
        public String toString() {
            return "X: " + x + " Y: " + y;
        }
    }

    
    //Fin clases de soporte
    private HashMap<Pair<Integer, Integer>, nodo_raton> nodos_conocidos;
    
    //Contiene los nodos conocidos del laberinto. 
    //Usa una posición (x,y) como clave.
    //Almacenada en un Pair de entero-entero   

    private Stack<Integer> camino;
    //Contiene los movimientos a realizar. Bien para llegar a un Cheese, o para llegar a una casilla no explorada.

    private int Contador_bomba;  //Cuenta los movimientos. Se reinicia al colocar una bomba.
    private int n_bombas;  //Cuenta las bombas que quedan por poner.

    private boolean busqueda_informada;

    public M24A11bae() {
        super("M24A11bae");
        Contador_bomba = 0;
        n_bombas = 5;
        busqueda_informada = false;
        camino = new Stack<>();
        nodos_conocidos = new HashMap<>();
    }
   
    
    @Override
    public int move(Grid Grid_actual, Cheese cheese) {
        
        
        Pair<Integer, Integer> currentPos = new Pair<>(Grid_actual.getX(), Grid_actual.getY());
        nodo_raton Nodo_actual;

 //Buscamos en nodos_conocidos la posición actual. Si está, Nodo_actual será el nodo almacenado en caso contrario, se crea un nuevo nodo y se almacena.
        
        if (nodos_conocidos.containsKey(currentPos)) {
            Nodo_actual = nodos_conocidos.get(currentPos);
        } else {
            Nodo_actual = new nodo_raton(
                    currentPos,
                    Grid_actual.canGoUp(), Grid_actual.canGoDown(),
                    Grid_actual.canGoLeft(), Grid_actual.canGoRight()
            );

            nodos_conocidos.put(currentPos, Nodo_actual);
        }

        //En caso de que nos encontremos en la casilla del cheese, abandonamos la casilla y volvemos a ella
        if (cheese.getX() == Nodo_actual.x && cheese.getY() == Nodo_actual.y && camino.isEmpty()) {
            if (Grid_actual.canGoUp()) {
                camino.add(Mouse.DOWN);
                camino.add(Mouse.UP);
            } else {
                if (Grid_actual.canGoDown()) {
                    camino.add(Mouse.UP);
                    camino.add(Mouse.DOWN);
                } else {
                    if (Grid_actual.canGoLeft()) {
                        camino.add(Mouse.RIGHT);
                        camino.add(Mouse.LEFT);
                    } else {
                        if (Grid_actual.canGoRight()) {
                            camino.add(Mouse.LEFT);
                            camino.add(Mouse.RIGHT);
                        }
                    }
                }
            }
        }

        //Comprobamos si quedan bombas
        if (n_bombas > 0) {
            int cuenta_salida = 0;
            //Almacena la cantidad de direcciones por las que se puede avanzar, desde el nodo actual.
            if (Nodo_actual.up)cuenta_salida++;
            if (Nodo_actual.down)cuenta_salida++;
            if (Nodo_actual.left)cuenta_salida++;
            if (Nodo_actual.right)cuenta_salida++;
            
            //Según el número de movimientos y el número de salidas, se decide si colocar, o no, una bomba.
            if (Contador_bomba > 30 && cuenta_salida > 3) {
                Contador_bomba = 0;
                n_bombas--;
                return Mouse.BOMB;
            } else {
                if (Contador_bomba > 100 && cuenta_salida > 2) {
                    Contador_bomba = 0;
                    n_bombas--;
                    return Mouse.BOMB;
                } else {
                    Contador_bomba++;
                }
            }
        }

        //Si no hay camino, creamos uno.
        if (camino.isEmpty()) {
            Pair<Integer, Integer> target = new Pair<>(cheese.getX(), cheese.getY());

            if (nodos_conocidos.containsKey(target)) {
                busqueda_informada = true;
                //Si sabemos donde está el objetivo,
                //usamos A* (
            } else {
                busqueda_informada = false;
                //Exploramos con profundidad_limitada
            }

            getCamino(Nodo_actual, target);
            //Obtenemos un camino al queso o a una casilla no explorada.
        }

        return camino.pop();
    }
    
    void getCamino(nodo_raton root_node, Pair<Integer, Integer> target) {
        List<Pair<Integer, nodo_raton>> candidatos = new ArrayList<>(); //Guarda la profundidad del nodo y el nodo
        HashMap<Pair<Integer, Integer>, nodo_raton> anteriores = new HashMap<>();
        nodo_raton target_node = null;

        //Llamadas a búsquedas
        if (busqueda_informada) {
            busqueda_A_star(root_node, target, anteriores);
            target_node = nodos_conocidos.get(target); //El nodo objetivo es el mismo queso.
        } else {
            //Comenzamos con límite 5. Si no hay casillas sin explorar, incrementamos dicho límite en 5 unidades.

            int limite = 5;
            target_node = null;
            while (target_node == null) {
                target_node = busqueda_profundidad_limitada(root_node, target, anteriores, limite);
                limite += 5;
            }
        }

        //Si A* seleccionado, pero target inaccesible, empleamos la búsqueda de exploración.
        if (busqueda_informada && !anteriores.containsKey(target)) {
            //Conocemos la posición del queso, pero es inaccesible.
            //El A* no llega a él.

            int limite = 5;
            target_node = null;
            while (target_node == null) {
                target_node = busqueda_profundidad_limitada(root_node, target, anteriores, limite);
                limite += 5;
            }
        }

        //Finalmente calculamos el camino al nodo objetivo          
        nodo_raton curNode = anteriores.get(target_node.get_pos());
        camino.add(get_direction(curNode.get_pos(), target_node.get_pos()));

        while (curNode != root_node) {
            Pair<Integer, Integer> targetPos = curNode.get_pos();
            curNode = anteriores.get(curNode.get_pos());
            camino.add(get_direction(curNode.get_pos(), targetPos));
        }

    }
    
    /**
     * Dadas dos posiciones, devuelve la dirección a seguir por el ratón para llegar de una a otra.
     * @param init posición inicial
     * @param target posición destino
     * @return Movimiento para ir de init a target.
     */
    private int get_direction(Pair<Integer, Integer> init, Pair<Integer, Integer> target) {
        if (target.second - 1 == init.second) {
            return Mouse.UP;
        } else if (target.second + 1 == init.second) {
            return Mouse.DOWN;
        } else if (target.first - 1 == init.first) {
            return Mouse.RIGHT;
        } else {
            return Mouse.LEFT;
        }
    }
    /**
     * Realiza una búsqueda en profundidad limitada. Almacenara los antecesores de cada nodo para poder calcular el camino y manejará una lista de nodos
     * candidatos, no explorados exclusivamente, que se emplearán en el cálculo del nodo a devolver.
     * @param root_node Nodo inicial.
     * @param target Posición objetivo.
     * @param anteriores Almacena el nodo anterior de cada posición.
     * @param limite Limite de la busqueda
     * @return null si no hay casillas objetivo sino, mejor nodo candidato calculada.
     */
    nodo_raton busqueda_profundidad_limitada(nodo_raton root_node, Pair<Integer, Integer> target, HashMap<Pair<Integer, Integer>, nodo_raton> anteriores, int limite) {
        Stack<Pair<Integer, nodo_raton>> abiertos = new Stack<>();
        HashMap<Pair<Integer, Integer>, nodo_raton> cerrados = new HashMap<>();
        List<Pair<Integer, nodo_raton>> candidatos = new LinkedList<>();

        abiertos.add(new Pair<>(0, root_node));

        while (!abiertos.isEmpty()) {
            Pair<Integer, nodo_raton> v = abiertos.pop();
            cerrados.put(v.second.get_pos(), v.second);

            int nivel = v.first + 1;

            if (v.second.x == target.first && v.second.y == target.second) {
                candidatos.add(v);
                break;
            }

            if (v.second.explored) {
                //DOWN
                if (v.second.down) {
                    Pair<Integer, Integer> curPos = v.second.get_pos();
                    curPos.second--;

                    if (nodos_conocidos.containsKey(curPos)) {
                        nodo_raton w = nodos_conocidos.get(curPos);
                        Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                        if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                            abiertos.add(insert);
                            anteriores.put(w.get_pos(), v.second);
                        }
                    } else {
                        nodo_raton w = new nodo_raton(curPos);
                        Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                        if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                            abiertos.add(insert);
                            anteriores.put(w.get_pos(), v.second);
                            candidatos.add(insert);
                        }
                    }
                }

                //LEFT
                if (v.second.left) {
                    Pair<Integer, Integer> curPos = v.second.get_pos();
                    curPos.first--;

                    if (nodos_conocidos.containsKey(curPos)) {
                        nodo_raton w = nodos_conocidos.get(curPos);
                        Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                        if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                            abiertos.add(insert);
                            anteriores.put(w.get_pos(), v.second);
                        }
                    } else {
                        nodo_raton w = new nodo_raton(curPos);
                        Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                        if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                            abiertos.add(insert);
                            anteriores.put(w.get_pos(), v.second);
                            candidatos.add(insert);
                        }
                    }
                }
            }

            //RIGHT
            if (v.second.right) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.first++;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                } else {
                    nodo_raton w = new nodo_raton(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                        candidatos.add(insert);
                    }
                }
            }

            //UP
            if (v.second.up) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.second++;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                } else {
                    nodo_raton w = new nodo_raton(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (nivel <= limite && !cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                        candidatos.add(insert);
                    }
                }
            }
        }

        int targetIndex = get_min_index(candidatos, target, root_node);
        if (targetIndex == -1) {
            return null;
        }
        return candidatos.get(targetIndex).second;
    }
    


    @Override
    public void newCheese() {
        camino.clear();
    }
    
    /**
     * Emplea la búsqueda A* para hallar un camino entre el nodo root_node y la posición target
     * @param root_node nodo inicial.
     * @param target posición objetivo.
     * @param anteriores almacena el nodo antecesor a una posición dada.
     */
    void busqueda_A_star(nodo_raton root_node,Pair<Integer,Integer> target,HashMap<Pair<Integer,Integer>,nodo_raton>anteriores) 
    {
        List<Pair<Integer, nodo_raton>> abiertos = new LinkedList<>();
        HashMap<Pair<Integer, Integer>, nodo_raton> cerrados = new HashMap<>();
        abiertos.add(new Pair<>(0, root_node));
        while (!abiertos.isEmpty()) {
            int min = 999;
            int minIndex = 0;
            for (int i = 0; i < abiertos.size(); i++) {
                Pair<Integer, nodo_raton> w = abiertos.get(i);
                if (w.second.get_pos() == target) {
                    minIndex = i;
                    break;
                }
                int curValue = w.first + camino_minimo(w.second.get_pos(), target);
                if (curValue < min) {
                    min = curValue;
                    minIndex = i;
                }
            }

            Pair<Integer, nodo_raton> v = abiertos.get(minIndex);
            abiertos.remove(v);
            cerrados.put(v.second.get_pos(), v.second);
            int nivel = v.first + 1;

            if (v.second.x == target.first && v.second.y == target.second) {
                break;
            }

            //DOWN
            if (v.second.down) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.second--;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (!cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                }
            }

            //LEFT
            if (v.second.left) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.first--;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (!cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                }
            }

            //RIGHT
            if (v.second.right) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.first++;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (!cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                }
            }

            //UP
            if (v.second.up) {
                Pair<Integer, Integer> curPos = v.second.get_pos();
                curPos.second++;

                if (nodos_conocidos.containsKey(curPos)) {
                    nodo_raton w = nodos_conocidos.get(curPos);
                    Pair<Integer, nodo_raton> insert = new Pair<>(nivel, w);
                    if (!cerrados.containsKey(insert.second.get_pos())) {
                        abiertos.add(insert);
                        anteriores.put(w.get_pos(), v.second);
                    }
                }
            }
        }
    }


    @Override
    public void respawned() {
        camino.clear();
    }
    
    int camino_minimo(Pair<Integer, Integer> init, Pair<Integer, Integer> target) {
        return (Math.abs(target.first - init.first)) + (Math.abs(target.second - init.second));
    }

    /**
     * Dada una lista de nodos, emplea una función heurística para encontrar el nodo con menor valor y devuelve su índice.
     * @param nodes lista de nodos candidatos
     * @param target posición objetivo
     * @return Devuelve el índice de la lista nodes con menor valor.
     */
    private int get_min_index(List<Pair<Integer, nodo_raton>> nodes, Pair<Integer, Integer> target, nodo_raton init) {
        if (nodes.isEmpty()) {
            return -1;
        }

        int min_value = 99999;
        int min_pos = 0;

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).second == init) {
                continue;
            }
            if (nodes.get(i).second.get_pos() == target) {
                return i;
            }

            int curValue = get_value(nodes.get(i), target);

            if (curValue < min_value) {
                min_pos = i;
                min_value = curValue;
            }
        }

        return min_pos;
    }

    /**
     * El nodo de entrada es evaluado, respecto a target, mediante una función heurística y se devuelve el resultado
     * @param init nodo a calcular
     * @param target posición objetivo
     * @return Valor de la función heurística.
     */
    private int get_value(Pair<Integer, nodo_raton> init, Pair<Integer, Integer> target) {

        int dist_target = camino_minimo(init.second.get_pos(), target);
        int costeCasilla = init.first;

        return costeCasilla * 2 + dist_target;
    }

    
}