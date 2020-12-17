package agh.cs.lab.shared;

import java.util.Objects;

public final class Trio<T1, T2, T3> {

    public final T1 first;
    public final T2 second;
    public final T3 third;

    public Trio(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Trio) {
            return first.equals(((Trio) obj).first) && second.equals(((Trio) obj).second)
                    && third.equals(((Trio) obj).third);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
