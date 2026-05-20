package org.example.lab1.Domain.Entities.Details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class Detail {
    protected UUID id;
    protected String name;
    protected final DetailType type;
    protected List<UUID> compatibleModels;
    protected boolean inStock;
    protected Price price;

    void setInStock(boolean inStock){
        this.inStock = inStock;
    }

    public boolean isCompatibleWith(Model model) {
        return model != null && compatibleModels.contains(model.getId());
    }

    public void addCompatibleCar(Model model){
        this.compatibleModels.add(model.getId());
    }
}
