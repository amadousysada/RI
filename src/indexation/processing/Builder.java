package indexation.processing;

import indexation.AbstractIndex;
import indexation.AbstractIndex.LexiconType;
import indexation.ArrayIndex;
import indexation.HashIndex;
import indexation.TreeIndex;
import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.content.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import tools.Configuration;

/**
 * Objet construisant un index prenant 
 * la forme d'un fichier inversé. Il a
 * pour cela besoin de recevoir la liste
 * normalisée des paires (tokens, docId).
 */
public class Builder
{	/**
	 * Construit l'index à partir
	 * des tokens passés en paramètres.
	 * 
	 * @param tokens
	 * 		Liste normalisée de tokens à traiter.
	 * @param lexiconType
	 * 		Type de structure de données utilisée pour stocker le lexique.
	 * @return
	 * 		L'index produit.
	 */
	public AbstractIndex buildIndex(List<Token> tokens, LexiconType lexiconType)
	{	AbstractIndex result = null;
		int i =0;
		Long start , end=null;
		List<Integer> frequencies = new ArrayList<Integer>();
		
		{
			System.out.println("\tSorting tokens...");
			start = System.currentTimeMillis();
			Collections.sort(tokens);
			end = System.currentTimeMillis();
			System.out.println("\t"+tokens.size()+" tokens sorted, duration="+(end-start)+" ms\n\n");
		}
		
		{
			System.out.println("\tFiltering tokens...");
			start = System.currentTimeMillis();
			i = filterTokens(tokens,frequencies);
			end = System.currentTimeMillis();
			System.out.println("\t"+tokens.size()+ " tokens remaining, corresponding to "+i +" terms, duration="+(end-start)+" ms\n\n");
		}
		
		switch (lexiconType) {
		case ARRAY:
			result = new ArrayIndex(i);
			break;
		
		case HASH:
			result = new HashIndex(i);
			break;
			
		case TREE:
			result = new TreeIndex();
			break;

		default:
			break;
		}
		
		{
			System.out.println("\tBuilding posting lists...");
			start = System.currentTimeMillis();
			int j = buildPostings(tokens, frequencies, result);
			end = System.currentTimeMillis();
			System.out.format("\t%d postings listed, lexicon type=%s, duration=%d ms%n%n",j,lexiconType.name(),(end-start));
		}
		return result;
	}
	
	/**
	 * Supprime de la liste les occurrences
	 * multiples de tokens, à condition qu'ils
	 * appartiennent au même document.
	 * Bien sûr, on garde quand même une occurrence.
	 * 
	 * @param tokens
	 * 		La liste normalisée et triée de tokens à traiter.
	 * @return
	 * 		Nombre de termes distincts dans la liste.
	 */
	private int filterTokens(List<Token> tokens)
	{	int result = 0;
		//TODO méthode à compléter (TP2-ex1)
		Iterator<Token> it =tokens.iterator();
		Token previous = null;
		while (it.hasNext()) {
			Token token =it.next();
			if (previous == null) {
				result++;
				previous = token;
				continue;
			}
			if(token.equals(previous)) {
				it.remove();
				continue;
			}
			
			if(!previous.getType().equals(token.getType())){
				result++;
			}
			previous = token;
			
			
		}
		return result;
	}
	
	/**
	 * Supprime de la liste les occurrences
	 * multiples de tokens, à condition qu'elles
	 * appartiennent au même document.
	 * Bien sûr, on garde quand même une occurrence.
	 * <br/>
	 * Par rapport à {@link #filterTokens(List)},
	 * cette méthode calcule en plus les fréquences
	 * des tokens dans chaque document où ils apparaissent.
	 * 
	 * @param tokens
	 * 		La liste normalisée et triée de tokens à traiter.
	 * @param frequencies
	 * 		La liste des fréquences associées à ces tokens.
	 * @return
	 * 		Nombre de termes distincts dans la liste.
	 */
	private int filterTokens(List<Token> tokens, List<Integer> frequencies)
	{	int result = 0;
		//TODO méthode à compléter (TP6-ex4)
		Iterator<Token> it =tokens.iterator();
		Token previous = null;
		int frequence =0;
		while (it.hasNext()) {
			Token token =it.next();
			if (previous == null) {
				result++;
				frequence++;
				previous = token;
				continue;
			}
			if(token.equals(previous)) {
				it.remove();
				frequence++;
				continue;
			}
			
			frequencies.add(frequence);
			frequence=1;
			
			if(!previous.getType().equals(token.getType())){
				result++;
				
			}
			previous = token;
			
			
		}
		
		frequencies.add(frequence);
		
		return result;
	}
	
