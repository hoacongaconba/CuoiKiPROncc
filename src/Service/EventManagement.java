package Service;

import Model.Event;
import Model.Organizer;
import Model.Venue;
import Utility.Validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class EventManagement implements EventOperations {
    private ArrayList<Event> events;
    private ArrayList<Organizer> organizers;
    private ArrayList<Venue> venues;

    public EventManagement() {
        this.events = new ArrayList<>();
        this.organizers = new ArrayList<>();
        this.venues = new ArrayList<>();
    }

    //--------------------------------------Program Operations--------------------------------------

    @Override
    public void createEvent(Event event) {
        events.add(event);
    }

    @Override
    public void updateEvent(int idToUpdate, Event updatedEvent) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == idToUpdate) {
                events.set(i, updatedEvent);
                break;
            }
        }
    }

    @Override
    public boolean deleteEvent(int eventId) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == eventId) {
                events.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Event> findEventsByName(String name) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getEventName().toLowerCase().contains(name.toLowerCase())) {
                result.add(event);
            }
        }
        // Sắp xếp danh sách kết quả theo tên sự kiện tăng dần
        for (int i = 0; i < result.size() - 1; i++) {
            for (int j = i + 1; j < result.size(); j++) {
                String name1 = result.get(i).getEventName();
                String name2 = result.get(j).getEventName();
                if (name1.compareTo(name2) > 0) {
                    // Hoán đổi vị trí nếu sai thứ tự
                    Event temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }

        return result;
    }

    public Event findEventById(int id) {
        for (Event event : events) {
            if (event.getEventId() == id) {
                return event;
            }
        }
        return null;
    }

    @Override
    public List<Event> listAllEvents() {
        List<Event> sortedEvents = new ArrayList<>(events);

        // Dùng thuật toán bubble sort để sắp xếp theo tên và ID
        for (int i = 0; i < sortedEvents.size() - 1; i++) {
            for (int j = 0; j < sortedEvents.size() - i - 1; j++) {
                Event e1 = sortedEvents.get(j);
                Event e2 = sortedEvents.get(j + 1);

                // So sánh tên
                String name1 = e1.getEventName();
                String name2 = e2.getEventName();

                if (name1.compareTo(name2) > 0 ||
                        (name1.compareTo(name2) == 0 && e1.getEventId() > e2.getEventId())) {

                    // Hoán đổi nếu sai thứ tự
                    Event temp = sortedEvents.get(j);
                    sortedEvents.set(j, sortedEvents.get(j + 1));
                    sortedEvents.set(j + 1, temp);
                }
            }
        }

        return sortedEvents;
    }


    public boolean saveTheFile(String fileName) {
        // Security patch: prevent path traversal
        Path baseDirectory = Paths.get("Data").toAbsolutePath().normalize();
        Path targetPath = baseDirectory.resolve(fileName).normalize();

        if (!targetPath.startsWith(baseDirectory)) {
            return false;
        }

        try {
            Files.createDirectories(baseDirectory);
            PrintWriter writer = new PrintWriter(targetPath.toFile());
            for (Event event : events) {
                writer.println(event.getEventId() + "," +
                        event.getEventName() + "," +
                        event.getOrganizerId() + "," +
                        event.getVenueId() + "," +
                        event.getStartDate() + "," +
                        event.getEndDate() + "," +
                        event.getExpectedAttendees());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public List<Event> findEventsWithinRange(String startByDate, String endByDate) {
        // Chuyển chuỗi ngày bắt đầu thành đối tượng LocalDate
        LocalDate tempStart = LocalDate.parse(startByDate);

        // Chuyển chuỗi ngày kết thúc thành đối tượng LocalDate
        LocalDate tempEnd = LocalDate.parse(endByDate);

        // Danh sách để lưu các sự kiện thỏa điều kiện (nằm trong khoảng ngày)
        List<Event> eventsFound = new ArrayList<>();

        // Lặp qua toàn bộ danh sách sự kiện
        for (int i = 0; i < events.size(); i++) {
            // Lấy ngày bắt đầu và ngày kết thúc của sự kiện hiện tại và chuyển thành LocalDate
            LocalDate eventStart = LocalDate.parse(events.get(i).getStartDate());
            LocalDate eventEnd = LocalDate.parse(events.get(i).getEndDate());

            // Kiểm tra xem sự kiện có nằm trong khoảng từ tempStart đến tempEnd không
            if (tempStart.isBefore(eventStart) && tempEnd.isAfter(eventEnd)) {
                // Nếu đúng, thêm sự kiện vào danh sách kết quả
                eventsFound.add(events.get(i));
            }
        }

        // Trả về danh sách các sự kiện phù hợp
        return eventsFound;
    }
    //--------------------------------------Program Interface--------------------------------------

    public void runProgram() {
        Validation validator = new Validation();
        Scanner scanner = new Scanner(System.in);
        initializeData();

        while (true) {
            System.out.println("---Enter your option---");
            System.out.println("1. Create an event");
            System.out.println("2. Display all events");
            System.out.println("3. Update an event");
            System.out.println("4. Delete an event");
            System.out.println("5. Find events by name");
            System.out.println("6. Save events to file");
            System.out.println("7. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Event newEvent = validator.inputNewEvent(events, organizers, venues);
                    createEvent(newEvent);
                    break;
                case "2":
                    if (listAllEvents().isEmpty()) {
                        System.out.println("No event to display!");
                        break;
                    }
                    for (Event event : listAllEvents()) {
                        System.out.println(event);
                    }
                    break;
                case "3":
                    if (listAllEvents().isEmpty()) {
                        System.out.println("\uD83D\uDCDB No event to update!");
                        break;
                    }
                    int idToUpdate = validator.inputLooseEventId("update");
                    Event existingEvent = findEventById(idToUpdate);
                    if (existingEvent != null) {
                        System.out.println("\uD83D\uDEA9 Leave blank to keep current info.");
                        Event updatedEvent = validator.inputUpdatedEvent(events, organizers, venues, existingEvent);
                        updateEvent(idToUpdate, updatedEvent);
                        System.out.println("✅ Event updated successfully.");
                    } else {
                        System.out.println("❌ Event not found.");
                    }
                    break;
                case "4":
                    if (listAllEvents().isEmpty()) {
                        System.out.println("\uD83D\uDCDB No event to delete!");
                        break;
                    }
                    int idToDelete = validator.inputLooseEventId("delete");
                    Event eventToDelete = findEventById(idToDelete);
                    if (eventToDelete == null) {
                        System.out.println("❌ Event not found.");
                        break;
                    }
                    System.out.println("❓ Are you sure you want to delete: " + eventToDelete.getEventName() + " [" + eventToDelete.getEventId() + "]? [y/n]");
                    String confirmation = scanner.nextLine().toLowerCase();
                    if (confirmation.equals("y")) {
                        boolean isSuccessful = deleteEvent(idToDelete);
                        System.out.println(isSuccessful ? "✅ Delete successful." : "❌ Delete failed.");
                    } else if (confirmation.equals("n")) {
                        System.out.println("❌ Deletion cancelled.");
                    } else {
                        System.out.println("❌ Invalid option.");
                    }
                    break;
                case "5":
                    if (listAllEvents().isEmpty()) {
                        System.out.println("\uD83D\uDCDB No event to search for!");
                        break;
                    }
                    System.out.println("✋ Enter event name to search:");
                    String nameToSearch = scanner.nextLine().toLowerCase();
                    List<Event> foundEvents = findEventsByName(nameToSearch);
                    if (!foundEvents.isEmpty()) {
                        for (Event event : foundEvents) {
                            System.out.println(event);
                        }
                    } else {
                        System.out.println("❌ Event not found.");
                    }
                    break;
                case "6":
                    if (listAllEvents().isEmpty()) {
                        System.out.println("\uD83D\uDCDB No event to save!");
                        break;
                    }
                    System.out.println("✋ Enter the file name to save to:");
                    String fileName = scanner.nextLine() + ".csv";
                    if (saveTheFile(fileName)) {
                        System.out.println("✅ Events saved to " + fileName);
                    } else {
                        System.out.println("❌ Error saving file.");
                    }
                    break;
                case "7":
                    System.out.println("\uD83D\uDC96 Exited!");
                    return;
                default:
                    System.out.println("❌ Invalid option.");
                    break;
            }
        }
    }

    //--------------------------------------Initialize Data--------------------------------------

    private void initializeData() {
        organizers = new ArrayList<>();
        organizers.add(new Organizer(1, "EventPro Solutions"));
        organizers.add(new Organizer(2, "Green Light Agency"));
        organizers.add(new Organizer(3, "University Youth Union"));
        organizers.add(new Organizer(4, "TechWorld Group"));
        organizers.add(new Organizer(5, "Community Council"));
        organizers.add(new Organizer(6, "City Entertainment Co."));
        organizers.add(new Organizer(7, "Art & Culture Center"));
        venues = new ArrayList<>();
        venues.add(new Venue(1, "Grand Hall"));
        venues.add(new Venue(2, "City Conference Center"));
        venues.add(new Venue(3, "Open Air Park"));
        venues.add(new Venue(4, "Exhibition Pavilion"));
        venues.add(new Venue(5, "Downtown Auditorium"));
        venues.add(new Venue(6, "Community House"));
        venues.add(new Venue(7, "University Main Hall"));
    }

    //--------------------------------------Program Entry--------------------------------------

    public static void main(String[] args) {
        EventManagement eventManagement = new EventManagement();
        eventManagement.runProgram();
    }
}
