/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.contrib.jmxscraper.assertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class implementing convenience static methods to construct data point attribute matchers
 * and sets of matchers.
 */
public class DataPointAttributes {
  private DataPointAttributes() {}

  /**
   * Create instance of matcher that should be used to check if data point attribute with given name
   * has value identical to the one provided as a parameter (exact match).
   *
   * @param name name of the data point attribute to check
   * @param value expected value of checked data point attribute
   * @return instance of matcher
   */
  public static AttributeMatcher attribute(String name, String value) {
    return new AttributeMatcher(name, value);
  }

  /**
   * Create instance of matcher that should be used to check if data point attribute with given name
   * exists. Any value of the attribute is considered as matching (any value match).
   *
   * @param name name of the data point attribute to check
   * @return instance of matcher
   */
  public static AttributeMatcher attributeWithAnyValue(String name) {
    return new AttributeMatcher(name);
  }

  /**
   * Create a group of attribute matchers that should be used to verify list of data point
   * attributes.
   *
   * @param attributes list of matchers to create group from. It must contain matchers with unique
   *     names.
   * @return list of unique attribute matchers
   * @throws IllegalArgumentException if provided list contains two or more matchers with the same
   *     name.
   * @see MetricAssert#hasDataPointsWithAttributes(Collection[]) for detailed description of the
   *     algorithm used for matching
   */
  public static List<AttributeMatcher> attributeSet(AttributeMatcher... attributes) {
    List<AttributeMatcher> matcherList = new ArrayList<>(attributes.length);
    Set<String> registeredAttributeMatcherNames = new HashSet<>(attributes.length);

    for (AttributeMatcher attributeMatcher : attributes) {
      if (registeredAttributeMatcherNames.contains(attributeMatcher.getAttributeName())) {
        throw new IllegalArgumentException(
            "Duplicate matcher with name: " + attributeMatcher.getAttributeName());
      }
      registeredAttributeMatcherNames.add(attributeMatcher.getAttributeName());
      matcherList.add(attributeMatcher);
    }

    return matcherList;
  }
}
