import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Owner;
import java.util.Random;

import javax.security.auth.Destroyable;

public class HashTester {
	HashManager hm;
	PrintWriter pw;
	
  public HashTester() throws IOException {

		PrintWriter pw;
		pw = new PrintWriter(new BufferedWriter (new FileWriter("C:/test/test.txt",true)));

		this.pw = pw;
		
		int size = 100000000;
		int kmersize = 30;
		HashValues hv = new HashValues(size, kmersize);
		initiateCarterWegman(hv);
		
		pw.close();
		System.out.println("done");
  }

  private void initiateMultshift(HashValues hv){
		long now = 0;
		now = System.nanoTime();
		InitHashFunctionsMultshift();
		SetupHashValuesMultshift(hv);
		
		long then = 0;
		then = System.nanoTime();
		long taken = 0; 
		taken = then - now;
			
		double seconds =(double) taken / 1000000000.0; 
		pw.append("Multiply Shift, kmersize = " + hv.kmerSize + ", hashvalues = " + hv.size + ", time = " + seconds + "\n");
  }
  
  private void InitHashFunctionsMultshift(){
	  this.hm = new HashManager();
	  hm.hash_a = new int[hm.HASH_SIZE];
		hm.hash_b = new int[hm.HASH_SIZE];
		for (int i = 0; i < hm.HASH_SIZE; i++){
			hm.hash_a[i] = 2*i+1;
			hm.hash_b[i] = i;
		}
  }
  
  private void SetupHashValuesMultshift(HashValues hv){
	  long perm_value;
	  int moveit = hv.kmerSize*2<32?0:hv.kmerSize*2-32;
	  for(int i = 0; i < hv.size; i++){
			for(int j = 0; j < hm.HASH_SIZE; j++){
				perm_value = (((hm.hash_a[j] * hv.testHashes[i]  + hm.hash_b[j] ) >> moveit));
			}
		}
  }
  
  private void initiateCarterWegman(HashValues hv){
		long now = System.nanoTime();
		InitHashFunctions();
		SetupHashValuesCarterWegman(hv);
		long then = System.nanoTime();
		  
		long taken = then - now;
			
		double seconds =(double) taken / 1000000000.0; 
		pw.append("Carter Wegman, kmersize = " + hv.kmerSize + ", hashvalues = " + hv.size + ", time = " + seconds + "\n");
  }
  
  private void InitHashFunctions(){
	  	this.hm = new HashManager();
		hm.hash_a = new int[hm.HASH_SIZE];
		hm.hash_b = new int[hm.HASH_SIZE];
		for (int i = 0; i < hm.HASH_SIZE; i++){
			hm.hash_a[i] = i+1;
			hm.hash_b[i] = i;
		}
	}
  
	private void SetupHashValuesCarterWegman(HashValues hv){
		long perm_value;
		for(int i = 0; i < hv.size; i++){
			for(int j = 0; j < hm.HASH_SIZE; j++){
				perm_value = (((hm.hash_a[j] * hv.testHashes[i]  + hm.hash_b[j] ) % hm.prime_div));
			}
		}
	}
	
}
