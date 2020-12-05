package agh.cs.lab.element.animal;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Gene {

    private final List<Integer> sequence;
    private final String stringRepresentation;

    Gene(List<Integer> sequence, String stringRepresentation) {
        this.sequence = Collections.unmodifiableList(sequence);
        this.stringRepresentation = stringRepresentation;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gene) {
            return sequence.equals(((Gene) obj).sequence);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence);
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
