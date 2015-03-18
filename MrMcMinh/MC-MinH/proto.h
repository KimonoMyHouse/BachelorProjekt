/************************************************/
/*! \file

\brief This file contains the prototype definitions.

\date Started 05/15/2012.
\author Huzefa Rangwala
\version $Id$

\date 08/1/2012
\author Zeehasham
*/
/*********************************************************************/

/* io.c */

void ReadMetagenomeSequences (CtrlType *ctrl, MetagenomeSample *mgenome);
void ReadMetagenomeSequencesDir (CtrlType *ctrl, MetagenomeSample *mgenome); /* For Multiple Samples */

/* mwUtil.c */

void TranslateToKmerRepresentation (CtrlType *ctrl, MetagenomeSample *mg);
void TranslateToKmerRepresentationDir (CtrlType *ctrl, MetagenomeSample *mg, int sample_idx); /* For Multiple Samples */

void InitHashFunctions (CtrlType *ctrl);
void InitHashFunctionsDir (CtrlType *ctrl, MetagenomeSample *mg);

void SetupHashValues (CtrlType *ctrl, MetagenomeSample *mg);
void SetupHashValuesDir (CtrlType *ctrl, MetagenomeSample *mg); /* For Multiple Samples */

void ComputeMinwiseSimilarity (CtrlType *ctrl, MetagenomeSample *mg);
void ComputeMinwiseSimilarityDir (CtrlType *ctrl, MetagenomeSample *mg); /* For Multiple Samples */

void ComputeCluster (CtrlType *ctrl, MetagenomeSample *mg);
void ComputeKmerSimilarity (CtrlType *ctrl, MetagenomeSample *mg);

int comp(const void * a,const void * b);

