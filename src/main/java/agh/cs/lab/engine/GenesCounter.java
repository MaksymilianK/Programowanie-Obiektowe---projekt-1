package agh.cs.lab.engine;

import agh.cs.lab.element.animal.Gene;

import java.util.*;

public class GenesCounter {

    private final Map<Gene, Integer> genes = new HashMap<>();
    private final SortedMap<Integer, Set<Gene>> genesCounter = new TreeMap<>();

    {
        genesCounter.put(0, Collections.emptySet());
    }

    public void add(Gene gene) {
        if (genes.containsKey(gene)) {
            int geneCount = genes.get(gene);
            genesCounter.get(geneCount).remove(gene);

            geneCount++;
            setNewCount(gene, geneCount);
        } else {
            setNewCount(gene, 1);
        }
    }

    public void remove(Gene gene) {
        int geneCount = genes.get(gene);
        genesCounter.get(geneCount).remove(gene);

        if (genesCounter.get(geneCount).size() == 0) {
            genesCounter.remove(geneCount);
        }

        geneCount--;
        if (geneCount == 0) {
            genes.remove(gene);
            return;
        } else {
            setNewCount(gene, geneCount);
        }
    }

    public Set<Gene> getMostCommonGenes() {
        return Set.copyOf(genesCounter.get(genesCounter.lastKey()));
    }

    private void setNewCount(Gene gene, int newCount) {
        if (genesCounter.containsKey(newCount)) {
            genesCounter.get(newCount).add(gene);
        } else {
            var genesSet = new HashSet<Gene>();
            genesSet.add(gene);
            genesCounter.put(newCount, genesSet);
        }

        genes.put(gene, newCount);
    }
}
