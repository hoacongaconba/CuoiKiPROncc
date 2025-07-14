package Utility;

import Model.Event;
import Model.Organizer;
import Model.Venue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Validation {
    final Scanner scanner = new Scanner(System.in);

    // --------------------------- Input Methods for Creating New Event ---------------------------

    // Creates a new event by collecting all required details
    public Event createNewEvent(ArrayList<Event> events, ArrayList<Organizer> organizers, ArrayList<Venue> venues) {
        int eventId = getUniqueEventId(events);
        String eventName = getValidEventName();
        int organizerId = getExistingId("organizer", organizers);
        int venueId = getExistingId("venue", venues);
        String startDate;
        String endDate;
        // Ensure valid date order
        while (true) {
            startDate = getValidDate("start");
            endDate = getValidDate("end");
            if (isValidDateOrder(startDate, endDate)) {
                break;
            }
            System.out.println("End date must be on or after start date.");
        }

        int expectedAttendees = getValidAttendees();
        return new Event(eventId, eventName, organizerId, venueId, startDate, endDate, expectedAttendees);
    }

    // Gets a unique event ID that doesn't exist in the events list
    private int getUniqueEventId(ArrayList<Event> events) {
        while (true) {
            System.out.println("Enter the event ID:");
            String input = scanner.nextLine();
            try {
                int id = Integer.parseInt(input);
                if (id < 0) {
                    System.out.println("Event ID cannot be negative.");
                    continue;
                }
                if (doesIdExist(id, events)) {
                    System.out.println("This event ID already exists.");
                    continue;
                }
                return id;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Gets a valid event name (minimum 3 characters)
    private String getValidEventName() {
        while (true) {
            System.out.println("Enter the event name:");
            String name = scanner.nextLine();
            if (name.length() < 3) {
                System.out.println("Event name must be at least 3 characters long.");
                continue;
            }
            return name;
        }
    }

    // Gets an existing ID for organizer or venue
    private int getExistingId(String type, ArrayList<?> list) {
        while (true) {
            System.out.println("Enter the " + type + " ID:");
            displayAllIds(list);
            String input = scanner.nextLine();
            try {
                int id = Integer.parseInt(input);
                if (doesIdExist(id, list)) {
                    return id;
                }
                System.out.println("No " + type + " found with this ID.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Gets a valid date in YYYY-MM-DD format
    private String getValidDate(String dateType) {
        while (true) {
            System.out.println("Enter the " + dateType + " date (YYYY-MM-DD):");
            String date = scanner.nextLine();
            if (isValidDateFormat(date)) {
                return date;
            }
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    // Gets a valid number of expected attendees (positive number)
    private int getValidAttendees() {
        while (true) {
            System.out.println("Enter the number of expected attendees:");
            String input = scanner.nextLine();
            try {
                int attendees = Integer.parseInt(input);
                if (attendees > 0) {
                    return attendees;
                }
                System.out.println("Number of attendees must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }




    // --------------------------- Input Methods for Updating Event ---------------------------

    // Updates an existing event, allowing fields to remain unchanged
    public Event updateEvent(ArrayList<Event> events, ArrayList<Organizer> organizers, ArrayList<Venue> venues, Event existingEvent) {
        int newId = getUpdatedEventId(existingEvent.getEventId(), events);
        String newName = getUpdatedEventName(existingEvent.getEventName());
        int newOrganizerId = getUpdatedId("organizer", existingEvent.getOrganizerId(), organizers);
        int newVenueId = getUpdatedId("venue", existingEvent.getVenueId(), venues);

        String newStartDate;
        String newEndDate;
        // Ensure valid date order for updated dates
        while (true) {
            newStartDate = getUpdatedDate("start", existingEvent.getStartDate());
            newEndDate = getUpdatedDate("end", existingEvent.getEndDate());
            if (isValidDateOrder(newStartDate, newEndDate)) {
                break;
            }
            System.out.println("End date must be on or after start date.");
        }

        int newAttendees = getUpdatedAttendees(existingEvent.getExpectedAttendees());
        return new Event(newId, newName, newOrganizerId, newVenueId, newStartDate, newEndDate, newAttendees);
    }

    // Gets an updated event name or keeps current if blank
    private String getUpdatedEventName(String currentName) {
        while (true) {
            System.out.println("Enter new event name (press Enter to keep '" + currentName + "'):");
            String name = scanner.nextLine();
            if (name.isBlank()) {
                return currentName;
            }
            if (name.length() < 3) {
                System.out.println("Event name must be at least 3 characters long.");
                continue;
            }
            return name;
        }
    }

    // Gets an updated event ID or keeps current if blank
    private int getUpdatedEventId(int currentId, ArrayList<Event> events) {
        while (true) {
            System.out.println("Enter new event ID (press Enter to keep " + currentId + "):");
            String input = scanner.nextLine();
            if (input.isBlank()) {
                return currentId;
            }
            try {
                int newId = Integer.parseInt(input);
                if (newId < 0) {
                    System.out.println("Event ID cannot be negative.");
                    continue;
                }
                if (doesIdExist(newId, events)) {
                    System.out.println("This event ID already exists.");
                    continue;
                }
                return newId;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Gets an updated ID for organizer or venue or keeps current if blank
    private int getUpdatedId(String type, int currentId, ArrayList<?> list) {
        while (true) {
            System.out.println("Enter new " + type + " ID (press Enter to keep " + currentId + "):");
            displayAllIds(list);
            String input = scanner.nextLine();
            if (input.isBlank()) {
                return currentId;
            }
            try {
                int newId = Integer.parseInt(input);
                if (doesIdExist(newId, list)) {
                    return newId;
                }
                System.out.println("No " + type + " found with this ID.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Gets an updated date or keeps current if blank
    private String getUpdatedDate(String dateType, String currentDate) {
        while (true) {
            System.out.println("Enter new " + dateType + " date (press Enter to keep " + currentDate + "):");
            String date = scanner.nextLine();
            if (date.isBlank()) {
                return currentDate;
            }
            if (isValidDateFormat(date)) {
                return date;
            }
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    // Gets updated attendees or keeps current if blank
    private int getUpdatedAttendees(int currentAttendees) {
        while (true) {
            System.out.println("Enter new expected attendees (press Enter to keep " + currentAttendees + "):");
            String input = scanner.nextLine();
            if (input.isBlank()) {
                return currentAttendees;
            }
            try {
                int attendees = Integer.parseInt(input);
                if (attendees > 0) {
                    return attendees;
                }
                System.out.println("Number of attendees must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Gets an event ID for a specific purpose (e.g., deletion)
    public int getEventIdForAction(String purpose) {
        while (true) {
            System.out.println("Enter event ID to " + purpose + ":");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // --------------------------- Validation Methods ---------------------------

    // Checks if date string is in valid YYYY-MM-DD format
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Checks if start date is before or equal to end date
    public boolean isValidDateOrder(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return !start.isAfter(end);
    }

    // Checks if an ID exists in the given list
    private boolean doesIdExist(int id, ArrayList<?> list) {
        for (Object item : list) {
            if (item instanceof Event && ((Event) item).getEventId() == id) {
                return true;
            }
            if (item instanceof Organizer && ((Organizer) item).getOrganizerID() == id) {
                return true;
            }
            if (item instanceof Venue && ((Venue) item).getVenueID() == id) {
                return true;
            }
        }
        return false;
    }

    // --------------------------- Helper Methods ---------------------------

    // Displays all IDs and names in the given list
    private void displayAllIds(ArrayList<?> list) {
        for (Object item : list) {
            if (item instanceof Event event) {
                System.out.println(event.getEventId() + ") " + event.getEventName());
            } else if (item instanceof Organizer organizer) {
                System.out.println(organizer.getOrganizerID() + ") " + organizer.getOrganizerName());
            } else if (item instanceof Venue venue) {
                System.out.println(venue.getVenueID() + ") " + venue.getVenueName());
            }
        }
    }
}