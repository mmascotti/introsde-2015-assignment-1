package marshallutilities;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import data.People;

public class PeopleUnMarshaller {
	
	private static final String XSD_FILE_PATH = "people.xsd";
	private static final String DATA_PACKAGE = "data";

	/**
	 * Writes the {@link People} object in the XML-file with path <code>xmlfile_path</code>.
	 * @param people {@link People} object to serialize
	 * @param xmlfile_path file path where the people object is written
	 */
	public static void toXmlFile(People people, String xmlfile_path) {
		try {
			JAXBContext jaxbctx = JAXBContext.newInstance(DATA_PACKAGE);
			
			Marshaller marshaller = jaxbctx.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			marshaller.marshal(people, new File(xmlfile_path));
			
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(XSD_FILE_PATH));
			marshaller.setSchema(schema);
			
			PeopleValidationEventHandler validationEventHandler = new PeopleValidationEventHandler();
			marshaller.setEventHandler(validationEventHandler);
			
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}			
	}
	
	/**
	 * Reads the XML-file <code>xmlfile_path</code> and returns the content as {@link People} object.
	 * @param xmlfile_path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static People toPeopleObject(String xmlfile_path){
		People peopleElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DATA_PACKAGE);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(XSD_FILE_PATH));
			unmarshaller.setSchema(schema);
			
			PeopleValidationEventHandler validationEventHandler = new PeopleValidationEventHandler();
			unmarshaller.setEventHandler(validationEventHandler);

			peopleElement = (People) unmarshaller.unmarshal(new File(xmlfile_path));
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}		
		return peopleElement;
	}
}
