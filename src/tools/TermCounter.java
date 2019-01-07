package tools;

import indexation.AbstractIndex.TokenListType;
import indexation.content.Token;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Objet comptant les occurrences de termes
 * dans un corpus et exportant le résultat
 * sous forme de fichier texte.
 */
public class TermCounter
{	
	/**
	 * Méthode principale.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problème lors de la création du fichier.
	 * @throws UnsupportedEncodingException 
	 * 		Problème de décodage lors de la lecture d'un document.
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
	{	//TODO méthode à compléter (TP5-ex4)
		Configuration.setCorpusName("wp");
		TermCounter.processCorpus();
	}
	
	/**
	 * Compte le nombre d'occurrences de chaque
	 * terme présent dans le corpus courant, puis 
	 * enregistre ces décomptes dans un fichier CSV.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problème lors de la création du fichier.
	 * @throws UnsupportedEncodingException 
	 * 		Problème de décodage lors de la lecture d'un document.
	 */
	public static void processCorpus() throws FileNotFoundException, UnsupportedEncodingException
	{	//TODO méthode à compléter (TP5-ex3)
		Tokenizer tk = new Tokenizer();
		Normalizer nr = new Normalizer();
		
		List<Token> tokensListe = new ArrayList<Token>();
		
		System.out.println("Tokenizing corpus");
		Long start = System.currentTimeMillis();
		int i = tk.tokenizeCorpus(tokensListe);
		Long end =System.currentTimeMillis();
		System.out.format("%d tokens were found, duration=%d ms%n%n", i,(end-start));
		
		System.out.println("Normalizing tokens");
		Long start1 = System.currentTimeMillis();
		nr.normalizeTokens(tokensListe);
		end =System.currentTimeMillis();
		System.out.format("%d tokens remaining after normalization duration=%d ms%n%n", tokensListe.size(),(end-start1));
		
		System.out.println("Counting terms");
		start1 = System.currentTimeMillis();
		Map<String, Integer> m=countTerms(tokensListe);
		end = System.currentTimeMillis();
		System.out.format("There are %d distinct terms in the corpus, duration=%d ms%n%n",m.size(), (end-start1));
		
		System.out.println("Recording counts in "+FileTools.getTermCountFile());
		start1 =System.currentTimeMillis();
		writeCounts(m, FileTools.getTermCountFile());
		end = System.currentTimeMillis();
		System.out.format("Counts recorded, duration=%d ms%n%n", (end-start1));
		
		System.out.println("Stop-words file: "+FileTools.getStopWordsFile());
		
		end = System.currentTimeMillis();
		System.out.format("Total duration=%d ms", (end-start));
	}
	
	/**
	 * Compte le nombre d'occurrences de chaque
	 * terme dans la liste passée en paramètre.
	 * 
	 * @param tokens
	 * 		Liste de tokens normalisés à traiter.
	 * @return
	 * 		Map associant son nombre d'occurrences à chaque terme.
	 */
	private static Map<String,Integer> countTerms(List<Token> tokens)
	{	Map<String,Integer> result = null;
		//TODO méthode à compléter (TP5-ex1)
		result = new HashMap<String, Integer>();
		for(Token token:tokens){
			Integer i = result.get(token.getType());
			if(i==null){
				i=0;
			}
			i++;
			result.put(token.getType(), i);
		}
		return result;
	}
	
	/**
	 * Enregistre les décomptes des termes.
	 * 
	 * @param counts
	 * 		Map contenant les décomptes des termes.
	 * @param fileName
	 * 		Nom du fichier texte à créer.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problème lors de la création du fichier.
	 * @throws UnsupportedEncodingException 
	 * 		Problème lors de l'écriture des résultats.
	 */
	private static void writeCounts(Map<String,Integer> counts, String fileName) throws FileNotFoundException, UnsupportedEncodingException
	{	//TODO méthode à compléter (TP5-ex2)
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
		PrintWriter writer = new PrintWriter(osw);
		
		for(Entry<String, Integer> m:counts.entrySet()){
			String s = "\""+m.getKey()+"\","+m.getValue();
			writer.println(s);
		}
		writer.close();
		
	}
}
