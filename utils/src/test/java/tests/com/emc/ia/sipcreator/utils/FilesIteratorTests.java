/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.utils.FilesIterator;

public class FilesIteratorTests {

  private Predicate<File> inspectFolder;
  private Predicate<File> includeFile;
  private File root;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    inspectFolder = mock(Predicate.class);
    includeFile = mock(Predicate.class);
    root = mock(File.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullInspectFolderThrowException() {
    new FilesIterator(null, includeFile, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullIncludeFileThrowException() {
    new FilesIterator(inspectFolder, null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullRootThrowException() {
    new FilesIterator(inspectFolder, includeFile, null);
  }

  @Test
  public void toStringReturnDescriptiveString() {
    when(root.isFile()).thenReturn(true);
    FilesIterator iterator = new FilesIterator(inspectFolder, includeFile, root);
    String text = iterator.toString();
    assertTrue(text.contains(FilesIterator.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(inspectFolder)));
    assertTrue(text.contains(String.valueOf(includeFile)));
  }

  @Test
  public void iterateFileOnlyIncludeAllReturnFile() {
    when(root.isFile()).thenReturn(true);
    FilesIterator iterator = new FilesIterator(x -> true, x -> true, root);

    assertTrue(iterator.hasNext());
    assertEquals(root, iterator.next());
    assertFalse(iterator.hasNext());

  }

  @Test
  public void iterateFileOnlyIncludeNoneReturnNothing() {
    when(root.isFile()).thenReturn(true);
    FilesIterator iterator = new FilesIterator(x -> true, x -> false, root);
    assertFalse(iterator.hasNext());
  }

  @Test
  public void iterateEmptyDirectoryIncludeAllReturnNothing() {
    when(root.isFile()).thenReturn(false);
    when(root.listFiles()).thenReturn(new File[0]);
    FilesIterator iterator = new FilesIterator(x -> true, x -> false, root);
    assertFalse(iterator.hasNext());
  }

  private File dir(File... contents) {
    File dir = mock(File.class);
    when(dir.isFile()).thenReturn(false);
    when(dir.isDirectory()).thenReturn(true);
    when(dir.listFiles()).thenReturn(contents);
    return dir;
  }

  private File invalidDir() {
    File dir = mock(File.class);
    when(dir.isFile()).thenReturn(false);
    when(dir.isDirectory()).thenReturn(true);
    when(dir.listFiles()).thenReturn(null);
    return dir;
  }

  private File file() {
    File file = mock(File.class);
    when(file.isFile()).thenReturn(true);
    when(file.isDirectory()).thenReturn(false);
    return file;
  }

  @Test
  public void iterateDirectoryStructureIncludeOddReturnFiles() {
    File file1 = file();
    File file2 = file();
    File file3 = file();
    File file4 = file();
    File file5 = file();

    File dir = dir(file1, dir(), file2, dir(file3, dir(file4), file5, invalidDir()));

    FilesIterator iterator = new FilesIterator(x -> true, x -> x == file1 || x == file3 || x == file5, dir);

    Set<File> files = new HashSet<>();
    while (iterator.hasNext()) {
      files.add(iterator.next());
    }
    assertEquals(3, files.size());
    assertTrue(files.contains(file1));
    assertTrue(files.contains(file3));
    assertTrue(files.contains(file5));
  }

  @Test
  public void iterateDirectoryStructureIncludeAllFilesSkipOddDirReturnFiles() {
    File file1 = file();
    File file2 = file();
    File file3 = file();
    File file4 = file();
    File file5 = file();
    File dir3 = dir(file3, dir(file4), file5);

    File dir = dir(file1, dir(), file2, dir3);

    FilesIterator iterator = new FilesIterator(x -> x != dir3, x -> true, dir);

    Set<File> files = new HashSet<>();
    while (iterator.hasNext()) {
      files.add(iterator.next());
    }
    assertEquals(2, files.size());
    assertTrue(files.contains(file1));
    assertTrue(files.contains(file2));
  }
}
