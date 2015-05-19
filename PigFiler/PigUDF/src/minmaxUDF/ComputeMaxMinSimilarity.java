package minmaxUDF;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class ComputeMaxMinSimilarity extends EvalFunc<Tuple> {
	
	@Override
	public Tuple exec(Tuple input) throws IOException {
		if(input == null || input.size() == 0)
			return null;
		/* LOW MEMORY USAGE
		try{
			DataBag minmaxValues = (DataBag) input.get(0);
			int N_SEQ = (int) ((long) input.get(1));
			int N_HASH = (int) input.get(2);
			double THRESHOLD = (double) input.get(3);
			
			int i = 0;
			int j = 0;
			
			int sim[] = new int[N_SEQ];
			for(i = 0; i < N_SEQ; i++){
				sim[i] = 0;
			}
			int t1minhash,t1maxhash,t2minhash,t2maxhash,intersections, unions, cno = 0;
			double simval;
			i = 0;
			j = 0;
			for(Tuple t : minmaxValues){
				j = 0;
				if(sim[i] != 0){}
				else{
					cno++;
					sim[i] = cno;
					for(Tuple t2 : minmaxValues){
						if(i==j){}
						else{
							if(sim[j] != 0){}
							else{
								intersections = 0;
								unions = 0;
								for (int k = 0; k < N_HASH; k ++){
									Tuple t1 = (Tuple) t.get(k);
									Tuple tt2 = (Tuple) t2.get(k);
									t1minhash = (int) t1.get(0);
									t2minhash = (int) tt2.get(0);
									if(t1minhash == t2minhash){
										intersections++;
									}
									else{
										t1maxhash = (int) t1.get(1);
										t2maxhash = (int) tt2.get(1);
										if(t1maxhash == t2maxhash){
											intersections++;
										}
									}
									unions++;
								}
								simval = ((float) intersections) / ((float) unions);
								
								if(simval >= THRESHOLD){
									sim[j] = cno;
								}
							}
						}
						j++;
					}
				}
				i++;
			}
			
			
			TupleFactory tf = TupleFactory.getInstance();
			Tuple clusters = tf.newTuple(N_SEQ+1);
			
			clusters.set(0, cno);
			for(i = 1; i < N_SEQ + 1; i++){
				clusters.set(i,sim[i-1]);
			}
			return clusters;
		}catch(Exception e){
			throw new IOException("ComputeMinMaxSimilatiry: the following error occured" + e);
		}*/
		
		
		// HIGH MEMORY USAGE
		try{
			DataBag minmaxValues = (DataBag) input.get(0);
			int N_SEQ = (int) ((long) input.get(1));
			int N_HASH = (int) input.get(2);
			double THRESHOLD = (double) input.get(3);
			
			int[][] minhashArray = new int[N_SEQ][N_HASH];
			int[][] maxhashArray = new int[N_SEQ][N_HASH];
			
			int index = 0;
			for(Tuple t : minmaxValues){
				for (int j = 0; j < N_HASH; j++){
					Tuple hashvals = (Tuple) t.get(j);
					minhashArray[index][j] = (int) hashvals.get(0);
					maxhashArray[index][j] = (int) hashvals.get(1);
				}
				index++;
			}

			
			int [] sim = new int[N_SEQ];
			int k;
			int intersections;
			int unions;
			int cno;
			double simval;
			
			cno = 0;
			
			for(int i = 0; i < N_SEQ; i++){
				sim[i] = 0;
			}
			
			for (int i=0; i < N_SEQ; i++){
				if(sim[i]!=0){}
				else{
					cno += 1;
					sim[i] = cno;
						
					for (int j = 0; j < N_SEQ; j++) {
					
					if (sim[j]!=0){}
					else{
						intersections = 0;
						unions = 0;
						for (k = 0; k < N_HASH; k ++){
							if(minhashArray[i][k] == minhashArray[j][k] || maxhashArray[i][k] == maxhashArray[j][k]){
								intersections++;
							}
							unions ++;
						}
						simval = ((float) intersections) / ((float) unions);
						
						if(simval >= THRESHOLD){
							sim[j] = cno;
						}
					}
					}
			  }
			}
			
			TupleFactory tf = TupleFactory.getInstance();
			Tuple clusters = tf.newTuple(N_SEQ+1);
			
			clusters.set(0, cno);
			for(int i = 1; i < N_SEQ + 1; i++){
				clusters.set(i,sim[i-1]);
			}
			return clusters;
		}catch(Exception e){
			throw new IOException("ComputeMinMaxSimilatiry: the following error occured" + e);
		}
	}
}
