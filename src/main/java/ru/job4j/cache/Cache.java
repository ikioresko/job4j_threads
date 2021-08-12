package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        Base stored = memory.get(model.getId());
        int modelId = model.getId();
        memory.computeIfPresent(modelId, (base, base2) -> {
            if (stored.getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equal");
            }
            Base changed = new Base(model.getId(), model.getVersion() + 1);
            changed.setName(model.getName());
            return changed;
        });
        return true;
    }

    public Base get(int id) {
        return memory.get(id);
    }

    public void delete(Base model) {
        memory.remove(model.getId());
    }
}
