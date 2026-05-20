package org.example.persistence.specification;

import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.persistence.entity.CarModelJpaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class CarModelSpecifications {
    private CarModelSpecifications() {
    }

    public static Specification<CarModelJpaEntity> notRemoved() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("removed"));
    }

    public static Specification<CarModelJpaEntity> hasBrand(CarBrand brand) {
        return (root, query, criteriaBuilder) -> brand == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("carBrand"), brand);
    }

    public static Specification<CarModelJpaEntity> hasEngine(UUID engineId) {
        return hasNode("engine", engineId);
    }

    public static Specification<CarModelJpaEntity> hasTransmission(UUID transmissionId) {
        return hasNode("transmission", transmissionId);
    }

    public static Specification<CarModelJpaEntity> hasWheels(UUID wheelsId) {
        return hasNode("wheels", wheelsId);
    }

    public static Specification<CarModelJpaEntity> hasSteeringWheel(UUID steeringWheelId) {
        return hasNode("steeringWheel", steeringWheelId);
    }

    public static Specification<CarModelJpaEntity> hasInterior(UUID interiorId) {
        return hasNode("interior", interiorId);
    }

    private static Specification<CarModelJpaEntity> hasNode(String fieldName, UUID detailId) {
        return (root, query, criteriaBuilder) -> detailId == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get(fieldName).get("id"), detailId);
    }
}
