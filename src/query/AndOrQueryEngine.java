package query;

import indexation.AbstractIndex;
import indexation.ArrayIndex;

import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import tools.Configuration;

/**
 * Objet capable de traiter une
 * requête booléenne sur un index.
 */
public class AndOrQueryEngine
{	/**
	 * Initialise ce moteur de recherche avec
	 * l'index passé en paramètre, qui sera
	 * considéré comme index de référence
	 * lors de l'évaluation des requêtes
	 * reçues.
	 * 
	 * @param index
	 * 		Index de référence.
	 */
	public AndOrQueryEngine(AbstractIndex index)
	{	this.index = index;
	}
	
	////////////////////////////////////////////////////
	//	TRAITEMENT GENERAL
	////////////////////////////////////////////////////
	/**
	 * Traite la requête passée en paramètre
	 * et renvoie la liste des documents concernés.
	 * 
	 * @param query
	 * 		Requête à traiter.
	 * @return
	 * 		Liste des documents concernés.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public List<Posting> processQuery(String query) throws ClassNotFoundException, IOException
	{	List<Posting> result = null;
		//TODO méthode à compléter (TP3-ex5)
		List<List<List<Posting>>> list=new ArrayList<List<List<Posting>>>();
		
		Long start = System.currentTimeMillis();
		
		System.out.println("Processing query "+'"'+query+'"');
		
		splitOrQuery(query, list);
		
		List<List<Posting>>list2=new ArrayList<List<Posting>>();
		
		for (List<List<Posting>> l:list){
			if(l.size()>1){
				list2.add(processConjunctions(l));
			}
			else{
				list2.add(l.get(0));
			}
		}
		System.out.println(list2);
;
		if(list2.size()>1){
		result =processDisjunctions(list2);
		}
		if(list2.size()==1){
			result =list2.get(0);
		}
		if(list2.size()==0){
			result =new ArrayList<Posting>();
		}
		
		Long end = System.currentTimeMillis();
		
		System.out.format("Query processed, duration=%d ms%n Result: %d document(s)%n", (end-start),result.size());
		
		return result;
	}
	
	/**
	 * Comparateur traitant deux listes de postings.
	 * On utilise simplement leurs longueurs.
	 */
	private static final Comparator<List<Posting>> COMPARATOR = new Comparator<List<Posting>>()
	{	@Override
		public int compare(List<Posting> l1, List<Posting> l2)
		{	int result = -1;
			
			result = l1.size()-l2.size();
			//TODO méthode à compléter  (TP3-ex3)
			return result;
		}
	};
	
	/**
	 * Tokénise et normalise la requête, de manière
	 * à obtenir une liste de termes. Ces termes
	 * sont ensuite traités pour récupérer les
	 * entrées correspondantes dans l'index, et
	 * surtout leurs listes de postings.
	 * 
	 * @param query
	 * 		Requête à traiter.
	 * @param result
	 * 		Liste résultat à compléter, qui doit contenir à
	 * 		la fin du traitement les postings de l'index 
	 * 		correspondant aux termes obtenus après nettoyage 
	 * 		de la requête. 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void splitOrQuery(String query, List<List<List<Posting>>> result) throws ClassNotFoundException, IOException
	{	
		String[] req = query.split(",");
		for(String string: req){
			List<List<Posting>> l=new ArrayList<List<Posting>>();
			splitAndQuery(string, l);
			if(l.size()>0)
				result.add(l);
		}
		//TODO méthode à modifier  (TP4-ex10)
	}
	
	/**
	 * 
	 * @param query la requete ET
	 * @param result List<List<Posting>>
	 * @throws ClassNotFoundException exc
	 * @throws IOException exc
	 */
	private void splitAndQuery(String query, List<List<Posting>> result) throws ClassNotFoundException, IOException
	{	//TODO méthode à compléter (TP3-ex1)
		String req = "";
		Tokenizer tk=new Tokenizer();
		Normalizer nr=new Normalizer();

		for (String string:query.split("ET"))
			req+=string;
		
		for(String string: tk.tokenizeString(req)){
			String type = nr.normalizeType(string);
			if(!type.equals("")){
				IndexEntry entry = index.getEntry(type);
				
				if(entry ==null){
					result=new ArrayList<List<Posting>>();
					break;
				}
				result.add(entry.getPostings());
			}
		}		
		//TODO méthode à modifier  (TP4-ex10)
	}
	
