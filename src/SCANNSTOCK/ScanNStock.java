package SCANNSTOCK;

import API.Api;
import API.IHttpRequest;
import API.ShoppingApi;
import fr.sciencesu.sns.hibernate.jpa.Produit;
import fr.sciencesu.sns.hibernate.test.BDD;
import fr.sciencesu.sns.hibernate.test.InitDB;
import java.net.ConnectException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ScanNStock implements IScanNStock {

    //private ShoppingApi _shop;
    private IHttpRequest _shop;
    private static String _EAN;
    private Produit produit = null;
    private boolean connexion;
    
    InitDB database;

    public ScanNStock() {
        //_shop = new ShoppingApi();
        //Test.connection();
        database = new InitDB();
    }

    public ScanNStock(String isbn) {
        _shop = new Api();

        _EAN = isbn;

        connection();
    }

    public void connection() {
        if( null == database)
        {
         database = new InitDB();
        }
        try {
            database.connection();
        } catch (ConnectException ex) {
            System.out.println(ex.getMessage());
        }
        connexion = true;

    }

    public void deconnexion() {
        database.deconnection();
        connexion = false;

    }

    public boolean isConnected() {
        return connexion;
    }

    @Override
    public boolean getInfosProduct() {
        boolean b = false;
        //Recuperation des informations du produit
        b = _shop.runSearchProduct(_EAN);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScanNStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Recuperation du produit instancie
        produit = _shop.getProduct();

        return b;
    }
    
    void setDatePeremtion(Calendar date)
    {
        if(produit!=null)
        {
            produit.setDdp(date);
        }
    }
    
    public void setInfosProduct(Produit p)
    {
        produit = p;
    }

    @Override
    public boolean InsertToBase(String idstock) {
        boolean b = true;


        //Insertion des valeurs dans la base
       // b = BDD.Create("Produit", produit);
        //BDD.Update(produit,idstock);
        //BDD.UpdateProduit("Produit", //TABLE
           //     produit.getNom(), //CHAMP
             //   "produits_stock_stocks_id", //ID 
             //   idstock);//VALEUR
        
        database.Create(produit);
        database.Update(produit, Integer.getInteger(idstock));
        //basePersoScanNStock.insertInto(_tableName, produit);

        //D�connection de la base
        deconnexion();
        //_basePersoScanNStock.deconnection();

        return b;
    }
    
    public boolean InsertToBase(String idstock, int nb) {
        boolean b = true;

        for (int i = 0; i < nb; i++) {            
        
            connection();
            //Insertion des valeurs dans la base
           // b = BDD.Create("Produit", produit);

            //BDD.UpdateProduit("Produit", //TABLE
                 //   produit.getNom(), //CHAMP
                  //  "produits_stock_stocks_id", //ID 
                  //  idstock);//VALEUR

            database.Create(produit);
            database.Update(produit, Integer.getInteger(idstock));

            //basePersoScanNStock.insertInto(_tableName, produit);

            //D�connection de la base
            deconnexion();
        }
        //_basePersoScanNStock.deconnection();

        return b;
    }

    public static String get_EAN() {
        return _EAN;
    }

    public Produit getProduct() {
        return produit;
    }
}
