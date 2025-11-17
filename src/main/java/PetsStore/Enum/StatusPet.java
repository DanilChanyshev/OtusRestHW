package PetsStore.Enum;

public enum StatusPet {
    AVAILABLE ("available"),
    PENDING ("pending"),
    SOLD ("sold");

    private String title;

    StatusPet(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
