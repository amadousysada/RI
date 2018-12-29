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
public class AndQueryEngine
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
	public AndQueryEngine(AbstractIndex index)
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
		List<List<Posting>> list=new ArrayList<List<Posting>>();
		
		Long start = System.currentTimeMillis();
		
		System.out.println("Processing query "+'"'+query+'"');
		
		splitQuery(query, list);
		
		if(list.size() == 1){
			result=list.get(0);
		}
		else result=processConjunctions(list);
		
		Long end = System.currentTimeMillis();
		
		System.out.format("Query processed, returned %d postings, duration=%d ms%n", result.size(),(end-start));
		
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
	private void splitQuery(String query, List<List<Posting>> result) throws ClassNotFoundException, IOException
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
	{	List<Posting> result = null;
		//TODO méthode à compléter (TP3-ex2)
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
	{	List<Posting> result = null;
		
		//TODO méthode à compléter (TP3-ex4)
		Collections.sort(lists,COMPARATOR);
		Iterator<List<Posting>> it =lists.iterator();
		result = processConjunction(it.next(), it.next());
		
		while(it.hasNext()){
			result = processConjunction(it.next(), result);
		}
		//TODO méthode à modifier  (TP4-ex11)
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
		AndQueryEngine aqe = new AndQueryEngine(a);
		// test de splitQuery
		// TODO méthode à compléter (TP3-ex1)
		
		aqe.splitQuery("recherche ET INFORMATION ET Web", new ArrayList<List<Posting>>() );
		//List<String> ls = new Tokenizer().tokenizeString("aliou bsagags s9d89cxz 3 481*& ^5 $ ~ husj");
		//System.out.print(ls);
		// test de processConjunction
		// TODO méthode à compléter (TP3-ex2)
		List<Posting> list1=new ArrayList<Posting>(
				Arrays.asList(new Posting(1),new Posting(2),new Posting(3),new Posting(4),new Posting(5),new Posting(6),new Posting(7),new Posting(17),new Posting(18)));
		List<Posting> list2=new ArrayList<Posting>(
				Arrays.asList(new Posting(3),new Posting(4),new Posting(10),new Posting(16),new Posting(17),new Posting(19)));
		List<Posting> listpostings=aqe.processConjunction(list1, list2);
		System.out.println(listpostings);
		// test de COMPARATOR
		// TODO méthode à compléter (TP3-ex3)
		Comparator<List<Posting>> c=AndQueryEngine.COMPARATOR;
		int i =c.compare(list1, list2);
		System.out.println(i);
		// test de processConjunctions
		// TODO méthode à compléter (TP3-ex4)
		List<Posting> list3=new ArrayList<Posting>(
				Arrays.asList(new Posting(1),new Posting(4),new Posting(19),new Posting(26)));
		listpostings= aqe.processConjunctions(Arrays.asList(list1,list2,list3));
		System.out.println(listpostings);
		// test de processQuery
		// TODO méthode à compléter (TP3-ex5)
		aqe.processQuery("recherche INFORMATION Web");
	}
}
