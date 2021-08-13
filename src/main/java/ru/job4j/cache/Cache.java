package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        Base result = memory.computeIfPresent(model.getId(), (base, base2) -> {
            int modelId = model.getId();
            int modelVersion = model.getVersion();
            if (memory.get(modelId).getVersion() != modelVersion) {
                throw new OptimisticException("Versions are not equal");
            }
            Base changed = new Base(modelId, modelVersion + 1);
            changed.setName(model.getName());
            return changed;
        });
        return result != null;
    }

    public Base get(int id) {
        return memory.get(id);
    }

    public void delete(Base model) {
        memory.remove(model.getId());
    }
}