/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.ConstantPredicate;

public class ConstantPredicateTests {

  @Test
  public void testReturnsConstant() {
    assertFalse(ConstantPredicate.of(false)
      .test(new Object()));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    ConstantPredicate<Object> predicate = ConstantPredicate.of(true);
    String text = predicate.toString();
    assertTrue(text.contains(ConstantPredicate.class.getSimpleName()));
    assertTrue(text.contains("true"));
  }

}
