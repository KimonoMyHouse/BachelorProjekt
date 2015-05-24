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
		
		/*
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

		ic.KMER_SIZE = 20;
		hm.HASH_SIZE = 1;
		MinHash minHash = new MinHash(mg, ic, hm);
		
		ic.pw.close();
		
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		*/
		/*
		// FOR TESTING THE LARGE PICTURE OF KMER AND HASHSIZE
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMaxHalf/test" + n + "out.fna";

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
			int[] hashTestSizes = {10,20,30,40,50,60,70,100,150,200};
	
			for (int i : kmerTestSizes) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j = 2; j < 50; j = j + 2) {
					hm.HASH_SIZE = j;
					MinMaxHalf minHash = new MinMaxHalf(mg, ic, hm);
				}
			}
			ic.pw.close();
		}
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		*/
		
		/*
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMax/test" + n + "out.fna";

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
			int[] hashTestSizes = {10,20,30,40,50,60,70,100,150,200};
	
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
		now = System.nanoTime();
		
		taken = now - then;

		seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		*/
		
		
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMax/test" + n + "outprecise5.fna";

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
	
			for (int i : kmerTestSizes) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j=50;j<81;j++) {
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
		// for Testing at k=6
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMax/test" + n + "outprecise6.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			
			int[] kmerTestSizes = {6};
	
			for (int i : kmerTestSizes) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j =20;j<30;j++) {
					hm.HASH_SIZE = j;
					MinHash minHash = new MinHash(mg, ic, hm);
				}
			}
			ic.pw.close();
		}
		
		*/
		
		/*
		// for Testing at k=10
		
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMax/test" + n + "outprecise10.fna";

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
		now = System.nanoTime();
		
		taken = now - then;

		seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		
		*/
		
		// The big one
		/*
		for(int n = 1; n < 6; n++){
			
			String filein = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/test" + n + ".fna";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/MinMax/test" + n + "outpreciselarge.fna";

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileout)));
			
			ic.inputfileString = filein;
			ic.pw = pw;
			
	        ic.threshold = 0.95;
	        ic.program = "dna";
	        
	
			StringMapper strmap = new StringMapper(ic.program);
			mg.BASES_LENGTH = 4;
	        hm.prime_div = 1845587707;
	
			io.ReadMetagenomeData(mg, strmap, ic);
	
			for (int i = 5; i< 14; i++) {
				System.out.println(n + " " + i);
				ic.KMER_SIZE = i;
				for (int j =10;j<80;j++) {
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
		/*
			String filein = "C:/Users/User/Desktop/long.fasta";
			String fileout = "C:/Users/User/Documents/KU/Attending/BachelorProjekt/KmerTesting/testData/TheLargeOne/longpreciselarge.fna";

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
				System.out.println(i);
				ic.KMER_SIZE = i;
				for (int j =15;j<70;j++) {
					hm.HASH_SIZE = j;
					MinHash minHash = new MinHash(mg, ic, hm);
				}
			}
			ic.pw.close();
		long now = System.nanoTime();
		
		long taken = now - then;

		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		*/
		
		}
}
