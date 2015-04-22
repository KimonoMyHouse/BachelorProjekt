public class Kmers {
	int[] kmerTrans;
	int number_of_kmers;
	
	public Kmers(int size) {
		kmerTrans = new int[size];
		number_of_kmers = size;
	}
	
	public void setKmer(int i, int kmerRepresentation){
		kmerTrans[i] = kmerRepresentation;
	}
	
	public long getKmer(int i){
		return kmerTrans[i];
	}
	
}
