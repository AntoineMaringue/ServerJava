package SCANNSTOCK;

import API.Api;
import API.IHttpRequest;
import API.ShoppingApi;
import fr.sciencesu.sns.hibernate.jpa.Produit;
import fr.sciencesu.sns.hibernate.test.BDD;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ScanNStock implements IScanNStock {

    //private ShoppingApi _shop;
    private IHttpRequest _shop;
    private static String _EAN;
    private Produit produit = null;
    private boolean connexion;

    public ScanNStock() {
        //_shop = new ShoppingApi();
        //Test.connection();
    }

    public ScanNStock(String isbn) {
        _shop = new Api();

        _EAN = isbn;

        connection();
    }

    public void connection() {
        BDD.connection();
        connexion = true;

    }

    public void deconnexion() {
        BDD.deconnection();
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
    
    public void setInfosProduct(Produit p)
    {
        produit = p;
    }

    @Override
    public boolean InsertToBase(String idstock) {
        boolean b = true;


        //Insertion des valeurs dans la base
        b = BDD.Create("Produit", produit);

        BDD.UpdateProduit("Produit", produit.getNom(), "produits_stock_stocks_id", idstock);
        //basePersoScanNStock.insertInto(_tableName, produit);

        //D�connection de la base
        deconnexion();
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
