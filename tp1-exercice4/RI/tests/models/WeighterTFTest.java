package models;




import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import core.Document;
import indexation.Index;

import org.mockito.*;
//import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeighterTFTest {

	@Mock
	Index index;
	
	WeighterTF wt;
	
	@BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
		wt=new WeighterTF(index); 
    }
	
	@Test
	void  getDocWeightsForStemTest() {
		HashMap<String,Integer> ret=new HashMap<String,Integer>();
		ret.put("i1",5);
		ret.put("i2",6);
		
		when(index.getTfsForStem("cat")).thenReturn(ret);
		
		
		HashMap<String,Double> w=wt.getDocWeightsForStem("cat");
		for(String doc:w.keySet()){
			assertEquals(w.get(doc),ret.get(doc)*1.0);
		}
		verify(index,times(2)).getTfsForStem(any(String.class));
		
	}

}
