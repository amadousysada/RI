import indexation.AbstractIndex;
import indexation.AbstractIndex.LexiconType;
import indexation.AbstractIndex.TokenListType;
import indexation.content.Posting;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.List;

import query.AndOrQueryEngine;
import query.AndQueryEngine;

import tools.Configuration;
import tools.FileTools;

/**
 * Classe permettant de tester
 * notre indexation.
 */
public class Test1
{	/**
	 * Méthode principale.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 * 
	 * @throws IOException 
	 * 		Problème lors d'un accès fichier.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de la lecture de l'index
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{	// configuration de l'index
		//TODO méthode à compléter (TP2-ex5)
		Configuration.setCorpusName("wp_test");
		
		//TODO méthode à compléter (TP4-ex14)
		//TODO méthode à compléter (TP5-ex10)
		
		// test de l'indexation
		//TODO méthode à compléter (TP2-ex5) 
		//testIndexation();
		
		// test du chargement d'index
		//TODO méthode à compléter (TP2-ex11)
		//AbstractIndex index = AbstractIndex.read();
		
		// test du traitement de requêtes
		//TODO méthode à compléter (TP3-ex6)
		testQuery();
		
		// test de l'évaluation de performance
		//TODO méthode à compléter (TP4-ex9)
	}
	
	////////////////////////////////////////////////////
	//	INDEXATION
	////////////////////////////////////////////////////
	/**
	 * Teste les classes permettant de créer le fichier inverse.
	 * 
	 * @throws IOException 
	 * 		Problème lors d'un accès fichier.
	 */
	private static void testIndexation() throws IOException
	{	//TODO méthode à compléter (TP2-ex5)
		AbstractIndex a = (AbstractIndex.indexCorpus(TokenListType.LINKED, LexiconType.ARRAY));
		a.print();
		
		//TODO méthode à compléter (TP2-ex10)
		a.write();
	}
	
	////////////////////////////////////////////////////
	//	REQUÊTES
	////////////////////////////////////////////////////
	/**
	 * Teste les classes permettant de traiter les requêtes.
	 * 
	 * @throws IOException 
	 * 		Problème lors d'un accès fichier.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de la lecture de l'index
	 */
	private static void testQuery() throws IOException, ClassNotFoundException
	{	//TODO méthode à compléter (TP3-ex6)
		
		Long start = System.currentTimeMillis();
		System.out.println("Loading the index ...");
		
		AbstractIndex index = AbstractIndex.read();
		
		Long end = System.currentTimeMillis();
		System.out.format("Index loaded, duration=%d ms%n%n",(end-start));
		
		String[] requetes={"recherche","recherche INFORMATION","recherche INFORMATION Web"};
		AndQueryEngine aqe=new AndQueryEngine(index);
		for(String query:requetes){
			List<Posting> postings=aqe.processQuery(query);
			System.out.println(postings);
			List<String> files=FileTools.getFileNamesFromPostings(postings);
			System.out.format("Files:%n %s%n%n",files);
		}
		//TODO méthode à compléter (TP3-ex12)
		AndOrQueryEngine aqe1=new AndOrQueryEngine(index);
		System.out.println("----------------------------------------------------");
		String[] request ={"recherche,INFORMATION,Web,document,ordinateur","recherche INFORMATION Web document ordinateur","recherche INFORMATION Web,document ordinateur"};
		for(String query:request){
			List<Posting> postings=aqe1.processQuery(query);
			System.out.println(postings);
			List<String> files=FileTools.getFileNamesFromPostings(postings);
			System.out.format("Files:%n %s%n%n",files);
		}
		//TODO méthode à compléter (TP5-ex11)
		
		//TODO méthode à compléter (TP6-ex13)
	}
	
	////////////////////////////////////////////////////
	//	ÉVALUATION
	////////////////////////////////////////////////////
	/**
	 * Calcule les performances du moteur de recherche,
	 * et les affiche dans la console.
	 */
	private static void testEvaluation()
	{	//TODO méthode à compléter (TP4-ex9)
		//TODO méthode à compléter (TP6-ex16)
	}
}
