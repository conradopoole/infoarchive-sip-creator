/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.emc.ia.sdk.sip.assembly.BatchSipAssembler;
import com.emc.ia.sdk.sip.assembly.FileGenerationMetrics;
import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.MutableFilesCollection;

public class CreateSIPStep implements Step {

  private final AIUSource aiuSource;
  private final BatchSipAssemblerFactory assemblerFactory;
  private final String resultVariable;

  public CreateSIPStep(BatchSipAssemblerFactory assemblerFactory, AIUSource aiuSource, String resultVariable) {
    this.assemblerFactory = assemblerFactory;
    this.aiuSource = aiuSource;
    this.resultVariable = resultVariable;
  }

  @Override
  public void run(RuntimeState state) {
    try {
      BatchSipAssembler<AIU> assembler = assemblerFactory.create(state);
      Iterator<AIU> aius = aiuSource.getAIUs(state);
      while (aius.hasNext()) {
        assembler.add(aius.next());
      }
      assembler.end();

      Collection<FileGenerationMetrics> sipsMetrics = assembler.getSipsMetrics();

      MutableFilesCollection files = new MutableFilesCollection();

      for (FileGenerationMetrics fileMetrics : sipsMetrics) {
        files.add(fileMetrics.getFile());
      }

      state.set(resultVariable, files);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
