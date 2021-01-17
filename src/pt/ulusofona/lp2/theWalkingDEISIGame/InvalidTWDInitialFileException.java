package pt.ulusofona.lp2.theWalkingDEISIGame;


public class InvalidTWDInitialFileException extends Exception {
    int nrCreatures;
    String line;
    boolean validCreatures = true;


    public InvalidTWDInitialFileException(int nrCreatures, boolean validCreatures, String line) {
        this.nrCreatures = nrCreatures;
        this.validCreatures = validCreatures;
        this.line = line;
    }

    public boolean validNrOfCreatures() {
        if(nrCreatures >= 2) {
            return true;
        }
        return false;
    }

    public boolean validCreatureDefinition() {
        if(validCreatures == true) {
            return true;
        }
        return false;
    }

    public String getErroneousLine() {
        return line;
    }
}
