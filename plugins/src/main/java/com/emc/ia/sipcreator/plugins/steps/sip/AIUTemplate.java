/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import com.emc.ia.sdk.sip.assembly.ContentInfo;
import com.emc.ia.sdk.sip.assembly.FixedHeaderAndFooterTemplate;
import com.emc.ia.sipcreator.api.AIU;

public class AIUTemplate extends FixedHeaderAndFooterTemplate<AIU> {

  public AIUTemplate(InputStream header, InputStream footer) {
    super(header, footer);
  }

  public AIUTemplate(String header, String footer) {
    super(header, footer);
  }

  @Override
  public void writeRow(AIU domainObject, Map<String, ContentInfo> contentInfo, PrintWriter writer) throws IOException {
    // TODO: how to handle deduplicated content?
    domainObject.writeAsXml(writer);
  }

}
