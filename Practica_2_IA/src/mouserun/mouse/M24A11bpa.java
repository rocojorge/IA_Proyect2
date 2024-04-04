package mouserun.mouse;

import mouserun.game.Cheese;
import mouserun.game.Grid;
import mouserun.game.Mouse;


import java.util.*;


/**
 * Esta clase representa un ratón explorador que intenta visitar todas las celdas posibles del laberinto.
 * Utiliza dos mapas para almacenar las celdas visitadas: celdas_frecuentes y celdas_totales.
 * El mapa celdas_frecuentes se reinicia cada vez que el ratón muere, mientras que el mapa celdas_totales se mantiene durante toda la ejecución.
 * El ratón utiliza una pila de movimientos para volver atrás cuando se queda sin opciones, y hace return 0 cuando la pila está vacía.
 */
public class M24A11bpa extends Mouse {
    
    //Clase de soporte
    
    class Node{
        public final Grid actual;
        public final Grid padre;

        private Node(Grid a, Grid p){
            this.actual=a;
            this.padre=p;
        }
    }


    
    // Constante que indica que no hay movimientos posibles
    private final Integer SIN_MOVIMIENTOS = 0;

    // Mapa que almacena las celdas visitadas desde el último respawn
    private HashMap<Integer, Grid> celdas_frecuentes;
    // Mapa que almacena todas las celdas visitadas durante la ejecución
    private HashMap<Integer, Grid> celdas_totales;
    // Pila que almacena los movimientos realizados por el ratón
    private LinkedList<Integer> movimientos;
    //Lista de celdas cerradas
    private HashMap<Integer,Node> cerradas;
    //Lista nodo de celdas abiertas
    private LinkedList<Node> abiertas;
    //El camino de grid
    private LinkedList<Grid> camino;
    //Lista de los vecinos
    private LinkedList<LinkedList<Grid>> pila_vecinos;
    //Lista de nodos principales por niveles
    private LinkedList<Grid> nodos_principales;
    //Lista Nodos intersección
    private HashMap<Grid,LinkedList<Integer>> nodos_interseccion;
    //Profundida del arbol
    private int profundidad =0;
    private int num_elemento = 0;

    
    
    /**
     * Constructor de la clase Segunda_Prueba.
     * Inicializa los atributos con valores vacíos y asigna el nombre "Explorador" al ratón.
     */
    public M24A11bpa(){
        super("M24A11_BPA");
        this.movimientos=new LinkedList<>();
        this.celdas_frecuentes=new HashMap<>();
        this.celdas_totales=new HashMap<>();
        this.cerradas = new HashMap<>();
        this.camino = new LinkedList<>();
        this.abiertas = new LinkedList();
        this.pila_vecinos=new LinkedList<>();
        this.profundidad= 0;
        this.num_elemento=0;
        
    }

