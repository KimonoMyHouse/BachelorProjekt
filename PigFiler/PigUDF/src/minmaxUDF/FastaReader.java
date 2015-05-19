package minmaxUDF;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.pig.LoadFunc;
import org.apache.pig.PigException;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;


public class FastaReader extends LoadFunc{
	   protected RecordReader    in            = null;
	   private ArrayList<Object> mProtoTuple   = null;
	   private TupleFactory      mTupleFactory = TupleFactory.getInstance();

	   /**
	    * null constructor
	    */
	   public FastaReader()
	   {
	   }

	   /**
	    * returns the next sequence from the block
	    */
	   public Tuple getNext() throws IOException
	   {

	      if (mProtoTuple == null)
	      {
	         mProtoTuple = new ArrayList<Object>();
	      }

	      try {
	         boolean notDone = in.nextKeyValue();
	         if (!notDone)
	         {
	            return(null);
	         }

	         /*
	           check the id of the sequence to see if its a paired read
	          */
	         String seqid = (in.getCurrentKey()).toString();
	         String seqkey = null;
	         String seqkey2;
	         String header = "";
	         String direction;
			 String index = "0";
	         for (int i = 0; i < seqid.length(); i++) {
	            if (seqid.charAt(i) == ' ' || seqid.charAt(i) == '\t') {
	               seqkey = seqid.substring(0, i);
	               header = seqid.substring(i, seqid.length());
	               break;
	            }
	         }
	         if (seqkey == null) seqkey = seqid;
	         if (seqkey.indexOf("/") >= 0) {
	            String[] a = seqkey.split("/");
	            seqkey2 = a[0];
	            direction = a[1];
	         } else {
	            seqkey2 = seqkey;
	            direction = "0";
	         }
	         Text value     = ((Text)in.getCurrentValue());
	         //mProtoTuple.add(new DataByteArray(seqkey2.getBytes(), 0, seqkey2.length()));                   // add key
	         //mProtoTuple.add(new DataByteArray(direction.getBytes(), 0, direction.length()));               // add direction
	         //mProtoTuple.add(new DataByteArray(value.getBytes(), 0, value.getLength()));                    // add sequence
	         //mProtoTuple.add(new DataByteArray(header.getBytes(), 0, header.length()));                     // add header
			 //mProtoTuple.add(new DataByteArray(index.getBytes(), 0, index.length()));						// add index
	         mProtoTuple.add(value.toString());
	         
	         Tuple t = mTupleFactory.newTupleNoCopy(mProtoTuple);
	         mProtoTuple = null;
	         return(t);
	      } catch (InterruptedException e) {
	         int    errCode = 6018;
	         String errMsg  = "Error while reading input";
	         throw new ExecException(errMsg, errCode,
	                                 PigException.REMOTE_ENVIRONMENT, e);
	      }
	   }

	   public InputFormat getInputFormat()
	   {
	      return(new FastaInputFormat());
	   }

	   public void prepareToRead(RecordReader reader, PigSplit split)
	   {
	      in = reader;
	   }

	   public void setLocation(String location, Job job)
	   throws IOException
	   {
	      FileInputFormat.setInputPaths(job, location);
	   }
}
