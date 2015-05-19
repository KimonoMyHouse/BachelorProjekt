package minmaxUDF;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class toKmerTransformations extends EvalFunc<Tuple>{
	TupleFactory tf;
	
	static int NUM_OF_BASES = 4;
	@Override
	public Tuple exec(Tuple input) throws IOException {
    
		if(input == null || input.size() == 0)
			return null;
		
		try{
			DataByteArray dba = (DataByteArray) input.get(0);
			
			int kmer_size = (int) input.get(1);
		
			if(kmer_size == 0)
				return null;

			if(tf == null)
				tf = TupleFactory.getInstance();
            
            return translateToKmerTransformation(dba, tf, kmer_size);
		}catch(Exception e){
			throw new IOException("toKmerTransformations: the following exception was cast " + e);
		}
	}
	
	private Tuple translateToKmerTransformation(DataByteArray dba, TupleFactory tf, int kmer_size){
		int kmer_prod, kmer_sum;
		int i,k;
		byte[] byteSeq = dba.get();
		Tuple tp = tf.newTuple(byteSeq.length - kmer_size + 1);
		for( kmer_prod = 1, kmer_sum = 0, i = 0; i < byteSeq.length - kmer_size + 1; i++){
			kmer_prod = 1;
			kmer_sum=0;
			for(k=0; k < kmer_size; k++){
				kmer_sum += kmer_prod * ((int) byteSeq[i+k]);
				kmer_prod = kmer_prod * NUM_OF_BASES; 
			}
			try {
				tp.set(i,kmer_sum);
			} catch (ExecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tp;
	}
	
	public Schema outputSchema(Schema input) {
        try{
            Schema bagSchema = new Schema();
            bagSchema.add(new Schema.FieldSchema("kmer", DataType.INTEGER));

            return new Schema(new Schema.FieldSchema("kmerlist", bagSchema, DataType.TUPLE));
        }catch (Exception e){
           return null;
        }
   }
}
