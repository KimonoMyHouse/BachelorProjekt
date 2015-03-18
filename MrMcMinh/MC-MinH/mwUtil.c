/********************************************/
/*! \file 

\brief This file contains the utility routines.

\author Huzefa
\version $Id$
\date Started 05/12/2012

\date 08/1/2012
\author Zeehasham
*/
/********************************************/

#include <mw.h>
//#include <conio.h>

void InitHashFunctions (CtrlType *ctrl)
{
    int i;
    int value=1; 
    ctrl->hash_A = (long *) malloc (sizeof (long) * ctrl->num_hash_functions);
    ctrl->hash_B = (long *) malloc (sizeof (long) * ctrl->num_hash_functions);
    
    srand((unsigned)time(NULL));

    for (i=0;i<ctrl->num_hash_functions;i++)
    {
        ctrl->hash_A [i] = i+1; 
        ctrl->hash_B [i] = i;
        
        //ctrl->hash_A [i] = rand() % 100; 
        //ctrl->hash_B [i] = rand() % 100;
        
        //ctrl->hash_A [i] = i+1; 
        //ctrl->hash_B [i] = i+3;
    }

}


void TranslateToKmerRepresentation (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i,j;
    int kmer_sum;
    int kmer_prod;
    int k,l;
    long *temp;
    
    mg->kmer_indices = (long **) malloc (sizeof(long*)*mg->nseqs);
    printf ("kmer = %d\n", ctrl->kmer_size);

    for (i=0;i<mg->nseqs;i++)
    {
        //temp = (long*) malloc (sizeof (long*) * mg->seqlen[i]-ctrl->kmer_size); //New commented
        mg->kmer_indices [i] = (long *) malloc (sizeof (long) * mg->seqlen[i]-ctrl->kmer_size); //New Added Assuming Equal. Size changed
        
        for (j=0;j<mg->seqlen[i]-ctrl->kmer_size;j++){
            
            for (kmer_prod=1,kmer_sum=0, k=0; k<ctrl->kmer_size; k++){
              
              kmer_sum += kmer_prod * mg->comp_seq [i][j+k];
              kmer_prod = kmer_prod * NUM_ACGT;
              
            }
            
        mg->kmer_indices[i][j] = kmer_sum; //New change comment removed 
        //temp[j] = kmer_sum; //New change comment
                  
        
        
        }
        
        //qsort(temp,mg->seqlen[i]-ctrl->kmer_size, sizeof(long*), comp);
        
        //mg->kmer_indices [i] = (long *) malloc (sizeof (long) * mg->seqlen[i]-ctrl->kmer_size); //New commented Assuming Equal. Size changed
        
        /*for (k=0;k<mg->seqlen[i]-ctrl->kmer_size;k++){
            mg->kmer_indices[i][k] = temp[k];
            //printf("copying: %d\n",mg->kmer_indices[i][k]);
        }
        *///New Commented
        //free(temp); //New Commented
      }
/*    printf("\n***************\n");
    for (l=0;l<mg->seqlen[9]-ctrl->kmer_size;l++){
        printf("Last\n");
        printf ("kmer index = %d\n", mg->kmer_indices[9][l]);
    }
*/


}

int comp(const void * a, const void * b) 
{
  return (*(long*)a - *(long*)b);       
 }
 

void SetupHashValues (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i,j, k;
    
    int perm_value;

    mg->hash_values = (long **) malloc (sizeof (long *) * mg->nseqs);
    for (i=0;i<mg->nseqs;i++){
      mg->hash_values [i] = (long *) malloc (sizeof (long) *ctrl->num_hash_functions);//for each sequence
      for (j=0;j<ctrl->num_hash_functions; j++){
        mg->hash_values [i][j] = MAXINT;
        // Now go over all the kmers;
        for (k=0; k < mg->seqlen[i] - ctrl->kmer_size; k++){
            // Compute hash value; Ax + B % P;

            perm_value = (((ctrl->hash_A[j] * mg->kmer_indices[i][k] + ctrl->hash_B[j]) % ctrl->div));
            mg->hash_values[i][j] = (perm_value < mg->hash_values [i][j])? perm_value : mg->hash_values[i][j];
            //printf("hash_value: %d\n",mg->hash_values[i][j]);

          }
      }
    }

    // Check
/*
    for (i=0; i <mg->nseqs; i++){
        printf ("\n");
        for (j=0; j < ctrl->num_hash_functions; j++)
          printf ("%d,", mg->hash_values[i][j]);

    }
*/
}

