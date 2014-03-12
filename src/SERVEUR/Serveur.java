/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SERVEUR;

import fr.sciencesu.sns.hibernate.jpa.Produit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Antoine
 */
public class Serveur {
    
    private ServerSocket providerSocket;
    private  Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;

    public static void main(String args[]) {
        Serveur server = new Serveur();
        while (true) {
            server.run();
        }
    }
    

    Serveur() {
    }

    void run() {
        try {
            //Initialisation de la socket serveur
            providerSocket = new ServerSocket(5000, 10);
            //2. Attente de connexion d'un client
            System.out.println("Attente de connexion d'un client SNS");
            connection = providerSocket.accept();
            System.out.println("Connexion de l'hôte " + connection.getInetAddress().getHostName());
            //3. recupération des infos d'entrées / sorties
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection avec le serveur établit");
            //4. Communication avec les traitements envoyés par le client
            do {
                try {
                    message = (String) in.readObject();
                    if (!message.equals("_")) {
                        System.out.println("client>" + message);
                    }
                    if (message.equals("bye")) {
                        sendMessage("bye");
                    } else if (message.equals("_")) {
                        sendNothing("");
                    } else if (message.contains("Validation")) {
                        //String idmdp = message.split(";")[1];
                        //sendMessage("traitement" + idmdp);
                        String asso = message.split(",")[1];
                        Traitement t = new Traitement();
                        message = t.getIdStock(asso);
                        sendMessage(message);
                    }
                    else if (message.contains("new")) {
                        String qte = message.split(",")[1].split(";")[0]; 
                        String productInfos = message.split(";")[1].split("::")[0];                
                        String isbn = message.split("::")[1]; 
                        String idstock = message.split("::")[2];
                        
                        Traitement t = new Traitement(isbn,idstock);
                        System.out.println("Recherche des informations du produit");
                        t.setProduit(buildProduct(productInfos));
                        //Création du produit
                        //Message renvoyé au téléphone CREATION AUTO ou PASSAGE FORM SITE

                        System.out.println("Création de l'entitée produit");
                        //Insertion dans la base
                        //Message renvoyé au téléphone Si CREATION AUTO --> INSERTION OK | KO
                        System.out.println("Insertion dans la base");
                        boolean insertProduct = false;
                        
                        //boucle sur le nombre de produits à ajouter dans la base de doonées
                        for (int i = 0; i < Integer.valueOf(qte); i++) {
                             insertProduct = t.insertToBdd();
                        }                        
                        message += (insertProduct) ? "Insertion "+ qte +" fois dans la base \n" : "";

                        sendMessage(message);
                    }
                     else if (message.contains("produits")) {
                        String isbn = message.split(",")[1];                
                        Traitement t = new Traitement(isbn);
                        System.out.println("Recherche des informations du produit");
                        boolean searchInfos = t.searchInfos();
                        if (searchInfos) {
                            //Création du produit
                            //Message renvoyé au téléphone CREATION AUTO ou PASSAGE FORM SITE
                            message = "PRODUCTS"+t.getProduit().toString();
                            System.out.println("Envoi produit au client");
                        }
                        sendMessage(message);
                    }
                     
                    else if (message.contains("associations")) {
                        ArrayList<String> lst = new ArrayList<>();
                        Traitement t = new Traitement();
                        lst.addAll(t.getListSites());
                        for (String string : lst) {
                            message += string + ";";
                        }
                        sendMessage("ASSS" + message);
                    } else if (message.contains("isbn")) {
                        String isbn = message.split(",")[1].split(";")[0]; 
                        String idstock = message.split(",")[1].split(";")[1];                        
                        Traitement t = new Traitement(isbn,idstock);
                        System.out.println("Recherche des informations du produit");
                        boolean searchInfos = t.searchInfos();
                        if (searchInfos) {
                            //Création du produit
                            //Message renvoyé au téléphone CREATION AUTO ou PASSAGE FORM SITE
                            message = "Infos sur le produit trouvées \n ";
                            System.out.println("Création de l'entitée produit");
                            //Insertion dans la base
                            //Message renvoyé au téléphone Si CREATION AUTO --> INSERTION OK | KO
                            System.out.println("Insertion dans la base");
                            boolean insertProduct = t.insertToBdd();
                            message += (insertProduct) ? "Insertion dans la base \n" : "";

                        } else {
                            message += "Le produit scanné n'a pas été trouvé par l'api google,\n "
                                    + "merci d'insérer le produit via l'interface web !\n ";
                        }
                        sendMessage(message);
                    } else {
                        sendMessage(message + "OK");
                    }
                    
                } catch (ClassNotFoundException classnot) {
                    System.err.println("Format inconnu");
                }
            } while (!message.equals("bye"));
        } catch (IOException ioException) {
        } finally {
            //4: Femeture connexion
            try {
                in.close();
                out.close();
                providerSocket.close();
            } catch (IOException ioException) {
                System.err.println(ioException.getMessage());
            }
        }

   
    }
    
    
    /**
     * Création d'un produit n'ayant pas été trouvé par le scan
     * 
     * @param productInfos
     * @return 
     */
     private Produit buildProduct(String productInfos) {
         String  nom , marque , description , prix , unite , contenance, sdate;
         nom = productInfos.split("\n")[0];
         marque = productInfos.split("\n")[1];
         description = productInfos.split("\n")[2];
         prix = productInfos.split("\n")[3];
         unite = productInfos.split("\n")[4];
         contenance = productInfos.split("\n")[5];
         sdate = productInfos.split("\n")[6];
         Calendar date = Calendar.getInstance();
         if(!sdate.contains("null"))
         {
             date.set(Integer.valueOf(sdate.split("/")[0]), 
                     Integer.valueOf(sdate.split("/")[1]),
                     Integer.valueOf(sdate.split("/")[2]));
         }
         double dprix = prix.contains("null")?0.0:Double.parseDouble(prix);
         double dcontenance = unite.contains("null")?0.0:Double.parseDouble(unite);
         Produit p = new Produit(nom, dprix, contenance,dcontenance,"");
         p.setDdp(date);
         p.setImg("");
         return p;
     }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    private void sendNothing(String string) {
        try {
            out.writeObject(string);
            out.flush();
            //System.out.println("server>" + msg);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }
}
