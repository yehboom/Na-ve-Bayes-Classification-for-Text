/*--------------------------------------------------------
1.Tsai-Ting,Yeh 2020/06/05

2.java build 1.8.0_191 

3.(before you start it need to change the path the data folder and put the file in the same folder)
command line compilation examples:

>javac NaiveBayesClassifierForText.java

4.examples to run this program:

In shell window:

> java NaiveBayesClassifierForText


6. The file submit and you need for the program.

 a. NaiveBayesClassifierForText.java
 b. Document.java
 c.	terms.txt
 d. testClasses.txt
 e.	testMatrix.txt
 f.	trainClasses.txt
 g.	trainMatrix.txt

 



----------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayesClassifierForText {
	private static ArrayList<String>  wordlist;
	private static ArrayList<Document> documentList;	
	private static ArrayList<Document> documnetTestList;
	private static Double priorClass00;
	private static Double priorClass01;
	private static int totalWords;
	private static HashMap<String, Double> cCountMap;
	private static HashMap<String, Double> c2CountMap;	
	private static Double classCount;
	private static Double classCount1;	
	private static int numberOfClass;
	private static int numberOfClass1;	
	private static double correctP;
	private static double incorrectP;
	private static int wordlistSize;
	
	public static void main(String[] args) throws Exception {
	     wordlist= new ArrayList<String>();
	     documentList= new ArrayList<Document>();
	     documnetTestList= new ArrayList<Document>();
		 File directory = new File("");
		 
		 //1.Read terms file
	     String filePath=directory.getAbsolutePath()+"/terms.txt";
	     //display read file path
	     System.out.println("Read from: "+filePath);
	     String line = "";
	     //since the word in term file is separate by "\t" 
	     String splitBy = "\t";
	     int count=0;
	     int rowCount=0;
	     
	     //start to read the file, and add it to the wordlist
	     try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	         while ((line = br.readLine()) != null) {
	        	 //the term's file last line is equal to ""
	        	 if(!line.equals("")) {
	 	            wordlist.add(line);	 
	        	 }                     	
	         }
	     } catch (IOException e) {
	            e.printStackTrace();
	     }
	     
	     //After read data to the word list, we get it's size
	     wordlistSize=wordlist.size();
	     
	     //2.Read TrainMatrix file 
	     String filePath2=directory.getAbsolutePath()+"/trainMatrix.txt";
	     System.out.println("Read from: "+filePath2);        
	     String line2 = "";
	        	   
	     //start to read file trainMatrix
	     try (BufferedReader br2 = new BufferedReader(new FileReader(filePath2))) {
	         while ((line2 = br2.readLine()) != null) {
	        	//each loop it will read one line
	        	//and each line will contain several columns of document, we aslo use "\t" to separate and save 
	        	String[] read = line2.split(splitBy);	     
	        	
	        	//get the word from the wordList, it will match the row we just read from the file
	            String word=wordlist.get(rowCount);
	            	
	            if(count==0) {
		            //first row , initial variables
		            for(int i=0;i<read.length;i++) {
		            	//initial the temp hashmap for later store the word and frequency
			            HashMap<String,Double> tempDHashMap=new HashMap<String,Double>();
			            //get the index of the frequency number
		        		Double frequency=Double.parseDouble(read[i]);
		        		//add it to the total words
		        		totalWords+=frequency;
		        		//put the combine information to the temp HashMap
		        		tempDHashMap.put(word, frequency);
		        		//create a new document object
		        		Document newD=new Document();
		        		//load the hashMap to the object we create
		        		newD.setWordHashMap(tempDHashMap);	         
		        		//save the document to the document list
		        		documentList.add(newD);
		            }            	
	            }
	            //after first row 
	            if(count!=0) {
		            for(int i=0;i<read.length;i++) {
		            	//get the document file from the document list
	            		Document tempDD=documentList.get(i);
	            		//and get the original hashMap from the document
	            		HashMap<String,Double> tempDHashMap2=tempDD.getWordHashMap();
	            		//get the index of the frequency number
	            		Double frequency2=Double.parseDouble(read[i]);
	            		//add it to the total words
	            		totalWords+=frequency2;
	            		//put the combine information to the temp HashMap
	            		tempDHashMap2.put(word, frequency2);	            			
		            }           		
	            }
	            //count the row
	            rowCount++;
	            count++;
	            //System.out.println("read: "+read[0]);
	               	            
	          }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	     
	     
	     //3.read trainClasses file
	     String filePath3=directory.getAbsolutePath()+"/trainClasses.txt";
	     System.out.println("Read from: "+filePath3);        
	     String line3 = "";
	     //the hashMap count the separate word and put them into different class label
	     cCountMap=new HashMap<String, Double> ();
	     c2CountMap=new HashMap<String, Double> ();

	     //start to read from trainClasses.txt
	     try (BufferedReader br3 = new BufferedReader(new FileReader(filePath3))) {
	         while ((line3 = br3.readLine()) != null) {
	        	//separate the read line by "\t", this file would give you actual class id for each file
	        	String[] read3 = line3.split(splitBy);
	        	//get the document from the documentList
	        	Document tempDocument=documentList.get(Integer.parseInt(read3[0]));
	        	//set the class id to the document
	        	tempDocument.setClassID(read3[1]);
	        	
	        	//if the class id is equal to 0
	        	if(read3[1].equals("0")) {
	        		//get the hashMap from the document
	        		HashMap<String, Double> tempMap=tempDocument.getWordHashMap();
	        		//iteration the hash map
	        		for (Map.Entry<String, Double> entry : tempMap.entrySet()) {
	        			//if the count word map does not have the key from the original map 
	        			if(!cCountMap.containsKey(entry.getKey())) {
	        				//put the key and value to it
	        				cCountMap.put(entry.getKey(), entry.getValue());	        				
	        			}else {
	        				//if it already has data, get the value and add it a new value
	        				Double a=cCountMap.get(entry.getKey());
	        				a+=entry.getValue();
	        				//put back into the count map
	        				cCountMap.put(entry.getKey(), a);	        				
	        			}
	        		}
	        		//if the class id is equal to 1
	        	}else {
	        		HashMap<String, Double> tempMap=tempDocument.getWordHashMap();
	        		for (Map.Entry<String, Double> entry : tempMap.entrySet()) {
	        			if(!c2CountMap.containsKey(entry.getKey())) {
	        				c2CountMap.put(entry.getKey(), entry.getValue());
	        			}else {
	        				Double a=c2CountMap.get(entry.getKey());
	        				a+=entry.getValue();
	        				c2CountMap.put(entry.getKey(), a);
	        			}
	        		}	
	        	}
	        	
	         }
	     } catch (IOException e) {
	            e.printStackTrace();
	     }
	     
	          
	     
	     //4.Read test testMatrix.txt
	     String filePath4=directory.getAbsolutePath()+"/testMatrix.txt";
	     System.out.println("Read from: "+filePath4);        
	     String line4 = "";
	     int count1=0;
	     int rowCount1=0;
	        	
	     //read line from the testMatrix.txt
	     try (BufferedReader br4 = new BufferedReader(new FileReader(filePath4))) {
	         while ((line4 = br4.readLine()) != null) {	            	
	        	String[] read = line4.split(splitBy);	            	
	        	//get the word from the wordList
	            String word=wordlist.get(rowCount1);
	            	
	            if(count1==0) {
		            //first row 
		            for(int i=0;i<read.length;i++) {		            	
			            HashMap<String,Double> tempDHashMap=new HashMap<String,Double>();		        			
		        		Double frequency=Double.parseDouble(read[i]);
		        		tempDHashMap.put(word, frequency);
		        		Document newD=new Document();
		        		newD.setWordHashMap(tempDHashMap);	 
		        		//put it to the test document list
		        		documnetTestList.add(newD);	
		            }            	
	            }
	            //after first row 
	            if(count1!=0) {
		            for(int i=0;i<read.length;i++) {
	            		Document tempDD=documnetTestList.get(i);
	            		HashMap<String,Double> tempDHashMap4=tempDD.getWordHashMap();
	            		Double frequency4=Double.parseDouble(read[i]);
	            		tempDHashMap4.put(word, frequency4);		            		
		            }           		
	            }
	            rowCount1++;
	            count1++;	            
	          }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	     System.out.println(""); 
	     System.out.println("==========================================="); 
	     System.out.println("Train Document List Size: "+documentList.size()); 
	     System.out.println("Test Documnet List Size: "+documnetTestList.size()); 
	     System.out.println("Train Document Total Word:  "+totalWords); 
	     System.out.println("==========================================="); 	     
 		          
	     
	   //5.Read TestClasses file
	     String filePath5=directory.getAbsolutePath()+"/testClasses.txt";
	     System.out.println("Read from: "+filePath5);        
	     String line5 = "";
	     
	    //read the actual class id and set back to the document's class id
	     try (BufferedReader br5 = new BufferedReader(new FileReader(filePath5))) {
	         while ((line5 = br5.readLine()) != null) {
	        	String[] read5 = line5.split(splitBy);         	
	        	Document tempDocument=documnetTestList.get(Integer.parseInt(read5[0]));
	        	tempDocument.setClassID(read5[1]);	    	        	
	         }
	     } catch (IOException e) {
	            e.printStackTrace();
	     }
	  
	     //System.out.println("HashMap in DocumnetTestList size: "+documnetTestList.get(0).getWordHashMap().size());	     
	     
	     //count different class label word
	     //and count total number and separate number of category 
	     countDifferentClassWords();
	    
	     //Do NB Classifier and get prior         
	     //original format of NaiveBayesClassifier would cause underflow, so use the log fix version
	     NaiveBayesClassifier_fix();
	     
	     //calculate the Accuracy(for writing part)
	     calculateAccuracy();
	     
	     //firstTwenty(for writing part)
	     firstTwenty();
	     
	     //present the given word probabilities(for writing part)
	     outPutForSomeWords(new String[] {"program", "includ", "match", "game", "plai", "window", "file", "subject", "write"});
	     
	     
	}
	
	//present the given word probabilities {"program", "includ", "match", "game", "plai", "window", "file", "subject", "write"}
	public static void outPutForSomeWords(String[] newD) {			
		Double result=1.0*priorClass00;
		Double result1=1.0*priorClass01;
		
		for(int i=0;i<newD.length;i++) {
			Double wordInMap=cCountMap.get(newD[i]);
			Double wordInMap1=c2CountMap.get(newD[i]);			
			Double p=(wordInMap+1)/(classCount+wordlistSize+0.0);
			Double p1=(wordInMap1+1)/(classCount1+wordlistSize+0.0);									
			result*=p;
			result1*=p1;
		}
					
		System.out.println("(Not log version)The Windows class probabilities of"
				+ " \"program\", \"includ\", \"match\", \"game\", \"plai\", \"window\", \"file\", \"subject\", \"write: \""  				 
				);
		System.out.println(result);
		System.out.println("");
		
		System.out.println("(Not log version)The Hockey class probabilities of"
				+ " \"program\", \"includ\", \"match\", \"game\", \"plai\", \"window\", \"file\", \"subject\", \"write: \""  				 
				);
		System.out.println(result1);
					
	}

	//Provide output showing actual and predicted class labels for the first 20 document instances in the test data.
	public static void firstTwenty() {
		for(int i=0;i<20;i++) {
			Document d=documnetTestList.get(i);
			System.out.println("Test Item  "+ i +"  - Predicted Class:  "+d.getPreditClassID()+ " Actual Class:  "+ d.getClassID()+"  Prob:(use log)  "+d.getProbString());		
		}
		 System.out.println("==========================================================================================="); 
	}
	
	//Provide the overall classification accuracy obtained on the full test data.
	public static void calculateAccuracy() {
		double accuracy=correctP/(correctP+incorrectP+0.0);
		System.out.println("Overall Accuracy :"+accuracy);
	}
	
	//fix the underflow version
	public static void NaiveBayesClassifier_fix() {
	    //getPrior
	    getPrior();
		
	    //do the log to the prior
		Double result=Math.log(priorClass00);
		Double result1=Math.log(priorClass01);
		
		//iteration the test list
		for(int k=0;k<documnetTestList.size();k++) {	
			//get the hashmap from the document
			HashMap<String,Double> tempTestMap= documnetTestList.get(k).getWordHashMap();
			
			//iteration the HashMap
			for (Map.Entry<String, Double> entry : tempTestMap.entrySet()) {
				//get the test word and it's frequency
				String testWord= entry.getKey();
				Double frequency=entry.getValue();
				
				//get the number of this word shows in the class label one and class label two
				Double wordInMap=cCountMap.get(testWord);
				Double wordInMap1=c2CountMap.get(testWord);
				
				//do the Conditional Probabilities
				Double p=(wordInMap+1)/(classCount+wordlistSize+0.0);
				Double p1=(wordInMap1+1)/(classCount1+wordlistSize+0.0);						
				
				//times the word frequency to the Conditional Probabilities
				result+=frequency*Math.log(p);
				result1+=frequency*Math.log(p1);
																			
			}
						
			
			String predit;
			Double prob=0.0;
			
			//compare the result of class label one and two and assign decide to the prediction
			if(result>result1) {
				predit="0";
				prob=result;
			}else {
				predit="1";
				prob=result1;
			}
			
			//get the document from the test list
			Document d=documnetTestList.get(k);
			//set the predict class label id
			d.setPredictClassID(predit);
			d.setProb(prob);
			
			
			//compare the predict id and actual id is equal or not, increace the correct variable or incorrect variable for later use
			if(d.getClassID().equals(d.getPreditClassID())) {
				correctP++;
			}else {
				incorrectP++;
			}
			
			//initial the result for next document count
			result=Math.log(priorClass00);
			result1=Math.log(priorClass01);
									
		}

		
	}
	
	public static void getPrior() {	
		//total number of category divide by total number of document
		priorClass00=(double) (numberOfClass/(numberOfClass+numberOfClass1+0.0));
		//System.out.println("prior class label : "+priorClass00);
				
		priorClass01=(double) (numberOfClass1/(numberOfClass+numberOfClass1+0.0));
		//System.out.println("prior class1 label: "+priorClass01);
	}
		
	//count the word in different class and the class id number 
	public static void countDifferentClassWords() {
		//count the total class words in different class. (including repeat word)
		classCount=0.0;
		classCount1=0.0;
		
		//count the number Of class for prior later use
		numberOfClass=0;
		numberOfClass1=0;		
		
		for(int j=0;j<documentList.size();j++) {	
			//iteration and the document in the document list
			Document d=documentList.get(j);			
			//get the HashMap from document
			HashMap<String,Double> tempMap= d.getWordHashMap();	
			//get the class id from document
			String classId=d.getClassID();
						
			//count the total class label(number) of document
			if(classId.equals("0")) {			
				numberOfClass++;
			}else if(classId.equals("1")) {				
				numberOfClass1++;
			}else{
				System.out.println("Should not be here!");
			}
			
			//iteration the key in map and get value, so we can count the total words in different class
			for (Map.Entry<String, Double> entry : tempMap.entrySet()) {
				if(classId.equals("0")) {
					classCount+=entry.getValue();
				}else if(classId.equals("1")) {
					classCount1+=entry.getValue();
				}else{
					System.out.println("Should not be here!");
				}
			}		
							
		}
		
		System.out.println("The word number in first class count :"+classCount);
		System.out.println("The word number in sencond class count :"+classCount1);
		System.out.println("===========================================");
		System.out.println("Number of class :"+numberOfClass);
		System.out.println("Number of class1 :"+numberOfClass1);
		System.out.println("TotalNumber of class :"+documentList.size());
		System.out.println("===========================================");
		
		
	}
	
	
	
	
}
