package com.ekkelenkamp.netatmo2wow;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

public class Main {

    public static void main(String[] args) {
    	
    	BasicConfigurator.configure();
        new Cli(args).parse();
    }
}
