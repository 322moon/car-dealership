package org.example;


import lombok.Getter;
import org.example.exception.CompatibilityException;
import org.example.exception.ConfiguratorIllegalArgumentException;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ModelOptions;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyModels;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyOptions;
import org.example.lab1.Domain.configurator.Configurator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Getter
class ConfiguratorAndPresetsCheck {

    @Test
    void readyOptionsForModelShouldReturnOnlyCompatibleOptions() {
        Model model = ReadyModels.bmw320i();
        ModelOptions options = ReadyOptions.forModel(model);

        assertEquals(3, options.getWheels().size());
        assertEquals(2, options.getTransmissions().size());
        assertEquals(2, options.getSteeringWheels().size());
        assertEquals(2, options.getInteriors().size());
        assertTrue(options.getInteriors().stream().anyMatch(interior -> interior.getName().equals("Leather Dakota")));
        assertFalse(options.getInteriors().stream().anyMatch(interior -> interior.getName().equals("Sport Performance")));
    }

    @Test
    void readyCatalogFindersShouldReturnExpectedObjects() {
        assertEquals("BMW 330i", ReadyModels.findByName("BMW 330i").getName());
        assertEquals("17'' Standard", ReadyOptions.findWheelsByName("17'' Standard").getName());
        assertEquals("Automatic 8AT", ReadyOptions.findTransmissionByName("Automatic 8AT").getName());
        assertEquals("Sport Leather Standard", ReadyOptions.findSteeringWheelByName("Sport Leather Standard").getName());
        assertEquals("Fabric Graphite", ReadyOptions.findInteriorByName("Fabric Graphite").getName());
    }

    @Test
    void configuratorShouldRejectNullModel() {
        Configurator configurator = new Configurator(null);

        assertThrows(ConfiguratorIllegalArgumentException.ConfiguratorInvalidArgumentException.class, () -> configurator.configure(null, null, null, null));
    }

    @Test
    void configuratorShouldRejectIncompatibleComponent() {
        Configurator configurator = new Configurator(ReadyModels.bmw320i());

        assertThrows(
                CompatibilityException.IncompatibleComponentException.class,
                () -> configurator.configure(null, null, null, ReadyOptions.findInteriorByName("Sport Performance"))
        );
    }
}
