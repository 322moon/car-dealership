package org.example.storage.catalog;

import java.util.UUID;

public class StorageCatalogNotFoundException extends RuntimeException {
    public StorageCatalogNotFoundException(String entityName, UUID id) {
        super(entityName + " not found: " + id);
    }
}
