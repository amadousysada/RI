package performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.Configuration;
import tools.FileTools;

import indexation.content.Posting;

/**
 * Classe utilisée pour représenter une vérité terrain,
 * i.e. une séquence de requêtes d'évaluation, chacune 
 * accompagnée de sa liste de documents pertinents.
 */
public class GroundTruth
{	
	/**
	 * Initialise la vérité terrain à partir du fichier XML
	 * spécifié dans la configuration. 
	 * 
	 * @throws ParserConfigurationException
	 * 		Problème lors de la lecture de la vérité terrain.
	 * @throws IOException 
	 * 		Problème lors de la lecture de la vérité terrain.
	 * @throws SAXException 
	 * 		Problème lors de la lecture de la vérité terrain.
	 */
	public GroundTruth() throws ParserConfigurationException, SAXException, IOException
	{	//TODO méthode à compléter  (TP4-ex3)
		queries = new ArrayList<String>();
		postingLists = new ArrayList<List<Posting>>();
		
		System.out.println("Reading ground truth file ..\\Common\\springer_reference.xml\n\n");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(FileTools.getGroundTruthFile());
		Element element =document.getDocumentElement();
		NodeList nodes = element.getElementsByTagName("query");
		
		String nb = "(";
        for (int i = 0; i < nodes.getLength(); i++) {
        	Node nNode = nodes.item(i);
			
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    			Element eElement = (Element) nNode;
    			queries.add(eElement.getAttribute("expr"));
    			
    			List<String> fileNames = new ArrayList<String>();
    			for(int j=0;j<eElement.getElementsByTagName("doc").getLength();j++){
    				fileNames.add(eElement.getElementsByTagName("doc").item(j).getTextContent());
    			}
    			List<Posting> list=FileTools.getPostingsFromFileNames(fileNames);
    			nb+=" "+list.size();
    			postingLists.add(list);

    		}

        }
        System.out.format("Found %d queries %s )%n%n",queries.size(),nb);
	}
	
	////////////////////////////////////////////////////
	//	REQUÊTES
	////////////////////////////////////////////////////
	/** Liste des requêtes utilisées lors de l'évaluation */
	private List<String> queries;
	
	/**
	 * Renvoie les requêtes d'évaluation associées à cette vérité terrain.
	 * 
	 * @return
	 * 		Une liste de chaînes de caractères correspondant chacune à une requête.
	 */
	public List<String> getQueries()
	{	return queries;
	}
	
	////////////////////////////////////////////////////
	//	DOCUMENTS
	////////////////////////////////////////////////////
	/** Liste de documents pertinents pour chaque requête d'évaluation */
	private List<List<Posting>> postingLists;
	
	/**
	 * Renvoie la liste de postings associée
	 * à larequête d'évaluation dont le numéro
	 * est spécifié en paramètre.
	 * 
	 * @param queryId
	 * 		Numéro de la requête concernée.
	 * @return
	 * 		Liste de liste de postings.
	 */
	public List<Posting> getPostingList(int queryId)
	{	List<Posting> result = postingLists.get(queryId);
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
	{	Configuration.setCorpusName("springer");
		// test du constructeur
		//TODO méthode à compléter  (TP4-ex3)
		new GroundTruth();
	}
}
