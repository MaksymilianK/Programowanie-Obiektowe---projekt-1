package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.ObjectCreationException;

import java.util.Map;

public class SimulationStatisticsSnapshot {

    private final int livingAnimals;
    private final int livingPlants;
    private final Map<Gene, Integer> genes;
    private final float averageEnergy;
    private final float averageLifeTime;
    private final float averageChildren;

    public SimulationStatisticsSnapshot(int livingAnimals, int livingPlants, Map<Gene, Integer> genes,
                                        float averageEnergy, float averageLifeTime, float averageChildren) {
        this.livingAnimals = livingAnimals;
        this.livingPlants = livingPlants;
        this.genes = genes;
        this.averageEnergy = averageEnergy;
        this.averageLifeTime = averageLifeTime;
        this.averageChildren = averageChildren;
    }

    public int getLivingAnimals() {
        return livingAnimals;
    }

    public int getLivingPlants() {
        return livingPlants;
    }

    public Map<Gene, Integer> getGenes() {
        return genes;
    }

    public float getAverageEnergy() {
        return averageEnergy;
    }

    public float getAverageLifeTime() {
        return averageLifeTime;
    }

    public float getAverageChildren() {
        return averageChildren;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer livingAnimals;
        private Integer livingPlants;
        private Map<Gene, Integer> genes;
        private Float averageEnergy;
        private Float averageLifeTime;
        private Float averageChildren;

        public Builder livingAnimals(int livingAnimals) {
            this.livingAnimals = livingAnimals;
            return this;
        }

        public Builder livingPlants(int livingPlants) {
            this.livingPlants = livingPlants;
            return this;
        }

        public Builder genes(Map<Gene, Integer> genes) {
            this.genes = genes;
            return this;
        }

        public Builder averageEnergy(float averageEnergy) {
            this.averageEnergy = averageEnergy;
            return this;
        }

        public Builder averageLifeTime(float averageLifeTime) {
            this.averageLifeTime = averageLifeTime;
            return this;
        }

        public Builder averageChildren(float averageChildren) {
            this.averageChildren = averageChildren;
            return this;
        }

        public SimulationStatisticsSnapshot build() {
            if (livingAnimals == null || livingPlants == null || genes == null || averageEnergy == null
                    || averageLifeTime == null || averageChildren == null) {
                throw new ObjectCreationException("Cannot create a simulation snapshot; some of fields are nulls");
            }

            return new SimulationStatisticsSnapshot(livingAnimals, livingPlants, Map.copyOf(genes), averageEnergy,
                    averageLifeTime, averageChildren);
        }
    }
}
