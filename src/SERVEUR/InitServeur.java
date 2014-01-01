/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SERVEUR;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Antoine
 */
public class InitServeur {

    private ServerSocket socketServeur;
    private Socket socketclient;
    private static int PortNumber = 5000;
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public InitServeur() {
    }

    public void connexion() {
        try {
            socketServeur = new ServerSocket(PortNumber);

            /**
             * Création d'une socket par client qui se connecte
             */
            while (true) {
                try {
                    System.out.println("Lancement du serveur \nAttente d'un client ! \n");
                    socketclient = socketServeur.accept();
                    int i = 0;
                    for (i = 0; i < maxClientsCount; i++) {
                        if (threads[i] == null) {
                            (threads[i] = new clientThread(socketclient, threads)).start();
                            break;
                        }
                    }
                    if (i == maxClientsCount) {
                        PrintStream os = new PrintStream(socketclient.getOutputStream());
                        os.println("Server trop occupé. Merci de patienter.");
                        os.close();
                        socketclient.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}