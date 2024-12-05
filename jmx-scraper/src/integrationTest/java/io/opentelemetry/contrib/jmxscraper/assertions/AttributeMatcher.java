/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.contrib.jmxscraper.assertions;

import java.util.Objects;
import javax.annotation.Nullable;

/** Implements functionality of matching data point attributes. */
public class AttributeMatcher {
  private final String attributeName;
  @Nullable private final String attributeValue;

  /**
   * Create instance used to match data point attribute with any value.
   *
   * @param attributeName matched attribute name
   */
  AttributeMatcher(String attributeName) {
    this(attributeName, null);
  }

  /**
   * Create instance used to match data point attribute with te same name and with the same value.
   *
   * @param attributeName attribute name
   * @param attributeValue attribute value
   */
  AttributeMatcher(String attributeName, @Nullable String attributeValue) {
    this.attributeName = attributeName;
    this.attributeValue = attributeValue;
  }

  /**
   * Return name of data point attribute that this AttributeMatcher is supposed to match value with.
   *
   * @return name of validated attribute
   */
  public String getAttributeName() {
    return attributeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttributeMatcher)) {
      return false;
    }
    AttributeMatcher other = (AttributeMatcher) o;
    // Do not attributeValue into account so AttributeMatcher instances can be stored in collections
    // with guarantee of uniqueness per attribute
    return Objects.equals(attributeName, other.attributeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeName);
  }

  @Override
  public String toString() {
    return attributeValue == null
        ? '{' + attributeName + '}'
        : '{' + attributeName + '=' + attributeValue + '}';
  }

  /**
   * Verify if this matcher is matching provided attribute value. If this matcher holds null value
   * then it is matching any attribute value.
   *
   * @param value a value to be matched
   * @return true if this matcher is matching provided value, false otherwise.
   */
  boolean matchesValue(String value) {
    return (attributeValue == null) || Objects.equals(attributeValue, value);
  }
}
