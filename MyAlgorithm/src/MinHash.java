import java.util.Arrays;
import java.util.Random;

import sun.launcher.resources.launcher;

public class MinHash{	
    MetagenomeSample mg;
    InputController ic;
	HashManager hm;
	
    public MinHash(MetagenomeSample mg, InputController ic, HashManager hm) {
		this.mg = mg;
		this.ic = ic;
		this.hm = hm;

		InitHashFunctions();
		TranslateToKmerRepresentation();
		SetupHashValues();
		ComputeMinwiseSimilarity();
	}
    
    // Takes the number of hashes, and saves into hash_a and hash_b the
    // value of them at each hash_function.
    // The hashfunctions have the form ((a * x + b) mod p) mod m
    // where p > m is a primenumber, and m is the size of feature set.
	private void InitHashFunctions(){
		hm.hash_a = new int[hm.HASH_SIZE];
		hm.hash_b = new int[hm.HASH_SIZE];
		Random r = new Random();
		for (int i = 0; i < hm.HASH_SIZE; i++){
			hm.hash_a[i] = i+1;
			hm.hash_b[i] = i;
		}
	}
	
	// Translates all the sequences into Kmers and saves the Kmers into 
	// the variable kmers
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
    
	private void SetupHashValues(){
		long perm_value;
		hm.minhash_values = new int[mg.SEQUENCES_SIZE][hm.HASH_SIZE];
		hm.maxhash_values = new int[mg.SEQUENCES_SIZE][hm.HASH_SIZE];

		for(int i = 0; i < mg.SEQUENCES_SIZE; i++){
			for(int j = 0; j < hm.HASH_SIZE; j++){
				hm.minhash_values[i][j] = Integer.MAX_VALUE;
				hm.maxhash_values[i][j] = Integer.MIN_VALUE; 
				for(int k = 0; k < mg.seqlen[i] - ic.KMER_SIZE + 1; k++){
					perm_value = (((hm.hash_a[j] * mg.kmers[i].kmerTrans[k] + hm.hash_b[j] ) % hm.prime_div)) % mg.SEQUENCES_SIZE;
					if(hm.minhash_values[i][j] > perm_value ){
						hm.minhash_values[i][j] = (int)perm_value;
					}
					if(hm.maxhash_values[i][j] < perm_value ){
						hm.maxhash_values[i][j] = (int) perm_value; 
					}
				}
			}
		}
	}
	

	private void ComputeMinwiseSimilarity(){
		float similarity;
		int intersections, unions,k;
		int here = 0;
		int current_cluster = 0;
		int[] seq_cluster = new int[mg.SEQUENCES_SIZE];
		
		for(int i = 0; i < mg.SEQUENCES_SIZE; i++){
			if(seq_cluster[i] != 0 ){}
			else{
				current_cluster++;
				seq_cluster[i]= current_cluster;
				for(int j = 0; j < mg.SEQUENCES_SIZE;j++){
					if(seq_cluster[j]!= 0){}
					else{
						for(intersections=0,unions=0,k=0;k<hm.HASH_SIZE;k++){
							intersections += (((hm.minhash_values[i][k] == hm.minhash_values[j][k]) | hm.maxhash_values[i][k]== hm.maxhash_values[j][k])?1:0);
							unions++;
						}
						similarity =((float) intersections) / ((float) unions);
						if(similarity >= ic.threshold){
							seq_cluster[j]=current_cluster;
						}
					}
				}
			}
		}
		
		System.out.println("Number of clusters is  " + current_cluster);
	}
}
