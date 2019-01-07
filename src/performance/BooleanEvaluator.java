package performance;

import indexation.AbstractIndex;
import indexation.content.Posting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import query.AndQueryEngine;
import tools.Configuration;

/**
 * Classe utilisée pour évaluer la performance
 * d'un index booléen sur un jeu de requêtes prédéfinies,
 * pour lesquelles on connait la vérité terrain.
 */
public class BooleanEvaluator extends AbstractEvaluator
{	
	/**
	 * Initialise un évaluateur booléen pour la vérité terrain 
	 * spécifiée dans la configuration. 
	 * 
	 * @throws ParserConfigurationException
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws IOException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws SAXException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 */
	public BooleanEvaluator() throws ParserConfigurationException, SAXException, IOException
	{	super();
	}
	
	////////////////////////////////////////////////////
	//	ÉVALUATION
	////////////////////////////////////////////////////
	/**
	 * Evalue le moteur de recherche spécifié
	 * avec la vérité terrain précédemment chargée,
	 * et enregistre et renvoie les performances.
	 * 
	 * @param engine
	 * 		Moteur de recherche à évaluer.
	 * @return
	 * 		Liste de maps contenant les performances calculées pour les requêtes. Chaque
	 * 		map correspond à une requête de la vérité terrain, sauf la dernière, qui
	 * 		contient les valeurs moyennes.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public List<Map<MeasureName,Float>> evaluateEngine(AndQueryEngine engine) throws ClassNotFoundException, IOException
	{	List<Map<MeasureName,Float>> result = null;
		//TODO méthode à compléter  (TP4-ex8)
		List<List<Posting>> answers = new ArrayList<List<Posting>>();
		for(String query:getGroundTruth().getQueries()){
			answers.add(engine.processQuery(query));
		}
		result = evaluateQueryAnswers(answers);
		writePerformances(result);
		
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
	{	// test de evaluateQueryAnswer
		//TODO méthode à compléter  (TP4-ex5)
		Configuration.setCorpusName("springer");
		BooleanEvaluator b = new BooleanEvaluator();
		List<Posting> answer = new ArrayList<Posting>(
				Arrays.asList(new Posting(15),new Posting(16),new Posting(17),new Posting(18),new Posting(19),new Posting(20)));
		Map<MeasureName, Float> m=b.evaluateQueryAnswer(1, answer);
		System.out.println(m);
		// test de evaluateQueryAnswers
		//TODO méthode à compléter  (TP4-ex6)
		List<List<Posting>> answers=new ArrayList<List<Posting>>();
		for(int i=0;i<25;i++){
			answers.add(answer);
		}
		List<Map<MeasureName, Float>> m2=b.evaluateQueryAnswers(answers);
		System.out.println(m2);
		// test de writePerformances
		//TODO méthode à compléter  (TP4-ex7)
		b.writePerformances(m2);
		// test de evaluateEngine
		//TODO méthode à compléter  (TP4-ex8)
		Configuration.setCorpusName("wp");
		AbstractIndex a =AbstractIndex.read();
		AndQueryEngine engine = new AndQueryEngine(a);
		Configuration.setCorpusName("springer");
		b.evaluateEngine(engine);
	}
}
