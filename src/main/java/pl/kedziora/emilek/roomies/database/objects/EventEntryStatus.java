package pl.kedziora.emilek.roomies.database.objects;

public enum EventEntryStatus {

    NOT_FINISHED("Not finished"),
    FINISHED("Finished");

    private String label;

    EventEntryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
