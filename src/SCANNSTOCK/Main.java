package SCANNSTOCK;

import API.Api;
import API.IHttpRequest;
import API.IParseur;
import API.ParseurJSON;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Set;

public class Main
{
	
	public static void main(String[] args) 
	{
		//ScanNStock sns = new ScanNStock();
		//sns.launch();
	String COCA_LIGHT = "5449000050205";
        String SUCHARD = "7622210107718";
        String SPECIAL_K = "5050083425585";
            IHttpRequest api = new Api();
            api.runSearchProduct(COCA_LIGHT);
            
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
            }*/
	}
}
