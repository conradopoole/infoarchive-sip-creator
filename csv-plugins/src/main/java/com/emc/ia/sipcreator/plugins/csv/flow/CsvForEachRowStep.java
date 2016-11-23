/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv.flow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.csv.CsvConfig;
import com.emc.ia.sipcreator.utils.Params;

import au.com.bytecode.opencsv.CSVReader;

public class CsvForEachRowStep implements Step {

  private final CsvConfig config;
  private final Step continuation;

  public CsvForEachRowStep(CsvConfig config, Step continuation) {
    Params.notNull(config, "CSVForEachRowStep.config");
    Params.notNull(continuation, "CSVForEachRowStep.continuation");
    this.config = config;
    this.continuation = continuation;
  }

  @Override
  public void run(RuntimeState context) {

    try (CSVReader csvReader = config.reader(context)) {

      String[] nextLine;

      final String[] header = csvReader.readNext();

      Map<String, String> row = new HashMap<String, String>();
      context.set("row", row);

      while ((nextLine = csvReader.readNext()) != null) {
        for (int i = 0; i < header.length; ++i) {
          row.put(header[i], nextLine[i]);
        }
        continuation.run(context);
      }

      // TODO: clear context?
    } catch (IOException e) {
      throw new RuntimeException("Failed to process csv file...", e);
    }

  }

  @Override
  public String toString() {
    return "CsvForEachRowStep [config=" + config + ", continuation=" + continuation + "]";
  }

}
