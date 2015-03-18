import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

public class Kmers {
	long[] kmerTrans;
	int number_of_kmers;
	
	public Kmers(int size) {
		kmerTrans = new long[size];
		number_of_kmers = size;
	}
	
	public void setKmer(int i, long kmerRepresentation){
		kmerTrans[i] = kmerRepresentation;
	}
	
	public long getKmer(int i){
		return kmerTrans[i];
	}
	
}
