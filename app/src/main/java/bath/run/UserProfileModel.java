package bath.run;

public class UserProfileModel {

    private int weight = 0;
    private int height = 0;
    private String name = "";
    private static UserProfileModel instance = null;

    private UserProfileModel(){

    }
    //Singleton
    public static UserProfileModel getInstance(){
        if (instance == null){
            return new UserProfileModel();
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
}
