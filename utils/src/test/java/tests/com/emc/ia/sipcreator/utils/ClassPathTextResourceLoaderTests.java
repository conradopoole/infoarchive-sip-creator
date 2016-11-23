/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.function.Function;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.ClassPathTextResourceLoader;
import com.emc.ia.sipcreator.utils.TextResourceLoader;

public class ClassPathTextResourceLoaderTests {

  private final Function<String, String> loader = new ClassPathTextResourceLoader(getClass());

  @Test(expected = IllegalArgumentException.class)
  public void createWithNullThrowException() {
    new ClassPathTextResourceLoader(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNonDirectoryThrowException() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(false);
    new TextResourceLoader(file);
  }

  @Test
  public void applyRelativePathReturnContentOfAdotText() {
    assertEquals("2 Cs. CC", loader.apply("c.txt"));
  }

  @Test
  public void applyAbsolutePathReturnContentOfBdotText() {
    assertEquals("5 Bs. BBBBB", loader.apply("/b.txt"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void applyNonExistingThrowException() {
    loader.apply("nonExisting.txt");
  }

  @Test
  public void toStringReturnDescriptiveString() {
    String text = loader.toString();
    assertTrue(text.contains(ClassPathTextResourceLoader.class.getSimpleName()));
    assertTrue(text.contains(getClass().getCanonicalName()));
  }
}
