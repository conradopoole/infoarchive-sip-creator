/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class FileSystemTestBase extends PluginTestBase {

  protected List<File> createRandomFiles(File dir, int n) throws IOException {
    List<File> list = new ArrayList<>();
    for (int i = 0; i < n; ++i) {
      list.add(createRandomFile(dir, UUID.randomUUID()
        .toString()));
    }
    return list;
  }

  protected File createRandomFile(File dir, String name) throws IOException {
    File file = new File(dir, name);
    try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
      writer.write(randomString());
    }
    return file;
  }

}
