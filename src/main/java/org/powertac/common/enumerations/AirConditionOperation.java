/*
* Copyright 2009-2010 the original author or authors.
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

package org.powertac.common.enumerations;

/**
 * This enumerator defines the two different kinds of Air Conditions available.
 * The one is the normal type and the second one is the Inverter type. This has
 * an impact on their consumption and their duration cycle.
 * 
 * @author Antonios Chrysopoulos
 * @since 0.5
 * @version 1 Last Updated: 12.01.2012
 */

public enum AirConditionOperation
{
  Cooling, Heating, Off
}
