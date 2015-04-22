public class HashTester {
	MetagenomeSample mg;
    InputController ic;
	HashManager hm;
	
	  public HashTester(MetagenomeSample mg, InputController ic, HashManager hm) {
			this.mg = mg;
			this.ic = ic;
			this.hm = hm;
			
			TranslateToKmerRepresentation();
			
			
		}
	    
	  private void TranslateToKmerRepresentation(){
    	long kmer_sum;
    	long kmer_prod;
    	
    	mg.kmers = new Kmers[mg.SEQUENCES_SIZE];
    	for(int i = 0; i < mg.SEQUENCES_SIZE; i++){
    		mg.kmers[i] = new Kmers(mg.seqlen[i] - ic.KMER_SIZE + 1);
    		for(int j = 0; j < mg.seqlen[i] - ic.KMER_SIZE + 1; j++){
    			kmer_prod = 1;
    			kmer_sum = 0;
    			for(int k = 0; k < ic.KMER_SIZE; k++){
    				kmer_sum += kmer_prod * mg.converted_sequences[i].intSequence[j + k];
    				kmer_prod = kmer_prod * mg.BASES_LENGTH;
    			}
    			mg.kmers[i].kmerTrans[j] = kmer_sum;
    		}
    	}
    }

}
