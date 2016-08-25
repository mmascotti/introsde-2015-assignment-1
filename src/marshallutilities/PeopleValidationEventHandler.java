package marshallutilities;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class PeopleValidationEventHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
		if ((event.getSeverity() == ValidationEvent.ERROR)|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {
			ValidationEventLocator locator = event.getLocator();
			System.out.println("Validation Error:" + event.getMessage());
			System.out.println("at line number:" + locator.getLineNumber());
			return false;
		}
		return true;
	}
}
