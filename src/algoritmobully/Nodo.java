package algoritmobully;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Hugo Espinosa
 */
public class Nodo implements Runnable {

    /**
     * ID del proceso
     */
    public final int PID;
    /**
     * ID del coordinador
     */
    public int coordinadorID;

    /**
     * IP de la máquina receptora/emisora
     */
    private String host;

    /**
     * Puerto de escucha
     */
    private int puerto;
    /**
     * Lista de nodos vecinos
     */
    public ArrayList<Nodo> listaNodos;
    /**
     * Banderas
     */
    public boolean estado, eleccionEnCurso;

    public Nodo(int PID, int puerto) {
        this.PID = PID;
        this.puerto = puerto;
        this.listaNodos = new ArrayList<>();
        this.coordinadorID = -1;
        this.estado = true;
        this.eleccionEnCurso = false;
    }
    
    /**
     * Obtiene el máximo PID de todos los nodos
     * @param nodosVecinos
     * @return máximo PID de la lista de nodos
     */
    public int getMaxPID(ArrayList<Nodo> nodosVecinos) {
        if (nodosVecinos == null || nodosVecinos.isEmpty()) {
            throw new IllegalArgumentException("La lista de nodos vecinos no puede estar vacía o ser nula.");
        }

        int maxPID = 0; // Asume que la lista no está vacía
        for (Nodo nodo : nodosVecinos) {
            if (nodo.isEstado() && (maxPID == 0 || nodo.getPID() > maxPID)) {
                maxPID = nodo.getPID();
            }
        }

        return maxPID;
    }

    /**
     * Inicia la elección cuando el coordinador falla
     *
     * @param nodosVecinos Lista de nodos vecinos
     */
    public void iniciarEleccion(ArrayList<Nodo> nodosVecinos) {

        this.listaNodos = nodosVecinos;

        for (Nodo nodo : listaNodos) {

            if (nodo.getPID() > this.PID && nodo.isEstado()) {
                enviaMensaje(nodo, "PID: " + this.PID + ", port: " + this.puerto + ": ELECCION");
            }

            if (this.PID == this.getMaxPID(listaNodos)) {
                anunciarNuevoCoordinador();
            }

        }
    }

    /**
     * Responde a la elección con un OK
     *
     * @param vecino
     * @param mensaje
     */
    public void responderEleccion(Nodo vecino, String mensaje) {
        enviaMensaje(vecino, mensaje);
    }

    /**
     * Anuncia al nuevo coordinador
     */
    public void anunciarNuevoCoordinador() {
        for (Nodo vecino : listaNodos) {
            if (vecino.getPID() != PID && vecino.isEstado()) {
                enviaMensaje(vecino, "PID: "  + this.PID + ", port: " + this.puerto + ": SOY EL NUEVO COORDINADOR");
            }
        }
    }

    public int getPID() {
        return PID;
    }

    public int getCoordinadorID() {
        return coordinadorID;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEleccionEnCurso(boolean eleccionEnCurso) {
        this.eleccionEnCurso = eleccionEnCurso;
    }

    public void setCoordinadorId(int coordinadorID) {
        this.coordinadorID = coordinadorID;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setCoordinadorID(int coordinadorID) {
        this.coordinadorID = coordinadorID;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public int getPuerto() {
        return puerto;
    }

    public ArrayList<Nodo> getListaNodos() {
        return listaNodos;
    }

    /**
     * Envia mensaje a los nodos vecinos
     *
     * @param vecino Socket vecino
     * @param mensaje Cadena de texto con el mensaje
     */
    public void enviaMensaje(Nodo vecino, String mensaje) {
        try {

            System.out.println(mensaje);

            Socket socket = new Socket(vecino.getHost(), vecino.getPuerto());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(mensaje);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(puerto);
            //System.out.println("Nodo " + PID + " escuchando en el puerto " + puerto);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ManejadorMensajes(socket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
