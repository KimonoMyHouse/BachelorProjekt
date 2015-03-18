#*************************************************************************
# Compilation flags
#*************************************************************************
dmalloc = no
debug = yes

VERNUM = 1.0







#--------------------------------------------------------------------------
# Which compiler to use
CC = gcc

# What optimization level to use
OPTFLAGS = -O6 

# What options to be used by the compiler
COPTIONS = 

# What options to be used by the loader
LDOPTIONS = 

# What archiving to use
AR = ar rv

# What to use for indexing the archive
#RANLIB = ranlib
RANLIB = ar -ts


#------------------------------------------------------------------
BINDIR = ./bin

DMALLOCINC = 
DMALLOCLIB = 
DMALLOCLIBDIR = 
DMALLOCFLAGS =
DEBUGFLAGS =

INCLUDES = -I./ 
CFLAGS = $(COPTIONS) $(OPTFLAGS) $(INCLUDES)

LIBSDIR = -L..  $(LIBDIR) $(DMALLOCLIBDIR)
LIBS = -lm $(DMALLOCLIB)

LD = $(CC) $(LDOPTIONS) $(LIBSDIR) $(LIBS)
#------------------------------------------------------------------


#ALLPAIROBJ = allPairAlnScores.o io.o cmdline.o util.o sim.o matrices.o pairwiseScores.o
MWOBJ      = mwUtil.o mw.o io.o 

.c.o:
	$(CC) $(CFLAGS) -c $*.c

default: $(BINDIR)/mw$(VERNUM)

$(BINDIR)/mw$(VERNUM): $(MWOBJ) 
	$(LD)  -o $@ $(MWOBJ) $(LIBS)
	chmod 744 $@



checkin:
	@for file in *.[c,h]; \
	do \
	ci -u -m'Maintance' $$file;\
	done 

checkin2:
	@for file in *.[c,h]; \
	do \
	ci $$file;\
	rcs -U $$file;\
	co $$file;\
	done

clean:
	rm -f *.o

realclean:
	rm -f *.o ;\
	rm -f $(BINDIR)/aasim$(VERNUM) ;
	rm -f $(BINDIR)/pairsim$(VERNUM) ;