    /**
     * Método que determina el siguiente movimiento del ratón en función de la celda actual.
     * Añade la celda actual a los mapas de memoria y comprueba si hay movimientos posibles.
     * Si los hay, devuelve uno de ellos al azar, evitando los que ya ha visitado.
     * Si no los hay, devuelve el método sin_movimientos() que gestiona la pila de movimientos.
     * @param currentGrid La celda actual del ratón
     * @param cheese El queso actual
     * @return Un entero que representa el movimiento elegido (Mouse.UP, Mouse.DOWN, Mouse.LEFT, Mouse.RIGHT o Mouse.BOMB)
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        agrega_celda(currentGrid);
        Integer movimiento=posibles_movimientos(currentGrid, cheese);
        Grid cheese_grid = new Grid(cheese.getX(),cheese.getY());
       
        for(int i=0;i<celdas_totales.size();i++){
            if (cheese_grid==celdas_totales.get(i)){
                BPA(currentGrid,cheese);
                return seguir_camino(camino,currentGrid);
                
            }
        }
        if (!movimiento.equals(SIN_MOVIMIENTOS))
            return movimiento;
        else{
            return sin_movimientos();
        }
            
    }

    public void BPA(Grid currentGrid, Cheese cheese){
        Grid inicial = celdas_frecuentes.get(identificador_celda(cheese.getX(),cheese.getY()));
        abiertas.add(new Node(inicial,null));
        while(true){
            Node procesando = abiertas.pollFirst();//////////////////////IMPORTANTE
            assert procesando != null;
            cerradas.put(identificador_celda(procesando.actual.getX(),procesando.actual.getY()),procesando);
            if(procesando.actual.getX() == currentGrid.getX() && procesando.actual.getY()== currentGrid.getY()){
                while(true){
                    if(procesando.padre!=null) {
                        camino.add(procesando.padre);
                        procesando=cerradas.get(identificador_celda(procesando.padre.getX(),procesando.padre.getY()));
                    }else{
                        return;
                    }
                }
            }
            if(procesando.actual.canGoUp() &&
                    celdas_frecuentes.containsKey(identificador_celda(procesando.actual.getX(),procesando.actual.getY()+1)) &&
                    !cerradas.containsKey(identificador_celda(procesando.actual.getX(),procesando.actual.getY()+1))){
                Grid sucesor=celdas_frecuentes.get(identificador_celda(procesando.actual.getX(),procesando.actual.getY()+1));
                abiertas.add(new Node(sucesor,procesando.actual));
            }
            if(procesando.actual.canGoDown() &&
                    celdas_frecuentes.containsKey(identificador_celda(procesando.actual.getX(),procesando.actual.getY()-1)) &&
                    !cerradas.containsKey(identificador_celda(procesando.actual.getX(),procesando.actual.getY()-1))){
                Grid sucesor=celdas_frecuentes.get(identificador_celda(procesando.actual.getX(),procesando.actual.getY()-1));
                abiertas.add(new Node(sucesor,procesando.actual));
            }
            if(procesando.actual.canGoLeft() &&
                    celdas_frecuentes.containsKey(identificador_celda(procesando.actual.getX()-1,procesando.actual.getY())) &&
                    !cerradas.containsKey(identificador_celda(procesando.actual.getX()-1,procesando.actual.getY()))){
                Grid sucesor=celdas_frecuentes.get(identificador_celda(procesando.actual.getX()-1,procesando.actual.getY()));
                abiertas.add(new Node(sucesor,procesando.actual));
            }
            if(procesando.actual.canGoRight() &&
                    celdas_frecuentes.containsKey(identificador_celda(procesando.actual.getX()+1,procesando.actual.getY())) &&
                    !cerradas.containsKey(identificador_celda(procesando.actual.getX()+1,procesando.actual.getY()))){
                Grid sucesor=celdas_frecuentes.get(identificador_celda(procesando.actual.getX()+1,procesando.actual.getY()));
                abiertas.add(new Node(sucesor,procesando.actual));
            }
        }
    }
    
    private int seguir_camino(LinkedList<Grid> camino, Grid currentGrid){
        
        if (currentGrid.getX()+1==camino.pollFirst().getX()){
            camino.pollFirst();
            return Mouse.RIGHT;}
        if (currentGrid.getX()-1==camino.pollFirst().getX()){
            camino.pollFirst();
            return Mouse.LEFT;}
        if (currentGrid.getY()+1==camino.pollFirst().getX()){
            camino.pollFirst();
            return Mouse.UP;}
        if (currentGrid.getY()+1==camino.pollFirst().getX()){
            camino.pollFirst();
            return Mouse.DOWN;}
        return SIN_MOVIMIENTOS;
        
    }
    
    /**
     * Método que devuelve una lista de los movimientos posibles desde una celda_actual.
     * Comprueba si la celda_actual tiene salidas en las cuatro direcciones y las añade a una lista.
     * Si la lista no está vacía, recorre sus elementos y elimina los que corresponden a celdas ya visitadas.
     * @param celda_actual La celda actual del ratón
     * @return Un entero que representa un movimiento posible al azar, o SIN_MOVIMIENTOS si no hay ninguno
     */
    private Integer posibles_movimientos(Grid celda_actual, Cheese cheese)
    {
        ArrayList<Integer> posibles = new ArrayList<>();
        if (celda_actual.canGoUp()) posibles.add(Mouse.UP);
        if (celda_actual.canGoRight()) posibles.add(Mouse.RIGHT);
        if (celda_actual.canGoDown()) posibles.add(Mouse.DOWN);
        if (celda_actual.canGoLeft()) posibles.add(Mouse.LEFT);
        if (!posibles.isEmpty()){
            if(celda_actual.getX()+1==cheese.getX()&&celda_actual.getY()==cheese.getY()) return Mouse.RIGHT;
            if(celda_actual.getX()-1==cheese.getX()&&celda_actual.getY()==cheese.getY()) return Mouse.LEFT;
            if(celda_actual.getX()==cheese.getX()&&celda_actual.getY()+1==cheese.getY()) return Mouse.UP;
            if(celda_actual.getX()==cheese.getX()&&celda_actual.getY()-1==cheese.getY()) return Mouse.DOWN;
            
            for (int i=0;i<posibles.size(); i++){
                Integer movimiento_ret = posibles.get(i);
                if (!celda_visitada(movimiento_ret, celda_actual)){
                    movimientos.push(movimiento_inverso(movimiento_ret));
                    return movimiento_ret;
                }
            }
        }
        return SIN_MOVIMIENTOS;
    }
    
