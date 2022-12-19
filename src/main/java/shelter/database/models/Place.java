package shelter.database.models;

public class Place extends AbstractModel {
    private Integer number;

    private String description;

    private Integer maxAnimal;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxAnimal() {
        return maxAnimal;
    }

    public void setMaxAnimal(Integer maxAnimal) {
        this.maxAnimal = maxAnimal;
    }
}
