package pl.kedziora.emilek.roomies.database.objects;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromString(String value) {
        for(Gender gender : Gender.values()) {
            if(gender.getValue().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Given value don't fit with genders values");
    }

}
