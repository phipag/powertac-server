/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.powertac.evcustomer.beans;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powertac.common.config.Configurator;
import org.powertac.common.interfaces.ServerConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


/**
 * @author Govert Buijs
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@DirtiesContext
public class CarTypeTest
{
  private CarType carType;
  private String carName = "TestCar";
  private double maxCapacity = 100.0;
  private double range = 200.0;
  private double homeCharging = 20.0;
  private double awayCharging = 10.0;

  private SimpleConfig configSvc;
  private CompositeConfiguration config;

  @Before
  public void setUp ()
  {
    carType = new CarType(carName);
    configSvc = new SimpleConfig();
  }

  @After
  public void tearDown ()
  {
    carType = null;
  }

  private void initializeConfig ()
  {
    configSvc = new SimpleConfig();
    configSvc.initialize();
  }

  @Test
  public void testInitialization ()
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(carName, carType.getName());
    assertEquals(carType.getMaxCapacity(), maxCapacity, 1E-06);
    assertEquals(carType.getRange(), range, 1E-06);
    assertEquals(carType.getHomeCharging(), homeCharging, 1E-06);
    assertEquals(carType.getAwayCharging(), awayCharging, 1E-06);
  }

  @Test
  public void testCurrentCapacity ()
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    // We assume a new carType has a half full battery
    assertEquals(0.5 * maxCapacity, carType.getCurrentCapacity(), 1E-06);
  }

  @Test
  public void testDischargeValid () throws CarType.ChargeException
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(0.5 * maxCapacity, carType.getCurrentCapacity(), 1E-06);
    carType.discharge(25);
    assertEquals(0.5 * maxCapacity - 25, carType.getCurrentCapacity(), 1E-06);
  }

  @Test(expected = CarType.ChargeException.class)
  public void testDischargeInvalid () throws CarType.ChargeException
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(0.5 * maxCapacity, carType.getCurrentCapacity(), 1E-06);
    carType.discharge(300);
    assertEquals(0.5 * maxCapacity - 300, carType.getCurrentCapacity(), 1E-06);
  }

  @Test
  public void testChargeValid () throws CarType.ChargeException
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(0.5 * maxCapacity, carType.getCurrentCapacity(), 1E-06);
    carType.discharge(50);
    carType.charge(25);
    assertEquals(0.5 * maxCapacity - 25, carType.getCurrentCapacity(), 1E-06);
  }

  @Test(expected = CarType.ChargeException.class)
  public void testChargeInvalid () throws CarType.ChargeException
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(0.5 * maxCapacity, carType.getCurrentCapacity(), 1E-06);
    carType.charge(300);
    assertEquals(0.5 * maxCapacity + 300, carType.getCurrentCapacity(), 1E-06);
  }

  @Test
  public void testNeededCapacity ()
  {
    carType.configure(carName, maxCapacity, range, homeCharging, awayCharging);
    assertEquals(maxCapacity, carType.getNeededCapacity(range), 1E-06);
  }

  @Test
  public void testAutoConfigure ()
  {
    initializeConfig();
    Class<?> carClass;
    try {
      carClass = Class.forName("org.powertac.evcustomer.beans.CarType");
      @SuppressWarnings("unchecked")
      Collection<CarType> cars =
          (Collection<CarType>) configSvc.configureInstances(carClass);
      assertEquals("correct number", 2, cars.size());
      CarType result = null;
      for (CarType car : cars) {
        if (car.getName().equals("Tesla_40_kWh")) {
          result = car;
        }
      }
      assertNotNull("collection contains Tesla", result);
      assertEquals("correct range", 257.6, result.getRange(), 1e-6);
    }
    catch (ClassNotFoundException e) {
      fail("car class not found");
    }
  }

  // ===== Config framework ========
  class SimpleConfig implements ServerConfiguration
  {
    private Configurator configurator;

    SimpleConfig ()
    {
      super();
    }

    void initialize ()
    {
      config = new CompositeConfiguration();
      configurator = new Configurator();
      InputStream stream =
          CarTypeTest.class.getResourceAsStream("/config/test-properties.xml");
      XMLConfiguration xconfig = new XMLConfiguration();
      try {
        xconfig.load(stream);
        config.addConfiguration(xconfig);
        configurator.setConfiguration(config);
      }
      catch (ConfigurationException e) {
        e.printStackTrace();
        fail(e.toString());
      }
    }

    @Override
    public void configureMe (Object target)
    {
      configurator.configureSingleton(target);
    }

    @Override
    public Collection<?> configureInstances (Class<?> target)
    {
      return configurator.configureInstances(target);
    }

    @Override
    public void publishConfiguration (Object target)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void saveBootstrapState (Object thing)
    {
      // TODO Auto-generated method stub
      
    }
    
  }
}