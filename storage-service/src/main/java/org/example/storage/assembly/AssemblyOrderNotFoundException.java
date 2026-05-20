package org.example.storage.assembly;

import java.util.UUID;

public class AssemblyOrderNotFoundException extends RuntimeException {
    public AssemblyOrderNotFoundException(UUID id) {
        super("Assembly order with id " + id + " not found");
    }
}
