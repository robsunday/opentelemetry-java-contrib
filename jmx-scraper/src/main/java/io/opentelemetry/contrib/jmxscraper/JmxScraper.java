/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.contrib.jmxscraper;

import io.opentelemetry.contrib.jmxscraper.config.ConfigurationException;
import io.opentelemetry.contrib.jmxscraper.config.JmxScraperConfig;
import io.opentelemetry.contrib.jmxscraper.config.JmxScraperConfigFactory;
import io.opentelemetry.contrib.jmxscraper.jmx.JmxClient;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JmxScraper {
  private static final Logger logger = Logger.getLogger(JmxScraper.class.getName());
  private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
  private final JmxScraperConfig config;

  JmxScraper(JmxScraperConfig config) {
    this.config = config;

    try {
      @SuppressWarnings("unused") // TODO: Temporary
      JmxClient jmxClient = new JmxClient(config);
    } catch (MalformedURLException e) {
      throw new ConfigurationException("Malformed serviceUrl: ", e);
    }
  }

  @SuppressWarnings("FutureReturnValueIgnored") // TODO: Temporary
  private void start() {
    exec.scheduleWithFixedDelay(
        () -> {
          logger.fine("JMX scraping triggered");
          //            try {
          //              runner.run();
          //            } catch (Throwable e) {
          //              logger.log(Level.SEVERE, "Error gathering JMX metrics", e);
          //            }
        },
        0,
        config.getIntervalMilliseconds(),
        TimeUnit.MILLISECONDS);
    logger.info("JMX scraping started");
  }

  private void shutdown() {
    logger.info("Shutting down JmxScraper and exporting final metrics.");
    exec.shutdown();
  }

  /**
   * Main method to create and run a {@link JmxScraper} instance.
   *
   * @param args - must be of the form "-config {jmx_config_path,'-'}"
   */
  public static void main(String[] args) {
    JmxScraperConfigFactory factory = new JmxScraperConfigFactory();
    JmxScraperConfig config = factory.createConfigFromArgs(Arrays.asList(args));

    JmxScraper jmxScraper = new JmxScraper(config);
    jmxScraper.start();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread() {
              @Override
              public void run() {
                jmxScraper.shutdown();
              }
            });
  }
}
