package algoritmobully;

import java.util.ArrayList;

/**
 *
 * @author Hugo Espinosa
 */
public class Test {

    public static void main(String[] args) {

        Nodo nodoLider = new Nodo(6, 6006);
        Nodo nodo1 = new Nodo(1, 6001);
        Nodo nodo2 = new Nodo(2, 6002);
        Nodo nodo3 = new Nodo(3, 6003);
        //Nodo nodo4 = new Nodo(4, 6004);
        //Nodo nodo5 = new Nodo(5, 6005);

        //Lista de todos los nodos
        ArrayList<Nodo> listaNodos = new ArrayList<>();
        listaNodos.add(nodoLider);
        listaNodos.add(nodo1);
        listaNodos.add(nodo2);
        listaNodos.add(nodo3);
        //listaNodos.add(nodo4);
        //listaNodos.add(nodo5);

        //Inicialización de nodos
        new Thread(nodoLider).start();
        new Thread(nodo1).start();
        new Thread(nodo2).start();
        new Thread(nodo3).start();
        //new Thread(nodo4).start();
        //new Thread(nodo5).start();

        //Simular caída del nodo líder
        nodoLider.setEstado(false);
        nodo1.iniciarEleccion(listaNodos);

        //nodo3.iniciarEleccion(listaNodos);
    }
}
