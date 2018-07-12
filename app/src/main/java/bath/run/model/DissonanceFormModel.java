package bath.run.model;

public class DissonanceFormModel {
    private static DissonanceFormModel instance = null;
    private int care = 0;
    private int frequency = 0;
    private int competitiveness = 0;

    private DissonanceFormModel() {

    }

    //Singleton
    public static DissonanceFormModel getInstance() {
        if (instance == null) {
            instance = new DissonanceFormModel();
        }
        return instance;
    }
    /*
     * Mutator methods for dissonance fed from Dissonance form controller (fragment).
     */

    public int getCare() {
        return care;
    }

    public void setCare(int care) {
        this.care = care;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getCompetitiveness() {
        return competitiveness;
    }

    public void setCompetitiveness(int competitiveness) {
        this.competitiveness = competitiveness;
    }

    public int getAvg() {
        int total = ((getCare() + getFrequency()) + 1) / 2;
        return total;
    }

    @Override
    public String toString() {
        return "Care: " + getCare() + ". Frequency: " + getFrequency() + ". Competitiveness: " + getCompetitiveness();
    }
}
