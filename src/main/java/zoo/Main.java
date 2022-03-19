package zoo;


import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        Options options = new Options();

        Option configType = new Option("ct", "configtype", true, "config file type");
        configType.setRequired(true);
        options.addOption(configType);

        Option configFile = new Option("cf", "configfile", true, "config file path");
        configFile.setRequired(true);
        options.addOption(configFile);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        //not a good practice, it serves it purpose
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("zoo-simulation", options);
            throw new RuntimeException("Invalid commandline arguments");
        }

        String configFilePath = cmd.getOptionValue(configFile);
        String configFileType = cmd.getOptionValue(configType);

        // Create zoo
        Zoo zoo = new Zoo();
        // Add animals to the zoo
        zoo.addAnimals(configFilePath, configFileType);

        // Create user action trigger
        ActionTrigger trigger = new ActionTrigger(zoo);

        AnimalType herbivore = AnimalType.HERBIVORE;
        AnimalType carnivore = AnimalType.CARNIVORE;

        // All of the following actions are up to users choice
        zoo.printAllStates();
        trigger.setMorning();
        zoo.printAllStates();

        trigger.visit(herbivore);
        zoo.printAllStates();
        trigger.visit(carnivore);
        trigger.feedAnimals(herbivore);
        zoo.printAllStates();

        trigger.setNight();
        zoo.printAllStates();

        trigger.setMorning();
        zoo.printAllStates();

        trigger.setThunder();
        zoo.printAllStates();
        trigger.feedAnimals(carnivore);
        zoo.printAllStates();

        trigger.feedAnimals(herbivore);
        zoo.printAllStates();
        trigger.setNight();
        zoo.printAllStates();

        trigger.setMorning();
        zoo.printAllStates();

        trigger.visit(carnivore);
        zoo.printAllStates();
        trigger.setRain();
        zoo.printAllStates();
    }
}