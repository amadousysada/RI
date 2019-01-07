import indexation.AbstractIndex;
import indexation.AbstractIndex.LexiconType;
import indexation.AbstractIndex.TokenListType;
import indexation.content.Posting;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import performance.BooleanEvaluator;
import performance.AbstractEvaluator.MeasureName;

import query.AndOrQueryEngine;
import query.AndQueryEngine;
import query.DocScore;
import query.RankingQueryEngine;

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
 * @throws SAXException 
 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException
	{	// configuration de l'index
		//TODO méthode à compléter (TP2-ex5)
		Configuration.setCorpusName("wp");
		
		//TODO méthode à compléter (TP4-ex14)
		//Configuration.setStemmingTokens(true);
		
		//TODO méthode à compléter (TP5-ex10)
		//Configuration.setFilteringStopWords(true);
		
		// test de l'indexation
		//TODO méthode à compléter (TP2-ex5) 
		testIndexation();
		
		// test du chargement d'index
		//TODO méthode à compléter (TP2-ex11)
		//AbstractIndex index = AbstractIndex.read();
		
		// test du traitement de requêtes
		//TODO méthode à compléter (TP3-ex6)
		testQuery();
		
		// test de l'évaluation de performance
		//TODO méthode à compléter (TP4-ex9)
		//testEvaluation();
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
		//a.print();
		
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
		
		/*String[] requetes={"recherche","recherche INFORMATION","recherche INFORMATION Web"};
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
		//TODO méthode à compléter (TP5-ex11)*/
		
		//TODO méthode à compléter (TP6-ex13)
		String[] queries = {"roman","recherche d\'information sur le Web","panneaux solaires électricité"};
		RankingQueryEngine rankEng = new RankingQueryEngine(index);
		for(String query:queries){
			int k=5;
			List<DocScore> docScores = rankEng.processQuery(query, k);
			System.out.println("Result: "+docScores.size()+" document(s)");
			System.out.println(docScores);
			List<String> files=FileTools.getFileNamesFromDocScores(docScores);
			System.out.format("Files:%n %s%n%n",files);
		}
	}
	
	////////////////////////////////////////////////////
	//	ÉVALUATION
	////////////////////////////////////////////////////
	/**
	 * Calcule les performances du moteur de recherche,
	 * et les affiche dans la console.
	 * @param <M>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	private static <M> void testEvaluation() throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException
	{	//TODO méthode à compléter (TP4-ex9)
		AbstractIndex index = AbstractIndex.read();
		AndQueryEngine engine=new AndQueryEngine(index);
		Configuration.setCorpusName("springer");
		BooleanEvaluator b = new BooleanEvaluator();
		
		List<Map<MeasureName,Float>> list =b.evaluateEngine(engine);
		float p_mean=0;
		float r_mean=0;
		float f_mean=0;
		
		int i=0;
		System.out.println(MeasureName.PRECISION+"   "+MeasureName.RECALL+"   "+MeasureName.F_MEASURE);
		for (Map<MeasureName,Float> map:list) {
			System.out.format("%f   %f   %f   %s%n%n", map.get(MeasureName.PRECISION),map.get(MeasureName.RECALL),map.get(MeasureName.F_MEASURE),b.getGroundTruth().getQueries().get(i));
			p_mean=p_mean+map.get(MeasureName.PRECISION)/25;
			r_mean=r_mean+map.get(MeasureName.RECALL)/25;
			f_mean=f_mean+map.get(MeasureName.F_MEASURE)/25;
			i++;
		}
		System.out.format("-----------------------------------------------%n%f   %f   %f", p_mean,r_mean,f_mean);
		//TODO méthode à compléter (TP6-ex16)
	}
}
