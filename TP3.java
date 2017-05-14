package correction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

/**
 * TP 2
 * 
 * @author xtannier
 *
 */
public class TP3_ {

	/**
	 * Le répertoire du corpus
	 */
	private static String DIRNAME = "/Users/admin/Downloads/lemonde-utf8";
	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "/Users/admin/Documents/Recherche_Information/frenchST.txt";

	/**
	 * Une méthode renvoyant le nombre d'occurrences de chaque mot dans un
	 * fichier.
	 */
	public static HashMap<String, Integer> getTermFrequencies(File file, Normalizer normalizer) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// Appel de la méthode de normalisation
		ArrayList<String> words = normalizer.normalize(file);
		Integer number;
		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			number = hits.get(word);
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			if (number == null) {
				hits.put(word, 1);
			}
			// Sinon, on incrémente le nombre d'occurrence
			else {
				hits.put(word, ++number);
			}
		}
		return hits;
		// // Affichage du résultat
		// for (Map.Entry<String, Integer> hit : hits.entrySet()) {
		// System.out.println(hit.getKey() + "\t" + hit.getValue());
		// }
	}

	/**
	 * exo 2.1 : Calcule le df, c'est-à-dire le nombre de documents pour chaque
	 * mot apparaissant dans la collection. Le mot "à" doit ainsi apparaître
	 * dans 88 documents, le mot "ministère" dans 4 documents.
	 */
	public static HashMap<String, Integer> getDocumentFrequency(File dir, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		ArrayList<String> wordsInFile;
		ArrayList<String> words;
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			String[] fileNames = dir.list();

			Integer number;
			for (String fileName : fileNames) {
				System.err.println("Analyse du fichier " + fileName);
				// Les mots présents dans ce document
				wordsInFile = new ArrayList<String>();
				// Appel de la méthode de normalisation
				words = normalizer.normalize(new File(dir, fileName));
				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					// si le mot n'a pas déjà été trouvé dans ce document :
					if (!wordsInFile.contains(wordLC)) {
						number = hits.get(wordLC);
						// Si ce mot n'était pas encore présent dans le
						// dictionnaire,
						// on l'ajoute (nombre d'occurrences = 1)
						if (number == null) {
							hits.put(wordLC, 1);
						}
						// Sinon, on incrémente le nombre d'occurrence
						else {
							hits.put(wordLC, number + 1);
						}
						wordsInFile.add(wordLC);
					}
				}
			}
		}

		// Affichage du résultat (avec la fréquence)
		// for (Map.Entry<String, Integer> hit : hits.entrySet()) {
		// System.out.println(hit.getKey() + "\t" + hit.getValue());
		// }
		return hits;
	}

	/**
	 * exo 2.4 : Calcule le tf.idf des mots d'un fichier en fonction des df déjà
	 * calculés, du nombre de documents et de la méthode de normalisation.
	 */
	public static HashMap<String, Double> getTfIdf(File file, HashMap<String, Integer> dfs, int documentNumber,
			Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		// Appel de la méthode de normalisation
		ArrayList<String> words = normalizer.normalize(file);
		Integer number;

		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			number = hits.get(word);
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			if (number == null) {
				hits.put(word, 1);
			}
			// Sinon, on incrémente le nombre d'occurrence
			else {
				hits.put(word, ++number);
			}
		}

		Integer tf;
		Double tfIdf;
		String word;
		HashMap<String, Double> tfIdfs = new HashMap<String, Double>();

		// Calcul des tf.idf
		for (Map.Entry<String, Integer> hit : hits.entrySet()) {
			tf = hit.getValue();
			word = hit.getKey();
			tfIdf = (double) tf * Math.log((double) documentNumber / (double) dfs.get(word));
			tfIdfs.put(word, tfIdf);
		}
		return tfIdfs;
	}

	/**
	 * exo 2.5 : Crée, pour chaque fichier d'un répertoire, un nouveau fichier
	 * contenant les poids de chaque mot. Ce fichier prendra la forme de deux
	 * colonnes (mot et poids) séparées par une tabulation. Les mots devront
	 * être placés par ordre alphabétique. Les nouveaux fichiers auront pour
	 * extension .poids et seront écrits dans le répertoire outDirName.
	 */
	private static void getWeightFiles(File inDir, File outDir, Normalizer normalizer) throws IOException {
		// calcul des dfs
		HashMap<String, Integer> dfs = getDocumentFrequency(inDir, normalizer);
		// Nombre de documents
		File[] files = inDir.listFiles();
		int documentNumber = files.length;
		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		// TfIdfs
		for (File file : files) {
			HashMap<String, Double> tfIdfs = getTfIdf(file, dfs, documentNumber, normalizer);
			TreeSet<String> words = new TreeSet<String>(tfIdfs.keySet());
			// on écrit dans un fichier
			try {
				FileWriter fw = new FileWriter(new File(outDir, file.getName().replaceAll(".txt$", ".poids")));
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				// Ecriture des mots
				for (String word : words) {
					out.println(word + "\t" + tfIdfs.get(word));
				}
				out.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public static TreeMap<String, TreeSet<String>> getInvertedFile(File dir, Normalizer normalizer) throws IOException {

		File[] files = dir.listFiles();

		TreeMap<String, TreeSet<String>> invertedFile = new TreeMap<String, TreeSet<String>>();
		for (File file : files) {
			ArrayList<String> words = normalizer.normalize(file);
			for (String word : words) {
				word = word.toLowerCase();
				// TODO
				// on récupère le nombre d'occurrences pour ce mot
				// Si ce mot n'était pas encore présent dans le dictionnaire,
				// on l'ajoute (nombre d'occurrences = 1)
				// Sinon, on incrémente le nombre d'occurrence
				TreeSet<String> treSet = invertedFile.get(word);
				if (treSet != null) {
					treSet.add(file.getName());
					invertedFile.put(word, treSet);
				}

				else {
					TreeSet<String> tree = new TreeSet<String>();
					tree.add(file.getName());
					invertedFile.put(word, tree);
				}
			}

		}

		return invertedFile;
	}

	public static void saveInvertedFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile)
			throws IOException {

		try {
			FileWriter fw = new FileWriter(outFile);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			// Ecriture des mots

			for (String key : invertedFile.keySet()) {
				out.println(key + "\t" + invertedFile.get(key).size() + "\t" + invertedFile.get(key));
			}
			out.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

	public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(File dir, Normalizer normalizer)
			throws IOException {
		File[] files = dir.listFiles();

		TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = new TreeMap<String, TreeMap<String, Integer>>();
		for (File file : files) {
			ArrayList<String> words = normalizer.normalize(file);
			for (String word : words) {
				word = word.toLowerCase();
				// TODO
				// on récupère le nombre d'occurrences pour ce mot
				// Si ce mot n'était pas encore présent dans le dictionnaire,
				// on l'ajoute (nombre d'occurrences = 1)
				// Sinon, on incrémente le nombre d'occurrence
				TreeMap<String, Integer> treMap = invertedFileWithWeights.get(word);
				if (treMap != null) {
					Integer df = treMap.get(file.getName());
					if (df != null)
						treMap.put(file.getName(), treMap.get(file.getName()) + 1);
					else
						treMap.put(file.getName(), 1);
					invertedFileWithWeights.put(word, treMap);
				}

				else {
					TreeMap<String, Integer> tree = new TreeMap<String, Integer>();
					tree.put(file.getName(), 1);
					invertedFileWithWeights.put(word, tree);
				}
			}

		}

		return invertedFileWithWeights;

	}

	/**
	 * Main, appels de toutes les méthodes des exercices du TD1.
	 */
	public static void main(String[] args) {
		try {
			String outDirName = "/Users/admin/Documents/Recherche_Information/sortie";
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
			Normalizer[] normalizers = { stemmerAllWords, stemmerNoStopWords, tokenizerAllWords, tokenizerNoStopWords };
			for (Normalizer normalizer : normalizers) {
				String name = normalizer.getClass().getName();
				if (!normalizer.getStopWords().isEmpty()) {
					name += "_noSW";
				}
				System.out.println("Normalisation avec " + name);
				System.out.println(getDocumentFrequency(new File(DIRNAME), normalizer).size());
				System.out.println("GetWeightFiles avec " + name);
				getWeightFiles(new File(DIRNAME), new File(new File(outDirName), name), normalizer);
				TreeMap<String, TreeSet<String>> invertedFile = new TreeMap<String, TreeSet<String>>();
				invertedFile = getInvertedFile(new File(DIRNAME), normalizer);
				saveInvertedFile(invertedFile, new File("invertedfile.txt"));
				TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = new TreeMap<String, TreeMap<String, Integer>>();
				invertedFileWithWeights = getInvertedFileWithWeights(new File(DIRNAME), normalizer);
				System.out.println("invertedFileWithWeights" + invertedFileWithWeights);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
