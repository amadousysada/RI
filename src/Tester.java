import indexation.ArrayIndex;

import java.util.Arrays;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] s =new String[10];
		String[] sp="web ET rds".split("ET");
		System.out.println(Arrays.toString(sp));
		s[0]="uji";
		s[1]="po78";
		s=Arrays.copyOf(s, 2);
		Arrays.sort(s);

	}

}
