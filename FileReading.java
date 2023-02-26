package Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class FileReading {

	public static void main(String[] args) throws IOException {
		 File dir = new File("bbc");	
		 String DELIMITERS = "[-+=" + " " + "\r\n " + "1234567890" +"’'\"" + "(){}<>\\[\\]" +":" +"," +"‒–—―" +"…" +"!" +"." +"«»" +"-‐" +"?" +"‘’“”" +";" +"/" +"⁄" +"␠" +"·" +"&" +"@" +"*" +"\\" +"•" + "^" +"¤¢$€£¥₩₪" +"†‡" +"°" +"¡" +"¿" +  "¬" +"#" +"№" +
				 "%‰‱" +"¶" +"′" +"§" + "~" + "¨" +  "_" +"|¦" +"⁂" + "☞" + "∴" +"‽" +  "※" + "]";

				HashedDictionary<String,LinkedList> dataBase = new HashedDictionary<String,LinkedList>();		      
	            File[] folder = dir.listFiles();	      	     	                 
	            ArrayList <Word> wordlist = new ArrayList<Word>();
	            ArrayList <String> wordlist2 = new ArrayList<String>();
	            
	            
	            
	            Word word = null;	           
	            for(File table : folder) {      
	                File[] filenames = table.listFiles();
	                if(filenames != null){
	                	for (File file : filenames) {			                		
		                		 String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));  
		                		 String[] splitted = content.split(DELIMITERS);           		  		                		
		                		 File myObj = new File("stop_words_en.txt");
		                		 Scanner myReader = new Scanner(myObj);
		                		 while(myReader.hasNextLine()) {
		                	    	 String data = myReader.nextLine();
		                	    	 for (int i = 0; i < splitted.length; i++) {	      	    		
		                				if(splitted[i].toLowerCase(Locale.ENGLISH).equals(data.toLowerCase(Locale.ENGLISH))) {
		                					splitted[i] = splitted[i].replace(splitted[i],"");		                					      					 
		                				}
		                			 }	                	    	 
		                	     }		                		 
		                		 for (int i = 0; i < splitted.length; i++) {
		                			 if(!splitted[i].equals("")) {
		                				 wordlist2.add(splitted[i]);
		                			 }						
								 }
		                		 
		                		 int i = 0;
		                		 while(!wordlist2.isEmpty()) {
		                			 word = new Word(wordlist2.get(i).toLowerCase(Locale.ENGLISH),1,file.getName(),table.getName());		         			 
		                			 wordlist2.remove(i);
		                			 for (int j = 0; j < wordlist2.size(); j++) {
		                				 if(wordlist2.get(j).toLowerCase(Locale.ENGLISH).equals(word.getName().toLowerCase(Locale.ENGLISH))) {
		                					 word.setFrequency(word.getFrequency()+1);
		                					 wordlist2.remove(j);
		                				 }
									}
		                			 wordlist.add(word);
		                				
		                				
		                			 
		                			
		                		 }
		                   }
						}		                		
	             }

	           

	            Scanner sc = new Scanner(System.in);
	            System.out.println("Enter 1 for SSF_Probe: 2 for PAF_Probe: ");
	            System.out.print("Enter 3 for SSF_Double: 4 For PAF_Double: ");        
	           int choice = sc.nextInt();	          
	           if(choice == 1 || choice == 2 || choice == 3 || choice == 4) {
	               for (int i = 0; i < wordlist.size(); i++) {		        	  
		        	   LinkedList <String> linkedlist = new LinkedList<String>();
		        	   String name1 = wordlist.get(i).getName();
					   for (int j = 0; j <wordlist.size(); j++) {
						  if(name1.equals(wordlist.get(j).getName())) {						 
							  linkedlist.add(wordlist.get(j).getFrequency() + "_" + wordlist.get(j).getFoldername() + "_" + wordlist.get(j).getTextName());	
							 				  
						  }
					   }					   
					   if(choice == 1) 				  
						   dataBase.addSSF_PROBE(name1, linkedlist);		 
					   else if(choice == 2) 
						   dataBase.addPAF_PROBE(name1, linkedlist);		
					   else if(choice  == 3) 
						   dataBase.addSSF_Double(name1, linkedlist);							   
					   else if(choice == 4) 
						   dataBase.addPAF_Double(name1, linkedlist);		
					   
					  
						  
					   
				    }
	           }
	            File search = new File("search.txt");
     		    Scanner myReaderSearch = new Scanner(search);
     		    while(myReaderSearch.hasNextLine()) {
     		    	String data = myReaderSearch.nextLine();
     		    	dataBase.get(data);
     		    }
		          
		        	  
	           

	           


	           
       	
	            
	            
	          
	           

	           
	         
	          
	           
	                	
	                	 
	   }
	
	

}
