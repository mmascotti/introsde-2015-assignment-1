package test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import marshallutilities.JsonMarshaller;
import marshallutilities.PeopleUnMarshaller;
import print.PrintUtilies;
import data.Healthprofile;
import data.People;
import data.Person;

public class TestMarshallUtilities {
	
	/**
	 * Executes the test method specified in the program arguments. Valid methods are:
	 * <li> <code> marshallXML </code>: marshalls the test data as XML in the file specified in the second argument
	 * <li> <code> unmarshallXML </code>: reads the data from the XML file specified in the second argument
	 * <li> <code>  marshallJSON </code>: marshalls the test data as JSON in the file specified in the second argument <p>
	 * 
	 * The second argument specifies the file to read from/write the data.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.print("Usage: TestMarshallUtilities (marshallXML | unmarshallXML | marshallJSON) file.xml");
			System.exit(-1);
		}
		
		String test = args[0];
		String filename = args[1];
		
		if (test.equals("marshallXML")){
			People people = createTestData();
			
			PeopleUnMarshaller.toXmlFile(people, filename);
			
			System.out.println("The following persons have been written to the XML-file " + filename);
			PrintUtilies.printPeople(people);			
		}
		else if (test.equals("unmarshallXML")){
			People people = PeopleUnMarshaller.toPeopleObject(filename);
			
			System.out.println("The following persons have been read from XML-file " + filename);
			PrintUtilies.printPeople(people);			
			
		}
		else if (test.equals("marshallJSON")){
			People people = createTestData();
			
			JsonMarshaller.PeopletoJson(people, filename);
			
			System.out.println("The following persons have been written to the JSON-file " + filename);
			PrintUtilies.printPeople(people);
		}
		else 
			System.out.print("Unrecognized method.");
	}
	
	/**
	 * Creates a {@link People} object with three {@link Person}s as test data.
	 * @return a test {@link People} object
	 */
	public static People createTestData() {
		People ret = new People();
		Person p;
		Healthprofile hp;
		
		p = new Person();
		p.setId(new Long(1));
		p.setFirstname("Alice");
		p.setLastname("Tulip");
		p.setBirthdate(asDate(1968, 1, 17));
		hp = createHealthprofile(56, 1.50, 24.88, asDate(2016, 1, 4));
		p.setHealthprofile(hp);
		ret.getPerson().add(p);
		
		p = new Person();
		p.setId(new Long(2));
		p.setFirstname("Bernd");
		p.setLastname("Breeder");
		p.setBirthdate(asDate(1955, 11, 2));
		hp = createHealthprofile(108, 1.90, 29.91, asDate(2016, 2, 5));
		p.setHealthprofile(hp);
		ret.getPerson().add(p);
		
		p = new Person();	
		p.setId(new Long(3));
		p.setFirstname("Carol");
		p.setLastname("Emerald");
		p.setBirthdate(asDate(1942, 7, 25));
		hp = createHealthprofile(52, 1.62, 19.81, asDate(2016, 3, 6));
		p.setHealthprofile(hp);
		ret.getPerson().add(p);
				
		return ret;		
	}
	
	/**
	 * Creates and returns an {@link XMLGregorianCalendar} set to the date specified in the parameters.
	 * @param year
	 * @param month
	 * @param day
	 * @return  
	 */
	private static XMLGregorianCalendar asDate(int year, int month, int day) {		
		XMLGregorianCalendar ret = null;
		try {
			ret = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			ret.setYear(year);
			ret.setMonth(month);
			ret.setDay(day);
			ret.setTime(0, 0, 0);
			ret.setTimezone(0);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Creates and returns a {@link Healthprofile} of the specified parameters.
	 * @param weigth
	 * @param height
	 * @param bmi
	 * @param lastupdate
	 * @return
	 */
	private static Healthprofile createHealthprofile(int weigth, double height, double bmi, XMLGregorianCalendar lastupdate){
		Healthprofile ret = new Healthprofile();
		ret.setWeight(weigth);
		ret.setHeight(height);
		ret.setBmi(bmi);
		ret.setLastupdate(lastupdate);
		
		return ret;		
	}
}
