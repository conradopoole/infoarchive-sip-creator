/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.app;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public class CommandLineOptions {

  private File configDirectory;
  private File configFile;

  private Map<String, String> settings;

  private Optional<File> settingsFile;

  private String batchId;

  public Map<String, String> getSettings() {
    return settings;
  }

  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }

  public File getConfigFile() {
    return configFile;
  }

  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

  public Optional<File> getSettingsFile() {
    return settingsFile;
  }

  public void setSettingsFile(Optional<File> settingsFile) {
    this.settingsFile = settingsFile;
  }

  public File getConfigDirectory() {
    return configDirectory;
  }

  public void setConfigDirectory(File configDirectory) {
    this.configDirectory = configDirectory;
  }

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  @Override
  public String toString() {
    return "CommandLineOptions [configDirectory=" + configDirectory + ", configFile=" + configFile + ", settings="
        + settings + ", settingsFile=" + settingsFile + ", batchId=" + batchId + "]";
  }

}
