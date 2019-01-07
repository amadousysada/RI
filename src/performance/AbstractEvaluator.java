package performance;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FileTools;

import indexation.content.Posting;

/**
 * Classe utilisée pour mettre en commun les méthodes
 * et champs nécessaires à l'évaluation de la performance
 * d'un index sur un jeu de requêtes prédéfinies,
 * pour lesquelles on connait la vérité terrain.
 */
public abstract class AbstractEvaluator
{	
	/**
	 * Initialise un évaluateur pour la vérité terrain 
	 * spécifiée dans la configuration. 
	 * 
	 * @throws ParserConfigurationException
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws IOException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 * @throws SAXException 
	 * 		Problème lors de la lecture de la vérité terrain 
	 */
	public AbstractEvaluator() throws ParserConfigurationException, SAXException, IOException
	{	//TODO méthode à compléter  (TP4-ex4)
		groundTruth = new GroundTruth();
	}
	
	/**
	 * Nom d'une mesure de performance
	 * utilisée pour évaluer le processus
	 * d'indexation et de recherche.
	 */
	public enum MeasureName
	{	/** Précision */
		PRECISION,
		/** Rappel */
		RECALL,
		/** F-mesure ou F1-score */
		F_MEASURE;
	}
	
	////////////////////////////////////////////////////
	//	VÉRITÉ TERRAIN
	////////////////////////////////////////////////////
	/** Vérité terrain */
	protected GroundTruth groundTruth;
	
	/**
	 * Renvoie la vérité terrain chargée à la construction
	 * de cet évaluateur.
	 * 
	 * @return
	 * 		Objet représentant la vérité terrain.
	 */
	public GroundTruth getGroundTruth()
	{	return groundTruth;
	}
	
	////////////////////////////////////////////////////
	//	MESURES
	////////////////////////////////////////////////////
	/**
	 * Calcule les trois mesures du cours permettant
	 * d'évaluer le resultat de la requête considérée.
	 * Le paramètre {@code queryId} est le numéro de cette
	 * requête parmi celles constituant la vérité terrain,
	 * et le paramètre {@code answer} est la liste de posting
	 * renvoyée par le moteur de recherche pour la requête.
	 *   
	 * @param queryId
	 * 		Numéro de la requête parmi celles constituant la vérité terrain.
	 * @param answer
	 * 		Liste de postings renvoyée par le moteur de recherche pour cette requête.
	 * @return
	 * 		Map contenant les performances calculées pour cette requête et ce résultat.
	 */
	protected Map<MeasureName,Float> evaluateQueryAnswer(int queryId, List<Posting> answer)
	{	 Map<MeasureName,Float> result = null;
		//TODO méthode à compléter  (TP4-ex5)
		List<Posting> postingsTruth = groundTruth.getPostingList(queryId);
		
		int vp =0;
		int fp=0;
		int fn=0;
		for(Posting post:answer){
			if(postingsTruth.contains(post)){
				vp++;
			}
			else{
				fp++;
			}
		}
		fn=(postingsTruth.size()-vp);
		float precision = (float) 0;
		float rappel = (float) 0;
		float f_measure = (float) 0;
		
		
		if(vp==0 && answer.size()==0) rappel =1;
		else{
			precision = (float) (vp)/(vp+fp);
			rappel = (float) (vp)/(vp+fn);
			if(precision>0 && rappel>0) f_measure = (2*precision*rappel)/(precision+rappel);
		}
		
		
		result = new HashMap<AbstractEvaluator.MeasureName, Float>();
		result.put(MeasureName.PRECISION, precision);
		result.put(MeasureName.RECALL, rappel);
		result.put(MeasureName.F_MEASURE, f_measure);
		
		return result;
	}
	
	/**
	 * Calcule les trois mesures sur toutes les requêtes considérées
	 * Le paramètre {@code answers} est la liste des réponses
	 * renvoyées par le moteur de recherche pour toutes ces requêtes.
	 *   
	 * @param answers
	 * 		Liste de listes de postings renvoyée par le moteur de recherche pour les requêtes traitées.
	 * @return
	 * 		Liste de maps contenant les performances calculées pour les requêtes. Chaque
	 * 		map correspond à une requête de la vérité terrain, sauf la dernière, qui
	 * 		contient les valeurs moyennes.
	 */
	protected List<Map<MeasureName,Float>> evaluateQueryAnswers(List<List<Posting>> answers)
	{	List<Map<MeasureName,Float>> result = null;
		//TODO méthode à compléter  (TP4-ex6)
		result = new ArrayList<Map<MeasureName,Float>>();
		for(int i=0;i<answers.size();i++){
			result.add(evaluateQueryAnswer(i, answers.get(i)));
		}
		return result;
	}
	
	////////////////////////////////////////////////////
	//	EXPORTATION
	////////////////////////////////////////////////////
	/**
	 * Ecrit dans le fichier texte spécifié dans la configuration
	 * les valeurs passées en paramètre. Chaque type de mesure prend
	 * la forme d'une colonne dans le fichier.
	 *  
	 * @param values
	 * 		Listes de maps de valeurs à traiter.
	 * @throws IOException 
	 */
	protected void writePerformances(List<Map<MeasureName,Float>> values) throws IOException
	{	//TODO méthode à compléter  (TP4-ex7)
		File file = new File(FileTools.getPerformanceFile());
		FileOutputStream fos =new FileOutputStream(file);
		OutputStreamWriter out = new OutputStreamWriter(fos);
		BufferedWriter bw =new BufferedWriter(out);
		
		for (Map<MeasureName,Float> map: values) {
			bw.write(Float.toString(map.get(MeasureName.PRECISION)));
			bw.write("\t\t");
			bw.write(Float.toString(map.get(MeasureName.RECALL)));
			bw.write("\t\t");
			bw.write(Float.toString(map.get(MeasureName.F_MEASURE)));
			bw.newLine();
			
		}
		bw.flush();
		bw.close();
		
		
	}
}