	/**
	 * Construit un index à partir de
	 * la liste de tokens normalisée, triée et filtrée
	 * passée en paramètre.
	 * 
	 * @param tokens
	 * 		Liste normalisée, triée et filtrée de tokens.
	 * @param index
	 * 		L'index obtenu, sous forme de fichier inverse.
	 * @return
	 * 		Nombre de postings listés.
	 */
	private int buildPostings(List<Token> tokens, AbstractIndex index)
	{	int result = 0;
		//TODO méthode à compléter (TP2-ex2)
		
		int rank =0;
		IndexEntry entry =null;
		Iterator<Token> it =tokens.iterator();
		while (it.hasNext()) {
			Token token = it.next();
			
			if(entry ==null || !entry.getTerm().equals(token.getType())){
				entry = new IndexEntry(token.getType());
				index.addEntry(entry, rank);
				rank++;
			}
			Long start = System.currentTimeMillis();
			entry.addPosting(new Posting(token.getDocId()));
			Long end = System.currentTimeMillis();
			System.out.println("\t\t\t"+(end-start));
			result++;
		}
		return result;
	}
	
	/**
	 * Construit un index à partir de
	 * la liste de tokens normalisée, triée et filtrée
	 * passée en paramètre.
	 * <br/>
	 * La différence avec {@link #buildPostings(List, AbstractIndex)}
	 * est que cette méthode prend une liste supplémentaires contenant
	 * les fréquences des termes.
	 * 
	 * @param tokens
	 * 		Liste normalisée, triée et filtrée de tokens.
	 * @param frequencies
	 * 		La liste des fréquences associées à ces tokens.
	 * @param index
	 * 		L'index obtenu, sous forme de fichier inverse.
	 * @return
	 * 		Nombre de postings listés.
	 */
	private int buildPostings(List<Token> tokens, List<Integer> frequencies, AbstractIndex index)
	{	int result = 0;
		//TODO méthode à compléter (TP6-ex5)
		int rank =0;
		IndexEntry entry =null;
		
		Iterator<Token> it =tokens.iterator();
		
		while (it.hasNext()) {
			Token token = it.next();
			
			if(entry ==null || !entry.getTerm().equals(token.getType())){
				entry = new IndexEntry(token.getType());
				index.addEntry(entry, rank);
				rank++;
			}
			Long start = System.currentTimeMillis();
			entry.addPosting(new Posting(token.getDocId(),frequencies.get(result)));
			Long end = System.currentTimeMillis();
			System.out.println("\t\t\t"+(end-start));
			result++;
			
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
	{	
		Builder bui = new Builder();
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("type1", 1));
		tokens.add(new Token("type2", 2));
		tokens.add(new Token("type1", 1));
		tokens.add(new Token("type1", 3));
		tokens.add(new Token("type2", 2));
		tokens.add(new Token("type1", 3));
		tokens.add(new Token("type8", 4));
		tokens.add(new Token("type1", 1));
		tokens.add(new Token("type1", 3));
		tokens.add(new Token("type2", 2));
		tokens.add(new Token("type1", 3));
		tokens.add(new Token("type1", 4));
		
		// test de filterTokens
		//TODO méthode à compléter (TP2-ex1)
		Collections.sort(tokens);
		System.out.println("==========  test de filterTokens  ============\n");
		int i = bui.filterTokens(tokens);
		System.out.format("Nombre de Termes: %d%n",i);
		System.out.println(tokens.size()+"\n");
		// test de buildPostings
		//TODO méthode à compléter (TP2-ex2)
		System.out.println("============== test de buildPostings  ==================\n");
		ArrayIndex index = new ArrayIndex(tokens.size());
		int j = bui.buildPostings(tokens, index);
		System.out.println("Size of token :"+tokens.size()+" Result: "+j+"\n");
		
		// test de buildIndex
		//TODO méthode à compléter (TP2-ex3)
		System.out.println("==============  test de buildIndex  ================\n");
		Configuration.setCorpusName("wp");
		List<Token> tokensListe = new LinkedList<Token>();
		Tokenizer t =new Tokenizer();
		t.tokenizeCorpus(tokensListe);
		bui.buildIndex(tokensListe, LexiconType.ARRAY);
		
		// test de filterTokens
		//TODO méthode à compléter (TP6-ex4)
		List<Integer> frequencies = new ArrayList<Integer>();
		
		i = bui.filterTokens(new ArrayList<Token>(Arrays.asList(new Token("type1", 3),new Token("type1", 3),new Token("type1", 5),new Token("type1", 5),new Token("type2", 5),new Token("type2", 5),new Token("type3", 1))),frequencies);
		System.out.format("Nombre de Termes: %d%n",i);
		System.out.println(frequencies);
		
		// test de buildPostings
		//TODO méthode à compléter (TP6-ex5)
	}
}
