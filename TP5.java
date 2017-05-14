package tp5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.lang.Math;
public class TP5 {

	
	
	
	
	
	
	public static double getSimilarity(File file1, File file2) throws IOException{
		
		FileReader ips = new FileReader(file1);
		BufferedReader br = new BufferedReader(ips);
		FileReader ips2 = new FileReader(file2);
		BufferedReader br2 = new BufferedReader(ips2);	
		String ligne1;
		String ligne2;
		TreeMap<String,Double > mapf1 = new TreeMap<String, Double >();
		TreeMap<String,Double > mapf2 = new TreeMap<String, Double >();
	
		double similarite=0.0;
		double sum1=0.0;
		double sum2=0.0;
		double sumAll=0.0;
		int n;
		int k=0;
		
		//lecture des donnés , puis stockage des donnés dans les treemap : mapf1 et mapf2
		while ((ligne1 = br.readLine()) != null) {
			String[] motLigne = ligne1.split("\t");
			mapf1.put(motLigne[0], Double.parseDouble(motLigne[1]));
		}
		br.close();
		
		while ((ligne2 = br2.readLine()) != null) {
			String[] motLigne = ligne2.split("\t");
			mapf2.put(motLigne[0], Double.parseDouble(motLigne[1]));
		}
		br2.close();
		// fin de la lecture des données
		
		

		if(mapf1.size()<mapf2.size()){
			n=mapf2.size();
		}
		else{
			n=mapf1.size(); 
		}
		//on crée des tableau de double pour stocker les valeur des poid
		double []valF1= new double [n];
		double []valF2=new double[n];
		
		
		//on calcul les différentes sum : 
		for (Map.Entry<String, Double> entree : mapf1.entrySet()) {
			valF1[k]=entree.getValue();
			sum1=(valF1[k]*valF1[k] )+sum1;
			k++;
		}
		k=0;
		for (Map.Entry<String, Double> entree : mapf2.entrySet()) {
			valF2[k]=entree.getValue();
			sum2= (valF2[k]*valF2[k])+sum2;
		}
		
		
		for(int i=0;i<n;i++){
			sumAll=sumAll+(valF1[i]*valF2[i]);
		}
		
		similarite = sumAll/( Math.sqrt(sum1)* Math.sqrt(sum2));
		
		
		return similarite;
		
	}
	
	
	
	
	public static void getSimilarDocuments(File file, Set<File> fileList){
		
		TreeMap< Double, String> res = new TreeMap<Double, String>();
		Iterator i=fileList.iterator(); // on crée un Iterator pour parcourir notre Set
	
		while(i.hasNext()) 
		{
			File f= new File(i.next().toString());
			 try {
				res.put(getSimilarity(file,f), f.toString()); // on appel la fct getSimilarity puis on stock dans le treemap res :  key = la similarité, et valu = le nom du fichier
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		NavigableSet<Double> tmp =res.descendingKeySet();
		double d=0.0;
		Iterator ite=tmp.iterator(); // on crée un Iterator pour parcourir notre Set
		while(ite.hasNext()) 
		{
			d=(double) ite.next();
			 System.out.println("key = "+d+" and value ="+res.get(d)	);
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		/*TreeMap< Double, String> res = new TreeMap<Double, String>();
		res.put(5.0, "tsssss");
		res.put(6.0, "dzdazd");
		res.put(1.0, "daaa");
		NavigableSet<Double> tmp =res.descendingKeySet();
		double d=0.0;
		Iterator i=tmp.iterator(); // on crée un Iterator pour parcourir notre Set
		while(i.hasNext()) 
		{
			d=(double) i.next();
			 System.out.println("key = "+d+" and value ="+res.get(d)	);
		}*/
		
		
		File index1 = new File("/TC3/TP/REI/index1.ind");
		File index2 = new File("/TC3/TP/REI/index2.ind");
		try {
			System.out.println(getSimilarity(index1,index2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
