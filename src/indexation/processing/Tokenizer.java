package indexation.processing;

import indexation.content.Token;
import tools.Configuration;
import tools.FileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Objet segmentant des textes
 * en utilisant tous les caractères
 * non alphanumériques comme séparateurs.
 */
public class Tokenizer implements Serializable
{	/** Class id (juste pour éviter le warning) */
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////
	//	TRAITEMENT
	////////////////////////////////////////////////////
	/**
	 * Tokenize tout le corpus et renvoie les
	 * tokens obtenus via la liste passée en
	 * paramètre. La méthode renvoie aussi le
	 * nombre de documents traités.
	 * 
	 * @param tokens
	 * 		Liste de tokens résultant du traitement.
	 * @return
	 * 		Nombre de documents traités.
	 * 
	 * @throws UnsupportedEncodingException 
	 * 		Problème de décodage lors de la lecture d'un document.
	 */
	public int tokenizeCorpus(List<Token> tokens) throws UnsupportedEncodingException
	{	int result = 0;
		//TODO méthode à compléter (TP1-ex5)
	
		String corpusPath = FileTools.getCorpusFolder();
		File file = new File(corpusPath);
		String[] paths = file.list();
		Arrays.sort(paths);
		if(tokens.isEmpty()) {
			int docId = 0;
			for (String string : paths) {
				tokenizeDocument(new File(corpusPath+File.separator+string), docId, tokens);
				//System.out.println(corpusPath+File.separator+string);
				docId++;
			}
		}
		result=tokens.size();
		
		return result;
		
	}
	
	/**
	 * Méthode qui segmente le document
	 * spécifié, et renvoie le résultat
	 * en complétant la liste passée en 
	 * paramètre.
	 * 
	 * @param document
	 * 		Fichier contenant le document à traiter.
	 * @param docId
	 * 		Numéro du document à traiter.
	 * @param tokens
	 * 		La liste de tokens à compléter.
	 * 
	 * @throws UnsupportedEncodingException
	 * 		Problème de décodage lors de la lecture d'un document.
	 */
	public void tokenizeDocument(File document, int docId, List<Token> tokens) throws UnsupportedEncodingException
	{	//TODO méthode à compléter (TP1-ex4)
		String regex = "[^\\pL\\pN]";
		try {
			FileInputStream fis = new FileInputStream(document);
			InputStreamReader is = new  InputStreamReader(fis,"UTF-8");
			Scanner scanner = new Scanner(is);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				for (String string : tokenizeString(line)) {
					if(!string.equals("")) {
						tokens.add(new Token(string, docId));
					}
				}
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Renvoie la liste des tokens pour
	 * la chaîne de caractères spécifiée.
	 * 
	 * @param string
	 * 		Chaîne de caractères à traiter.
	 * @return
	 * 		La liste de types correspondant.
	 */
	public List<String> tokenizeString(String string)
	{	List<String> result = null;
		//TODO méthode à compléter (TP1-ex3)
		String regex = "[^\\pL\\pN]";
		result = Arrays.asList(string.split(regex));
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
	{	// test de tokenizeString
		// TODO méthode à compléter (TP1-ex3)
		Tokenizer t =new Tokenizer();
		List<String> l=t.tokenizeString("En mathématiques, le théorème de la dimension pour les espaces vectoriels énonce que deux bases quelconques d'un même espace vectoriel ont même cardinalité. Joint au théorème de la base incomplète qui assure l'existence de bases, il permet de définir la dimension d'un espace vectoriel comme le cardinal (fini ou infini) commun à toutes ses bases.");
		for (String string : l) {
			System.out.println(string);
		}
		
		// test de tokenizeDocument
		// TODO méthode à compléter (TP1-ex4)
		List<Token> tokens = new ArrayList<Token>();
		File file = new File("../Common/wp/001f1107-8e72-4250-8b83-ef02eeb4d4a4.txt");
		try {
			t.tokenizeDocument(file, 0, tokens);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(tokens+"\n");
				
		// test de tokenizeCorpus
		// TODO méthode à compléter (TP1-ex5)
		try {
			Configuration.setCorpusName("wp_test");
			List<Token> tokens2 = new ArrayList<Token>();
			System.out.print(t.tokenizeCorpus(tokens2));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
