package pt.ulusofona.lp2.theWalkingDEISIGame;


// no construtor receber um ficheiro igual ao do startgame ou receber as criaturas e isso

public class InvalidTWDInitialFileException extends Exception {
    int nrCreatures;
    String line;
    boolean validCreatures = true;

    public InvalidTWDInitialFileException(int nrCreatures) {
        this.nrCreatures = nrCreatures;
    }

    public InvalidTWDInitialFileException(boolean validCreatures, String line) {
        this.validCreatures = validCreatures;
        this.line = line;
    }

    public boolean validNrOfCreatures() {
        if(nrCreatures < 2) {
            return false;
        }
        return true;
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
