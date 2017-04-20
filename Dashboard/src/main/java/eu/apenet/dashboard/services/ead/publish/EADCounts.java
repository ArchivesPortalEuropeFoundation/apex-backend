package eu.apenet.dashboard.services.ead.publish;

public class EADCounts {

    private long numberOfTotalDAOs = 0l;
    private long numberOfUnitsWithDaosBelow = 0l;
    private long numberOfUnits = 0l;

    public void addClevel(long numberOfDAOs) {
        numberOfTotalDAOs += numberOfDAOs;
//        if (numberOfDAOs > 0) {
//            numberOfUnitsWithDaosBelow++;
//        }
        numberOfUnits++;

    }

    public void addEadCounts(EADCounts counts) {
        numberOfTotalDAOs += counts.getNumberOfTotalDAOs();
        numberOfUnitsWithDaosBelow += counts.getNumberOfUnitsWithDaosBelow();
        numberOfUnits += counts.getNumberOfUnits();
    }

    public long getNumberOfTotalDAOs() {
        return numberOfTotalDAOs;
    }

    public long getNumberOfUnitsWithDaosBelow() {
        return numberOfUnitsWithDaosBelow;
    }

    public void addNumberOfDAOs(long numberOfDAOs) {
        numberOfTotalDAOs += numberOfDAOs;
    }

    public void addNumberOfDAOsBelow(long numberOfDAOsBelow) {
        this.numberOfTotalDAOs += numberOfDAOsBelow;
    }

    public void addNumberOfUnitsWithDaosBelow(long numberOfUnitsWithDaosBelow) {
        this.numberOfUnitsWithDaosBelow += numberOfUnitsWithDaosBelow;
    }

    public long getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfTotalDAOs(long numberOfTotalDAOs) {
        if (numberOfTotalDAOs > 0) {
            this.numberOfUnitsWithDaosBelow++;
        }
        this.numberOfTotalDAOs = numberOfTotalDAOs;
    }

    public void setNumberOfUnitsWithDaosBelow(long numberOfUnitsWithDaosBelow) {
        this.numberOfUnitsWithDaosBelow = numberOfUnitsWithDaosBelow;
    }

    public void setNumberOfUnits(long numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    @Override
    public String toString() {
        return "EADCounts [numberOfDAOsBelow=" + numberOfTotalDAOs + ", numberOfUnitsWithDaosBelow="
                + numberOfUnitsWithDaosBelow + ", numberOfUnits=" + numberOfUnits + "]";
    }

}
