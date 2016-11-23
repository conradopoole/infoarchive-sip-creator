/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.ia.sipcreator.app.CommandLineOptions;
import com.emc.ia.sipcreator.app.SipCreatorAppFactory;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.utils.Fmt;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class DefaultCommandLineInitiator {

  private static final Collection<String> VALID_LOG_LEVELS =
      new HashSet<>(Arrays.asList("DEBUG", "INFO", "WARN", "ERROR"));

  public Callable<Result> parse(String... args) {
    OptionParser parser = new OptionParser();
    parser.accepts("help", "Prints a help message on the optional topic ([major:minor]).")
      .withOptionalArg()
      .withValuesSeparatedBy(":")
      .forHelp();
    parser.accepts("prop", "Define a property (e.g. -pmyproperty=myvalue).")
      .withRequiredArg()
      .ofType(String.class);
    parser.accepts("settings", "Load a set of properties from the specified file.")
      .withRequiredArg();
    parser.accepts("loglevel", "The log level: DEBUG, INFO, WARN, ERROR. Defaults to INFO.")
      .withRequiredArg();
    parser.accepts("logdir", "The directory where the log file will be placed. Defaults to logs.")
      .withRequiredArg();
    parser.accepts("batchid", "The batch id of this run. Is used to distinguish log files etc for specific runs.")
      .withRequiredArg();
    parser.accepts("config", "Specifies the configuration file.")
      .withRequiredArg()
      .defaultsTo("conf.yml");

    OptionSet options = parser.parse(args);

    // Init logging before anything else.
    String logLevel = getLogLevel(options);
    String logDir = getLogDir(options);
    String batchId = getBatchId(options);
    initLogging(logDir, batchId, logLevel);

    welcomeMessage(options, batchId);

    if (options.has("help")) {
      List<String> helpArgs = (List<String>)options.valuesOf("help");

      return createHelpCommand(parser, helpArgs);
    } else {

      String settingsPath = (String)options.valueOf("settings");
      String confPath = String.valueOf(options.valueOf("config"));
      List<?> properties = options.valuesOf("prop");

      CommandLineOptions cfg = new CommandLineOptions();
      File file = getConfigFile(confPath);
      cfg.setConfigDirectory(file.getParentFile());
      cfg.setConfigFile(file);
      cfg.setSettingsFile(asFile(settingsPath));
      cfg.setSettings(asSettings(properties));
      cfg.setBatchId(batchId);
      return SipCreatorAppFactory.fromOptions(cfg);
    }
  }

  private void welcomeMessage(OptionSet options, String batchId) {
    Logger log = LoggerFactory.getLogger(getClass());
    log.info("SIP Creator");
    log.info(StringUtils.repeat('-', 79));
    // TODO: get proper version
    log.info(Fmt.format("Version: {}", "TODO"));
    log.info(Fmt.format("Batch id: {}", batchId));
    log.info(Fmt.format("Command line options: {}.", options.asMap()));
    log.info(Fmt.format("Environment variables: {}.", System.getenv()));
    log.info(Fmt.format("System properties: {}.", System.getProperties()));
    log.info(StringUtils.repeat('=', 79));
  }

  private String getLogLevel(OptionSet options) {
    String level = StringUtils.upperCase(StringUtils.defaultIfBlank((String)options.valueOf("loglevel"), "INFO"));
    if (VALID_LOG_LEVELS.contains(level)) {
      return level;
    } else {
      // TODO: fix
      throw new RuntimeException("Invalid log level");
    }
  }

  private String getBatchId(OptionSet options) {
    return StringUtils.defaultIfBlank((String)options.valueOf("batchid"),
        Long.toString(System.currentTimeMillis(), Character.MAX_RADIX));
  }

  private String getLogDir(OptionSet options) {
    return StringUtils.defaultIfBlank((String)options.valueOf("logdir"), new File("logs").getAbsolutePath());
  }

  private void initLogging(String logDirectory, String batchId, String rootLevel) {
    try {
      LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
      lc.reset();
      lc.putProperty("logging.directory", logDirectory);
      lc.putProperty("logging.batchid", batchId);
      lc.putProperty("logging.rootlevel", rootLevel);

      JoranConfigurator jc = new JoranConfigurator();
      jc.setContext(lc);
      try (InputStream inputStream = getClass().getResourceAsStream("/defaultLoggingConfiguration.xml")) {
        jc.doConfigure(inputStream);
      }
    } catch (Exception e) {
      // TODO: improve me
      throw new RuntimeException(e);
    }
  }

  private Callable<Result> createHelpCommand(OptionParser parser, List<String> args) {
    if (args == null || args.isEmpty()) {
      return new HelpCommand(parser);
    } else if (args.size() == 1) {
      String arg = StringUtils.lowerCase(args.get(0));
      switch (arg) { // NOPMD
        case "plugin":
          return new HelpPlugins(new BriefPluginDocumentationPrinter());
        default:
          return new UnknownHelpTopicCommand(arg);
      }
    } else if (args.size() == 2) {
      String arg = StringUtils.lowerCase(args.get(0));
      switch (arg) { // NOPMD
        case "plugin":
          String plugin = args.get(1);
          return new HelpPluginById(plugin, new FullPluginDocumentationPrinter());
        default:
          return new UnknownHelpTopicCommand(StringUtils.join(args, ' '));
      }
    } else {
      return new UnknownHelpTopicCommand(StringUtils.join(args, ' '));
    }
  }

  private static File getConfigFile(String confPath) {
    File file = new File(confPath);
    return file.getAbsoluteFile();
  }

  private static Map<String, String> asSettings(List<?> properties) {
    Map<String, String> map = new HashMap<>();
    for (Object o : properties) {
      String spec = String.valueOf(o);
      Pattern p = Pattern.compile("([a-zA-Z0-9_\\.\\-]+)=(.*)");
      Matcher m = p.matcher(spec);
      if (m.matches()) {
        String key = m.group(1);
        String value = m.group(2);
        map.put(key, value);
      } else {
        throw new IllegalArgumentException("...");
      }
    }
    return map;
  }

  private static Optional<File> asFile(String confPath) {
    if (StringUtils.isNotBlank(confPath)) {
      return Optional.of(new File(confPath));
    } else {
      return Optional.empty();
    }
  }
}
