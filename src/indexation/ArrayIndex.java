package indexation;

import java.util.Arrays;

import indexation.content.IndexEntry;
import indexation.content.Posting;

/**
 * Objet représentant un index sous
 * la forme d'un fichier inverse simple,
 * dont le lexique est stocké dans un
 * tableau.
 */
public class ArrayIndex extends AbstractIndex
{	/** Class id (juste pour éviter le warning) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construit un nouvel index vide,
	 * de la taille indiquée en paramétre.
	 * 
	 * @param size
	 * 		Taille de l'index (expriée en nombre de termes).
	 */
	public ArrayIndex(int size)
	{	//TODO méthode à compléter (TP1-ex10)
		this.data = new IndexEntry[size];
	}
	
	////////////////////////////////////////////////////
	//	DONNÉES
	////////////////////////////////////////////////////
	/** Lexique et postings de l'index */
	private IndexEntry[] data;
	
	@Override
	public void addEntry(IndexEntry indexEntry, int rank)
	{	//TODO méthode à compléter (TP1-ex12)
		
		this.data[rank] = indexEntry;
	}
	
	@Override
	public IndexEntry getEntry(String term)
	{	IndexEntry result = null;
	
		//TODO méthode à compléter (TP1-ex13)
		int i = Arrays.binarySearch(this.data, new IndexEntry(term));
		if (i>0)
			result = this.data[i];
		
		return result;
	}
	
	@Override
	public int getSize()
	{	int result = 0;
		//TODO méthode à compléter (TP1-ex14)
		result = this.data.length;
		return result;
	}
	
	/**
	 * Renvoie le tableau correspondant
	 * au lexique de cet index.
	 * 
	 * @return
	 * 		Lexique sous forme de tableau d'entrées.
	 */
	public IndexEntry[] getEntries()
	{	return data;
	}
	
	/**
	 * 
	 * @param entries
	 * @return
	 */
	public void setEntries(IndexEntry[] entries) {
		this.data=entries;
		
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
		for (IndexEntry indexEntry : data) {
			if(indexEntry ==null){
				continue;
			}
			
			System.out.println(indexEntry.toString());
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
		ArrayIndex arrIndex = new ArrayIndex(3);
		arrIndex.data[0] = ie;
		arrIndex.data[1] = ie1;
		arrIndex.data[2] = ie2;
		
		// test de print
		//TODO méthode à compléter (TP1-ex11)
		System.out.println("Test de print() ");
		arrIndex.print();
		
		// test de addEntry
		//TODO méthode à compléter (TP1-ex12)
		System.out.println("Test de addEntry() ");
		IndexEntry in =new IndexEntry("testeEntry");
		in.addPosting(new Posting(23));
		arrIndex.addEntry(in, 2);
		arrIndex.print();
		// test de getEntry
		//TODO méthode à compléter (TP1-ex13)
		System.out.println("Test de getEntry() ");
		IndexEntry i = arrIndex.getEntry("bateau");
		System.out.print(i.toString()+"\n");
		// test de getSize
		//TODO méthode à compléter (TP1-ex14)
		System.out.println("Test de getSize() ");
		System.out.print("Size : "+arrIndex.getSize());
	}
}
