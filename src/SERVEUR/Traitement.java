/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SERVEUR;


import API.ShoppingApi;
import SCANNSTOCK.ScanNStock;
import fr.sciencesu.sns.hibernate.jpa.Produit;
import fr.sciencesu.sns.hibernate.test.BDD;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author Antoine
 */
public class Traitement implements Runnable
{
    /**
     * Les différentes fonctions permettant de faire des traitements entre serveur/bdd
     */
    private ScanNStock sns = null;
    private boolean finish;
    private String idStock;

    /**
     * Constructeur par défaut
     */
    public Traitement() 
    {
        finish = false;
    }     

    /**
     * Constructeur d'un traitement avec connexion à la base de données
     * 
     * @param isbn : le code du produit scanne par le telephone
     */
    public Traitement(String isbn) 
    {
        sns = new ScanNStock(isbn);
        finish = false;
    }    
    
    public Traitement(String isbn, String idStock) 
    {
        sns = new ScanNStock(isbn);
        this.idStock = idStock;
        finish = false;
    }    
    
    @Override
    public void run() 
    {       
        System.out.println("Exécution du traitement dans un thread détaché\n");
    }
    
    public void insertProduct(int qte)
    {
        //BDD.create(qte);
    }

    /**
     * Récupération dans la base de données des sites existant
     * 
     * @return : la liste des noms de site présent dans la base de données
     */
    public Collection<String> getListSites() 
    {
        //Déclaration de la collection de site
        Collection<String> col = new ArrayList<>();
        
        //Connection à la base
        BDD.connection();
        
        //Création de la requéte
        //Site unsite = new Site();
        //unsite.setId(1);
        
        //BDD.CreateAssociation("Association1", "adresse", "CP", "VILLE", "TEL", "MAIL");
        //BDD.CreateAssociation("Association2", "adresse", "CP", "VILLE", "TEL", "MAIL");
        //BDD.CreateSite("name1", "pays1", "region1", "dep1");
        //BDD.CreateSite("name2", "pays1", "region1", "dep1");
        //BDD.CreateSite("name3", "pays1", "region1", "dep1");
        //BDD.CreateSite("name4", "pays1", "region1", "dep1");
        
        String lstSite = BDD.ReadAssociation("Association");
        //incorporation des données de la bdd dans la collection temporaitre
        for (String string : lstSite.split("\n")) {
            col.add(string);
        }
        
        //Déconnexion de la base
        BDD.deconnection(); 
        
        finish = true;
        
        return col;
    }

    /**
     * Les infos du produit scanne ont bien ete trouve
     * 
     * @return : vrai = ok , faux = ko
     */
    public boolean searchInfos() 
    {
        return sns.getInfosProduct();
    }
    

    /**
     * Test une insertion dans la base de données
     * 
     * @return vrai si le produit inséré
     */
   public boolean insertToBdd() 
    {
        return sns.InsertToBase(idStock);
    }
   
   /**
    * 
    * @param qte : nombre d'éléments à ajouter
    * @return 
    */
   public boolean insertToBdd(int qte) 
    {
        return sns.InsertToBase(idStock);
    }

    public boolean validateUserAndSite(String id, String mdp, String site) 
    {
        boolean b = false;
        BDD.connection();
        
        //b = BDD.ValideUser(id,mdp);
        
        BDD.deconnection();
        
        return b;
        
    }

    String getIdStock(String asso)
    {
        String idStock = "";
        
        BDD.connection();
        
        idStock = BDD.ReadAssociationWithStock("associations",asso);
        
        BDD.deconnection();
        
        return idStock;
    }

    Produit getProduit() {
        return sns.getProduct();
    }
    
     void setProduit(Produit p) {
        sns.setInfosProduct(p);
    }
    
}
