package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.Pair;

import java.util.*;

public class GenesCounter {

    private final Map<Gene, Integer> genes = new HashMap<>();
    private final TreeMap<Integer, Set<Gene>> genesCounter = new TreeMap<>();

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
        } else {
            setNewCount(gene, geneCount);
        }
    }

    public Map<Gene, Integer> getAll() {
        return Collections.unmodifiableMap(genes);
    }

    public List<Pair<Gene, Integer>> getMostCommonGenes(int max) {
        var mostCommonGenes = new ArrayList<Pair<Gene, Integer>>(max);

        Integer iterator = genesCounter.lastKey();
        while (mostCommonGenes.size() < max && iterator != null) {
            for (var gene : genesCounter.get(iterator)) {
                if (mostCommonGenes.size() == max) {
                    break;
                }
                mostCommonGenes.add(new Pair<>(gene, iterator));
            }

            iterator = genesCounter.lowerKey(iterator);
        }

        return mostCommonGenes;
    }

    private void setNewCount(Gene gene, int newCount) {
        if (genesCounter.containsKey(newCount)) {
            genesCounter.get(newCount).add(gene);
        } else {
            var genesSet = new LinkedHashSet<Gene>();
            genesSet.add(gene);
            genesCounter.put(newCount, genesSet);
        }

        genes.put(gene, newCount);
    }
}
