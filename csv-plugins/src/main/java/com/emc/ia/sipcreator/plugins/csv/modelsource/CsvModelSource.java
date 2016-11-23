/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv.modelsource;

import org.apache.commons.io.IOUtils;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.csv.CsvConfig;

import au.com.bytecode.opencsv.CSVReader;

public class CsvModelSource implements ModelSource {

  private final CsvConfig config;
  private CSVReader reader;

  public CsvModelSource(CsvConfig config) {
    this.config = config;
  }

  @Override
  public CloseableIterator<Model> getModels(RuntimeState state) {
    try {
      if (reader != null) {
        close();
      }
      reader = config.reader(state);
      final String[] header = reader.readNext();
      return new ModelFromCSV(header, reader, new SimpleModelImpl());
    } catch (Exception e) {
      close();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    IOUtils.closeQuietly(reader);
    reader = null;
  }

  @Override
  public String toString() {
    return "CsvModelSource [config=" + config + ", reader=" + reader + "]";
  }

}
