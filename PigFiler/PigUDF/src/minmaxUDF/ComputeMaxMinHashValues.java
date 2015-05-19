package minmaxUDF;

import java.io.IOException;
import java.util.Iterator;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

public class ComputeMaxMinHashValues extends EvalFunc<Tuple>{
	TupleFactory tf;
	int HASH_SIZE;
	int FEATURE_SET_SIZE;
	int KMER_SIZE;
	int prime = Integer.MAX_VALUE;
	int[] a;
	int[] b;
	
	
	@Override
	public Tuple exec(Tuple input) throws IOException {
		if(input == null || input.size() == 0)
			return null;
			
		try{
			Tuple kmers = (Tuple) input.get(0);
			
			if(HASH_SIZE < 1)
				HASH_SIZE = (int) input.get(1);
			
			if(KMER_SIZE < 1)
				KMER_SIZE = (int) input.get(2);
			
			if(FEATURE_SET_SIZE < 1)
				setFeatureSetSize(KMER_SIZE);
			
			if(a == null || b == null)
				InitHashFunctions();
			
			if(tf == null)
				tf = TupleFactory.getInstance();

			return ComputeHashes(kmers);
		}catch(Exception e){
			throw new IOException("ComputeMaxMinHashValues: the following error came " + e);
		}
	}
	
	private Tuple ComputeHashes(Tuple kmers) throws IOException{
		int nKMERS = kmers.size();
		int[] kmerList = loadKmers(kmers, nKMERS);
		int perm_value;
		int minhash_value = Integer.MAX_VALUE;
		int maxhash_value = Integer.MIN_VALUE;
	
		Tuple all_hashes_tuple = tf.newTuple(HASH_SIZE);
		
		for(int i = 0; i < HASH_SIZE; i++){
			minhash_value = Integer.MAX_VALUE;
			maxhash_value = Integer.MIN_VALUE;
			
			for(int j = 0; j < nKMERS; j++){
				perm_value = ((a[i] * kmerList[j] + b[i]) % prime) % FEATURE_SET_SIZE;
				minhash_value = perm_value < minhash_value ? perm_value: minhash_value;
				maxhash_value = perm_value > maxhash_value ? perm_value: maxhash_value;
			}
			
			Tuple tp = tf.newTuple(2);
			
			tp.set(0, minhash_value);
			tp.set(1, maxhash_value);
			
			all_hashes_tuple.set(i, tp);
		}
		return all_hashes_tuple;
	}

	private int[] loadKmers(Tuple kmers, int KMER_SIZE){
		int[] tmp = new int[KMER_SIZE];
		Iterator it = kmers.iterator();
		int i=0;
		while(it.hasNext()){
			int kmer = (int) it.next();
			tmp[i] = kmer;
			i++;
		}
		
		return tmp;
	}
	
	private void InitHashFunctions(){
		a = new int[HASH_SIZE];
		b = new int[HASH_SIZE];
		for(int i = 0; i < HASH_SIZE; i++){
			a[i] = i+1;
			b[i] = i;
		}
	}
	
	public Schema outputSchema(Schema input) {
        try{
            Schema minmaxSchema = new Schema();
            minmaxSchema.add(new Schema.FieldSchema("minhash", DataType.INTEGER));
            minmaxSchema.add(new Schema.FieldSchema("maxhash", DataType.INTEGER));

            Schema hashSchema = new Schema(new Schema.FieldSchema("minmax", minmaxSchema, DataType.TUPLE));

            return new Schema(new Schema.FieldSchema("hashes", hashSchema, DataType.TUPLE));
        }catch (Exception e){
           return null;
        }
   }
	
	private void setFeatureSetSize(int kmersize){
		int kmer_sum = 0;
		int kmer_prod = 1;
		
		for (int i = 0; i < kmersize; i++){
			kmer_sum += kmer_prod * 3;
			kmer_prod = kmer_prod * 4;
		}
		FEATURE_SET_SIZE = kmer_sum;
	}
}
