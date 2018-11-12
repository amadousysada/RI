package indexation;

import java.util.Map;
import java.util.TreeMap;

import indexation.content.IndexEntry;
import indexation.content.Posting;

/**
 * Objet représentant un index sous
 * la forme d'un fichier inverse simple,
 * dont le lexique est stocké dans un arbre
 * de recherche.
 */
public class TreeIndex extends AbstractIndex
{	/** Class id (juste pour éviter le warning) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construit un nouvel index vide,
	 * de la taille indiquée en paramètre.
	 */
	public TreeIndex()
	{	//TODO méthode à compléter (TP1-ex10)
		this.data = new TreeMap<String, IndexEntry>();
	}
	
	////////////////////////////////////////////////////
	//	DONNÉES
	////////////////////////////////////////////////////
	/** Lexique et postings de l'index */
	private TreeMap<String,IndexEntry> data;
	
	@Override
	public void addEntry(IndexEntry indexEntry, int rank)
	{	//TODO méthode à compléter (TP1-ex12)
		if(this.data.containsValue(indexEntry)) return;
		this.data.put(indexEntry.getTerm(), indexEntry);
	}
	
	@Override
	public IndexEntry getEntry(String term)
	{	IndexEntry result = null;
		//TODO méthode à compléter (TP1-ex13)
		result = this.data.get(term);
		return result;
	}
	
	@Override
	public int getSize()
	{	int result = 0;
		//TODO méthode à compléter (TP1-ex14)
		result = this.data.size();
		return result;
	}

	////////////////////////////////////////////////////
	//	AFFICHAGE
	////////////////////////////////////////////////////
	/**
	 * Affiche le contenu de l'index.
	 */
	@Override
	public void print()
	{	//TODO méthode à compléter (TP1-ex11)
		
		for (Map.Entry<String, IndexEntry> element : data.entrySet()) {
			System.out.println(element.getValue().toString());
		}
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
		IndexEntry ie = new IndexEntry("barque");
		IndexEntry ie1 = new IndexEntry("bateau");
		IndexEntry ie2 = new IndexEntry("coquille");
		ie.addPosting(new Posting(1));
		ie.addPosting(new Posting(5));
		ie.addPosting(new Posting(99));
		ie.addPosting(new Posting(694));
		ie1.addPosting(new Posting(702));
		ie1.addPosting(new Posting(694));
		ie1.addPosting(new Posting(702));
		ie2.addPosting(new Posting(694));
		ie2.addPosting(new Posting(702));
		// test du constructeur
		//TODO méthode à compléter (TP1-ex10)
		TreeIndex treeind = new TreeIndex();
		treeind.data.put("barque", ie);
		treeind.data.put("bateau", ie1);
		treeind.data.put("coquille", ie2);
		
		// test de print
		//TODO méthode à compléter (TP1-ex11)
		System.out.println("Test de print() ");
		treeind.print();
		
		// test de addEntry
		//TODO méthode à compléter (TP1-ex12)
		System.out.println("Test de addEntry() ");
		IndexEntry in =new IndexEntry("testeEntry");
		in.addPosting(new Posting(23));
		treeind.addEntry(in, 0);
		treeind.print();
		// test de getEntry
		//TODO méthode à compléter (TP1-ex13)
		System.out.println("Test de getEntry() ");
		IndexEntry i = treeind.getEntry("bateau");
		System.out.print(i.toString()+"\n");
		// test de getSize
		//TODO méthode à compléter (TP1-ex14)
		System.out.println("Test de getSize() ");
		System.out.print("Size : "+treeind.getSize());
	}
}
