package test;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.Person;

public class HealthProfileReader {

	private static final String PEOPLE_XML_FILE = "people.xml";
	public static Document database;

	static {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			System.out.println("File: " + PEOPLE_XML_FILE);
			database = builder.parse(PEOPLE_XML_FILE);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main first checks whether a method is specified in the arguments. Based on the chosen method it checks 
	 * the remaining parameters for the correct type. Then it executes the chosen method.
	 * @param args
	 */
	public static void main(String[] args) {
		
		int argCount = args.length;
		if (argCount < 1) {
			System.out.println("Please specify method and parameters.");
			System.exit(-1);
		}
		
		String method = args[0];
		if (method.equals("weight")){
			if (argCount < 2){
				System.out.println("Please specify the personId.");
			}
			else {
				Long id; 
				Integer weight;
				
				id = readId(args[1]);
				if (id == null)
					System.exit(-1);
				
				weight = getWeigth(id);
				if (weight == null)
					System.exit(-1);			
				
				System.out.println("Id: " + id + ", weight: " + weight + "kg");
			}			
		}
		else if (method.equals("height")){
			if (argCount < 2){
				System.out.println("Please specify the personId.");
			}
			else {
				Long id; 
				Double height;
				
				id = readId(args[1]);
				if (id == null)
					System.exit(-1);
				
				height = getHeigth(id);
				if (height == null)
					System.exit(-1);			
				
				System.out.println("Id: " + id + ", height: " + height);
			}			
		}
		else if (method.equals("print_profile")){
			if (argCount < 2){
				System.out.println("Please specify the personId.");
			}
			else {
				Long id; 
				
				id = readId(args[1]);
				if (id == null)
					System.exit(-1);
				
				printHealthProfile(id);
			}			
			
		} 
		else if (method.equals("print_people")){
			printAllPeople();
		}
		else if (method.equals("filter_weight")){
			if (argCount < 3){
				System.out.println("Please specify an operator (<, >, =) and a weight.");				
			}
			else {
				Character operator = readOperator(args[1]);
				Double weight = readWeight(args[2]);
				if (weight != null && operator != null)
					filterByWeight(operator, weight);					
			}
		} 
		else {		
			System.out.println("The system did not find the method '" + method + "'");
		}
			
	}

	/**
	 * Returns the weight of the person identified by <code>personId</code> or 'null' if there is 
	 * no person with such personId in the file {@link #PEOPLE_XML_FILE}.
	 * @param personId id of the person
	 * @return the weight or 'null'
	 */
	private static Integer getWeigth(Long personId) {
		String expr_weight = "/people/person[@id='" + personId + "']/healthprofile/weight";
		
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(expr_weight);
			String w = (String) expr.evaluate(database, XPathConstants.STRING);
			if (w.isEmpty())
				return null;
			Integer ret =  Integer.valueOf(w);
			return ret;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return null;		
	}

	/**
	 * Returns the height of the person identified by <code>personId</code> or 'null' if there is 
	 * no person with such personId in the file {@link #PEOPLE_XML_FILE}.
	 * @param personId id of the person
	 * @return the height or 'null'
	 */
	private static Double getHeigth(Long personId) {
		String expr_weight = "/people/person[@id='" + personId + "']/healthprofile/height";

		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(expr_weight);
			String h = (String) expr.evaluate(database, XPathConstants.STRING);
			if (h.isEmpty())
				return null;
			Double ret = Double.parseDouble(h);
			return ret;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Prints all persons contained in the file {@link #PEOPLE_XML_FILE}.
	 */
	private static void printAllPeople(){
		String expr = "/people/person";
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression xpath_expr;
		NodeList result;
		
		try {
			xpath_expr = xpath.compile(expr);
			result = (NodeList) xpath_expr.evaluate(database, XPathConstants.NODESET);
			
			for (int i = 0; i < result.getLength(); i++){
				Node person_node = result.item(i);
				printPerson(person_node);
				printHealthProfile(person_node);
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		

	}	

	/**
	 * Prints the healthprofile of the person contained in the node <code>person_node</code>.
	 * @param person_node
	 */
	private static void printHealthProfile(Node person_node) {
		
		//xpath expressions for various data of the healthprofile
		String weight_expr = "person/healthprofile/weight";
		String height_expr = "person/healthprofile/height";
		String bmi_expr = "person/healthprofile/bmi";
		String lastupdate_expr = "person/healthprofile/lastupdate";
		
		Document doc = createNewDocument(person_node);
		
		//evaluate all xpath expressions (see above) and print the result
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression xpath_expr;
		String result;
		
		try {
			xpath_expr = xpath.compile(weight_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.println("weight: " + result + " kg");
			xpath.reset();
			
			xpath_expr = xpath.compile(height_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.println("height: " + result + " cm");
			xpath.reset();
			
			xpath_expr = xpath.compile(bmi_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.println("bmi: " + result);
			xpath.reset();
			
			xpath_expr = xpath.compile(lastupdate_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.println("last update: " + result);
			xpath.reset();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Prints all person having a weight that satisfies the condition specified with the parameters 
	 * <code>operation</code> and <code>weight</code>. <p>
	 * For example <code>filterByWeight('<', new Double(50)) </code> prints all persons having weight
	 * less than 50 kg.
	 * @param operation XPath comparision operator: <, >, =
	 * @param weight value used for comparision
	 */
	private static void filterByWeight(char operation, Double weight){
		NodeList result = null;
		String query = "/people/person[healthprofile/weight" + operation + "'" + weight + "']";
		
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(query);
			result = (NodeList) expr.evaluate(database, XPathConstants.NODESET);
			xpath.reset();
			
			for (int i = 0; i < result.getLength(); i++){
				Node person = result.item(i);
				printPerson(person);			
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} 	
	}

	/**
	 * Parses the string using {@link Long#parseLong(String)} and returns the result.
	 * If the string cannot be parsed as number, the method returns 'null' and prints an error message on {@link System#out}. 
	 * @param string
	 * @return the number value contained in the string
	 */
	private static Long readId(String string) {
		Long ret = null; 		
		try {
			ret = Long.parseLong(string);
		} catch (NumberFormatException e) {
			System.out.println("'" + string + "' is not a valid personId. Please specify a number.");
		}		
		return ret;
	}

	/**
	 * Parses the string using {@link Double#parseDouble(String)} and returns the result.
	 * If the string cannot be parsed as number, the method returns 'null' and prints an error message on {@link System#out}. 
	 * @param string
	 * @return the number value contained in the string
	 */
	private static Double readWeight(String string) {
		Double ret = null; 		
		try {
			ret = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			System.out.println("'" + string + "' is not a valid weight. Please specify a number.");
		}		
		return ret;
	}

	/**
	 * Decides if the string represents a valid operator for comparision.
	 * Valid operators are '<' , '>' , '=' .
	 * @param string
	 * @return the operator or null if the operator is not valid.
	 */
	private static Character readOperator(String string) {
		Character ret = null; 		
		ret = new Character(string.charAt(0));
		
		if (ret == '>' || ret == '<' || ret == '=')
			return ret;
		return null;
	}
	
	/**
	 * Prints the {@link Person} contained in the node <code>person_node</code> with its details. 
	 * @param person_node
	 */
	private static void printPerson(Node person_node){
		String firstname_expr = "/person/firstname";
		String lastname_expr = "/person/lastname";
		String birthdate_expr = "/person/birthdate";
		
		Document doc = createNewDocument(person_node);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression xpath_expr;
		String result;
		
		try {
			xpath_expr = xpath.compile(firstname_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.print(result + " ");
			xpath.reset();
			
			xpath_expr = xpath.compile(lastname_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			System.out.print(result + ", ");
			xpath.reset();
			
			xpath_expr = xpath.compile(birthdate_expr);
			result = (String) xpath_expr.evaluate(doc, XPathConstants.STRING);
			result = result.substring(0, 10);
			System.out.println(result);
			xpath.reset();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the healthprofile of the {@link Person} identified by the parameter <code>person_id</code>.
	 * @param person_id
	 */
	private static void printHealthProfile(Long person_id) {
		String person_expr = "/people/person[@id='" + person_id + "']";
	
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(person_expr);
			Node person_node = (Node) expr.evaluate(database, XPathConstants.NODE);
			
			if (person_node == null)
				return;					
			
			printHealthProfile(person_node);			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new {@link Document} having <code>root_node</code> as root node.
	 * @param root_node the root node
	 * @return a {@link Document} having <code>root_node</code> as root node
	 */
	private static Document createNewDocument(Node root_node) {
		//init new xml document
		Document doc = null;
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException e) {			
			e.printStackTrace();
		}

		//import and set root node
		Node root = doc.importNode(root_node, true);
		doc.appendChild(root);
		return doc;
	}
}