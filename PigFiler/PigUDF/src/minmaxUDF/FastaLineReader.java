package minmaxUDF;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class that provides a line reader from an input stream.
 */
public class FastaLineReader {
    private static final Log LOG = LogFactory.getLog(FastaLineReader.class);
  private static final int DEFAULT_BUFFER_SIZE = 64 * 1024;
  private int bufferSize = DEFAULT_BUFFER_SIZE;
  private InputStream in;
  private byte[] buffer;
  // the number of bytes of real data in the buffer
  private int bufferLength = 0;
  // the current position in the buffer
  private int bufferPosn = 0;

  private static final byte CR = '\r';
  private static final byte LF = '\n';
  private static final byte seperator = '>';

  /**
   * Create a line reader that reads from the given stream using the
   * default buffer-size (64k).
   * @param in The input stream
   * @throws java.io.IOException
   */
  public FastaLineReader(InputStream in) {
    this(in, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Create a line reader that reads from the given stream using the
   * given buffer-size.
   * @param in The input stream
   * @param bufferSize Size of the read buffer
   * @throws java.io.IOException
   */
  public FastaLineReader(InputStream in, int bufferSize) {
    this.in = in;
    this.bufferSize = bufferSize;
    this.buffer = new byte[this.bufferSize];
  }

  /**
   * Create a line reader that reads from the given stream using the
   * <code>io.file.buffer.size</code> specified in the given
   * <code>Configuration</code>.
   * @param in input stream
   * @param conf configuration
   * @throws java.io.IOException
   */
  public FastaLineReader(InputStream in, Configuration conf) throws IOException {
    this(in, conf.getInt("io.file.buffer.size", DEFAULT_BUFFER_SIZE));
  }

  /**
   * Close the underlying stream.
   * @throws IOException
   */
  public void close() throws IOException {
    in.close();
  }

  /**
   * Read one line from the InputStream into the given Text.  A line
   * can be terminated by one of the following: '\n' (LF) , '\r' (CR),
   * or '\r\n' (CR+LF).  EOF also terminates an otherwise unterminated
   * line.
   *
   * @param str the object to store the given line (without newline)
   * @param maxLineLength the maximum number of bytes to store into str;
   *  the rest of the line is silently discarded.
   * @param maxBytesToConsume the maximum number of bytes to consume
   *  in this call.  This is only a hint, because if the line cross
   *  this threshold, we allow it to happen.  It can overshoot
   *  potentially by as much as one buffer length.
   *
   * @return the number of bytes read including the (longest) newline
   * found.
   *
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text key, Text str, int maxLineLength,
                      int maxBytesToConsume) throws IOException {
        int totalBytesRead = 0;
        int numRecordsRead = 0;
        Boolean eof = false;
        int startPosn;
        StringBuilder recordBlock = new StringBuilder(this.bufferSize);

        /*
        first thing to do is to move forward till you see a start character
         */
        startPosn = bufferPosn;
        do {
            if (bufferPosn >= bufferLength) {
                totalBytesRead += bufferPosn - startPosn;
                bufferPosn = 0;
                bufferLength = in.read(buffer);
                if (bufferLength <= 0) {
                    eof = true;
                    break; // EOF
                }
            }
        } while (buffer[bufferPosn++] != '>');

        /*
        if we hit the end of file already, then just return 0 bytes processed
         */
        if (eof)
            return totalBytesRead;  

        /*
        now bufferPosn should be at the start of a fasta record
         */
        totalBytesRead += (bufferPosn - 1) - startPosn;
        startPosn = bufferPosn-1;  // startPosn guaranteed to be at a ">"

        /*
        find the next record start:  first scan to end of the line
         */
        eof = false;
        do {
            if (bufferPosn >= bufferLength) {

                /*
                copy the current buffer before refreshing the buffer
                 */
                int appendLength = bufferPosn - startPosn;
                for (int copyi = startPosn; copyi < startPosn+appendLength; copyi++) {
                   recordBlock.append((char) buffer[copyi]);
                }
                //recordBlock.append(buffer, startPosn, appendLength);
                totalBytesRead += appendLength;

                startPosn = bufferPosn = 0;
                bufferLength = in.read(buffer);
                if (bufferLength <= 0) {
                    eof = true;
                    break; // EOF
                }
            }
            bufferPosn++;
        } while (buffer[bufferPosn-1] != CR && buffer[bufferPosn-1] != LF) ;


        /*
        find the next record start:  scan till next ">"
         */
        do {
            if (bufferPosn >= bufferLength) {

                /*
                copy the current buffer before refreshing the buffer
                 */
                int appendLength = bufferPosn - startPosn;
                for (int copyi = startPosn; copyi < startPosn+appendLength; copyi++) {
                   recordBlock.append((char) buffer[copyi]);
                }
                //recordBlock.append(buffer, startPosn, appendLength);
                totalBytesRead += appendLength;

                startPosn = bufferPosn = 0;
                bufferLength = in.read(buffer);
                if (bufferLength <= 0) {
                    eof = true;
                    break; // EOF
                }
            }
        } while (buffer[bufferPosn++] != '>');  // only read one record at a time

        if (!eof) {
            bufferPosn--;  // make sure we leave bufferPosn pointing to the next record
            int appendLength = bufferPosn - startPosn;
            for (int copyi = startPosn; copyi < startPosn+appendLength; copyi++) {
               recordBlock.append((char) buffer[copyi]);
            }
            //recordBlock.append(buffer, startPosn, appendLength);
            totalBytesRead += appendLength;
        }

        /*
        record block now has the byte array we want to process for reads
         */

        int i = 1; // skip initial record seperator ">"
        int j = 1;
        do {
            key.clear();
            str.clear();
            /*
            first parse the key
             */
            i = j;
            Boolean junkOnLine = false;
            while (j < recordBlock.length()) {
                int c = recordBlock.charAt(j++);
                if (c == CR || c == LF) {
                    break;
                } //else if (c == ' ' || c == '\t') {
                  //  junkOnLine = true;
                  //  break;
                //}
            }
            if (j == i) {
               // then we didn't parse out a proper id
               LOG.error("Unable to parse entry: " + recordBlock);
               str.clear();
               key.clear();
               return totalBytesRead;
            }
            key.set(recordBlock.substring(i, j-1));
           
            /*
            in case there is additional metadata on the header line, ignore everything after
            the first word.
             */
            if (junkOnLine) {
                while (j < recordBlock.length() && recordBlock.charAt(j) != CR && recordBlock.charAt(j) != LF ) j++;
            }

            //LOG.info ("key = " + k.toString());

            /*
           now skip the newlines
            */
            while (j < recordBlock.length() && (recordBlock.charAt(j) == CR || recordBlock.charAt(j) == LF)) j++;

            /*
           now read the sequence
            */
           StringBuilder sequenceTmp = new StringBuilder(recordBlock.length());
            do {
                i = j;
                while (j < recordBlock.length()) {
                    int c = recordBlock.charAt(j++);
                    if (c == CR || c == LF) {
                        break;
                    }
                }
                //byte[] ba = recordBlock.getBytes();
                //if (ba.length <= i || ba.length <= j - i - 1) {
                //    LOG.fatal("hmm... ba.length = " + ba.length + " i = " + i + " j-i-1 = " + (j-i-1));
                //}

               if (j == i) {
                  // then we didn't parse out a proper id
                  LOG.error("Unable to parse entry: " + recordBlock);
                  str.clear();
                  key.clear();
                  return totalBytesRead;
               }
               for (int copyi = i; copyi < j-1; copyi++) {
                  sequenceTmp.append((char) recordBlock.charAt(copyi));
               }


                while (j < recordBlock.length() && (recordBlock.charAt(j) == CR || recordBlock.charAt(j) == LF)) j++;

            } while (j < recordBlock.length() && recordBlock.charAt(j) != '>');
            str.set(sequenceTmp.toString());           

            numRecordsRead++;

            /*
           now skip characters (newline or carige return most likely) till record start
            */
            while (j < recordBlock.length() && recordBlock.charAt(j) != '>') {
                j++;
            }

            j++; // skip the ">"

        } while (j < recordBlock.length());

//        LOG.info("");
//        LOG.info("object key = " + key);
        
        return totalBytesRead;
  }

  /**
   * Read from the InputStream into the given Text.
   * @param str the object to store the given line
   * @param maxLineLength the maximum number of bytes to store into str.
   * @return the number of bytes read including the newline
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text key, Text str, int maxLineLength) throws IOException {
    return readLine(key, str, maxLineLength, Integer.MAX_VALUE);
}

  /**
   * Read from the InputStream into the given Text.
   * @param str the object to store the given line
   * @return the number of bytes read including the newline
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text key, Text str) throws IOException {
    return readLine(key, str, Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

}