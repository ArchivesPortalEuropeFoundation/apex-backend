/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dashboard.services.eaccpf.publish;

/**
 *
 * @author papp
 */
public class EacCpfCounts {
    private long numberOfCpfRelations = 0l;
    private long numberOfResourceRelations = 0l;
    private long numberOfFunctionRelations = 0l;

    public void incrementCpfRelations() {
        numberOfCpfRelations++;
    }

    public void incrementResourceRelations() {
        numberOfResourceRelations++;
    }

    public void incrementFunctionRelations() {
        numberOfFunctionRelations++;
    }

    public long getNumberOfCpfRelations() {
        return numberOfCpfRelations;
    }

    public long getNumberOfResourceRelations() {
        return numberOfResourceRelations;
    }

    public long getNumberOfFunctionRelations() {
        return numberOfFunctionRelations;
    }

    @Override
    public String toString() {
        return "EacCpfCounts [numberOfCpfRelations=" + numberOfCpfRelations + ", numberOfResourceRelations="
                + numberOfResourceRelations + ", numberOfFunctionRelations=" + numberOfFunctionRelations + "]";
    }
}
