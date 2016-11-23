/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.filesystem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.emc.ia.sipcreator.api.FilesCollection;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;

public class FilesStepPluginTests extends FileSystemTestBase {

  private static final String FILECOLLECTION = "filecollection";
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(FILECOLLECTION));
    chk.name(FILECOLLECTION);
    chk.description("Creates / updates a collection of files");
    chk.param("files", RuntimeVariable.class, "The files to add to the collection", "", true);
    chk.param("variable", String.class, "The name of the variable that holds the file collection", "files");
  }

  @Test
  public void runExecuteStepsForEachRow() throws IOException {
    File root = folder.newFolder("outputdir");

    List<File> files = new ArrayList<>();
    files.add(createRandomFile(root, "a.txt"));
    files.add(createRandomFile(root, "b.txt"));
    files.add(createRandomFile(root, "c.txt"));

    PluginContext context = context("files.yml", FILECOLLECTION);
    RuntimeState state = state("directory", root.getAbsolutePath());
    Step step = newInstance(Step.class, FILECOLLECTION, context);

    step.run(state);

    FilesCollection collection = (FilesCollection)state.get("files");

    Iterator<File> iterator = collection.iterator();

    assertTrue(files.remove(iterator.next()
      .getAbsoluteFile()));
    assertTrue(files.remove(iterator.next()
      .getAbsoluteFile()));
    assertTrue(files.remove(iterator.next()
      .getAbsoluteFile()));
    assertFalse(iterator.hasNext());
  }
}
