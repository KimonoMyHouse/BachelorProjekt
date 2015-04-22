	import java.util.Hashtable;
import java.util.Map;

public class StringMapper {
	Map<Character,Integer> Bases = new Hashtable<Character, Integer>();
	
	public StringMapper(String program) {
		if(program.toLowerCase() == "rna"){
			Bases.put('a', 0);
			Bases.put('c', 1);
			Bases.put('g', 2);
			Bases.put('u', 3);
			Bases.put('A', 0);
			Bases.put('C', 1);
			Bases.put('G', 2);
			Bases.put('U', 3);
		}
		else{
			if(program.toLowerCase() == "dna"){
				Bases.put('a', 0);
				Bases.put('c', 1);
				Bases.put('g', 2);
				Bases.put('t', 3);
				Bases.put('A', 0);
				Bases.put('C', 1);
				Bases.put('G', 2);
				Bases.put('T', 3);
			}
			else{
				System.out.println("The program should either be rna or dna. The given data was : " + program + " and is not recognized as a program.");
				throw new IllegalArgumentException("program must either be rna or gna, the input given was: " + program );
			}
		}
	}
	
	public int[] mapStringToIntArray(String str, int strlen){
		int[] temp = new int[strlen];
		for(int i = 0; i < strlen; i++){
			temp[i] = Bases.get(str.charAt(i));
		}
		return temp;
	}
}
