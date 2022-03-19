package zoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for concrete zoo
 */
@Getter
public class Zoo {
    /**
     * List of all animals in the zoo
     */
    private List<AnimalSpecies> zooAnimalSpecies;

    /**
     * Static field for storing state of ALL carnivore animals
     * can be transformed to List for containing states of a concrete type (e.g. Lion)
     */
    @Getter @Setter
    private static AnimalState allCarnivoreState;

    /**
     * Static field for storing state of ALL herbivore animals
     * can be transformed to List for containing states of a concrete type (e. g. Cow)
     */
    @Getter @Setter
    private static AnimalState allHerbivoreState;

    /**
     * Initialization of animals list and default states of animal types
     */
    public Zoo() {

        zooAnimalSpecies = new LinkedList<>();
        allCarnivoreState = AnimalState.CALM;
        allHerbivoreState = AnimalState.CALM;
    }

    /**
     * Method for adding animals to the zoo from the specified JSON file
     *
     * @param configFilePath path to config file with animals info
     * @param configFileType type of config file
     */
    public void addAnimals(String configFilePath, String configFileType) {
        ObjectMapper mapper = getMapper(configFileType);
        File animalsFile = new File(configFilePath);
        try {
            AnimalsDataFile animalsData = mapper.readValue(animalsFile, AnimalsDataFile.class);
            zooAnimalSpecies.addAll(animalsData.getCarnivoreAnimals());
            zooAnimalSpecies.addAll(animalsData.getHerbivoreAnimals());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("File hasn't been parsed or it's not json or xml");
        }
    }

    /**
     * Method for define mapper demand on type of config file.
     *
     * @param configFileType type of config file
     * @return mapper
     */
    private ObjectMapper getMapper(String configFileType) {
        switch (configFileType) {
            case "json":
                return new ObjectMapper();
            case "xml":
                return new XmlMapper();
        }
        return null;
    }

    /**
     * Method for handling user actions with the specified type of animals
     *
     * @param event event to do a certain actions with animals
     * @param animalType type of animals for event
     */
    public void performAction(Events event, AnimalType animalType) {
        updateAllSpeciesCurrentStates();
        switch (animalType) {
            case CARNIVORE:
                for(AnimalSpecies species : zooAnimalSpecies) {
                    if(species instanceof Carnivore) {
                        species.updateState(event);
                    }
                }
                break;
            case HERBIVORE:
                for(AnimalSpecies species : zooAnimalSpecies) {
                    if(species instanceof Herbivore) {
                        species.updateState(event);
                    }
                }
                break;
            default:
                System.out.println("No such animal type in the zoo");
        }
        updateAllSpeciesCurrentStates();
    }

    /**
     * Method for handling user actions with ALL animals
     *
     * @param event event to do a certain actions with ALL animals
     */
    public void performAction(Events event) {
        updateAllSpeciesCurrentStates();
        for(AnimalSpecies species : zooAnimalSpecies) {
            species.updateState(event);
        }
        updateAllSpeciesCurrentStates();
    }

    /**
     * Method for updating current state of ALL animals
     * Used as a way for animals, to be aware of a current state of their type
     */
    private void updateAllSpeciesCurrentStates() {
        for (AnimalSpecies animal : zooAnimalSpecies) {
            animal.updateState(Events.UPDATE_STATE);
        }
    }

    /**
     * NOTE: ONLY A DEBUGGING AND REPRESENTATION METHOD
     * MAY NOT BE USED IN ACTUAL MODEL
     *
     * Used for printing states of concrete animals
     * and states of carnivore/herbivore types in general
     */
    public void printAllStates() {
        System.out.println("\n --- CURRENT ZOO INFO --- ");
        System.out.println("Carnivore state: " + allCarnivoreState);
        System.out.println("Herbivore state: " + allHerbivoreState);
        for (AnimalSpecies animal : zooAnimalSpecies) {
            animal.printDescription();
        }
    }
}
