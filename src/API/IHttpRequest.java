/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import fr.sciencesu.sns.hibernate.jpa.Produit;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author antoi_000
 */
public interface IHttpRequest 
{
    /**
     * 
     */
    public final static String URIOpenFoodFacts = "http://fr.openfoodfacts.org/produit/";
    /**
     * 
     */
    public final static String APIOpenFoodFacts = "http://fr.openfoodfacts.org/api/v0/produit/";
    
    public enum TypeFormat
    {
        json,xml,csv
    }
    
    /**
     * 
     * @param ean
     * @return 
     */
    public boolean runSearchProduct(String ean);

    /**
     * 
     * @param informationsProduit 
     */
    public void createProduit(Set<Map.Entry<String, String>> informationsProduit);
    
    /**
     * 
     * @param EAN
     * @param format
     * @return 
     */
    public String requestHttp(String EAN, TypeFormat format);    
    public String requestHttp(String EAN);

    /**
     * Transforme string en calendar
     *
     * @param dateString : la date récupérée par le parseur JSON
     * @param pattern : le format que la date doit avoir pour être accepter dans la base de données
     * @return : un objet java.util.Calendar
     */
    public Calendar toCalendar(String dateString, String pattern);
    
    public Produit getProduct();
    
}
