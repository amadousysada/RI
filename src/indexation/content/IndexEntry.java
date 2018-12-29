package indexation.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une entrée de l'index,
 * comprenant un terme, une liste
 * de postings et la fréquence du terme
 * exprimée en documents.
 */
public class IndexEntry implements Serializable, Comparable<IndexEntry>
{	/** Class id (juste pour éviter le warning) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée une nouvelle entrée d'index,
	 * à partir du terme passé en paramètre.
	 * 
	 * @param term
	 * 		Terme inséré dans l'index.
	 */
	public IndexEntry(String term)
	{	this.term = term;
		this.postings = new ArrayList<Posting>();
		this.frequency = 0;
	}
	
	////////////////////////////////////////////////////
	//	TERME
	////////////////////////////////////////////////////
	/** Terme concerné par ce posting */
	private String term;
	
	/**
	 * Renvoie le terme associé à cette entrée de l'index.
	 * 
	 * @return
	 * 		Le terme de cette entrée.
	 */
	public String getTerm()
	{	return term;
	}
	
	////////////////////////////////////////////////////
	//	POSTINGS
	////////////////////////////////////////////////////
	/** Liste des postings contenant le terme */
	private List<Posting> postings;
	
	/**
	 * Renvoie la liste de postings associée à cette entrée de l'index.
	 * 
	 * @return
	 * 		La liste de postings de cette entrée.
	 */
	public List<Posting> getPostings()
	{	return postings;
	}
	
	/**
	 * Ajoute le posting spécifié à la liste associée
	 * à cette entrée de l'index.
	 * 
	 * @param posting
	 * 		Posting à ajouter à la liste de cette entrée.
	 */
	public void addPosting(Posting posting)
	{	postings.add(posting);
		incrementFrequency();
		//     ajouter l'incrementation la frequence (appeler incrementFrequency)
	}
	
	////////////////////////////////////////////////////
	//	FREQUENCE
	////////////////////////////////////////////////////
	/** Fréquence du terme exprimée en nombre de documents */
	private int frequency;
	
	/**
	 * Renvoie la fréquence associée à cette entrée de l'index,
	 * exprimée en nombre de documents.
	 * 
	 * @return
	 * 		La fréquence de cette entrée, en nombre de documents.
	 */
	public int getFrequency()
	{	return frequency;
	}
	
	/**
	 * Incrémente la fréquence de cette entrée, d'une unité.
	 */
	public void incrementFrequency()
	{	frequency++;
	}
	
	////////////////////////////////////////////////////
	//	COMPARABLE
	////////////////////////////////////////////////////
	@Override
	public int compareTo(IndexEntry entry)
	{	int result = 0;
		//TODO méthode à compléter (TP1-ex9)
		result = this.term.compareTo(entry.term);
		
		
		
		return result;
	}
	
	////////////////////////////////////////////////////
	//	OBJECT
	////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = null;
		//TODO méthode à compléter (TP1-ex9)
		result = "<"+term+" ["+frequency+"] " + postings+">";
		return result;
	}
	
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		//TODO méthode à compléter (TP1-ex9)
		result = (this.compareTo((IndexEntry)o)==0);
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
		IndexEntry ie = new IndexEntry("bateau");
		IndexEntry ie1 = new IndexEntry("avion");
		IndexEntry ie2 = new IndexEntry("tgv");
		ie.addPosting(new Posting(1));
		ie.addPosting(new Posting(5));
		ie.addPosting(new Posting(99));
		ie.addPosting(new Posting(694));
		ie.addPosting(new Posting(702));
		// test de equals
		// TODO méthode à compléter (TP1-ex9)
		System.out.println(ie.equals(ie1));
		System.out.println(ie.equals(ie2));
		System.out.println(ie.equals(ie));
		
				
		// test de compareTo
		// TODO méthode à compléter (TP1-ex9)
		System.out.println(ie.compareTo(ie1));
		System.out.println(ie1.compareTo(ie2));
				
		// test de toString
		// TODO méthode à compléter (TP1-ex9)
		System.out.println(ie.toString());
	}
}
