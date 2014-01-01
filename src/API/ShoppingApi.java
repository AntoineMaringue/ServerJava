package API;

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
import java.util.Map.Entry;
import java.util.Set;

public class ShoppingApi {

    private final static String googleUri = "https://www.googleapis.com/shopping/search/v1/public/products?key=";
    private final static String keyGoogle = "AIzaSyAc7_OzUpgYJ2tg0_XLRY2FtTpbGpL1yIU";
    private final static String countryName = "FR";
    private static String informations;
    private static Produit _produit;

    public ShoppingApi() {
        informations = "";
    }

    public static boolean runSearchProduct(String ean) {
        IParseur parseur = null;
        Set<Entry<String, String>> informationsProduit = null;
        ean = ean.split("/")[0];
        parseur = new ParseurJSON();
        informationsProduit = parseur.getInformations(requestHttpForGoogle(ean, "JSON")).entrySet();

        if (informationsProduit.isEmpty()) 
        {
            return false;
        } else {
            createProduit(informationsProduit);
            return true;
        }


    }

    private static void createProduit(Set<Entry<String, String>> informationsProduit) {
        //Affichage des donnees parsee
        for (Entry<String, String> en : informationsProduit) {
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
            }
        }
    }

    private  static void errorProduit() {
        //RETOURNER UNE ERREUR SI LE PRODUIT RECHERCHE NE RETOURNE AUCUN RESULTAT
    }

    public static Produit getProduit() {
        return _produit;
    }

    public static String getInformations() {
        return informations;
    }

    private static String requestHttpForGoogle(String EAN, String format) {
        HttpURLConnection connection = null;
        BufferedReader serverResponse = null;

        String replyServer = "";

        String url = googleUri              //uri de l'api
                + keyGoogle                 //google key
                + "&country=" + countryName //France
                + "&q=" + EAN               //code produit
                + "&maxResults=1"           //1 seul résultat
                //+ "&restrictBy=gin=" + EAN  //seulement ce produit la
                + "&alt=" + format;         //json

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
            System.err.println("L'uri n'est pas construite correctement ! "+ mue.getMessage());
        } catch (IOException ioe) {
            System.err.println("Impossible d'atteindre l'API Shopping, reessayé plus tard ! "+ ioe.getMessage());
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
     * @param pattern : le format que la date doit avoir pour être accepter dans la base de données
     * @return : un objet java.util.Calendar
     */
    public static Calendar toCalendar(String dateString, String pattern) {
        /*try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = format.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }*/Calendar cal=Calendar.getInstance();
        try {  
            DateFormat formatter ; 
            Date date ; 
             formatter = new SimpleDateFormat("yyyy-mm-dd");
             date = (Date)formatter.parse(dateString); 
            
            cal.setTime(date);
            
        }catch(ParseException e)
        {
            
            
        }return cal;}
}
