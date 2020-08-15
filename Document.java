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
import java.util.HashMap;

//the object document
public class Document {
	private String classID;
	private HashMap<String,Double> wordHashMap;
	private String preditClassID;
	private Double prob;
	private String probS;
	
	//set the actual class id 
	public void setClassID(String classID) {
		this.classID=classID;
	}
	
	//set the word hash map
	public void setWordHashMap(HashMap<String,Double> wordHashMap){
		this.wordHashMap=wordHashMap;	
	}
	
	//set the predict class id
	public void setPredictClassID(String preditClassID) {
		this.preditClassID=preditClassID;
	}
	
	//set the prob
	public void setProb(Double prob) {
		this.prob=prob;
		//transfer to the string and set
		setProbString();
	}
	
	//set the prob string
	public void setProbString() {
		this.probS=String.valueOf(this.prob);
	}
	
	//get the prob string
	public String getProbString() {
		return this.probS;
	}
	
	//get the prob double
	public Double getProb(Double prob) {
		return prob;
	}
	
	//get the actual class id
	public String getClassID() {
		return this.classID;
	}
	
	//get the predict class id
	public String getPreditClassID() {
		return this.preditClassID;
	}
	
	//get the word hash map
	public HashMap<String,Double> getWordHashMap(){
		return this.wordHashMap;
	}
		
}
