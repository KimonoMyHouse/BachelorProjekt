SET mapreduce.map.java.opts -Xmx2048m;

%default KMER_SIZE 5
%default INPUT 'long.fasta'
%default HASH_SIZE 100
%default THRESHOLD 0.95
%default OUTPUT 'clusters.cl'

REGISTER '/home/mehdi/Documents/MinMaxHash/minmaxUDF.jar';

-- Loads the file

sequences = LOAD '$INPUT' USING minmaxUDF.FastaReader AS (line: chararray);

-- Convert strings to byte arrays.
converted = FOREACH sequences GENERATE minmaxUDF.SequenceToByteArray(line, 'DNA');

NoN = FILTER converted BY byteSeq is not null;

kmers = FOREACH NoN GENERATE minmaxUDF.toKmerTransformations(byteSeq, $KMER_SIZE);

minmaxvalues = FOREACH kmers GENERATE FLATTEN(minmaxUDF.ComputeMaxMinHashValues(kmerlist, $HASH_SIZE, $KMER_SIZE));

grouped = GROUP minmaxvalues ALL;

a_count = FOREACH grouped GENERATE COUNT(minmaxvalues);

results = FOREACH grouped GENERATE FLATTEN(minmaxUDF.ComputeMaxMinSimilarity(minmaxvalues, a_count.$0, $HASH_SIZE, $THRESHOLD));

rm out.cl;
STORE results INTO 'out.cl';