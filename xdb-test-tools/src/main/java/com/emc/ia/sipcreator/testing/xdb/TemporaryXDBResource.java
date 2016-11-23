/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.testing.xdb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.rules.ExternalResource;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.SAXException;

import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.xhive.XhiveDriverFactory;
import com.xhive.core.XhiveDriver;
import com.xhive.core.interfaces.XhiveClusterFactoryIf;
import com.xhive.core.interfaces.XhiveClusterIf;
import com.xhive.core.interfaces.XhiveDatabaseConfigurationIf;
import com.xhive.core.interfaces.XhiveDatabaseIf;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveDocumentIf;
import com.xhive.dom.interfaces.XhiveLSParserIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.dom.interfaces.XhiveMetadataMapIf;

public class TemporaryXDBResource extends ExternalResource {

  private static final String XHIVE_BOOTSTRAP_DEFAULT_NAME = "XhiveDatabase.bootstrap";
  private static final String DEFAULT_LICENSE = getDefaultDeveloperLicense();
  private static final String DEFAULT_DB = "db";
  private static final String DEFAULT_PASSWORD = "admin";
  private static final int DEFAULT_PORT = 9235;
  private final String databaseName;
  private final String password;
  private final String license;
  private XhiveDriverIf driver;
  private final int port;
  private ServerSocket socket;

  public TemporaryXDBResource() {
    this(DEFAULT_DB, DEFAULT_PASSWORD, DEFAULT_LICENSE);
  }

  private static String getDefaultDeveloperLicense() {
    return System.getProperty("xdb.license");
  }

  public TemporaryXDBResource(int port) {
    this(DEFAULT_DB, DEFAULT_PASSWORD, DEFAULT_LICENSE, port);
  }

  public TemporaryXDBResource(String databaseName, String password, String license) {
    this(databaseName, password, license, DEFAULT_PORT);
  }

  public TemporaryXDBResource(String databaseName, String password, String license, int port) {
    this.databaseName = databaseName;
    this.password = password;
    this.license = license;
    this.port = port;
  }

  @Override
  protected void after() {
    XhiveSessionIf session = driver.createSession();
    connectAsSuperUser(session);
    XhiveClusterIf cluster = session.getCluster();
    cluster.getDataNode(XhiveClusterIf.DEFAULT_DATA_NODE_NAME)
      .shutdown(null);
    session.terminate();
    driver.getListenerThreads()
      .stream()
      .forEach(IOUtils::closeQuietly);
    driver.close();
    driver = null;
    IOUtils.closeQuietly(socket);
    socket = null;
    XhiveDriver.clearCurrent();

    super.after();
  }

  @Override
  protected void before() throws Throwable {
    super.before();

    try {
      createDriver(Files.createTempDir());
      createXDBDatabase();
      startListener();
    } catch (Exception e) {
      Throwables.propagate(e);
    }
  }

  protected void startListener() {
    if (port > 0) {
      try {
        socket = new ServerSocket(port);
        driver.startListenerThread(socket);
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void createXDBDatabase() throws SAXException, IOException {

    XhiveSessionIf session = null;
    try {
      session = driver.createSession();
      connectAsSuperUser(session);

      session.begin();

      final XhiveClusterIf cluster = session.getCluster();

      final XhiveDatabaseConfigurationIf conf = cluster.createDatabaseConfiguration();
      cluster.createDatabase(databaseName, conf, password.toCharArray());

      session.commit();

    } finally {
      terminate(session);
    }

  }

  private void connectAsSuperUser(final XhiveSessionIf session) {
    session.connect("superuser", "secret".toCharArray(), null);

  }

  private void createDriver(final File location) {
    final String xdbBootstrap = getBootstrap(location);
    createXhiveBootstrap(xdbBootstrap);
  }

  private void createXhiveBootstrap(final String xdbBootstrap) {

    final XhiveClusterFactoryIf clusterFactory = XhiveDriverFactory.getClusterFactory();
    clusterFactory.createDataNode(xdbBootstrap, clusterFactory.createDataNodeConfiguration(), "secret".toCharArray());
    driver = getXhiveDriverIf(xdbBootstrap);
    setLicenseKey();
  }

  public static XhiveDriverIf getXhiveDriverIf(final String bootstrap) {
    final XhiveDriverIf newDriver = XhiveDriverFactory.getDriver(bootstrap);
    if (!newDriver.isInitialized()) {
      newDriver.init();
    }
    return newDriver;
  }

  private String getBootstrap(final File location) {
    final File bootstrapDefaultFile = new File(location, XHIVE_BOOTSTRAP_DEFAULT_NAME);
    return bootstrapDefaultFile.getAbsolutePath();
  }

  private void setLicenseKey() {
    XhiveSessionIf session = null;
    try {
      session = driver.createSession();

      connectAsSuperUser(session);
      try {
        session.begin();
        final XhiveClusterIf cluster = session.getCluster();
        cluster.setLicenseKey(license);
        session.commit();
      } finally {
        session.rollback();
      }

    } catch (final Exception e) {
      throw new IllegalArgumentException("An error during XDB license set.", e);
    } finally {
      terminate(session);
    }
  }

  private void terminate(final XhiveSessionIf session) {
    if (session != null) {
      if (session.isOpen()) {
        session.rollback();
      }
      if (session.isConnected()) {
        session.disconnect();
      }
      session.terminate();
    }
  }

  public void add(final String name, final InputStream document, final Map<String, String> vars) throws SAXException {

    XhiveSessionIf session = null;
    try {
      session = getSession();
      session.begin();
      final XhiveDatabaseIf database = session.getDatabase();
      final XhiveLibraryIf root = database.getRoot();
      final XhiveLSParserIf parser = root.createLSParser();
      final LSInput input = root.createLSInput();
      input.setByteStream(document);
      final XhiveDocumentIf doc = parser.parse(input);
      doc.setName(name);
      root.appendChild(doc);
      final XhiveMetadataMapIf metadata = doc.getMetadata();
      metadata.putAll(vars);

      session.commit();
    } finally {
      if (session != null) {
        session.rollback();
        releaseSession(session);
      }
    }
  }

  public XhiveSessionIf getSession() {

    final XhiveSessionIf session = driver.createSession(databaseName);
    session.connect("Administrator", password.toCharArray(), databaseName);
    return session;
  }

  public void releaseSession(final XhiveSessionIf session) {
    session.rollback();
    session.disconnect();
  }
}