    /**
     * Método que comprueba si una celda en una dirección determinada ha sido visitada o no.
     * Calcula las coordenadas de la celda en la dirección indicada y busca su identificador_celda en el mapa celdas_frecuentes.
     * @param direccion La dirección a comprobar (Mouse.UP, Mouse.DOWN, Mouse.LEFT o Mouse.RIGHT)
     * @param celda La celda actual del ratón
     * @return Un booleano que indica si la celda ha sido visitada (true) o no (false)
     */
    private Boolean celda_visitada(Integer direccion,Grid celda){
        
        int x=celda.getX();
        int y=celda.getY();
        switch (direccion)
        {
            case Mouse.UP:
                y++;
                break;
            case Mouse.DOWN:
                y--;
                break;
            case Mouse.LEFT:
                x--;
                break;
            case Mouse.RIGHT:
                x++;
                break;
        }
        boolean to_ret=(celdas_frecuentes.get(identificador_celda(x,y)) == null);
        return !(to_ret);
    }
    
    private LinkedList<Grid> obtener_ruta(Grid currentGrid,Grid target){
        LinkedList<Grid> ruta = new LinkedList<>();
       

        return ruta;
    }
    
    /**
     * Método que devuelve el movimiento movimiento_inverso a uno dado.
     * Utiliza un switch para asignar el valor correspondiente según la dirección.
     * @param direccion La dirección a invertir (Mouse.UP, Mouse.DOWN, Mouse.LEFT o Mouse.RIGHT)
     * @return Un entero que representa el movimiento movimiento_inverso
     */
    private Integer movimiento_inverso(Integer direccion){
        
        Integer movimiento_inverso = 0;

        switch (direccion){
            case Mouse.UP:
                movimiento_inverso=Mouse.DOWN;
                break;
            case Mouse.LEFT:
                movimiento_inverso=Mouse.RIGHT;
                break;
            case Mouse.RIGHT:
                movimiento_inverso=Mouse.LEFT;
                break;
            case Mouse.DOWN:
                movimiento_inverso=Mouse.UP;
                break;
        }
        return movimiento_inverso;
    }

    /**
     * Método que se ejecuta cuando el ratón muere y vuelve a aparecer en el laberinto.
     * Reinicia el mapa celdas_frecuentes y la pila de movimientos, manteniendo el mapa celdas_totales.
     */
    @Override
    public void respawned()
        {
        celdas_frecuentes=new HashMap<>();
        movimientos=new LinkedList<>();
    }

    /**
     * Método que se ejecuta cuando aparece un nuevo queso en el laberinto.
     * Reinicia el mapa celdas_frecuentes y la pila de movimientos, manteniendo el mapa celdas_totales.
     */
    @Override
    public void newCheese() {
        camino.clear();
        cerradas.clear();
        abiertas.clear();
        
    }

    /**
     * Método que se ejecuta cuando el ratón no tiene movimientos posibles.
     * Si la pila de movimientos no está vacía, devuelve el último movimiento inverso almacenado.
     * Si la pila está vacía, reinicia el mapa celdas_frecuentes y devuelve 0.
     * @return Un entero que representa el movimiento elegido (Mouse.UP, Mouse.DOWN, Mouse.LEFT, Mouse.RIGHT o Mouse.BOMB)
     */
    private Integer sin_movimientos(){
        if ( !movimientos.isEmpty() ){
            return movimientos.pop();
        } else 
        {
            celdas_frecuentes = new HashMap<>();
        }
        return 0;
    }

    /**
     * Método que añade una celda a los mapas de memoria.
     * Genera una identificador_celda única para la celda a partir de sus coordenadas x e y, y la añade al mapa celdas_frecuentes si no está presente.
     * Si la celda no está en el mapa celdas_totales, la añade y llama al método incExploredGrids() para incrementar el contador de celdas exploradas.
     * @param celda La celda a añadir
     */
    private void agrega_celda( Grid celda ){
        
        int x=celda.getX();
        int y=celda.getY();

        celdas_frecuentes.putIfAbsent( identificador_celda(x, y), celda );

        if (celdas_totales.get(identificador_celda(x, y)) == null) {
            celdas_totales.put(identificador_celda(x, y), celda);
            incExploredGrids();
        }
    }

    /**
     * Método que genera una identificador_celda única para una celda a partir de sus coordenadas x e y.
     * Multiplica la coordenada x por 99999 y le suma la coordenada y, asumiendo que ambas son positivas y menores que 100.
     * @param x La coordenada x de la celda
     * @param y La coordenada y de la celda
     * @return Un entero que representa la identificador_celda de la celda
     */
    private Integer identificador_celda( int x, int y ){
        int to_ret= (x*99999+y);
        return to_ret;
    }    
}