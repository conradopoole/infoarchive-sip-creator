/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

public final class RuntimeVariables {

  public static final String SIP_MANIFEST_FILE = "sip_manifest_file";
  public static final String SIP_PDI_FILE = "sip_pdi_file";
  public static final String SIP_PDI_FILE_IS_LAST = "sip_pdi_file_is_last";
  public static final String SIP_CI_REFERENCES = "sip_ci_references";
  public static final String SIP_FILE_NAME = "sip_file_name";
  public static final String SIP_MANIFEST_MODEL = "sip_manifest_model";

  public static final String SIP_COUNT = "sip_count";
  public static final String DSS_COUNT = "dss_count";
  public static final String SIP_IN_DSS_COUNT = "sip_in_dss_count";

  private RuntimeVariables() {
    throw new IllegalStateException(
        "RuntimeVariables is a static utility class and its constructor should never be called.");
  }
}
