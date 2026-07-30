package srpmixins.config;

import srpmixins.SRPMixins;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Inspired by fonnymunkey RLMixins, altered
public class EarlyConfigReader {
	private static File configFile = null;
	private static String configBooleanString = null;
	private static String configIntString = null;
	private static String configDoubleString = null;
	private static Map<String,Boolean> configArrayFilledMap = null;

	public static boolean getBoolean(String name, boolean defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configBooleanString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					//All lines starting with "B:"
					configBooleanString = stream.filter(s -> s.trim().startsWith("B:")).collect(Collectors.joining());
				} catch (Exception e) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + e);
				}
			} else configBooleanString = "";
		}

		if (configBooleanString.contains("B:\"" + name + "\"="))
			return configBooleanString.contains("B:\"" + name + "\"=true");
		//If config is not generated yet or missing entries, we use the default value that would be written into it
		else return defaultValue;
	}

	public static int getInt(String name, int defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configIntString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configIntString = stream.filter(s -> s.trim().startsWith("I:")).collect(Collectors.joining());
				} catch (Exception ex) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			} else configIntString = "";
		}

		if (configIntString.contains("I:\"" + name + "\"=")) {
			int index = configIntString.indexOf("I:\"" + name + "\"=");
			try {
				Matcher matcher = Pattern.compile("(\\d+)").matcher(configIntString.substring(index));
				matcher.find();
				return Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				SRPMixins.LOGGER.error(SRPMixins.NAME + ": Failed to parse int config "+ name + ", " + e);
				return 0;
			}
		}
		//If config is not generated yet or missing entries, we use the default value that will get written into it right after this
		else return defaultValue;
	}

	public static double getDouble(String name, double defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configDoubleString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configDoubleString = stream.filter(s -> s.trim().startsWith("D:")).collect(Collectors.joining());
				} catch (Exception ex) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			} else configDoubleString = "";
		}

		if (configDoubleString.contains("D:\"" + name + "\"=")) {
			int index = configDoubleString.indexOf("D:\"" + name + "\"=");
			try {
				int startindex = configDoubleString.indexOf("=", index)+1;
				int endindex = configDoubleString.indexOf("D:", startindex);
				return Double.parseDouble(configDoubleString.substring(startindex, endindex == -1 ? configDoubleString.length() : endindex).trim());
			} catch (Exception e) {
                SRPMixins.LOGGER.error(SRPMixins.NAME + ": Failed to parse double config {}, {}", name, e);
				return 0;
			}
		}
		//If config is not generated yet or missing entries, we use the default value that will get written into it right after this
		else return defaultValue;
	}
	
	public static boolean isArrayFilled(String name, boolean filledByDefault) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configArrayFilledMap == null) {
			configArrayFilledMap = new HashMap<>();
			if (configFile.exists() && configFile.isFile()) {
				try {
					String allLines = Files.lines(configFile.toPath()).collect(Collectors.joining("\t")); //whitespace \s doesnt match \n here for whatever reason, otherwise i could easily use that for the config name special character
					Matcher matcher = Pattern.compile("[SID]:\"([^\t]+)\" <([^>]*)>").matcher(allLines); //regex matches any config lines with any < ... > entry and a name allowing anything except tabspace (\t). had to use some special character no one would use in config name. idk if this one was a good one
					while (matcher.find())
                        configArrayFilledMap.put(matcher.group(1), !matcher.group(2).matches("\\s*")); //save config name and whether its empty (only whitespace) or not
				} catch (Exception ex) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			}
		}

        return configArrayFilledMap.getOrDefault(name, filledByDefault);
	}
}