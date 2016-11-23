/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv.modelsource;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.utils.Params;

import au.com.bytecode.opencsv.CSVReader;

public class ModelFromCSV implements CloseableIterator<Model> {

  private final String[] header;
  private final CSVReader csvReader;
  private final Model model;
  private String[] nextRow;

  public ModelFromCSV(String[] header, CSVReader csvReader, Model model) throws IOException {
    Params.notNull(header, "ModelFromCSV.header");
    Params.notTrue(header.length == 0, "ModelFromCSV.header cannot be empty.");
    Params.notNull(csvReader, "ModelFromCSV.csvReader");
    Params.notNull(model, "ModelFromCSV.model");

    this.header = ArrayUtils.clone(header);
    this.csvReader = csvReader;
    this.model = model;

    nextRow = csvReader.readNext();
  }

  private void prepareModel() {
    for (int i = 0; i < nextRow.length; ++i) {
      model.set(header[i], nextRow[i]);
    }
  }

  @Override
  public boolean hasNext() {
    return nextRow != null;
  }

  @Override
  public Model next() {
    try {
      prepareModel();
      nextRow = csvReader.readNext();
      return model;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    IOUtils.closeQuietly(csvReader);

  }

}
