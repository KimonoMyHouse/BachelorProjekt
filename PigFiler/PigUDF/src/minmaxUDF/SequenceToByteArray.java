package minmaxUDF;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class SequenceToByteArray extends EvalFunc<DataByteArray>{
	Map<Character,Byte> Bases;
	char gg;
	
	@Override
	public DataByteArray exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return(null);
		
		try{
			String theString = (String) input.get(0);
			String program = (String) input.get(1);
			if(Bases == null){
				initBases(program);
			}
			
			DataByteArray byteSequence = mapStringToByteArray(theString);
			
			return byteSequence;
		}catch(Exception e){
			throw new IOException("LoadAsIntArray: Input string could not be transformed. Following error " + e + "and character " + gg);
		}
	}
	
	private void initBases(String program){
		Bases = new Hashtable<Character,Byte>();
		if(program.toLowerCase().equals("rna")){
			Bases.put('a', (byte) 0);
			Bases.put('c', (byte) 1);
			Bases.put('g', (byte) 2);
			Bases.put('u', (byte) 3);
			Bases.put('A', (byte) 0);
			Bases.put('C', (byte) 1);
			Bases.put('G', (byte) 2);
			Bases.put('U', (byte) 3);
			Bases.put('n', (byte) 4);
			Bases.put('N', (byte) 4);
		}
		else{
			if(program.toLowerCase().equals("dna")){
				Bases.put('a', (byte) 0);
				Bases.put('c', (byte) 1);
				Bases.put('g', (byte) 2);
				Bases.put('t', (byte) 3);
				Bases.put('A', (byte) 0);
				Bases.put('C', (byte) 1);
				Bases.put('G', (byte) 2);
				Bases.put('T', (byte) 3);
				Bases.put('n', (byte) 4);
				Bases.put('N', (byte) 4);
			}
			else{
				throw new IllegalArgumentException("program must either be rna or dna, the input given was: " + program );
			}
		}
	}
	
	private DataByteArray mapStringToByteArray(String str){
		try{
		int strlen = str.length();
		DataByteArray byteSeq = new DataByteArray();
		byte[] temp = new byte[strlen];
		for(int i = 0; i < strlen; i++){
			gg = str.charAt(i);
			temp[i] = Bases.get(gg);
			if(temp[i] == 4){
				return null;
			}
		}
		
		byteSeq.append(temp);
		return byteSeq;
		}catch(NullPointerException e){
			return null;
		}
	}
	
	public Schema outputSchema(Schema input) {
        try{
            Schema bagSchema = new Schema();
            bagSchema.add(new Schema.FieldSchema("byteSeq", DataType.BYTEARRAY));

            return bagSchema;
        }catch (Exception e){
           return null;
        }
	}
	
}
