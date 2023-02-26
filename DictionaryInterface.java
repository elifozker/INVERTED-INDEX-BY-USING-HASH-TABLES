package Test;



import java.util.Iterator;

public interface DictionaryInterface<K, V> {

	public V  addSSF_PROBE(K key, V value);
	public V addPAF_PROBE(K key, V value);
	public int getSize();
	public void get(K key);
	public int locate(int index,K key);
	public V removeSSF_PROBE(K key);
	public V removePAF_PROBE(K key);
	public V removeSSF_DOUBLE(K key);
	public V removePAF_DOUBLE(K key);
	public V addSSF_Double(K key,V value);
	public V addPAF_Double(K key,V value) ;
	
	
	
}

