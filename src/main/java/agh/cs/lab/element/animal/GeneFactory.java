package agh.cs.lab.element.animal;

import agh.cs.lab.shared.Rand;

import java.util.*;

public class GeneFactory {

    public static final int GENES_LENGTH = 32;

    private final Rand rand;

    public GeneFactory(Rand rand) {
        this.rand = rand;
    }

    /**
     * Creates a gene not depending on animal's parents.
     *
     * @return a gene
     */
    public Gene create() {
        var genes = new ArrayList<Integer>(GENES_LENGTH);
        for (int i = 0; i < GENES_LENGTH; i++) {
            genes.add(rand.randInt(8));
        }

        fixGenes(genes);
        sortGenes(genes);

        return new Gene(Collections.unmodifiableList(genes), stringRepresentation(genes));
    }

    /**
     * Creates a gene consisting of sequences from animal's parents.
     *
     * @return a gene
     */
    public Gene create(Gene parent1, Gene parent2) {
        int break1 = rand.randInt(1, GENES_LENGTH - 1);
        int break2 = rand.randInt(break1 + 1, GENES_LENGTH);

        var dominantParent = rand.randInt(2) == 0 ? parent1 : parent2;
        var weakerParent = parent1 == dominantParent ? parent2 : parent1;
        int weakerParentSequence = rand.randInt(3);

        var childGenes = new ArrayList<Integer>(GENES_LENGTH);

        if (weakerParentSequence == 0) {
            childGenes.addAll(weakerParent.getSequence().subList(0, break1));
        } else {
            childGenes.addAll(dominantParent.getSequence().subList(0, break1));
        }

        if (weakerParentSequence == 1) {
            childGenes.addAll(weakerParent.getSequence().subList(break1, break2));
        } else {
            childGenes.addAll(dominantParent.getSequence().subList(break1, break2));
        }

        if (weakerParentSequence == 2) {
            childGenes.addAll(weakerParent.getSequence().subList(break2, GENES_LENGTH));
        } else {
            childGenes.addAll(dominantParent.getSequence().subList(break2, GENES_LENGTH));
        }

        fixGenes(childGenes);
        sortGenes(childGenes);

        return new Gene(Collections.unmodifiableList(childGenes), stringRepresentation(childGenes));
    }

    private String stringRepresentation(List<Integer> genes) {
        var stringRepresentation = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            stringRepresentation.append(genes.get(i));
        }
        return stringRepresentation.toString();
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
                    geneToChange = rand.randInt(GENES_LENGTH);
                } while (genesCounter[genes.get(geneToChange)] < 2);

                genes.set(geneToChange, i);
            }
        }
    }

    private void sortGenes(List<Integer> genes) {
        genes.sort(Comparator.naturalOrder());
    }
}
