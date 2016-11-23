/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.impl.SimpleExecutionPlan;

public class SimpleExecutionPlanTests {

  private Step step;
  private SimpleExecutionPlan plan;

  @Before
  public void before() {
    step = mock(Step.class);
    plan = new SimpleExecutionPlan(step);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullStepThrowException() {
    new SimpleExecutionPlan(null);
  }

  @Test
  public void run() {
    plan.run();
    verify(step).run(notNull(RuntimeState.class));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = plan.toString();
    assertTrue(text.contains(SimpleExecutionPlan.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(step)));
  }
}
