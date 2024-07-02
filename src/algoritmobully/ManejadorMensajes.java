package algoritmobully;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Hugo Espinosa
 */
public class ManejadorMensajes implements Runnable {

    private Socket socket;
    private Nodo nodo;

    public ManejadorMensajes(Socket socket, Nodo nodo) {
        this.socket = socket;
        this.nodo = nodo;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String mensaje = in.readUTF();

            if (mensaje.length() >= 18) {
                int PIDremitente = Integer.parseInt(mensaje.substring(5, 6));
                int puertoRemitente = Integer.parseInt(mensaje.substring(14, 18));
                String respuestaOk = "PID: " + nodo.getPID() + ", port: " + nodo.getPuerto() + ": OK";

                if (mensaje.contains("ELECCION")) {
                    nodo.responderEleccion(new Nodo(PIDremitente, puertoRemitente), respuestaOk);
                    return;
                }

                if (mensaje.contains("COODINADOR")) {
                    nodo.setCoordinadorId(PIDremitente);
                    return;
                }

                if (mensaje.contains("OK")) {
                    Nodo nodoRemitente = new Nodo(PIDremitente, puertoRemitente);
                    nodoRemitente.iniciarEleccion(nodo.getListaNodos());
                    return;
                }
            } else {
                System.out.println("Received message is shorter than expected: " + mensaje);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
