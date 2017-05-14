package tp4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


public class TP4 {

	public static TreeSet<String> fusion(String str1, String str2) {

		String[] liste1 = str1.split(",");
		String[] liste2 = str2.split(",");

		/*
		 * String []listeRes = new String[liste1.length+liste2.length] ;
		 * System.arraycopy(liste2, 0, listeRes, 0, liste2.length); int
		 * compt=liste2.length-1; boolean occ= false;
		 * 
		 * for(int i=0;i<liste1.length;i++){ //System.out.println("mot1 ="
		 * +liste1[i]); for(int j=0;j<liste2.length;j++){ //System.out.println(
		 * "mo21 ="+liste2[j]);
		 * 
		 * if(liste1[i].equals(liste2[j])){ j=listeRes.length; occ=true;// le
		 * mot est déja présent dans la liste final } } if(occ==false){// le mot
		 * est nouveau //System.out.println("occc "+liste1[i]);
		 * listeRes[compt+1]=liste1[i]; compt++; } occ=false; }
		 *
		 * ou
		 *
		 *
		 */

		TreeSet<String> t = new TreeSet<String>();
		for (int i = 0; i < liste1.length; i++) {
			t.add(liste1[i]);
		}

		for (int j = 0; j < liste2.length; j++) {
			if (!t.contains(liste2[j])) {
				t.add(liste2[j]);
			}
		}
		System.out.println("fusion = " + t.toString());
		return t;
	}

	public static void mergeInvertedFiles(File invertedFile1, File invertedFile2, File mergedInvertedFile)
			throws IOException {

		try {

			FileWriter fw = new FileWriter(mergedInvertedFile);

			FileReader ips = new FileReader(invertedFile1);

			BufferedReader br = new BufferedReader(ips);

			String ligne1;
			String ligne2;
			boolean find = false;
			TreeSet<String> strFusion = new TreeSet<String>();

			while ((ligne1 = br.readLine()) != null) {
				String[] motLigne1 = ligne1.split("\t");

				FileReader ips2 = new FileReader(invertedFile2);
				BufferedReader br2 = new BufferedReader(ips2);
				System.out.println("ligne1 = " + ligne1);

				while (((ligne2 = br2.readLine()) != null) || (find)) {
					String[] motLigne2 = ligne2.split("\t");

					System.out.println("ligne2 = " + ligne2);
					// System.out.println("mot1 = "+motLigne1[0]+"
					// mot2="+motLigne2[0]);
					if (motLigne1[0].equals(motLigne2[0])) {
						System.out.println("pareil : " + motLigne1[0]);
						find = true;// on sort de la boucle de l'index 2 pour
									// passer à la ligne suivante
						strFusion = fusion(motLigne1[2], motLigne2[2]);// on
																		// fusion
																		// les
																		// deux
																		// liste
						fw.write(motLigne1[0] + "\t\t\t" + strFusion.size() + "\t\t\t" + strFusion.toString());// on
																												// écrit
																												// dans
																												// le
																												// fichier
																												// de
																												// sortie
						fw.write("\r\n");
					}

					// a supp
					/*
					 * if((find==false)&&(br2.readLine()==null)){ // bug
					 * System.out.println("new \n mot1 : "
					 * +motLigne1[0]+"\t\t\t"+motLigne1[1]+"\t\t\t"+motLigne1[2]
					 * +"\n"+
					 * motLigne2[0]+"\t\t\t"+motLigne2[1]+"\t\t\t"+motLigne2[2])
					 * ; fw.write(motLigne1[0]+"\t\t\t"+motLigne1[1]+"\t\t\t"+
					 * motLigne1[2]);//on écrit dans le fichier de sortie
					 * fw.write ("\r\n");
					 * fw.write(motLigne2[0]+"\t\t\t"+motLigne2[1]+"\t\t\t"+
					 * motLigne2[2]);//on écrit dans le fichier de sortie
					 * fw.write ("\r\n");
					 * 
					 * }
					 */

					// fin a supp
				}

				find = false;// on remet find a false

				br2.close();

			}
			fw.close();
			br.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static String mapToString(TreeSet t) {
		String res = "";
		Iterator iterator = t.iterator();
		int k=0;
		while (iterator.hasNext()) {
			if(k==(t.size()-1)){
				res = res + iterator.next() ;
			}
			else{
				res = res + iterator.next() + ",";

			}
			k++;
		}
		return res;
	}

	public static void mergeInvertedFiles2(File invertedFile1, File invertedFile2, File mergedInvertedFile)
			throws IOException {

		try {

			FileWriter fw = new FileWriter(mergedInvertedFile);

			FileReader ips = new FileReader(invertedFile1);
			BufferedReader br = new BufferedReader(ips);

			String ligne1;
			String ligne2;
			boolean find = false;
			TreeMap<String, String> strFusion = new TreeMap<String, String>();
			TreeSet<String> tmp = new TreeSet<String>();
			FileReader ips2 = new FileReader(invertedFile2);
			BufferedReader br2 = new BufferedReader(ips2);

			// on stock tout les lignes du premier fichier dans une treemap,:
			// key = le mot, value = la ligne entière
			while ((ligne1 = br.readLine()) != null) {
				String[] motLigne1 = ligne1.split("\t");
				strFusion.put(motLigne1[0], ligne1);
				System.out.println("ici2");

			}
			br.close();

			while (((ligne2 = br2.readLine()) != null) || (find)) {
				String[] motLigne2 = ligne2.split("\t");
				String[] ligneParam = {};
				if (strFusion.containsKey(motLigne2[0])) {
					ligneParam = strFusion.get(motLigne2[0]).split("\t");
					tmp = fusion(ligneParam[2], motLigne2[2]); // on fusion les
																// deux listes
					strFusion.put(motLigne2[0], motLigne2[0] + "\t" + tmp.size() + "\t" + mapToString(tmp));// on
																											// ajoute
																											// le
																											// mot
																											// et
																											// la
																											// ligne
																											// dans
																											// le
																											// treemap
				} else {
					strFusion.put(motLigne2[0], ligne2);
				}
			}
			br2.close();

			for (Map.Entry<String, String> entree : strFusion.entrySet()) {

				fw.write(entree.getValue());
				fw.write("\r\n");
			}

			fw.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static void main(String[] args) {
		File out = new File("/TC3/TP/REI/indexDaro.ind");
		File index1 = new File("/TC3/TP/REI/index1.ind");
		File index2 = new File("/TC3/TP/REI/index2.ind");
		try {
			mergeInvertedFiles2(index1, index2, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block System.out.println(

			e.printStackTrace();
		}

		// String t1 = "texte.95-107.txt,texte.95-31.txt";
		// String t2 =
		// "texte.95-107.txt,texte.95-33.txt,texte.95-71.txt,texte.95-9.txt";
		// TreeSet<String> t = fusion(t1, t2);
		// System.out.println(t.toString());

	}
}
