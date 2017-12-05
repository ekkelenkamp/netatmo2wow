
package com.ekkelenkamp.netatmo2wow;

import com.ekkelenkamp.netatmo2wow.model.Measures;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class Cli {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Cli.class);
    private String[] args = null;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    private CommandLine cmd = null;
    private NetatmoHttpClient netatmoHttpClient = new NetatmoHttpClientImpl();

    public Cli(String[] args) {

        this.args = args;
        Option option = new Option("h", "help", false, "show help.");
        option.setRequired(false);
        options.addOption(option);

        OptionGroup group = new OptionGroup();
        option = new Option("c", "clientid", true, "Client id of netatmo application. See: https://dev.netatmo.com/dev/createapp");
        option.setRequired(false);
        options.addOption(option);

        option = new Option("s", "secret", true, "Client secret of netatmo application. See: https://dev.netatmo.com/dev/createapp");
        option.setRequired(false);
        options.addOption(option);

        option = new Option("e", "email", true, "Email of netatmo account. See: https://auth.netatmo.com/access/signup");
        option.setRequired(false);
        options.addOption(option);

        option = new Option("p", "password", true, "Password of netatmo account. See: https://auth.netatmo.com/access/signup");
        option.setRequired(false);
        options.addOption(option);


        option = new Option("t", "timeperiod", true, "timeperiod to retrieve observations from current time minus timeperiod in seconds.");
        option.setRequired(false);
        options.addOption(option);


        option = new Option("i", "siteid", true, "siteid of the WOW site to upload data to.");
        option.setRequired(false);
        options.addOption(option);

        option = new Option("a", "awspin", true, "AWS pin of the WOW site to upload data to.");
        option.setRequired(false);
        options.addOption(option);

    }

    public void parse() {

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) help();
           run();

        } catch (ParseException e) {
            logger.info("Error parsing command line parameters.", e);
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);
        System.exit(0);
    }

    private void run() {
        long previousTimestepRead = 0;

        Preferences prefs = Preferences.userNodeForPackage(Cli.class);
        // Preference key name
        final String PREF_NAME = "last_timestep";
        String propertyValue = prefs.get(PREF_NAME, "0");
        previousTimestepRead = Long.parseLong(propertyValue);

        logger.debug("Previous time was: " + new java.util.Date(previousTimestepRead));
        NetatmoDownload download = new NetatmoDownload(netatmoHttpClient);
        WowUpload upload = new WowUpload(previousTimestepRead);
        try 
        {
            List<Measures> measures = download.downloadMeasures(cmd.getOptionValue("e"), cmd.getOptionValue("p"), cmd.getOptionValue("c"), cmd.getOptionValue("s"), cmd.getOptionValue("t"));
            logger.info("Number of Netatmo measurements read: " + measures.size());
            if (measures.size() > 0) 
            {
                logger.debug("First measurement: " + measures.get(0));
                logger.debug("Last measurement: " + measures.get(measures.size() - 1));
            }
            WowUpload wowClient = new WowUpload(previousTimestepRead);
            long lastTimestepRed = wowClient.upload(measures, cmd.getOptionValue("i"), Integer.parseInt(cmd.getOptionValue("a")));
            prefs.put(PREF_NAME, "" + lastTimestepRed);

        } 
        catch (IOException e) 
        {
            throw new RuntimeException(e);
        } 
        catch (Exception e) 
        {
        	logger.info(e.getStackTrace());
            throw new RuntimeException(e);
        }

    }
}

