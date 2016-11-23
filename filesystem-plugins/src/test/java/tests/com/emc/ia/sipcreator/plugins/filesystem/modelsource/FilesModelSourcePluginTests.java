/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.filesystem.modelsource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;

import tests.com.emc.ia.sipcreator.plugins.filesystem.FileSystemTestBase;

public class FilesModelSourcePluginTests extends FileSystemTestBase {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("files"));

    chk.description("Creates models by traversing the filesystem.");
    chk.param("directory", RuntimeVariable.class, "The directory to start the filesystem traversal from.");
    chk.param("enrich", EnrichModelFromDomainObject.class, "Model enrichers.");

  }

  @Test
  public void runExecuteStepsForEachRow() throws IOException {
    File root = folder.newFolder("outputdir");

    List<File> files = createRandomFiles(root, 3);

    PluginContext context = context("filesmodel.yml", "files");
    RuntimeState state = state("directory", root.getAbsolutePath());
    try (ModelSource source = newInstance(ModelSource.class, "files", context)) {
      Iterator<Model> models = source.getModels(state);

      check(models, files);
      check(models, files);
      check(models, files);
      assertFalse(models.hasNext());
    }
  }

  private void check(Iterator<Model> models, List<File> files) {
    assertTrue(models.hasNext());
    Model model = models.next();

    File file = files.stream()
      .filter(x -> model.get("name")
        .equals(x.getName()))
      .findFirst()
      .get();

    assertEquals(file.getName(), model.get("name"));
    assertEquals(file.length(), model.get("size"));
    assertEquals(file.getAbsolutePath(), model.get("absolutepath"));
  }

}
