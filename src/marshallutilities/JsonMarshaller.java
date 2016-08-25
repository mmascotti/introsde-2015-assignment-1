package marshallutilities;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import data.People;

public class JsonMarshaller {
	/**
	 * Writes the {@link People} object in the JSON-file with path <code>file_path</code>.
	 * @param people {@link People} object to serialize
	 * @param file_path path of the destination file
	 */
	public static void PeopletoJson(People people, String file_path) {
		ObjectMapper mapper = new ObjectMapper();
		JaxbAnnotationModule module = new JaxbAnnotationModule();

		mapper.registerModule(module);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try {
			mapper.writeValue(new File(file_path), people);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
