package eu.apenet.dashboard.services.ead.publish;

public class EADCounts {

    private long numberOfDAOsBelow = 0l;
    private long numberOfUnitsWithDaosBelow = 0l;
    private long numberOfUnits = 0l;

    public void addClevel(long numberOfDAOs) {
        numberOfDAOsBelow += numberOfDAOs;
        if (numberOfDAOs > 0) {
            numberOfUnitsWithDaosBelow++;
        }
        numberOfUnits++;

    }

    public void addEadCounts(EADCounts counts) {
        numberOfDAOsBelow += counts.getNumberOfDAOsBelow();
        numberOfUnitsWithDaosBelow += counts.getNumberOfUnitsWithDaosBelow();
        numberOfUnits += counts.getNumberOfUnits();
    }

    public long getNumberOfDAOsBelow() {
        return numberOfDAOsBelow;
    }

    public long getNumberOfUnitsWithDaosBelow() {
        return numberOfUnitsWithDaosBelow;
    }

    public void addNumberOfDAOs(long numberOfDAOs) {
        numberOfDAOsBelow += numberOfDAOs;
    }

    public long getNumberOfUnits() {
        return numberOfUnits;
    }

    @Override
    public String toString() {
        return "EADCounts [numberOfDAOsBelow=" + numberOfDAOsBelow + ", numberOfUnitsWithDaosBelow="
                + numberOfUnitsWithDaosBelow + ", numberOfUnits=" + numberOfUnits + "]";
    }

}
