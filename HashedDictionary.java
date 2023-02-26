package Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HashedDictionary <K,V>implements DictionaryInterface <K,V> {

	private int numberOfEntries;
	private static final int DEFAULT_CAPACITY = 3;
	private static final int MAX_CAPACITY = 10000;	
	private TableEntry<K,V>[] hashTable;
	private int tableSize;
	private static final int MAX_SIZE = 2 * MAX_CAPACITY;
	private boolean initialized = false;
	private static final double MAX_LOAD_FACTOR = 0.5;
	public int collisionCount_SSF_Probe = 0;
	public int collisionCount_SSF_DH = 0;
	public int collisionCount_PAF_Probe = 0;
	public int collisionCount_PAF_DH = 0;
	
	
	public HashedDictionary() {
		this(DEFAULT_CAPACITY);
	}
	public HashedDictionary(int initialCapacity) {
		//checkCapacity(initialCapacity);
		numberOfEntries = 0;
		int tableSize = getNextPrime(initialCapacity);
		//checkSize(tableSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[] temp = (TableEntry<K,V>[])new TableEntry[tableSize];
		hashTable = temp;
		initialized = true;
	}
	public V addSSF_PROBE(K key,V value) { 
		//checkInitialization();
		
		if((key == null)|| (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue;
			int index = hashFunction1_SSF(key);
			index = probe(index,key);			
			assert (index >= 0) && (index < hashTable.length);
			
			if((hashTable[index] == null) || hashTable[index].isRemoved()) {
				hashTable[index] = new TableEntry<>(key,value);
				numberOfEntries++;
				oldValue = null;
			}
			else {
				oldValue = hashTable[index].getValue();
				hashTable[index].setValue(value);
				collisionCount_SSF_Probe++;
			}
			if(isHashTableTooFull())
				resizeSSF();
			
			return oldValue;
		}
			
	}
	
	
	public V addPAF_PROBE(K key,V value) { 
		//checkInitialization();
		
		if((key == null)|| (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue;
			int index = hashFunction1_PAF(key);
			index = probe(index,key);			
			assert (index >= 0) && (index < hashTable.length);
			
			if((hashTable[index] == null) || hashTable[index].isRemoved()) {
				hashTable[index] = new TableEntry<>(key,value);				
				numberOfEntries++;
				oldValue = null;
			}
			else {
				oldValue = hashTable[index].getValue();
				hashTable[index].setValue(value);
				collisionCount_PAF_Probe++;
			}
			if(isHashTableTooFull())
				resizePAF();
			
			return oldValue;
		}
		
			
	}
	
	private int SSF(K key) {  
		
		 String word = key.toString().toLowerCase(); 
		 int sum = 0;
		 for( int i = 0; i < word.length(); ++i) {
		    int position = word.charAt(i) - 'a' + 1 ;
		    sum += position;
		 }
		 return sum ;
		
	}
	private int PAF(K key) { 
		String word = key.toString().toLowerCase(); 
		int hash = 0;				
		int g = 31;		
		
		for (int k = 0; k < word.length(); k++) {
			  int position = word.charAt(k) - 'a' + 1 ;
			  hash += Math.pow(g, key.toString().length() - k - 1) * position;
		}
		return hash;
		
	}

	private int hashFunction1_SSF(K key) {
		int hashValue = SSF(key);
		hashValue %= hashTable.length;
		 if (hashValue < 0)
	            hashValue += hashTable.length;
		 return hashValue;
	}
	private int hashFunction1_PAF(K key) {
		int hashValue = PAF(key);
		hashValue %= hashTable.length;
		 if (hashValue < 0)
	            hashValue += hashTable.length;
		 return hashValue;
	}


	
	private int hashFunctionDouble_SSF(K key) {
		int k = hashFunction1_SSF(key);		
		int q = 31;
		int dk = 31 - k % 31;
		
		return dk;		
	}
	private int hashFunctionDouble_PAF(K key) {
		int k = hashFunction1_PAF(key);		
		int q = 31;
		int dk = 31 - k % 31;
		
		 
		return dk;			
	}

	private int doubleHash(int index,K key) {
		boolean found = false;
		int removedStateIndex = -1;
		int i = 1;
		while(!found && (hashTable[index] != null)) {
			if(hashTable[index].isIn()) {
				if(key.equals(hashTable[index].getKey()))
					found = true;
				else
					index = (hashFunction1_SSF(key)+ hashFunctionDouble_SSF(key) *i) % hashTable.length;
			}
			else {
				if(removedStateIndex == -1)
					removedStateIndex = index;
			}
			i++;
		}
		if(found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}
	private int doubleHash2(int index,K key) {
		boolean found = false;
		int removedStateIndex = -1;
		int i = 1;
		while(!found && (hashTable[index] != null)) {
			if(hashTable[index].isIn()) {
				if(key.equals(hashTable[index].getKey()))
					found = true;
				else
					index = (hashFunction1_PAF(key)+ hashFunctionDouble_PAF(key) *i) % hashTable.length;
			}
			else {
				if(removedStateIndex == -1)
					removedStateIndex = index;
			}
			i++;
		}
		if(found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}
	
	public V addSSF_Double(K key,V value) { 
		//checkInitialization();
		
		if((key == null)|| (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue;
			int index = hashFunction1_SSF(key);
			index = doubleHash(index,key);		
			assert (index >= 0) && (index < hashTable.length);
			
			if((hashTable[index] == null) || hashTable[index].isRemoved()) {
				hashTable[index] = new TableEntry<>(key,value);				
				numberOfEntries++;
				oldValue = null;
			}
			else {
				oldValue = hashTable[index].getValue();
				hashTable[index].setValue(value);
				collisionCount_SSF_DH++;
			}
			if(isHashTableTooFull())
				resizeSSF_DH();
			return oldValue;
			
		}
		
		
			
	}
	public V addPAF_Double(K key,V value) { 
		//checkInitialization();
		
		if((key == null)|| (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue;
			int index = hashFunction1_PAF(key);
			index = doubleHash2(index,key);		
			assert (index >= 0) && (index < hashTable.length);
			
			if((hashTable[index] == null) || hashTable[index].isRemoved()) {
				hashTable[index] = new TableEntry<>(key,value);				
				numberOfEntries++;
				oldValue = null;
			}
			else {
				oldValue = hashTable[index].getValue();
				hashTable[index].setValue(value);
				collisionCount_PAF_DH++;
			}
			if(isHashTableTooFull())
				resizePAF_DH();
			return oldValue;
		}
		
			
	}
	
	public void get(K key) {
	    boolean control = false;
		System.out.println(">Search: " + key);		
		for (int i = 0; i < hashTable.length; i++) {
			if(hashTable[i] != null) {
				if(hashTable[i].getKey().equals(key)) {		
					System.out.println("Founding txts are: ");
					System.out.println(hashTable[i].getValue());
					control = true;
				}
			}			
		}
		if(!control) {
			System.out.println("Not found!");
		}
	

		

	}
	
	
	
	private boolean isHashTableTooFull() {
		float loadFactor = loadFactor();
		if(loadFactor >= MAX_LOAD_FACTOR) {
			return true;
		}
		return false;		
	}
	
	private float loadFactor() {
		float load = numberOfEntries / hashTable.length;
		return load;
	}
	
	private int probe(int index,K key) {
		boolean found = false;
		int removedStateIndex = -1;
		
		while(!found && (hashTable[index] != null)) {
			if(hashTable[index].isIn()) {
				if(key.equals(hashTable[index].getKey()))
					found = true;
				else
					index = (index+1) % hashTable.length;
			}
			else {
				if(removedStateIndex == -1)
					removedStateIndex = index;
			}
		}
		if(found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}

	
	private void resizePAF() {
		
		TableEntry<K,V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[]temp = (TableEntry<K,V>[])new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if((oldTable[index] != null) && oldTable[index].isIn())
				addPAF_PROBE(oldTable[index].getKey(),oldTable[index].getValue());
		}
		
	}
    private void resizeSSF() {
		
		TableEntry<K,V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[]temp = (TableEntry<K,V>[])new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if((oldTable[index] != null) && oldTable[index].isIn())
				addSSF_PROBE(oldTable[index].getKey(),oldTable[index].getValue());
		}
		
	}
	private void resizePAF_DH() {
		
		TableEntry<K,V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[]temp = (TableEntry<K,V>[])new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if((oldTable[index] != null) && oldTable[index].isIn())
				addPAF_Double(oldTable[index].getKey(),oldTable[index].getValue());
		}
		
	}
    private void resizeSSF_DH() {
		
		TableEntry<K,V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[]temp = (TableEntry<K,V>[])new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if((oldTable[index] != null) && oldTable[index].isIn())
				addSSF_Double(oldTable[index].getKey(),oldTable[index].getValue());
		}
		
	}


	
	private class TableEntry <S,T>{
		private S key;
		private T value;
		private boolean control;
		
		private TableEntry(S searchKey,T dataValue) {
			this.key = searchKey;
			this.value = dataValue;
			this.control = true;
	    }
		public S getKey() {
			return key;
		}
		public T getValue() {						
			return value;
		}
		public void setValue(T value) {
			this.value = value;
		}
		public boolean isIn(){
			return control == true;
		}
		public boolean isRemoved(){
			return control == false;
		}		
		public void setToRemoved(int index){
			hashTable[index] = null;
		}

	}

	
	@Override
	public int getSize() {		
		return numberOfEntries;
	}

	public int locate(int index,K key) {
		boolean found = false;
		
		while(!found && (hashTable[index] != null)) {
			if(hashTable[index].isIn() && key.equals(hashTable[index].getKey())) 
				found = true;
			else
				index = (index + 1) % hashTable.length;
		}
		int result = -1;
		if(found)
			result = index;
		return result;
	}

	public V removeSSF_PROBE(K key) {
		  //checkInitialization();
		  V removedValue = null;
		  int index = SSF(key);
		  index = locate(index,key);
		  
		  if(index != -1) {
			  removedValue = hashTable[index].getValue();							 
			  hashTable[index].setToRemoved(index);
			  numberOfEntries--;
		   }
		  return removedValue;
		  
	}
	public V removePAF_PROBE(K key) {
		  //checkInitialization();
		  V removedValue = null;
		  int index = PAF(key);
		  index = locate(index,key);
		  
		  if(index != -1) {
			  removedValue = hashTable[index].getValue();			 
			  hashTable[index].setToRemoved(index);
			  numberOfEntries--;
		   }
		  return removedValue;
		  
	}
	public V removeSSF_DOUBLE(K key) {
		  //checkInitialization();
		  V removedValue = null;
		  int index = SSF(key);
		  index = doubleHash(index,key);
		  
		  if(index != -1) {
			  removedValue = hashTable[index].getValue();			 
			  hashTable[index].setToRemoved(index);
			  numberOfEntries--;
		   }
		  return removedValue;
		  
	}
	public V removePAF_DOUBLE(K key) {
		  //checkInitialization();
		  V removedValue = null;
		  int index = PAF(key);
		  index = doubleHash2(index,key);
		  
		  if(index != -1) {
			  removedValue = hashTable[index].getValue();			 
			  hashTable[index].setToRemoved(index);
			  numberOfEntries--;
		   }
		  return removedValue;
		  
	}
	private int getNextPrime(int size){
		
		if(size % 2 == 0){
			size++;
		}
		while(!isPrime(size)){
			size = size + 2;
		}
		return size;
	}
	private boolean isPrime(int n)
	{	        
	        if (n <= 1)
	            return false;
	  
	        
	        for (int i = 2; i < n; i++)
	            if (n % i == 0)
	                return false;
	  
	        return true;
	}


	


}

	
	


