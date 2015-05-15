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
		
		/* SINGLE
		String filein = "C:/Users/User/Desktop/short.fasta";
		String fileout = "C:/Users/User/Desktop/short1.fasta";

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
		
		ic.inputfileString = filein;
		ic.pw = pw;
		
        ic.threshold = 0.95;
        ic.program = "dna";
        

		StringMapper strmap = new StringMapper(ic.program);
		mg.BASES_LENGTH = 4;
        hm.prime_div = 1845587707;

		io.ReadMetagenomeData(mg, strmap, ic);

		ic.KMER_SIZE = 5;
		hm.HASH_SIZE = 50;
		MinHash minHash = new MinHash(mg, ic, hm);
		
		ic.pw.close();
		
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		*/
		
		// FOR TESTING THE LARGE PICTURE OF KMER AND HASHSIZE
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
		
		/*
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + "outprecise5.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			
			int[] kmerTestSizes = {5};
			int[] hashTestSizes = {60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78};
	
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
		*/
		// for Testing at k=10
		/*
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + "outprecise10.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			
			int[] kmerTestSizes = {10};
	
			for (int i : kmerTestSizes) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j =15;j<40;j++) {
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
		
		*/
		// The big one
		/*
		for(int n = 1; n < 2; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + "outpreciselarge.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	        
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			
	
			for (int i = 5; i< 11; i++) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j =15;j<70;j++) {
					hm.HASH_SIZE = j;
					MinHash minHash = new MinHash(mg, ic, hm);
				}
			}
			ic.pw.close();
		}
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);*/
	}
}
