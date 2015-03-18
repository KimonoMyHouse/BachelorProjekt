import com.sun.xml.internal.ws.commons.xmlutil.Converter;

public class MetagenomeSample {
	public int SEQUENCES_SIZE; // number of sequences
	public String[] sequences; // all sequences
	public ConvertedSequence[] converted_sequences; // sequences after mapping
	
	public int maxseqlength;
	public int[] seqlen;
	
    public Kmers[] kmers; // storage of transformed kmers
    
    public int BASES_LENGTH;
}