void ComputeMinwiseSimilarity (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i, j;
    //float *sim;
    float simval;
    int k;
    int intersections;
    int unions;
    FILE *file;
    int counter=-1;

    // Can be improved.
     
    file = fopen(ctrl->output_sequence_file,"a+");
    fprintf (file,"%d\n",mg->nseqs);
    //sim = (float *) malloc (sizeof (float) * mg->nseqs * mg->nseqs); 
    for (i=0; i < mg->nseqs; i++){
      for (j=0; j < mg->nseqs; j++) {
        for (intersections =0, unions=0, k=0; k < ctrl->num_hash_functions; k ++){
            intersections += ((mg->hash_values[i][k] == mg->hash_values [j][k]) ? 1:0);
            unions ++;
          }
        //sim [i*mg->nseqs + j] = 1.0*intersections/unions;
        simval = 1.0*intersections/unions;
        fprintf (file,"%.2f ",simval);
      }
      fprintf(file,"\n");
      }
      fclose(file);  
      
/*  
    file = fopen("S1_Minhash_Result_Hie.txt","a+");
    fprintf (file,"%d\n",mg->nseqs);
    for (i=0;i<mg->nseqs; i++){
        for (j=0; j <mg->nseqs; j++){
          fprintf(file,"%.2f ",sim[i*mg->nseqs + j]);
          }
    fprintf(file,"\n");
    }
    printf ("************************\n");
    free (sim);
    fclose(file);
*/  

//n(n-1)/2 implementation
/*    file = fopen("S1_Minhash_Result_Hie.txt","a+");
    sim = (float *) malloc (sizeof (float) * (mg->nseqs * (mg->nseqs-1))/2); 
    for (i=0; i < mg->nseqs; i++)
      for (j=i+1; j < mg->nseqs; j++) {
          counter+=1;
        for (intersections =0, unions=0, k=0; k < ctrl->num_hash_functions; k ++){
            intersections += ((mg->hash_values[i][k] == mg->hash_values [j][k]) ? 1:0);            
            unions ++;
          }
        sim [counter] = 1.0*intersections/unions;
        //fprintf(file,"%f\n",sim[counter]);
        //printf ("%lf\n",sim [counter]);
        //printf ("%d\n",counter);
      }
    printf("counter: %d\n",counter); 
     
    file = fopen("S1_Minhash_Result_Hie.txt","a+");
    printf ("************************\n");
    for (i=0;i<counter; i++){
          fprintf(file,"%f ",sim[i]);
    }
   printf ("************************\n");
   

    free (sim);
    fclose(file);
*/
}

void ComputeKmerSimilarity (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i, j;
    //float *sim;
    float simval;
    int k,l;
    int intersections;
    int unions;
    FILE *file;
    int counter=-1;

    // Can be improved.
    file = fopen(ctrl->output_sequence_file,"a+");
    fprintf (file,"%d\n",mg->nseqs);
    //sim = (float *) malloc (sizeof (float) * mg->nseqs * mg->nseqs); 
    for (i=0; i < mg->nseqs; i++){
      for (j=0; j < mg->nseqs; j++){
          intersections = 0;
          unions = 0;
          k=0;
          l=0;
          while(k<mg->seqlen[i]-ctrl->kmer_size && l<mg->seqlen[j]-ctrl->kmer_size)
          {    
               if(mg->kmer_indices[k] < mg->kmer_indices[l])
                    k++;
               else if(mg->kmer_indices[l] < mg->kmer_indices[k])
                    l++;    
               else /* if arr1[i] == arr2[j] */
               {
                    intersections+=1;
                     k++;
                     l++;
                     }
          }
          unions = mg->seqlen[i]-ctrl->kmer_size + mg->seqlen[j]-ctrl->kmer_size - intersections;
          simval = 1.0*intersections/unions;
          fprintf (file,"%.2f ",simval);
      }               
      fprintf(file,"\n");
      }
      fclose(file);
}

//Compute clusters using Min Hash values
void ComputeCluster (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i, j;
    int *sim;
    int k;
    int intersections;
    int unions;
    int cno;
    double simval;
    int numcluster=0;
    FILE *file;
    FILE *file2; 


    // Can be improved.
    cno = 0;
    sim = (int *) malloc (sizeof (int) * mg->nseqs);
    for (i=0; i < mg->nseqs; i++){
        if(sim[i]!=0){}
        else{
    cno += 1;
    sim[i] = cno;
    
      for (j=0; j < mg->nseqs; j++) {
          if (sim[j]!=0){}
          else{
        for (intersections =0, unions=0, k=0; k < ctrl->num_hash_functions; k ++){
            intersections += ((mg->hash_values[i][k] == mg->hash_values [j][k]) ? 1:0);            
            unions ++;
          }
        simval = 1.0*intersections/unions;
        //printf ("%lf ", simval);
        if(simval >= ctrl->threshold){
                  sim[j] = cno;
                  }
                  }
      }
      }
    }
    
    printf ("************************\n");
/*    for (i=0;i<mg->nseqs; i++){
          printf ("%d ", sim[i]);
        printf ("\n");
    }
*/

        //Calculating Cluster Lables and saving into a file
    
    file = fopen(ctrl->output_screen,"a+");
    file2 = fopen(ctrl->output_sequence_file,"a+"); 


    //finding the number of clusters
    for (i=0;i<mg->nseqs; i++){
    numcluster = (sim[i] > numcluster)? sim[i]: numcluster; 
    fprintf(file2,"%d\n",sim[i]); /*writes*/ 
    }
    fprintf (file,"Number of Clusters: %d\n", numcluster);
    printf ("Number of Clusters: %d\n", numcluster);
    printf ("************************\n");
    free (sim);
    fclose(file);
    fclose(file2);
}


