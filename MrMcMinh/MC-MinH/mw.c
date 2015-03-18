/********************************************/
/*! \file 

\brief This file contains main routines.

\author Huzefa
\version $Id$
\date Started 05/12/2012

\date 08/1/2012
\author Zeehasham
*/
/********************************************/

#include <mw.h>
#include <stdio.h>


/**********************************************/
/* This is the entry point for the minwise hashing program.
********************************************************/

int main (int argc, char *argv []) {
    
    int c;
    CtrlType ctrl;
    MetagenomeSample mg;
    clock_t start;
    float total_time=0.0;
    FILE *file;
    // Parse the command line. 
    
    strcpy (ctrl.input_sequence_file, argv[1]);
    strcpy (ctrl.output_sequence_file, argv[2]);
    ctrl.kmer_size          = atoi(argv[3]);
    ctrl.num_hash_functions = atoi(argv[4]);
    ctrl.threshold          = atof(argv[5]);
    ctrl.div                = atoi(argv[6]);
    strcpy(ctrl.output_screen, strcat(argv[2],".out"));
    strcpy (ctrl.cat_filename, "");
    
    
    file = fopen(ctrl.output_screen,"a+");    
    fprintf (file,"%s %s %d %d %f %d\n\n", argv[1],argv[2],ctrl.kmer_size,ctrl.num_hash_functions,ctrl.threshold,ctrl.div);

    // Initialize Hash Function;
    fprintf(file,"Initialize Hash Functions\n");
    printf("Initialize Hash Functions\n");
    start = clock();                                  
    InitHashFunctions (&ctrl);
    fprintf(file,"Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    printf("Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    total_time = total_time + ((double)clock() - start) / CLOCKS_PER_SEC;
    
    // Read the Sequence file and kmer calculation;
    fprintf (file,"Reading Mgenomes .. \n");
    printf ("Reading Mgenomes .. \n");
    start = clock();  
    ReadMetagenomeSequences (&ctrl, &mg);
    fprintf(file,"Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    printf("Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    total_time = total_time + ((double)clock() - start) / CLOCKS_PER_SEC;
    
    // Calculating Min Hash values
    fprintf(file,"Calculate Hash Functions\n");
    printf("Calculate Hash Functions\n");
    start = clock(); 
    SetupHashValues (&ctrl, &mg);
    fprintf(file,"Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    printf("Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    total_time = total_time + ((double)clock() - start) / CLOCKS_PER_SEC;
    
    // Computing Clusters
    fprintf(file,"Compute Cluster Labels\n");
    fclose(file);
    printf("Compute Cluster Labels\n");
    start = clock();
    ComputeCluster (&ctrl, &mg);
    file = fopen(ctrl.output_screen,"a+"); 
    fprintf(file,"Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    printf("Time elapsed: %f\n\n", ((double)clock() - start) / CLOCKS_PER_SEC);
    total_time = total_time + ((double)clock() - start) / CLOCKS_PER_SEC;
    
    fprintf(file,"Total Time: %f\n\n", total_time);
    printf("Total Time: %f\n\n", total_time);

    fprintf(file,"End\n");
    printf("End\n");
    
    fclose(file);

}
