package PetsStore.Enum;

public enum StatusPet {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    private final String title;

    StatusPet(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
