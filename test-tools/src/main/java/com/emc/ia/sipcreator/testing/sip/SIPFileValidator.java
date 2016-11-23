/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.testing.sip;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import com.emc.ia.sdk.support.io.ByteArrayInputOutputStream;
import com.emc.ia.sdk.support.io.RepeatableInputStream;
import com.emc.ia.sdk.support.xml.XmlUtil;

public class SIPFileValidator {

  private final ZipFile zipFile;
  private Class<?> parent;

  public SIPFileValidator(Class<?> parent, File file) throws ZipException, IOException {
    this.parent = parent;
    this.zipFile = new ZipFile(file);
  }

  public SIPFileValidator(File file) throws ZipException, IOException {
    this(SIPFileValidator.class, file);
  }

  public SIPFileValidator assertFileCount(int count) {
    assertEquals("Entries in sip file.", count, zipFile.size());
    return this;
  }

  public SIPFileValidator assertPackagingInformation(int aiuCount) throws IOException {
    ZipEntry entry = zipFile.getEntry("eas_sip.xml");
    Assert.assertNotNull("Missing Packaging Information", entry);

    try (ByteArrayInputOutputStream packagingInformation = new ByteArrayInputOutputStream();
        InputStream in = zipFile.getInputStream(entry)) {
      IOUtils.copy(in, packagingInformation);
      Element sipElement =
          assertValidXml(packagingInformation.getInputStream(), "PackagingInformation", "sip.xsd").getDocumentElement();
      String actualAiuCount = XmlUtil.getFirstChildElement(sipElement, "aiu_count")
        .getTextContent();
      assertEquals("# AIUs", aiuCount, Integer.parseInt(actualAiuCount));
    }

    return this;
  }

  public SIPFileValidator assertPreservationInformationIdenticalTo(String resource) throws IOException {
    ZipEntry entry = zipFile.getEntry("eas_pdi.xml");
    Assert.assertNotNull("Missing Preservation Information", entry);

    try (InputStream expectedIn = parent.getResourceAsStream(resource);
        InputStream actualIn = zipFile.getInputStream(entry)) {

      Diff myDiff = DiffBuilder.compare(Input.fromStream(expectedIn))
        .withTest(Input.fromStream(actualIn))
        .checkForSimilar()
        .ignoreWhitespace()
        .ignoreComments()
        .build();
      Assert.assertFalse("XML similar " + myDiff.toString(), myDiff.hasDifferences());

    }
    return this;
  }

  public SIPFileValidator assertPreservationInformationIdenticalToText(String text) throws IOException {
    ZipEntry entry = zipFile.getEntry("eas_pdi.xml");
    Assert.assertNotNull("Missing Preservation Information", entry);

    try (InputStream actualIn = zipFile.getInputStream(entry)) {
      Diff myDiff = DiffBuilder.compare(Input.fromString(text))
        .withTest(Input.fromStream(actualIn))
        .checkForSimilar()
        .ignoreWhitespace()
        .ignoreComments()
        .build();
      Assert.assertFalse("XML similar " + myDiff.toString(), myDiff.hasDifferences());

    }
    return this;
  }

  protected Document assertValidXml(InputStream stream, String humanFriendlyDocumentType, String schema)
      throws IOException {
    RepeatableInputStream repetableStream = new RepeatableInputStream(stream);
    XmlUtil.validate(repetableStream.get(), getClass().getResourceAsStream("/" + schema), humanFriendlyDocumentType);
    return XmlUtil.parse(repetableStream.get());
  }

  public SIPFileValidator assertContentFileIdenticalTo(String referenceInformation, String resource)
      throws IOException {
    ZipEntry entry = zipFile.getEntry(referenceInformation);
    Assert.assertNotNull("Missing Digital Object", entry);
    try (InputStream expectedIn = parent.getResourceAsStream(resource);
        InputStream actualIn = zipFile.getInputStream(entry)) {
      Assert.assertTrue("The DigitalObject " + referenceInformation + " is not the expected one.",
          IOUtils.contentEquals(expectedIn, actualIn));
    }
    return this;
  }

  public SIPFileValidator dumpPreservationInformation() throws IOException {
    ZipEntry entry = zipFile.getEntry("eas_pdi.xml");
    Assert.assertNotNull("Missing Preservation Information", entry);

    try (InputStream actualIn = zipFile.getInputStream(entry)) {
      IOUtils.copy(actualIn, System.out);
    }
    return this;
  }

}
