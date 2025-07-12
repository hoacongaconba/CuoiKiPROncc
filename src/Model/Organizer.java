package Model;

public class Organizer {
    private int organizerID;//Mã người tổ chức (ID người tổ chức - kiểu số nguyên)
    private String organizerName;//Tên người tổ chức (kiểu chuỗi)

    public Organizer(int organizerID, String organizerName) {
        this.organizerID = organizerID;
        this.organizerName = organizerName;
    }

    public int getOrganizerID() {
        return organizerID;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerID(int organizerID) {
        this.organizerID = organizerID;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    @Override
    public String toString() {
        return "Organizer{ID=" + organizerID + ", Name='" + organizerName + "'}";
    }
}
