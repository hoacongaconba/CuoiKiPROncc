package Model;

public class Event {
    private int eventID;//Mã sự kiện (ID sự kiện - kiểu số nguyên)
    private String eventName;//Tên sự kiện (kiểu chuỗi)
    private int organizerID;//Mã người tổ chức (ID người tổ chức - kiểu số nguyên)
    private int venueID;//Mã địa điểm tổ chức (ID địa điểm - kiểu số nguyên)
    private String startDate;//Ngày bắt đầu (kiểu chuỗi, định dạng ngày)
    private String endDate;//Ngày kết thúc (kiểu chuỗi, định dạng ngày)
    private int expectedAttendees;//Số người tham dự dự kiến (kiểu số nguyên)

    public Event(int eventID, String eventName, int organizerID, int venueID, String startDate, String endDate, int expectedAttendees) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.organizerID = organizerID;
        this.venueID = venueID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectedAttendees = expectedAttendees;
    }

    public int getEventId() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public int getOrganizerId() {
        return organizerID;
    }

    public int getVenueId() {
        return venueID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getExpectedAttendees() {
        return expectedAttendees;
    }

    public void setEventId(int eventId) {
        this.eventID = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerID = organizerId;
    }

    public void setVenueId(int venueId) {
        this.venueID = venueId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setExpectedAttendees(int expectedAttendees) {
        this.expectedAttendees = expectedAttendees;
    }

    @Override
    public String toString() {
        return "Event{ID=" + eventID + ", Name='" + eventName + "', OrganizerID=" + organizerID +
                ", VenueID=" + venueID + ", StartDate='" + startDate + "', EndDate='" + endDate +
                "', ExpectedAttendees=" + expectedAttendees + "}";
    }
}
