/**************************************************************/
/*! \file

\brief This file contains the data type definitions.

\date Started 05/15/2012
\author Huzefa
\version $Id$

\date 08/1/2012
\author Zeehasham

*/

/**************************************************/

/**********************************************/
/* This data holds the control information. */
/***********************************************/

typedef struct{

    int prg_type; /*Type of Program I am running */

    int dbg_lvl;

    char input_sequence_file [200];
    
    char output_sequence_file [200];
    
    char output_screen [200];

    int kmer_size;

    int num_hash_functions;
    
    float threshold;

    long *hash_A; // Ax + B % P;
    long *hash_B;
    long div;
    long div2;
    
    char cat_filename [200]; /* For making absoulte path of filename */


} CtrlType;

typedef struct{

    int    nseqs; /* Number of sequences */
    int    nsamples; /* Number of Samples/Files */
    char  ** sequences;
    long   ** comp_seq; /* Converted Sequence */
   
    int maxseqlen;
    int *seqlen;
    
    long **kmer_indices; /*Which k-mers are present */
    long **kmer_frequencies; /*We use it to store total number of kmers in one sample*/
    int num_unique_kmers; /*Number of unique kmer occurences */

    int nsymbols;
    int i2c [256];
    int c2i [256];
  
    
    long **hash_values; //per sequence;
    

} MetagenomeSample;
