package agh.cs.lab.engine;

import java.util.*;

public class GenesFactory {

    public static final int GENES_LENGTH = 32;

    private final Random rand = new Random();

    public List<Integer> create() {
        var genes = new ArrayList<Integer>(32);

        for (int i = 0; i < 32; i++) {
            genes.add(rand.nextInt(8));
        }

        fixGenes(genes);

        return Collections.unmodifiableList(genes);
    }

    public List<Integer> inherit(List<Integer> parent1, List<Integer> parent2) {
        int break1 = rand.nextInt(31) + 1;

        int break2;
        do {
            break2 = rand.nextInt(31) + 1;
        } while (break2 == break1);

        var dominantParent = rand.nextInt(2) == 0 ? parent1 : parent2;
        var weakerParent = parent1 == dominantParent ? parent2 : parent1;
        int weakerParentSequence = rand.nextInt(3);

        var childGenes = new ArrayList<Integer>(32);

        if (weakerParentSequence == 0) {
            childGenes.addAll(weakerParent.subList(0, break1));
        } else {
            childGenes.addAll(dominantParent.subList(0, break1));
        }

        if (weakerParentSequence == 1) {
            childGenes.addAll(weakerParent.subList(break1, break2));
        } else {
            childGenes.addAll(dominantParent.subList(break1, break2));
        }

        if (weakerParentSequence == 2) {
            childGenes.addAll(weakerParent.subList(break2, GENES_LENGTH));
        } else {
            childGenes.addAll(dominantParent.subList(break2, GENES_LENGTH));
        }

        fixGenes(childGenes);

        return Collections.unmodifiableList(childGenes);
    }

    private void fixGenes(List<Integer> genes) {
        var genesCounter = new int[8];
        for (int i = 0; i < 8; i++) {
            genesCounter[i] = 0;
        }
        genes.forEach(gene -> genesCounter[gene] = genesCounter[gene] + 1);

        for (int i = 0; i < 8; i++) {
            if (genesCounter[i] == 0) {
                int geneToChange;
                do {
                    geneToChange = rand.nextInt(32);
                } while (genesCounter[genes.get(geneToChange)] < 2);

                genes.set(geneToChange, i);
            }
        }
    }
}
