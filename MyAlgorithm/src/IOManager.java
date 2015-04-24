import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.MGF1ParameterSpec;
import java.time.chrono.JapaneseChronology;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

public class IOManager {
	void ReadMetagenomeData(MetagenomeSample mg, StringMapper sm, InputController ic) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(ic.inputfileString));
		
		String line;
		int nSeq = 0;
		mg.maxseqlength = 0;
		while((line = reader.readLine())!= null){
			nSeq++;
		}
		nSeq = nSeq / 2;
		reader.close();
		mg.SEQUENCES_SIZE = nSeq;
		
		System.out.println("there are " + mg.SEQUENCES_SIZE + " sequences");
		
		InsertSequencesInMemory(ic.inputfileString, mg);

		
		mg.converted_sequences = new ConvertedSequence[mg.SEQUENCES_SIZE];
		for(int i = 0; i < mg.SEQUENCES_SIZE; i++){
			mg.converted_sequences[i] = new ConvertedSequence(mg.seqlen[i]);
			mg.converted_sequences[i].intSequence = sm.mapStringToIntArray(mg.sequences[i], mg.seqlen[i]); 
		}
		
	}
	
	private static void InsertSequencesInMemory(String file,MetagenomeSample mg) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		
		int i = 0;
		int j = 0;
		mg.maxseqlength = 0;
		mg.sequences = new String[mg.SEQUENCES_SIZE];
		mg.seqlen = new int[mg.SEQUENCES_SIZE];
		while((line = reader.readLine())!= null){
			if((j % 2) == 1){
				mg.sequences[i] = line;
				mg.seqlen[i] = line.length();
				if(mg.seqlen[i] > mg.maxseqlength){
					mg.maxseqlength = mg.seqlen[i];
				}
				i++;
			}
			j++;
		}
		reader.close();
	}
}
