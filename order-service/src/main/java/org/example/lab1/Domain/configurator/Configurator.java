package org.example.lab1.Domain.configurator;

import lombok.AllArgsConstructor;
import org.example.exception.CompatibilityException.*;
import org.example.exception.ConfiguratorIllegalArgumentException.*;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.*;
import org.example.lab1.Domain.ValueObjects.Price;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Configurator {
    private Model model;
    public ConfiguredModel configure(Wheels wheels,
                                     SteeringWheel steeringWheel,
                                     Transmission transmission,
                                     Interior interior)
    {
        if (model == null){
             throw new ConfiguratorInvalidArgumentException("Model should be initialized");
        }

        Wheels selectedWheels = wheels != null ? wheels : this.model.getWheels();
        Interior selectedInterior = interior != null ? interior : this.model.getInterior();
        SteeringWheel selectedSteeringWheel = steeringWheel != null ? steeringWheel : this.model.getSteeringWheel();
        Transmission selectedTransmission = transmission != null ? transmission : this.model.getTransmission();

        validateRequiredNodes(selectedWheels, selectedSteeringWheel, selectedTransmission, selectedInterior);

        ConfiguredModel configuredModel = new ConfiguredModel(this.model);

        List<Detail> details = List.of(
                selectedWheels,
                selectedSteeringWheel,
                selectedTransmission,
                selectedInterior);

        for (Detail detail : details){
            if (!detail.isCompatibleWith(this.model)){
                throw new IncompatibleComponentException(detail.getName() + " not compatible with " + this.model.getName());
            }

            switch (detail){
                case Wheels w -> configuredModel.setWheels(w);
                case SteeringWheel sw -> configuredModel.setSteeringWheel(sw);
                case Interior i -> configuredModel.setInterior(i);
                case Transmission t -> configuredModel.setTransmission(t);
                default -> throw new InvalidComponentException("Unknown detail type");
            }
        }

        configuredModel.setTotalPrice(calculateTotalPrice(details));
        return configuredModel;
    }

    private Price calculateTotalPrice(List<Detail> details){
        Price basePrice = model.getBasePrice();
        Price surcharge = details.stream()
                .map(Detail::getPrice)
                .reduce(Price.of(0), Price::add);
        Price totalPrice = basePrice.add(surcharge);
        return totalPrice;
    }

    private void validateRequiredNodes(Wheels wheels, SteeringWheel steeringWheel, Transmission transmission, Interior interior){
        List<String> missingNodes = new ArrayList<>();
        if (wheels == null){
            missingNodes.add("wheels");
        }
        if(steeringWheel == null){
            missingNodes.add("SteeringWheel");
        }
        if(transmission == null){
            missingNodes.add("Transmission");
        }
        if(interior == null){
            missingNodes.add("Interior");
        }
        if(!missingNodes.isEmpty()){
            throw new DomainValidationException("Missing nodes: " + String.join(",", missingNodes));
        }
    }
}
