package correction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

/**
 * TP 1
 * 
 * @author xtannier
 *
 */
public class CorrectionTP1Comptages {
	/**
	 * Le répertoire du corpus
	 */
	// TODO CHEMIN A CHANGER
	protected static String DIRNAME = "/home/xtannier/data/lemonde/lemonde-utf8/";
	/**
	 * Un fichier de ce répertoire
	 */
	protected static String FILENAME = DIRNAME + "texte.95-1.txt";

	/**
	 * 2.1 Créez une méthode \emph{stemming} permettant de raciniser le texte
	 * d'un fichier du corpus.
	 * 
	 */
	protected static ArrayList<String> stemming(File file) throws IOException {
		ArrayList<String> words = (new FrenchStemmer()).normalize(file);
		return words;
	}

	/**
	 * 2.2.1 Une méthode renvoyant le nombre d'occurrences de chaque mot dans un
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
	}

	/**
	 * 2.2.2
	 * Une méthode permettant d'afficher le nombre d'occurrences
	 * de chaque mot pour l'ensemble du corpus.
	 */
	public static HashMap<String, Integer> getCollectionFrequency(File dir, Normalizer normalizer) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			File[] files = dir.listFiles();
			
			// Parcours des fichiers et remplissage de la table			
			Integer number;
			for (File file : files) {
				System.err.println("Analyse du fichier " + file.getAbsolutePath());
				// Appel de la méthode de normalisation
				ArrayList<String> words = normalizer.normalize(file);

				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					number = hits.get(wordLC);
					// Si ce mot n'était pas encore présent dans le dictionnaire,
					// on l'ajoute (nombre d'occurrences = 1)
					if (number == null) {
						hits.put(wordLC, 1);
					}
					// Sinon, on incrémente le nombre d'occurrence
					else {
						hits.put(wordLC, ++number);
					}
				}
			}
		}
		return hits;	
	}

	private static void testNormalizer(Normalizer normalizer) throws IOException {
		File file = new File(FILENAME);
		File dir = new File(DIRNAME);
		// TF
		HashMap<String, Integer> tfs = getTermFrequencies(file, normalizer);
		// Affichage du résultat
		for (Map.Entry<String, Integer> tf : tfs.entrySet()) {
			System.out.println(tf.getKey() + "\t" + tf.getValue());
		}

		HashMap<String, Integer> cfs = getCollectionFrequency(dir, normalizer);
		// Affichage du résultat (avec la fréquence)
		for (Map.Entry<String, Integer> cf : cfs.entrySet()) {
			System.out.println(cf.getKey() + "\t" + cf.getValue());
		}

		System.out.println("Taille du vocabulaire : " + cfs.size());
		String[] testWordTokenizer = { "point", "ministère", "minister", "à" };
		for (String word : testWordTokenizer) {
			System.out.println("Nombre d'occurrences du mot " + word + " : " + cfs.get(word));
		}

	}

	/**
	 * Main, appels de toutes les méthodes des exercices du TP1.
	 */
	public static void main(String[] args) {
		try {
			File file = new File(FILENAME);
			// 2.1
			System.out.println(stemming(file));

			// 2.2
			// Tokenizer (segmentation simple)
			Normalizer tokenizer = new FrenchTokenizer();
			testNormalizer(tokenizer);

			// Stemmer (racinisation)
			Normalizer stemmer = new FrenchStemmer();
			testNormalizer(stemmer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
