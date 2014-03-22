package SCANNSTOCK;

import API.Api;
import API.IHttpRequest;
import API.IParseur;
import API.ParseurJSON;
import fr.sciencesu.sns.hibernate.jpa.Produit;
import fr.sciencesu.sns.hibernate.test.InitDB;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
	
	public static void main(String[] args) 
	{
            
            testRequestHibernate(40);
	
            //testAPIOpenFoodFact();
           
            //testParseurJSON();
	}

    private static Produit testAPIOpenFoodFact() {
        	//ScanNStock sns = new ScanNStock();
		//sns.launch();
	String COCA_LIGHT = "5449000050205";
        String SUCHARD = "7622210107718";
        String SPECIAL_K = "5050083425585";
            IHttpRequest api = new Api();
            api.runSearchProduct(COCA_LIGHT);
            api.getProduct().setDdp(Calendar.getInstance());
           return api.getProduct();
		
    }

    private static void testParseurJSON() {
         /*IParseur p = new ParseurJSON();
		String s = "";
		try{
			InputStream ips=new FileInputStream("C:\\Users\\antoi_000\\Desktop\\t.txt"); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				//System.out.println(ligne);
				s+=ligne;
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
            Set<Entry<String, String>> productFind = p.getInformations(s).entrySet();
            if(!productFind.isEmpty())
            {
                    for(Entry<String, String> en : productFind) 
                {
                            String cle = en.getKey();
                            String valeur = en.getValue();

                            System.out.println("KEY = " + cle );
                            System.out.println("VALUE = " + valeur );

                            //Envoi des donnees dans la base de donnees afin de les stocker


                }
            }
            else
            {
                System.out.println("Le produit demandé n'a pas été trouvé dans la base google !");
            }*/}

    private static void testRequestHibernate(int numberAddProduct) {
     
        for (int i = 0; i < numberAddProduct; i++) {
            InitDB database = new InitDB();
        
            try 
            {
                database.connection();
                
                //Traitement
                Produit p = testAPIOpenFoodFact();
                //Création produit via open food fact
                database.Create(p);
                // database.deconnection();
                //Ajout produit
                //database.connection();
                //Update d'un stock
                database.Update(p, 2);
                
               
            
            }             
            catch (ConnectException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            finally
            {
                 database.deconnection();
            }
        }
        
        
        
    }
    
}
