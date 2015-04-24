import java.io.IOException;

public class MinHashMain {
	public static void main(String[] args) throws IOException {
		MetagenomeSample mg = new MetagenomeSample();
		InputController ic = new InputController();
		HashManager hm = new HashManager();
		IOManager io = new IOManager();
		String file = "C:\\MC-MinH\\SampleSeq.txt";
		
		ic.inputfileString = file;
		
        ic.KMER_SIZE = 7;
        ic.threshold = 0.95;
        ic.program = "dna";
        
        hm.prime_div = 112211;
		hm.HASH_SIZE = 100;
		long now = System.nanoTime();
		StringMapper strmap = new StringMapper(ic.program);
		io.ReadMetagenomeData(mg, strmap, ic);
		
		mg.BASES_LENGTH = strmap.Bases.size() - 2;
		MinHash minHash = new MinHash(mg, ic, hm);

		long then = System.nanoTime();
		long taken = then - now;
		
		double seconds =(double) taken / 1000000000.0; 
		System.out.println("The time it took in seconds was " + seconds);
		
		
	}
}