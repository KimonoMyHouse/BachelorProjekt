import java.util.Random;

public class HashValues {
	int size;
	int kmerSize;
	long testHashes[];
	public HashValues(int size, int kmerSize){
		this.size = size;
		this.kmerSize = kmerSize;
		testHashes = new long[size];
		long bits = (((long)1) << (2*kmerSize)) - 1;
		Random r = new Random();
		for(int i = 0; i < size; i++){
			testHashes[i] = nextLong(r, bits);
		}
	}
	
	long nextLong(Random rng, long n) {
		   // error checking and 2^x checking removed for simplicity.
		   long bits, val;
		   do {
		      bits = (rng.nextLong() << 1) >>> 1;
		      val = bits % n;
		   } while (bits-val+(n-1) < 0L);
		   return val;
	}
}
