/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.utils.Params;

import au.com.bytecode.opencsv.CSVReader;

public class CsvConfig {

  public static final Consumer<PluginDocumentationBuilder> DOCUMENTATION = new Consumer<PluginDocumentationBuilder>() {

    @Override
    public void accept(PluginDocumentationBuilder builder) {
      builder.param(PARAM_FILE, RuntimeVariable.class, "The CSV file to load")
        .param(PARAM_CHARSET, RuntimeVariable.class, "The character set of the CSV file.", DEFAULT_CHARSET)
        .param(PARAM_SEPARATOR, String.class, "The CSV separator character.", DEFAULT_SEPARATOR)
        .param(PARAM_QUOTE, String.class, "The CSV quote character.", DEFAULT_QUOTE)
        .param(PARAM_ESCAPE, String.class, "The CSV escape character.", DEFAULT_ESCAPE)
        .param(PARAM_URIRESOLVER, String.class, "The name of a URIResolver.", DEFAULT_URIRESOLVER);
    }
  };

  private static final String PARAM_ESCAPE = "escape";
  private static final String PARAM_QUOTE = "quote";
  private static final String PARAM_SEPARATOR = "separator";
  private static final String PARAM_CHARSET = "charset";
  private static final String DEFAULT_SEPARATOR = ",";
  private static final String DEFAULT_CHARSET = "UTF-8";
  private static final String DEFAULT_QUOTE = "\"";
  private static final String DEFAULT_ESCAPE = "\\";
  private static final String PARAM_FILE = "file";
  private static final String DEFAULT_URIRESOLVER = "uriresolver.default";
  private static final String PARAM_URIRESOLVER = "uriresolver";

  private final char separator;
  private final char quote;
  private final char escape;
  private final RuntimeVariable charsetVariable;
  private final RuntimeVariable fileVariable;
  private final URIResolver resolver;

  public CsvConfig(char separator, char quote, char escape, RuntimeVariable charsetVariable,
      RuntimeVariable fileVariable, URIResolver resolver) {
    Params.notNull(fileVariable, "CSVForEachRowStep.fileVariable");
    Params.notNull(resolver, "CSVForEachRowStep.uriResolver");
    Params.notNull(charsetVariable, "CSVForEachRowStep.charsetVariable");

    this.separator = separator;
    this.quote = quote;
    this.escape = escape;
    this.charsetVariable = charsetVariable;
    this.fileVariable = fileVariable;
    this.resolver = resolver;
  }

  public char getSeparator() {
    return separator;
  }

  public char getQuote() {
    return quote;
  }

  public char getEscape() {
    return escape;
  }

  public RuntimeVariable getFileVariable() {
    return fileVariable;
  }

  public URIResolver getResolver() {
    return resolver;
  }

  public RuntimeVariable getCharsetVariable() {
    return charsetVariable;
  }

  public CSVReader reader(RuntimeState context) throws IOException {
    String path = fileVariable.getValue(context);
    String charsetSpec = charsetVariable.getValue(context);
    Charset charset = Charset.forName(charsetSpec);
    return new CSVReader(new InputStreamReader(resolver.open(path), charset), separator, quote, escape);
  }

  public static CsvConfig fromConfig(String pluginName, PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    RuntimeVariable fileVariable = configUtils.getRuntimeVariable(context, PARAM_FILE);
    RuntimeVariable charsetVariable = configUtils.getOptionalRuntimeVariable(context, PARAM_CHARSET, DEFAULT_CHARSET);
    char separator = getChar(configUtils, pluginName, context, PARAM_SEPARATOR, DEFAULT_SEPARATOR);
    char quote = getChar(configUtils, pluginName, context, PARAM_QUOTE, DEFAULT_QUOTE);
    char escape = getChar(configUtils, pluginName, context, PARAM_ESCAPE, DEFAULT_ESCAPE);

    URIResolver resolver = context.getObject(URIResolver.class,
        StringUtils.defaultString(configUtils.getString(context, PARAM_URIRESOLVER), DEFAULT_URIRESOLVER));

    return new CsvConfig(separator, quote, escape, charsetVariable, fileVariable, resolver);
  }

  private static char getChar(ConfigUtils configUtils, String pluginName, PluginContext context, String param,
      String defaultValue) {
    String string = configUtils.getOptionalString(context, param, defaultValue);
    if (string.length() == 1) {
      return string.charAt(0);
    } else {
      throw new IllegalArgumentException("For plugin " + pluginName + " parameter " + param
          + " should be a single character but was \"" + string + "\"");
    }
  }

  @Override
  public String toString() {
    return "CsvConfig [separator=" + separator + ", quote=" + quote + ", escape=" + escape + ", charsetVariable="
        + charsetVariable + ", fileVariable=" + fileVariable + ", resolver=" + resolver + "]";
  }

}
