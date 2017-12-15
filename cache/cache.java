package aed.cache;

import es.upm.aedlib.Position;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;


public class Cache<Key, Value> {
    private int maxCacheSize;
    private Storage<Key, Value> storage;
    private Map<Key, CacheCell<Key, Value>> map;
    private PositionList<Key> lru;

    public Cache(int maxCacheSize, Storage<Key, Value> storage) {
        this.storage = storage;
        this.map = new HashTableMap<Key, CacheCell<Key, Value>>();
        this.lru = new NodePositionList<Key>();
        this.maxCacheSize = maxCacheSize;
    }

    public Value get(Key key) {
        // CAMBIA este metodo
        CacheCell<Key, Value> aux = map.get(key);
        //Not in Cache
        if (aux == null) {
            lru.addFirst(key);
            aux = new CacheCell<Key, Value>(storage.read(key), false, lru.first());
            map.put(key, aux);
            if (lru.size() > maxCacheSize) {
                if (map.get(lru.last().element()).getDirty()) {
                    storage.write(lru.last().element(), map.get(lru.last().element()).getValue());
                }
                map.remove(lru.last().element());
                lru.remove(lru.last());
                return aux.getValue();
            }
            return aux.getValue();
        }
        //In Cache
        else {
            inCache(key);
            return map.get(key).getValue();
        }
    }

    public void put(Key key, Value value) {
        // CAMBIA este metodo
        CacheCell<Key, Value> aux = map.get(key);
        //Not in Cache
        if (aux == null) {
            lru.addFirst(key);
            aux = new CacheCell<Key, Value>(value, true, lru.first());
            map.put(key, aux);
            if (lru.size() > maxCacheSize) {
                if (map.get(lru.last().element()).getDirty()) {
                    storage.write(lru.last().element(), map.get(lru.last().element()).getValue());
                }
                map.remove(lru.last().element());
                lru.remove(lru.last());
            }
        }
        //In Cache
        else {
            inCache(key);
            map.get(key).setValue(value);
            map.get(key).setDirty(true);
        }
    }

    private void inCache(Key key) {
        Position<Key> keyPosition = lru.first();
        while (keyPosition != null) {
            if (keyPosition.element().equals(key)) {
                lru.addFirst(keyPosition.element());
                lru.remove(keyPosition);
                map.get(key).setPos(lru.first());
                break;
            }
            keyPosition = lru.next(keyPosition);
        }
    }
}
