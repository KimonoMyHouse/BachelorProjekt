package minmaxUDF;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class FastaInputFormat extends FileInputFormat<Text, Text> {

	  public RecordReader<Text, Text>
	    createRecordReader(InputSplit split,
	                       TaskAttemptContext context) {
	    return new FastaRecordReader();
	  }

	  protected boolean isSplitable(JobContext context, Path file) {
	    CompressionCodec codec =
	      new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
	    return codec == null;
	  }
}
