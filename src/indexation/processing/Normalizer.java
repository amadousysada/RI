package indexation.processing;

import indexation.content.Token;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.TreeSet;

import tools.Configuration;
import tools.FileTools;

/**
 * Objet normalisant des tokens
 * en supprimant les signes diacritiques
 * et en les passant en minuscules.
 */
public class Normalizer implements Serializable
{	/** Class id (juste pour éviter le warning) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialise le normalisateur,
	 * en fonction de la configuration 
	 * de {@link Configuration}.
	 * 
	 * @throws FileNotFoundException
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 * @throws UnsupportedEncodingException 
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 */
	public Normalizer() throws FileNotFoundException, UnsupportedEncodingException
	{	initStemmer();
		initStopWords();
	}
	
	////////////////////////////////////////////////////
	//	TRAITEMENT
	////////////////////////////////////////////////////
	/**
	 * Nettoie les tokens reçus en paramètres.
	 * 
	 * @param tokens
	 * 		Liste de tokens à traiter.
	 */
	public void normalizeTokens(List<Token> tokens)
	{	//TODO méthode à compléter (TP1-ex7)
		
		ListIterator<Token> listIterator = tokens.listIterator();
		while(listIterator.hasNext()) {
			
			Token token = listIterator.next();
			String term = normalizeType(token.getType());
			if(term == null){
				listIterator.remove();
			}
			else {
				token.setType(term);
				listIterator.set(token);
			}
			
		}
	}
	
	/**
	 * Nettoie le type de token reçu en paramètre.
	 * S'il ne correspond pas à un terme, c'est
	 * la valeur {@code null} qui est renvoyée.
	 * 
	 * @param string
	 * 		La chaîne à traiter : un type associé à un token.
	 * @return
	 * 		La chaîne après traitement : un terme (ou {@code null}).
	 */
	public String normalizeType(String string)
	{	String result = null;
		//TODO méthode à compléter (TP1-ex6)
		result = java.text.Normalizer.normalize(string.toLowerCase(), Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		
		if(result.isEmpty())
			result = null;
		
		//TODO méthode à modifier  (TP4-ex14)
		if(stemmer !=null && result!=null) stemmer.stemType(result);
		
		//TODO méthode à modifier  (TP5-ex8)
		if(stopWords.contains(result) && result!=null) result =null;
		
		return result;
	}

	////////////////////////////////////////////////////
	//	RACINISATEUR
	////////////////////////////////////////////////////
	/** Stemmeur utilisé lors de la normalistion (optionnel) */
	private AbstractStemmer stemmer;
	
	/**
	 * Met en place le racinisateur utilisé par ce normalisateur.
	 * En l'absence de racinisateur, le normalisateur n'effectue
	 * pas de racinisation.
	 */
	private void initStemmer()
	{	if(Configuration.isStemmingTokens())
			stemmer = new PorterStemmer();
		else
			stemmer = null;
	}
	
	////////////////////////////////////////////////////
	//	MOTS-VIDES
	////////////////////////////////////////////////////
	/** Liste des mots vides */
	private TreeSet<String> stopWords;
	
	/**
	 * Met en place la liste de mots-vides utilisés
	 * lors du filtrage. En l'absence de liste,
	 * aucun filtrage n'est réalisé.
	 * 
	 * @throws FileNotFoundException
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 * @throws UnsupportedEncodingException 
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 */
	private void initStopWords() throws FileNotFoundException, UnsupportedEncodingException
	{	stopWords = new TreeSet<String>();
		if(Configuration.isFilteringStopWords())
			loadStopWords();
	}	
	
	/**
	 * Charge la liste de mots vides.
	 * 
	 * @throws FileNotFoundException
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 * @throws UnsupportedEncodingException 
	 * 		Problème lors de l'accès au fichier de mots vides. 
	 */
	private void loadStopWords() throws FileNotFoundException, UnsupportedEncodingException
	{	//TODO méthode à compléter (TP5-ex7)
		String filename = FileTools.getStopWordsFile();
		if(filename !=null){
			stopWords = new TreeSet<String>();
			File file = new File(filename);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			Scanner scanner = new Scanner(isr);
			while (scanner.hasNext()) {
				String string = scanner.next();
				stopWords.add(string);
				
			}
			scanner.close();
		}
		
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
	{	// test de normalizeType
		//TODO méthode à compléter (TP1-ex6)
		Normalizer norm = new Normalizer();
		System.out.println(norm.normalizeType("çéldon"));
		// test de normalizeTokens
		//TODO méthode à compléter (TP1-ex7)
		Tokenizer tokenizer = new Tokenizer();
		List<Token> tokens = new ArrayList<Token>();
		File file = new File("../Common/wp/001f1107-8e72-4250-8b83-ef02eeb4d4a4.txt");
		try {
			tokenizer.tokenizeDocument(file, 0, tokens);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		norm.normalizeTokens(tokens);
		System.out.print(tokens);
		// test de loadStopWords
		//TODO méthode à compléter (TP5-ex7)
	}
}
