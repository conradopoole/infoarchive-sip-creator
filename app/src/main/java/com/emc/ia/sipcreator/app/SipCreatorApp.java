/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.HouseKeeper;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.impl.ExecutionPlan;
import com.emc.ia.sipcreator.impl.SimpleExecutionPlan;
import com.emc.ia.sipcreator.plugins.steps.flow.SequentialStep;
import com.emc.ia.sipcreator.plugins.uriresolver.DefaultURIResolver;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.HouseKeeperImpl;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.runtime.plugins.PluginUtils;
import com.emc.ia.sipcreator.spi.PluginRegistry;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;
import com.emc.ia.sipcreator.utils.ConfigUtilsImpl;

public class SipCreatorApp implements Callable<Result> {

  private final Config config;
  private final ConfigUtils configUtils;
  private final RuntimeVariableFactory variableFactory;

  public SipCreatorApp(Config config, RuntimeVariableFactory variableFactory, Function<String, String> loader) {
    this.config = config;
    this.variableFactory = variableFactory;
    configUtils = new ConfigUtilsImpl(variableFactory, loader);
  }

  @Override
  public Result call() {
    final ApplicationState state = createApplicationState();

    defineContext(state);

    final ExecutionPlan executionPlan = createExecutionPlan(state);
    executionPlan.run();

    ((HouseKeeperImpl)state.getObject(HouseKeeper.class, "housekeeper")).close();

    return Result.of(ReturnCodes.SUCCESS);
  }

  private void defineContext(ApplicationState state) {
    Config cfg = state.getConfiguration();
    Config initGroup = configUtils.getOptionalGroup(cfg, "init");
    if (initGroup != null) {
      PluginRegistry plugins = state.getPlugins();
      configUtils.forEach(initGroup, it -> {
        Object object = PluginUtils.create(null, configUtils.getSingleChildGroup(it), state, plugins);
        state.setObject(it.getName(), object);
      });
    }
  }

  private ExecutionPlan createExecutionPlan(ApplicationState state) {
    Config cfg = state.getConfiguration();
    PluginRegistry plugins = state.getPlugins();
    Config main = configUtils.getGroup(cfg, "main");

    List<Step> steps = new ArrayList<Step>();
    configUtils.forEach(main, it -> steps.add(PluginUtils.create(Step.class, it, state, plugins)));
    Step step = new SequentialStep(steps);
    return new SimpleExecutionPlan(step);
  }

  private ApplicationState createApplicationState() {
    ApplicationState state = new ApplicationState(config, new DefaultPluginRegistry());
    state.setObject("configUtils", configUtils);
    state.setObject("variableFactory", variableFactory);
    state.setObject("housekeeper", new HouseKeeperImpl());
    state.setObject("uriresolver.default", new DefaultURIResolver());

    return state;
  }
}
