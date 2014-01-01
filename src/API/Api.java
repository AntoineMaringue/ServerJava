/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import static API.IHttpRequest.APIOpenFoodFacts;
import fr.sciencesu.sns.hibernate.jpa.Produit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author antoi_000
 */
public class Api implements IHttpRequest {

    public static String _informations = "";
    public static Produit _produit = null;

    public Api() {
    }

    @Override
    public boolean runSearchProduct(String ean) {
        Set<Map.Entry<String, String>> informationsProduit = null;
        ean = ean.split("/")[0];
        IParseur parseur = new ParseurJSON();
        informationsProduit = parseur.getInformationsApiOpenFoodFacts(requestHttp(ean, TypeFormat.json)).entrySet();

        if (informationsProduit.isEmpty()) {
            return false;
        } else {
            createProduit(informationsProduit);
            return true;
        }


    }

    @Override
    public void createProduit(Set<Map.Entry<String, String>> informationsProduit) {
        //Affichage des donnees parsee
        for (Map.Entry<String, String> en : informationsProduit) {
            String cle = en.getKey();
            String valeur = en.getValue();

            System.out.println("KEY = " + cle);
            System.out.println("VALUE = " + valeur);

            //Construction du produit
            if (_produit == null) {
                _produit = new Produit();
            }
            switch (cle) {
                case "brand":
                    _produit.setNom(valeur);
                    break;
                case "modificationTime":
                    _produit.setDdp(toCalendar(valeur.split("T")[0], "yyyy/mm/dd"));
                    break;
                case "creationTime":
                    _produit.setDluo(toCalendar(valeur.split("T")[0], "yyyy/mm/dd"));
                    break;
                case "origins":
                    _produit.setProvenance(valeur);
                    break;
                case "description":
                    _produit.setDescription(valeur);
                    break;
                case "price":
                    _produit.setPrix(Double.parseDouble(valeur));
                    break;
                case "unite":
                    _produit.setUnite(valeur);
                    break;
                case "contenance":
                    _produit.setContenance(Double.parseDouble(valeur));
                    break;
                case "img":
                    _produit.setImg(valeur);
                    break;
            }
        }
    }

    @Override
    public String requestHttp(String EAN, TypeFormat format) {
        HttpURLConnection connection = null;
        BufferedReader serverResponse = null;


        String replyServer = "";

        String url = APIOpenFoodFacts//France
                + EAN + "." + format.name();               //code produit


        try {

            //OUVERTURE CONNECTION  
            connection = (HttpURLConnection) new URL(url).openConnection();

            //RESPONSE BUFFERISEE
            serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //LECTURE DE LA RESPOSNE  
            String line;
            while ((line = serverResponse.readLine()) != null) {
                replyServer += line + "\n";
            }
            System.out.println(replyServer);
        } catch (MalformedURLException mue) {
            System.err.println("L'uri n'est pas construite correctement ! " + mue.getMessage());
        } catch (IOException ioe) {
            System.err.println("Impossible d'atteindre l'API , reessayé plus tard ! " + ioe.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (serverResponse != null) {
                try {
                    serverResponse.close();
                } catch (Exception ex) {
                }
            }
        }
        return replyServer;
    }

    public String requestHttp(String EAN) {
        HttpURLConnection connection = null;
        BufferedReader serverResponse = null;


        String replyServer = "";

        String url = APIOpenFoodFacts//France
                + EAN + "." + TypeFormat.json.name();               //code produit


        try {

            //OUVERTURE CONNECTION  
            connection = (HttpURLConnection) new URL(url).openConnection();

            //RESPONSE BUFFERISEE
            serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //LECTURE DE LA RESPOSNE  
            String line;
            while ((line = serverResponse.readLine()) != null) {
                replyServer += line + "\n";
            }
            System.out.println(replyServer);
        } catch (MalformedURLException mue) {
            System.err.println("L'uri n'est pas construite correctement ! " + mue.getMessage());
        } catch (IOException ioe) {
            System.err.println("Impossible d'atteindre l'API , reessayé plus tard ! " + ioe.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (serverResponse != null) {
                try {
                    serverResponse.close();
                } catch (Exception ex) {
                }
            }
        }
        return replyServer;
    }

    /**
     * Transforme string en calendar
     *
     * @param dateString : la date récupérée par le parseur JSON
     * @param pattern : le format que la date doit avoir pour être accepter dans
     * la base de données
     * @return : un objet java.util.Calendar
     */
    public Calendar toCalendar(String dateString, String pattern) {
        Calendar cal = Calendar.getInstance();
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("yyyy-mm-dd");
            date = (Date) formatter.parse(dateString);

            cal.setTime(date);

        } catch (ParseException e) {
        }
        return cal;
    }

    @Override
    public Produit getProduct() {
        return _produit;
    }
}
