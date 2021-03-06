package tools;

import indexation.AbstractIndex.LexiconType;
import indexation.ArrayIndex;
import indexation.content.Posting;
import indexation.content.Token;
import indexation.processing.Builder;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import query.DocScore;

/**
 * Permet de convertir les noms de fichiers en
 * postings, et inversement.
 */
public class FileTools
{	
	////////////////////////////////////////////////////
	//	CHEMINS
	////////////////////////////////////////////////////
	/**
	 * Renvoie le chemin vers le dossier racine du corpus.
	 * 
	 * @return
	 * 		Chemin du dossier racine du corpus.
	 */
	public static String getCorpusFolder()
	{	String corpusName = Configuration.getCorpusName();
		String result = ".." + File.separator + "Common" + File.separator + corpusName;
		return result;
	}
	
	/**
	 * Renvoie le chemin vers le fichier (XML) de la vérité terrain.
	 * 
	 * @return
	 * 		Chemin du fichier (XML) de la vérité terrain.
	 */
	public static String getGroundTruthFile()
	{	String corpusName = Configuration.getCorpusName();
		String result = ".." + File.separator + "Common" + File.separator + corpusName + "_reference.xml";
		return result;
	}
	
	/**
	 * Renvoie le chemin vers le fichier contenant l'index.
	 * 
	 * @return
	 * 		Chemin du fichier (binaire) de l'index.
	 */
	public static String getIndexFile()
	{	String corpusName = Configuration.getCorpusName();
		String options = "";
		if(Configuration.isFilteringStopWords())
			options = options + "_filter";
		if(Configuration.isStemmingTokens())
			options = options + "_stem";
		String result = "data" + File.separator + corpusName+ options + "_index.data";
		return result;
	}
	
	/**
	 * Renvoie le chemin vers le fichier (CSV) de décompte des termes.
	 * 
	 * @return
	 * 		Chemin du fichier (CSV) de décompte.
	 */
	public static String getTermCountFile()
	{	String corpusName = Configuration.getCorpusName();
		String options = "";
		if(Configuration.isStemmingTokens())
			options = options + "_stem";
		String result = "data" + File.separator + corpusName + options + "_termcount.csv";
		return result;
	}
	
	/**
	 * Renvoie le chemin vers le fichier (texte) des mots-vides.
	 * 
	 * @return
	 * 		Chemin du fichier (texte) des mots-vides.
	 */
	public static String getStopWordsFile()
	{	String corpusName = Configuration.getCorpusName();
		String options = "";
		if(Configuration.isStemmingTokens())
			options = options + "_stem";
		String result = "data" + File.separator + corpusName + options + "_stopwords.txt";
		return result;
	}
	
	/**
	 * Renvoie le chemin vers le fichier (texte) dans lequel
	 * on enregistre les mesures de performance.
	 * 
	 * @return
	 * 		Chemin du fichier (texte) des performances.
	 */
	public static String getPerformanceFile()
	{	String corpusName = Configuration.getCorpusName();
		String options = "";
		if(Configuration.isFilteringStopWords())
			options = options + "_filter";
		if(Configuration.isStemmingTokens())
			options = options + "_stem";
		if(Configuration.isComputingScores())
			options = options + "_score";
		String result = "data" + File.separator + corpusName + options + "_stopwords.txt";
		return result;
	}
	
	////////////////////////////////////////////////////
	//	CONVERSION
	////////////////////////////////////////////////////
	/**
	 * Renvoie la liste des noms de fichier correspondant
	 * aux postings passés en paramètres.
	 * 
	 * @param postings
	 * 		Liste de postings.
	 * @return
	 * 		Liste de noms de fichiers.
	 */
	public static List<String> getFileNamesFromPostings(List<Posting> postings)
	{	List<String> result = null;
		
		//TODO méthode à compléter  (TP2-ex6)
	
		File file = new File(FileTools.getCorpusFolder());
		String[] paths = file.list();
		Arrays.sort(paths);
		result =new ArrayList<String>();
		for (Posting post : postings) {
			int index  = post.getDocId();
			String filename = paths[index];
			result.add(filename);
		}
		return result;
	}
	
	/**
	 * Renvoie la liste des noms de fichier correspondant
	 * aux objets {@code DocScore} passés en paramètres.
	 * 
	 * @param docScores
	 * 		Liste d'objets {@code DocScore}.
	 * @return
	 * 		Liste de noms de fichiers.
	 */
	public static List<String> getFileNamesFromDocScores(List<DocScore> docScores)
	{	List<String> result = new LinkedList<String>();
		//TODO méthode à compléter  (TP6-ex12)
		File file = new File(FileTools.getCorpusFolder());
		String[] paths = file.list();
		Arrays.sort(paths);
		for (DocScore docScore : docScores) {
			int index =docScore.getDocId();
			String filename = paths[index];
			result.add(filename);
		}
		return result;
	}
	
	/**
	 * Renvoie la liste des postings correspondant
	 * aux noms de fichier passés en paramètres.
	 * 
	 * @param fileNames
	 * 		Liste de noms de fichiers.
	 * @return
	 * 		Liste de postings.
	 */
	public static List<Posting> getPostingsFromFileNames(List<String> fileNames)
	{	List<Posting> result = null;
		//TODO méthode à compléter  (TP4-ex1)
		File file = new File(FileTools.getCorpusFolder());
		String[] paths = file.list();
		Arrays.sort(paths);
		result =new ArrayList<Posting>();
		for (String string : fileNames) {
			int docId  = Arrays.asList(paths).indexOf(string);
			result.add(new Posting(docId));
		}
		return result;
	}

	////////////////////////////////////////////////////
	//	TEST
	////////////////////////////////////////////////////
	/**
	 * Test des méthodes de cette classe.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 * 
	 * @throws Exception 
	 * 		Problème quelconque rencontré.
	 */
	public static void main(String[] args) throws Exception 
	{	// test de getFileNamesFromPostings
		//TODO méthode à compléter  (TP2-ex6)
		Configuration.setCorpusName("wp_test");
		Tokenizer tk = new Tokenizer();
		List<Token> tokens =new ArrayList<Token>();
		int i=tk.tokenizeCorpus(tokens);
		(new Normalizer()).normalizeTokens(tokens);
		List<Posting> postings=new ArrayList<Posting>();
		for (Token token : tokens) {
			postings.add(new Posting(token.getDocId()));
		}
		
		List<String> filenames = FileTools.getFileNamesFromPostings(postings);
		for (String string : filenames) {
			System.out.println(string);
		}
		System.out.println("\n\n");
		
		
		
		// test de getPostingsFromFileNames
		//TODO méthode à compléter  (TP4-ex1)
		List<String> fileNames = new ArrayList<String>(
				Arrays.asList("c070aeda-4fd1-494d-99d0-250d24bca7e7.txt", "c11bb2d4-a507-4ea8-a140-774fcc3e37c8.txt", "c12b4c72-5015-42da-95a9-51779723a81d.txt"));
		postings = FileTools.getPostingsFromFileNames(fileNames);
		System.out.println(postings);
		System.out.println("\n\n");
		// test de getFileNamesFromDocScores
		//TODO méthode à compléter  (TP6-ex12)
	}
}