	////////////////////////////////////////////////////
	//	DISJONCTIONS
	////////////////////////////////////////////////////
	/**
	 * Combine les deux listes de postings passées
	 * en paramètre en utilisant l'opérateur OU.
	 * 
	 * @param list1
	 * 		Première liste de postings.
	 * @param list2
	 * 		Seconde liste de postings.
	 * @return
	 * 		Le résultat de OU sur ces deux listes.
	 */
	private List<Posting> processDisjunction(List<Posting> list1, List<Posting> list2)
	{	
		List<Posting> result = null;

		int k=0;
		List<Posting> postListe = new ArrayList<Posting>();
		for(Posting post:list1){
			if(k>=list2.size()){
				postListe.add(post);
			}
			for(int i=k;i<list2.size();i++){
				int c =post.compareTo(list2.get(i));
				if(c<0){
					postListe.add(post);
					break;
				}
				if(c==0){
					k=i+1;
					postListe.add(post);
					break;
				}
				if(c>0){
					postListe.add(list2.get(i));
					k=i+1;
				}
			}
			
		}
		
		result = postListe;

		return result;
	}

	/**
	 * Traite une disjonction de plus de
	 * deux termes.
	 * 
	 * @param lists
	 * 		Liste de listes de postings de l'index, correspondant aux termes à traiter.
	 * @return
	 * 		union de toutes les listes de postings.
	 */
	private List<Posting> processDisjunctions(List<List<Posting>> lists)
	{	List<Posting> result = null;
		
		Collections.sort(lists,COMPARATOR);
		Iterator<List<Posting>> it =lists.iterator();
		result = processDisjunction(it.next(), it.next());
		while(it.hasNext()){
			List<Posting> postings = it.next();
			Comparator<List<Posting>> c=COMPARATOR;
			int i =c.compare(postings, result);
			if(i>0){
				result = processDisjunction(result,postings);
			}
			else{
				result = processDisjunction(postings,result);
			}
			
		}
		return result;
	}
	
	
	////////////////////////////////////////////////////
	//	CONJONCTIONS
	////////////////////////////////////////////////////
	/**
	* Combine les deux listes de postings passées
	* en paramètre en utilisant l'opérateur ET.
	* 
	* @param list1
	* 		Première liste de postings.
	* @param list2
	* 		Seconde liste de postings.
	* @return
	* 		Le résultat de ET sur ces deux listes.
	*/
	private List<Posting> processConjunction(List<Posting> list1, List<Posting> list2)
	{	
		List<Posting> result = null;
		int k=0;
		List<Posting> postListe = new ArrayList<Posting>();
		for(Posting post:list1){
		
			for(int i=k;i<list2.size();i++){
				int c =post.compareTo(list2.get(i));
				if(c<0){
					break;
				}
				if(c==0){
					k=i+1;
					postListe.add(post);
					break;
				}
			}
			if(k>list2.size()-1)
			{break;}
		}
		result = postListe;
		//TODO méthode à modifier  (TP4-ex12)
		return result;
	}
	
	/**
	* Traite une conjonction de plus de
	* deux termes.
	* 
	* @param lists
	* 		Liste de listes de postings de l'index, correspondant aux termes à traiter.
	* @return
	* 		Intersection de toutes les listes de postings.
	*/
	private List<Posting> processConjunctions(List<List<Posting>> lists)
	{	
		List<Posting> result = null;
		if(lists.size()==0){
			return result;
		}
		
		Collections.sort(lists,COMPARATOR);
		Iterator<List<Posting>> it =lists.iterator();
		result = processConjunction(it.next(), it.next());
	
		while(it.hasNext()){
			result = processConjunction(it.next(), result);
		}
		return result;
	}

	////////////////////////////////////////////////////
	//	INDEX
	////////////////////////////////////////////////////
	/** Index de référence */
	private AbstractIndex index;
	
	/**
	 * Renvoie l'index associé à ce moteur.
	 * 
	 * @return
	 * 		Index associé à ce moteur.
	 */
	public AbstractIndex getIndex()
	{	return index;
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
		Configuration.setCorpusName("wp_test");
		AbstractIndex a =AbstractIndex.read();
		AndOrQueryEngine aqe = new AndOrQueryEngine(a);
		
		// test de splitOrQuery
		List<List<List<Posting>>> result = new ArrayList<List<List<Posting>>>();
		aqe.splitOrQuery("recherche INFORMATION Web,document ordinateur", result);
		System.out.println(result);
		
		// test de processDisjunction
		List<Posting> list1=new ArrayList<Posting>(
				Arrays.asList(new Posting(1),new Posting(2),new Posting(3),new Posting(4),new Posting(5),new Posting(6),new Posting(7),new Posting(17),new Posting(18)));
		List<Posting> list2=new ArrayList<Posting>(
				Arrays.asList(new Posting(3),new Posting(4),new Posting(10),new Posting(16),new Posting(17),new Posting(19)));
		List<Posting> listpostings=aqe.processDisjunction(list1, list2);
		System.out.println("Union");
		System.out.println(listpostings);
		
		// test de processConjunctions
		List<Posting> list3=new ArrayList<Posting>(
				Arrays.asList(new Posting(1),new Posting(4),new Posting(19),new Posting(26)));
		listpostings= aqe.processDisjunctions(Arrays.asList(list1,list2,list3));
		System.out.println(listpostings);
	}
}
