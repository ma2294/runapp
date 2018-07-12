package bath.run;

public class StepsModel {

    private final double kgToPounds = 2.2;

    public double getKgToPounds() {
        return kgToPounds;
    }

    public double convertWeight(int kg) {
        return kg *= kgToPounds;
    }

}