/**** For Multiple Samples in a directory ****/

void InitHashFunctionsDir (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i,k;
    long maxval;
    int value=1; 
    ctrl->hash_A = (long *) malloc (sizeof (long) * ctrl->num_hash_functions);
    ctrl->hash_B = (long *) malloc (sizeof (long) * ctrl->num_hash_functions);
    //ctrl->div    = (int) pow(NUM_ACGT, ctrl->kmer_size) -1;
    //ctrl->div = 4545551;
    maxval = -1;
    for (k=0;k<mg->nsamples;k++){
        if (mg->kmer_frequencies [k][0] > maxval)
           maxval = mg->kmer_frequencies [k][0];
    }
    ctrl->div = maxval;
    
    //srand((unsigned)time(NULL));


    for (i=0;i<ctrl->num_hash_functions;i++)
    {
        //ctrl->hash_A [i] = 1 + rand() % 100; 
        //ctrl->hash_B [i] = 1 + rand() % 100;
        ctrl->hash_A [i] = i+1; 
        ctrl->hash_B [i] = i+2;
    }

}

void TranslateToKmerRepresentationDir (CtrlType *ctrl, MetagenomeSample *mg, int sample_idx)
{
    int i,j;
    int kmer_sum;
    int kmer_prod;
    int k;
    int l=0;
    int m;

    //mg->kmer_indices = (int **) malloc (sizeof(int*)*mg->nsamples);
    printf ("kmer = %d\n", ctrl->kmer_size);
    m = sample_idx;
    
    //Allocating Memory for total number of Kmers in one Sample (contains all the sequences in one Sample)
    mg->kmer_indices [m] = (long *) malloc (sizeof (long) * (mg->maxseqlen-ctrl->kmer_size)*mg->nseqs);
    
    //Stores total number of Kmers
    mg->kmer_frequencies [m] = (long *) malloc (sizeof (long)* 1);
    
    for (i=0;i<mg->nseqs;i++)
    {
        for (j=0;j<mg->maxseqlen-ctrl->kmer_size;j++){
            
            for (kmer_prod=1,kmer_sum=0, k=0; k<ctrl->kmer_size; k++){  
              kmer_sum += kmer_prod * mg->comp_seq [i][j+k];
              kmer_prod = kmer_prod * NUM_ACGT;
           }
           
           mg->kmer_indices[m][l] = kmer_sum;
           l+=1;
           //printf ("kmer index = %d\n", mg->kmer_indices[i][j]);
        }

    }
    mg->kmer_frequencies [m][0] = l;
    printf("Total Number of Kmers: %d\n\n",mg->kmer_frequencies [m][0]);
}


void SetupHashValuesDir (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i,j, k;
    int len;
    
    int perm_value;

    mg->hash_values = (long **) malloc (sizeof (long *) * mg->nsamples);
    for (i=0;i<mg->nsamples;i++){
      mg->hash_values [i] = (long *) malloc (sizeof (long) *ctrl->num_hash_functions);
      for (j=0;j<ctrl->num_hash_functions; j++){
        mg->hash_values [i][j] = MAXINT;

        // Now go over all the kmers;
        for (k=0;k<mg->kmer_frequencies[i][0];k++){
        //for (k=0; k < mg->seqlen - ctrl->kmer_size; k++){
            // Compute hash value; Ax + B % P;

            perm_value = (ctrl->hash_A[j] * mg->kmer_indices[i][k] + ctrl->hash_B[j]) % ctrl->div;
            mg->hash_values[i][j] = (perm_value < mg->hash_values [i][j])? perm_value : mg->hash_values[i][j];
          }

      }
    }

    // Check
/*
    for (i=0; i <mg->nseqs; i++){
        printf ("\n");
        for (j=0; j < ctrl->num_hash_functions; j++)
          printf ("%d,", mg->hash_values[i][j]);

    }
*/
}

void ComputeMinwiseSimilarityDir (CtrlType *ctrl, MetagenomeSample *mg)
{
    int i, j;
    double *sim;
    int k;
    int intersections;
    int unions;

    // Can be improved.

    sim = (double *) malloc (sizeof (double) * mg->nsamples * mg->nsamples); 
    for (i=0; i < mg->nsamples; i++)
      for (j=0; j < mg->nsamples; j++) {
        for (intersections =0, unions=0, k=0; k < ctrl->num_hash_functions; k ++){
            intersections += ((mg->hash_values[i][k] == mg->hash_values [j][k]) ? 1:0);            
            unions ++;
          }
        sim [i*mg->nsamples + j] = 1.0*intersections/unions;
      }

    printf ("************************\n");
    for (i=0;i<mg->nsamples; i++){

        for (j=0; j <mg->nsamples; j++)
          printf ("%lf ", sim[i*mg->nsamples + j]);
        printf ("\n");
    }
    printf ("************************\n");
    free (sim);
   


}
