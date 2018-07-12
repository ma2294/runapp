package bath.run.model;

public class UserProfileModel {

    private static UserProfileModel instance = null;
    private int weight = 0;
    private int height = 0;
    private String name = "";
    private int weightPrompt = 0;

    private UserProfileModel() {

    }

    //Singleton. Enables me to change specific instance from notfications in future update.
    public static UserProfileModel getInstance() {
        if (instance == null) {
            instance = new UserProfileModel();
        }
        return instance;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeightPrompt() {
        return weightPrompt;
    }

    public void setWeightPrompt(int weightPrompt) {
        this.weightPrompt = weightPrompt;
    }

    @Override
    public String toString() {
        return getName() + " . H: " + getHeight()+ ". W: "+getWeight()+ ". " +
                "Time per weigh prompt: "+getWeightPrompt();
    }
}
