package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.ObjectCreationException;

public class SimulationStatisticsSnapshot {

    private final float totalAverageLivingAnimals;
    private final float totalAverageLivingPlants;
    private final String totalMostCommonGene;
    private final float totalAverageEnergy;
    private final float totalAverageLifeTime;
    private final float totalAverageChildren;

    public SimulationStatisticsSnapshot(float totalAverageLivingAnimals, float totalAverageLivingPlants,
                                        String totalMostCommonGene, float totalAverageEnergy, float totalAverageLifeTime,
                                        float totalAverageChildren) {
        this.totalAverageLivingAnimals = totalAverageLivingAnimals;
        this.totalAverageLivingPlants = totalAverageLivingPlants;
        this.totalMostCommonGene = totalMostCommonGene;
        this.totalAverageEnergy = totalAverageEnergy;
        this.totalAverageLifeTime = totalAverageLifeTime;
        this.totalAverageChildren = totalAverageChildren;
    }

    public float getTotalAverageLivingAnimals() {
        return totalAverageLivingAnimals;
    }

    public float getTotalAverageLivingPlants() {
        return totalAverageLivingPlants;
    }

    public String getTotalMostCommonGene() {
        return totalMostCommonGene;
    }

    public float getTotalAverageEnergy() {
        return totalAverageEnergy;
    }

    public float getTotalAverageLifeTime() {
        return totalAverageLifeTime;
    }

    public float getTotalAverageChildren() {
        return totalAverageChildren;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Float totalAverageLivingAnimals;
        private Float totalAverageLivingPlants;
        private Gene totalMostCommonGene;
        private Float totalAverageEnergy;
        private Float totalAverageLifeTime;
        private Float totalAverageChildren;

        public Builder totalLivingAnimals(float totalAverageLivingAnimals) {
            this.totalAverageLivingAnimals = totalAverageLivingAnimals;
            return this;
        }

        public Builder totalLivingPlants(float totalAverageLivingPlants) {
            this.totalAverageLivingPlants = totalAverageLivingPlants;
            return this;
        }

        public Builder totalMostCommonGene(Gene totalMostCommonGene) {
            this.totalMostCommonGene = totalMostCommonGene;
            return this;
        }

        public Builder totalAverageEnergy(float totalAverageEnergy) {
            this.totalAverageEnergy = totalAverageEnergy;
            return this;
        }

        public Builder totalAverageLifeTime(float totalAverageLifeTime) {
            this.totalAverageLifeTime = totalAverageLifeTime;
            return this;
        }

        public Builder totalAverageChildren(float totalAverageChildren) {
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
