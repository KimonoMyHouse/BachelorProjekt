import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.Clock;
import java.util.Arrays;

import javax.crypto.spec.IvParameterSpec;
import javax.print.DocFlavor.READER;
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
		hm.HASH_SIZE = 70;
		long now = System.nanoTime();
		System.out.println("Begins :" + now);
		StringMapper strmap = new StringMapper(ic.program);
		io.ReadMetagenomeData(mg, strmap, ic);
		
		mg.BASES_LENGTH = strmap.Bases.size();
		MinHash minHash = new MinHash(mg, ic, hm);

		long then = System.nanoTime();
		long taken = then - now;
		
		System.out.println("The time it took was " + taken);
		
		
	}
}
