
package com.ekkelenkamp.netatmo2wow;

import com.ekkelenkamp.netatmo2wow.model.Measures;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cli {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NetatmoHttpClient.class);

    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
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


        option = new Option("t", "timeperiod", true, "timeperiod to retrieve observations from current time minus timeperiod in milliseconds.");
        option.setRequired(false);
        options.addOption(option);


    }

    public void parse() {



        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) help();

           run();

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties", e);
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
        NetatmoDownload download = new NetatmoDownload();

        try {
            List<Measures> measures = download.downloadCsvData(cmd.getOptionValue("e"), cmd.getOptionValue("p"), cmd.getOptionValue("c"), cmd.getOptionValue("s"), cmd.getOptionValue("t"));
            logger.info("Number of measurements read: " + measures.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

