package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.ObjectCreationException;

public class SimulationStatisticsSnapshot {

    private final double totalAverageLivingAnimals;
    private final double totalAverageLivingPlants;
    private final String totalMostCommonGene;
    private final double totalAverageEnergy;
    private final double totalAverageLifeTime;
    private final double totalAverageChildren;

    private SimulationStatisticsSnapshot(double totalAverageLivingAnimals, double totalAverageLivingPlants,
                                        String totalMostCommonGene, double totalAverageEnergy, double totalAverageLifeTime,
                                        double totalAverageChildren) {
        this.totalAverageLivingAnimals = totalAverageLivingAnimals;
        this.totalAverageLivingPlants = totalAverageLivingPlants;
        this.totalMostCommonGene = totalMostCommonGene;
        this.totalAverageEnergy = totalAverageEnergy;
        this.totalAverageLifeTime = totalAverageLifeTime;
        this.totalAverageChildren = totalAverageChildren;
    }

    public double getTotalAverageLivingAnimals() {
        return totalAverageLivingAnimals;
    }

    public double getTotalAverageLivingPlants() {
        return totalAverageLivingPlants;
    }

    public String getTotalMostCommonGene() {
        return totalMostCommonGene;
    }

    public double getTotalAverageEnergy() {
        return totalAverageEnergy;
    }

    public double getTotalAverageLifeTime() {
        return totalAverageLifeTime;
    }

    public double getTotalAverageChildren() {
        return totalAverageChildren;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Double totalAverageLivingAnimals;
        private Double totalAverageLivingPlants;
        private Gene totalMostCommonGene;
        private Double totalAverageEnergy;
        private Double totalAverageLifeTime;
        private Double totalAverageChildren;

        Builder() {}

        public Builder totalLivingAnimals(double totalAverageLivingAnimals) {
            this.totalAverageLivingAnimals = totalAverageLivingAnimals;
            return this;
        }

        public Builder totalLivingPlants(double totalAverageLivingPlants) {
            this.totalAverageLivingPlants = totalAverageLivingPlants;
            return this;
        }

        public Builder totalMostCommonGene(Gene totalMostCommonGene) {
            this.totalMostCommonGene = totalMostCommonGene;
            return this;
        }

        public Builder totalAverageEnergy(double totalAverageEnergy) {
            this.totalAverageEnergy = totalAverageEnergy;
            return this;
        }

        public Builder totalAverageLifeTime(double totalAverageLifeTime) {
            this.totalAverageLifeTime = totalAverageLifeTime;
            return this;
        }

        public Builder totalAverageChildren(double totalAverageChildren) {
            this.totalAverageChildren = totalAverageChildren;
            return this;
        }

        public SimulationStatisticsSnapshot build() {
            if (totalAverageLivingAnimals == null || totalAverageLivingPlants == null || totalAverageEnergy == null
                    || totalAverageLifeTime == null || totalAverageChildren == null) {
                throw new ObjectCreationException("Cannot create a simulation snapshot; some of fields are nulls");
            }

            String mostCommonGeneStr = totalMostCommonGene == null ? "-" : totalMostCommonGene.toString();

            return new SimulationStatisticsSnapshot(totalAverageLivingAnimals, totalAverageLivingPlants,
                    mostCommonGeneStr, totalAverageEnergy, totalAverageLifeTime, totalAverageChildren);
        }
    }
}
