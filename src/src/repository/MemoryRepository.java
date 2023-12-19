package repository;

import domain.Unique;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<T extends Unique, U> implements Repository<T, U> {

    protected Map<U, T> data = new HashMap<>();
    private int nextID = 1;

    @Override
    public void add(T item) {
        item.setId((long) nextID);
        data.put((U) Integer.valueOf(nextID), item);
        nextID++;
    }

    @Override
    public T get(U id) {
        return data.get(id);
    }

    @Override
    public void update(U id, T item) {
        if (data.containsKey(id)) {
            item.setId((long) id);
            data.put(id, item);
        } else {
            throw new RuntimeException("Object with this ID does not exist");
        }
    }

    @Override
    public void remove(U id) {
        if (data.containsKey(id)) {
            data.remove(id);
        } else {
            throw new RuntimeException("Object with this ID does not exist");
        }
    }

    @Override
    public  Map<Number, T> getAll() {
        Map<Number, T> objectMap = new HashMap<>();
        for (U id : data.keySet()) {
            objectMap.put((Number) id, data.get(id));
        }
        return objectMap;
    }
    @Override
    public Iterable<T> getAllForClient()
    {
        return data.values();
    }
}
