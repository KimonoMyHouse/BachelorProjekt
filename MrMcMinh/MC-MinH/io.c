/*****************************************************************************/
/*! \file 

\bried This file contains all the I/O routines for mw.

\date Started 05/12/2012
\author Huzefa
\version $Id$

\date 08/1/2012
\author Zeehasham
*/
/***********************************************************/

#include <mw.h>
#include <stdio.h>

void ReadMetagenomeSequences (CtrlType *ctrl, MetagenomeSample *mgenome)
{
    int i, j;
    char line[MAXLINELEN];
    int nlines = 0;
    int nsequences;
    int maxseqlen = 0;
    FILE *fpin;


    /* Initialize the mapping */

    mgenome->nsymbols = strlen(NUCORDER);

    for (i=0;i<256;i++){
      mgenome->i2c[i] = mgenome->c2i[i] = 0;
      }

    for (i=0;i<mgenome->nsymbols;i++){
        mgenome->i2c[i] = NUCORDER[i];
        mgenome->c2i[(int) NUCORDER[i]] = i;
    }


    fpin = fopen (ctrl->input_sequence_file, "r");
    if (fpin == NULL) {
    printf("Error");
    exit(1);
}
    while (!feof (fpin)){
      fgets(line, MAXLINELEN-1, fpin);
      maxseqlen = (strlen (line) > maxseqlen)? strlen (line): maxseqlen;
      nlines ++;
    }
    fclose (fpin);
    
    printf ("Number of sequences %d \n", nlines);
    // Number of sequences are known;
    nsequences = nlines;
    mgenome->nseqs = nlines;
    
    mgenome->sequences = (char **) malloc (sizeof (char *) * nsequences);
    mgenome->comp_seq  = (long **) malloc (sizeof (long*)* nsequences);
    mgenome->maxseqlen    = maxseqlen;
    mgenome->seqlen    = (int *) malloc (sizeof (int) * nsequences);


    /*Open Sequence File Again */
    fpin = fopen (ctrl->input_sequence_file, "r"); /*  Open the input file */
    for (i=0; i< nsequences; i++){
      fgets(line, MAXLINELEN-1, fpin);
      mgenome->sequences[i] = (char *) malloc (sizeof (char) * strlen (line));    
      mgenome->comp_seq[i]  = (long *) malloc (sizeof (long) * strlen (line));
      mgenome->seqlen[i]    = strlen (line);
      
    }
    fclose (fpin);

    /*Open Sequence File Again */
    fpin = fopen (ctrl->input_sequence_file, "r"); /*  Open the input file */
    
    for (i=0; i< nsequences; i++)
    {
        //mgenome->sequences[i] = (char *) malloc (sizeof (char) * maxseqlen);    
        //mgenome->comp_seq[i]  = (unsigned int *) malloc (sizeof (unsigned int) * maxseqlen);
        fgets (line, MAXLINELEN-1, fpin);
        strcpy (mgenome->sequences[i], line);
        for (j=0;j<strlen(mgenome->sequences[i])-1;j++){
          mgenome->comp_seq[i][j] = mgenome->c2i[(int) mgenome->sequences[i][j]];
          }
    }
   
    fclose (fpin);

    TranslateToKmerRepresentation (ctrl, mgenome);

    /** Check **/
/*

    for (i =0; i <nsequences; i++){
     printf ("%d :: %s\n", i, mgenome->sequences[i]);
     for (j=0;j<strlen (mgenome->sequences[i])-1; j++)
       printf ("%d ",mgenome->comp_seq[i][j]);
     for (j=0;j<strlen (mgenome->sequences[i])-ctrl->kmer_size; j++)
       printf ("%d ",mgenome->kmer_indices[i][j]);
  
   }
*/
}


/* Reading Directory for Program Type 2 */

