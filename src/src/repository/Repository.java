package repository;

import domain.Unique;

import java.util.Map;

public interface Repository<T extends Unique, U> {
    void add(T item);

    T get(U id);

    void update(U id, T item);
    void remove(U id);
    Map<Number, T> getAll();
    String toString();

    Iterable<T> getAllForClient();
}
