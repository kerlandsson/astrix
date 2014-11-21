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
package com.avanza.astrix.service.registry.server;

import org.kohsuke.MetaInfServices;

import com.avanza.astrix.context.AstrixInject;
import com.avanza.astrix.context.AstrixServiceComponent;
import com.avanza.astrix.context.AstrixServiceRegistryPlugin;

@MetaInfServices(AstrixServiceRegistryPlugin.class)
public class AstrixServiceRegistryPluginImpl implements AstrixServiceRegistryPlugin {
	
	private AstrixServiceRegistryExporterWorker serviceRegistryExporterWorker;
	
	@Override
	public <T> void addProvider(Class<T> beanType, AstrixServiceComponent serviceComponent) {
		serviceRegistryExporterWorker.addServiceBuilder(new AstrixServicePropertiesBuilderHolder(serviceComponent, beanType));
	}
	
	@AstrixInject
	public void setServiceRegistryExporterWorker(AstrixServiceRegistryExporterWorker serviceRegistryExporterWorker) {
		this.serviceRegistryExporterWorker = serviceRegistryExporterWorker;
	}

	@Override
	public void startPublishServices() {
		serviceRegistryExporterWorker.startServiceExporter();
	}
	
}