void ReadMetagenomeSequencesDir (CtrlType *ctrl, MetagenomeSample *mgenome)
{
    int i, j;
    char line[MAXLINELEN];
    int nlines = 0;
    int nsamples = 0; /* stores number of samples in a directory */
    int nsequences;
    int maxseqlen = 0;
    FILE *fpin;
    DIR *dir; 
    struct dirent *ent; 
    int sample_idx=-1; 


    /* Initialize the mapping */

    mgenome->nsymbols = strlen(NUCORDER);

    for (i=0;i<256;i++){
      mgenome->i2c[i] = mgenome->c2i[i] = -1;
    }

    for (i=0;i<mgenome->nsymbols;i++){
        mgenome->i2c[i] = NUCORDER[i];
        mgenome->c2i[(int) NUCORDER[i]] = i;
    }


    dir = opendir (ctrl->input_sequence_file); 
    if (dir != NULL) { 
      /* Count Files in directories */ 
      while ((ent = readdir (dir)) != NULL) {
            nsamples +=1;
      }
    }
    
    nsamples = nsamples-2;
    printf("Number of Samples: %d\n\n",nsamples);
    mgenome->nsamples = nsamples;          
    
    mgenome->kmer_indices = (long **) malloc (sizeof(long*)*nsamples); /* Allocating outside the Kmer Translation Function */
    mgenome->kmer_frequencies = (long **) malloc (sizeof(long*)*nsamples); /*Stores total number of Kmers in a Sample */
    
    dir = opendir (ctrl->input_sequence_file); 
    if (dir != NULL) { 

      /* Read all the files and directories within directory */ 
      while ((ent = readdir (dir)) != NULL) {
            
        /*creating an absolute path and avoiding . and .. as a file name */    
        strcpy(ctrl->cat_filename,ctrl->input_sequence_file);
        strcat(ctrl->cat_filename, ent->d_name);
        //printf ("%s\n", ctrl->cat_filename);
        
        fpin = fopen (ctrl->cat_filename, "r");
        
        if (fpin == NULL) {
           //printf("Not a File\n");
        }
        else {
            sample_idx+=1;
            printf ("Sample Number: %d\n", sample_idx+1);
            
            while (!feof (fpin)){
            fgets(line, MAXLINELEN-1, fpin);
            maxseqlen = (strlen (line) > maxseqlen)? strlen (line): maxseqlen;
            nlines ++;
            }
            fclose (fpin);
            nlines--; /* There was an extra empty line in skin sample */
            printf ("Number of sequences: %d \n", nlines);
            
            // Number of sequences are known;
            nsequences = nlines;
            mgenome->nseqs = nlines;
        
            /*Open Sequence File Again */
            fpin = fopen (ctrl->cat_filename, "r"); /*  Open the input file */
            
            mgenome->sequences = (char **) malloc (sizeof (char *) * nsequences);
            mgenome->comp_seq  = (long **) malloc (sizeof (long*)* nsequences);
            //mgenome->seqlen    = maxseqlen;
            for (i=0; i< nsequences; i++)
            {
                mgenome->sequences[i] = (char *) malloc (sizeof (char) * maxseqlen);    
        
                fgets (line, MAXLINELEN-1, fpin);
                strcpy (mgenome->sequences[i], line);
                for (j=0;j<strlen(mgenome->sequences[i]) -1;j++)
                  mgenome->comp_seq[i][j] = mgenome->c2i[mgenome->sequences[i][j]];
                 
            }
           
            fclose (fpin);
        
            TranslateToKmerRepresentationDir (ctrl, mgenome, sample_idx);

            free(mgenome);
            nlines=0;
            nsequences=0;
            maxseqlen=0;
                     
            /** Check **/
        /*
        
            for (i =0; i <nsequences; i++){
             printf ("%d :: %s\n", i, mgenome->sequences[i]);
             for (j=0;j<strlen (mgenome->sequences[i])-1; j++)
               printf ("%d ",mgenome->comp_seq[i][j]);
             for (j=0;j<strlen (mgenome->sequences[i])-ctrl->kmer_size; j++)
               printf ("%d ",mgenome->kmer_indices[i][j]);
          
           }
        */  
           printf ("\n");

                  
        }
      }
      closedir (dir); 
    } 
    else { 
      /* could not open directory */ 
      perror ("Can't Read Directory"); 
      exit(1);
    } 

}
