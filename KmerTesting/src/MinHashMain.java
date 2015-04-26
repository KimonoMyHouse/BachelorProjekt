import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MinHashMain {
	public static void main(String[] args) throws IOException {
		MetagenomeSample mg = new MetagenomeSample();
		InputController ic = new InputController();
		HashManager hm = new HashManager();
		IOManager io = new IOManager();
		long then = System.nanoTime();
		
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + "out.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	        
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			
			int[] kmerTestSizes = {2,4,5,7,10,12,15,20,25,30};
			int[] hashTestSizes = {25,40,50,70,100,150,200,400};
	
			for (int i : kmerTestSizes) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j : hashTestSizes) {
					hm.HASH_SIZE = j;
					MinHash minHash = new MinHash(mg, ic, hm);
				}
			}
			ic.pw.close();
		}
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
	}
}
