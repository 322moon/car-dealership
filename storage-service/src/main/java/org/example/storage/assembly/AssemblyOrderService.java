package org.example.storage.assembly;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssemblyOrderService {
    private final AssemblyOrderRepository repository;

    @Transactional
    public AssemblyOrderJpaEntity create(AssemblyOrderRequestDto request) {
        AssemblyOrderJpaEntity entity = new AssemblyOrderJpaEntity();
        entity.setSourceOrderId(request.getSourceOrderId());
        entity.setSourceOrderType(request.getSourceOrderType());
        entity.setCarId(request.getCarId());
        entity.setModelId(request.getModelId());
        entity.setWarehouseEmployeeId(request.getWarehouseEmployeeId());
        entity.setTraceId(request.getTraceId());
        entity.setStatus(AssemblyOrderStatus.CREATED);
        entity.setRemoved(false);
        return repository.save(entity);
    }

    public List<AssemblyOrderJpaEntity> findAll() {
        return repository.findAllByRemovedFalse();
    }

    public AssemblyOrderJpaEntity findById(UUID id) {
        return repository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new AssemblyOrderNotFoundException(id));
    }

    @Transactional
    public AssemblyOrderJpaEntity update(UUID id, AssemblyOrderUpdateDto request) {
        AssemblyOrderJpaEntity entity = findById(id);
        entity.setWarehouseEmployeeId(request.getWarehouseEmployeeId());
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        entity.setFailReason(request.getFailReason());
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) {
        AssemblyOrderJpaEntity entity = findById(id);
        entity.setRemoved(true);
        repository.save(entity);
    }
}
