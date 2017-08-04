package com.weatherpred.util;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.dto.InputFeaturesDTO;
import com.weatherpred.utils.constants.Constants;

public class CmdLineHelper {

	private final static Logger logger = LoggerFactory
			.getLogger(CmdLineHelper.class);

	private String[] args = null;
	private Options options = new Options();
	private InputFeaturesDTO inputFeatures = new InputFeaturesDTO();

	public CmdLineHelper(String[] args) {

		this.args = args;

		options.addOption(Constants.LAT, true, Constants.LATITUDE);
		options.addOption(Constants.LONG, true, Constants.LONGITUDE);
		options.addOption(Constants.ELE, true, Constants.ELEVATION);
		options.addOption(Constants.TIME, true, Constants.UNIX_TIME);
		options.addOption(Constants.OUT, true, Constants.OUTPUT_LOCATION);
		options.addOption(Constants.HELP, false, Constants.HELP);

	}

	public InputFeaturesDTO parse() {
		CommandLineParser parser = new BasicParser();
		CommandLine cmdline = null;

		try {
			cmdline = parser.parse(options, args);

			if (cmdline.hasOption(Constants.HELP))
				help();

			inputFeatures.setLatitude(Double.parseDouble(cmdline
					.getOptionValue(Constants.LAT)));
			inputFeatures.setLongitude(Double.parseDouble(cmdline
					.getOptionValue(Constants.LONG)));
			inputFeatures.setElevation(Double.parseDouble(cmdline
					.getOptionValue(Constants.ELE)));
			inputFeatures.setUnixTime(cmdline
					.getOptionValue(Constants.TIME));
			inputFeatures
					.setOutLocation(cmdline.getOptionValue(Constants.OUT));
			return inputFeatures;
		} catch (ParseException e) {
			logger.error("Failed to parse command line properties", e);
			help();
			return null;

		}

	}

	private void help() {
		// This prints out help
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Options", options);
		}
}
