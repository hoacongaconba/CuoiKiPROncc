package Service;

import Model.Event;

import java.util.List;

public interface EventOperations {
    void createEvent(Event event);
    void updateEvent(int eventID, Event event);
    boolean deleteEvent(int eventID);
    List<Event> findEventsByName(String name);
    List<Event> listAllEvents();
    boolean saveTheFile(String fileName);
}
