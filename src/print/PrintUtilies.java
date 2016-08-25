package print;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.xml.datatype.XMLGregorianCalendar;

import data.Healthprofile;
import data.People;
import data.Person;

/**
 * This class contains various methods for pretty printing of the objects.
 * All objects are printed to {@link System#out}.
 */
public class PrintUtilies {
	
	/**
	 * Prints the specified {@link Person} with details. 
	 * @param p
	 */
	public static void printPerson(Person p){
		System.out.print("[" + p.getId() + "] ");
		System.out.print(p.getFirstname() + " ");
		System.out.print(p.getLastname() + ", born: ");
		printDate(p.getBirthdate());
		System.out.print("\n");
		printHealthProfile(p.getHealthprofile());
		System.out.print("\n\n");
	}

	/**
	 * Prints the specified {@link XMLGregorianCalendar}.
	 * @param xml_cal
	 */
	public static void printDate(XMLGregorianCalendar xml_cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date d = xml_cal.toGregorianCalendar().getTime();
		System.out.print(sdf.format(d));
	}

	/**
	 * Prints the specified {@link Healthprofile}.
	 * @param hp
	 */
	private static void printHealthProfile(Healthprofile hp) {
		System.out.println(hp.getWeight() + " kg");
		System.out.println(hp.getHeight() + " m");
		System.out.println("BMI: " + hp.getBmi());
		System.out.print("last update: ");
		printDate(hp.getLastupdate());
	}

	/**
	 * Prints all {@link Person} in the specified {@link People} object.
	 * @param people
	 */
	public static void printPeople(People people) {
		Iterator<Person> it = people.getPerson().iterator();
		
		while(it.hasNext())
			printPerson(it.next());
	}
}
