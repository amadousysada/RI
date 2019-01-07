import indexation.ArrayIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> l = new ArrayList<String>(Arrays.asList("hi","samba","Marie","","Ola"));
		System.out.println(l);
		ListIterator<String> it = l.listIterator();
		while(it.hasNext()){
			String s = it.next();
			if(s.isEmpty()){
				it.set("ca marche");
			}
		}
		
		System.out.println(l);
		

	}

}
