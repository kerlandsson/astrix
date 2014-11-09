/*
 * Copyright 2014-2015 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.avanzabank.asterix.integration.tests;

import java.io.IOException;

import se.avanzabank.asterix.gs.test.util.PartitionedPu;
import se.avanzabank.asterix.gs.test.util.PuConfigurers;

public class ServiceRegistryRunner {
	
	public static void main(String[] args) throws IOException {
		System.setProperty("com.gs.jini_lus.groups", "service-registry");
		PartitionedPu partitionedPu = new PartitionedPu(PuConfigurers.partitionedPu("classpath:/META-INF/spring/service-registry-pu.xml")
				.numberOfPrimaries(1)
				.numberOfBackups(0));
		partitionedPu.run();
	}
}
