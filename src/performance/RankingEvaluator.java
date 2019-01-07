package performance;

import indexation.AbstractIndex;
import indexation.content.Posting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import query.DocScore;
import query.RankingQueryEngine;

/**
 * Classe utilisée pour évaluer la performance
 * d'un index vectoriel sur un jeu de requêtes prédéfinies,
 * pour lesquelles on connait la vérité terrain.
 */
public class RankingEvaluator extends AbstractEvaluator
{	
	/**
	 * Initialise un évaluateur vectoriel pour la vérité terrain 
	 * spécifiée dans la configuration. 
	 * 
	 * @throws ParserConfigurationException
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws IOException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws SAXException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 */
	public RankingEvaluator() throws ParserConfigurationException, SAXException, IOException
	{	super();
	}
	
	////////////////////////////////////////////////////
	//	MESURES
	////////////////////////////////////////////////////
	/**
	 * Calcule les trois mesures sur toutes les requêtes considérées
	 * Le paramètre {@code answers} est la liste des réponses <i>ordonnée</i>
	 * renvoyées par le moteur de recherche pour toutes ces requêtes.
	 * Le paramètre {@code k} permet de distinguer les document considérés
	 * comme pertinents par le moteur ({@code k} premiers) des non-pertinents.
	 *   
	 * @param answers
	 * 		Liste de listes d'objets DocScore renvoyée par le moteur de recherche pour les requêtes traitées.
	 * @param k
	 * 		Seuil utilisé pour distinguer les documents considérs comme pertinents des non-pertinents.
	 * @return
	 * 		Liste de maps contenant les performances calculées pour les requêtes. Chaque
	 * 		map correspond à une requête de la vérité terrain, sauf la dernière, qui
	 * 		contient les valeurs moyennes.
	 */
	private List<Map<MeasureName,Float>> evaluateQueryAnswers(List<List<DocScore>> answers, int k)
	{	List<Map<MeasureName,Float>> result = null;
		//TODO méthode à compléter  (TP6-ex14)
		List<List<Posting>> convAnswers = new ArrayList<List<Posting>>();

		for(List<DocScore> answer: answers)
		{
			List<Posting> convAnswer = new ArrayList<Posting>();
			convAnswers.add(convAnswer);
			Iterator<DocScore> it = answer.iterator();
			int i = 0;

			while(it.hasNext() && i<k)
			{
				DocScore docScore = it.next();
				int docId = docScore.getDocId();
				Posting posting = new Posting(docId);
				convAnswer.add(posting);
				i++;
			}
		}

		result = evaluateQueryAnswers(convAnswers);
		
		return result;
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
	 * 		map correspond à des valeurs moyennes pour une valeur de k donnée. Ces moyennes
	 * 		sont obtenues en considérant toutes les requêtes d'évaluation.
	 * @throws IOException 
	 */
	public List<Map<MeasureName,Float>> evaluateEngine(RankingQueryEngine engine) throws IOException
	{	List<Map<MeasureName,Float>> result = null;
		//TODO méthode à compléter  (TP6-ex15)
		System.out.println("Evaluating the search engine");
		AbstractIndex index = engine.getIndex();
		int docNbr = index.getDocumentNumber();
		// on traite chaque requête d'évaluation
		List<List<DocScore>> answers = new ArrayList<List<DocScore>>();
		List<String> queries = groundTruth.getQueries();
		for(String query: queries)
		{
			List<DocScore> answer = engine.processQuery(query,0);
			answers.add(answer);
		}
		// on calcule les performances correspondant aux réponses
		result = new ArrayList<Map<MeasureName,Float>>();
		for(int k=1;k<=docNbr;k++)
		{
			System.out.println("k="+k);
			List<Map<MeasureName,Float>> temp = evaluateQueryAnswers(answers,k);
			Map<MeasureName,Float> meanVals = temp.get(temp.size()-1);
			result.add(meanVals);
		}
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
	{	// test de evaluateQueryAnswers
		//TODO méthode à compléter  (TP6-ex14)
		
		// test de evaluateEngine
		//TODO méthode à compléter  (TP6-ex15)
	}
}
