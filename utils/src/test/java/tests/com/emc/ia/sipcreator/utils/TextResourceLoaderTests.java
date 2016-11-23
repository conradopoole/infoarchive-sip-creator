/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.utils.TextResourceLoader;

public class TextResourceLoaderTests {

  private File root;

  @Before
  public void before() {
    root = new File("./src/test" + "/resources");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNullDirectoryThrowException() {
    new TextResourceLoader(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNonDirectoryThrowException() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(false);
    new TextResourceLoader(file);
  }

  @Test
  public void applyRelativePathReturnContentOfAdotText() {
    TextResourceLoader loader = new TextResourceLoader(root);
    assertEquals("10 As. AAAAAAAAAA", loader.apply("a.txt"));
  }

  @Test
  public void applyAbsolutePathReturnContentOfBdotText() {
    TextResourceLoader loader = new TextResourceLoader(root);
    assertEquals("5 Bs. BBBBB", loader.apply(new File(root, "b.txt").getAbsolutePath()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void applyNonExistingThrowException() {
    TextResourceLoader loader = new TextResourceLoader(root);
    loader.apply(new File(root, "nonExisting.txt").getAbsolutePath());
  }

  @Test
  public void toStringReturnDescriptiveString() {
    TextResourceLoader loader = new TextResourceLoader(root);
    String text = loader.toString();
    assertTrue(text.contains(TextResourceLoader.class.getSimpleName()));
    assertTrue(text.contains(root.getAbsolutePath()));
  }

  public static void main(String[] args) throws IOException {
    try (FileOutputStream out = new FileOutputStream("src/test/resources/wtf.txt")) {
      out.write(0x80);
    }
  }
}
