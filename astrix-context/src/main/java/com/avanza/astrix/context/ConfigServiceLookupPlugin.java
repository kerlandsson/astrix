/*
 * Copyright 2014 Avanza Bank AB
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
package com.avanza.astrix.context;

import org.kohsuke.MetaInfServices;

import com.avanza.astrix.beans.core.AstrixBeanKey;
import com.avanza.astrix.beans.core.AstrixConfigAware;
import com.avanza.astrix.beans.inject.AstrixInject;
import com.avanza.astrix.beans.service.ServiceComponent;
import com.avanza.astrix.beans.service.ServiceComponentRegistry;
import com.avanza.astrix.beans.service.ServiceDiscovery;
import com.avanza.astrix.beans.service.ServiceDiscoveryMetaFactoryPlugin;
import com.avanza.astrix.beans.service.ServiceProperties;
import com.avanza.astrix.config.DynamicConfig;
import com.avanza.astrix.provider.core.AstrixConfigLookup;
/**
 * 
 * @author Elias Lindholm (elilin)
 * @deprecated - Repaled by {@link ConfigServiceDiscoveryPlugin}
 *
 */
@MetaInfServices(ServiceDiscoveryMetaFactoryPlugin.class)
@Deprecated
public class ConfigServiceLookupPlugin implements ServiceDiscoveryMetaFactoryPlugin<AstrixConfigLookup>, AstrixConfigAware {

	private ServiceComponentRegistry serviceComponents;
	private DynamicConfig config;
	
	@Override
	public ServiceDiscovery create(AstrixBeanKey<?> key, AstrixConfigLookup lookupAnnotation) {
		return new ConfigDiscovery(serviceComponents, config, lookupAnnotation.value());
	}
	
	@Override
	public Class<AstrixConfigLookup> getDiscoveryAnnotationType() {
		return AstrixConfigLookup.class;
	}

	@Override
	public void setConfig(DynamicConfig config) {
		this.config = config;
	}
	
	@AstrixInject
	public void setServiceComponents(ServiceComponentRegistry serviceComponents) {
		this.serviceComponents = serviceComponents;
	}

	private static class ConfigDiscovery implements ServiceDiscovery {

		private ServiceComponentRegistry serviceComponents;
		private DynamicConfig config;
		private String configEntryName;
		
		
		public ConfigDiscovery(ServiceComponentRegistry serviceComponents,
				DynamicConfig config, String configEntryName) {
			super();
			this.serviceComponents = serviceComponents;
			this.config = config;
			this.configEntryName = configEntryName;
		}

		@Override
		public ServiceProperties run() {
			String serviceUri = config.getStringProperty(configEntryName, null).get();
			if (serviceUri == null) {
				return null;
			}
			return buildServiceProperties(serviceUri);
		}
		
		@Override
		public String description() {
			return "ConfigDiscovery[" + configEntryName + "]";
		}
		
		private ServiceProperties buildServiceProperties(String serviceUri) {
			String component = serviceUri.substring(0, serviceUri.indexOf(":"));
			String serviceProviderUri = serviceUri.substring(serviceUri.indexOf(":") + 1);
			ServiceComponent serviceComponent = getServiceComponent(component);
			ServiceProperties serviceProperties = serviceComponent.parseServiceProviderUri(serviceProviderUri);
			serviceProperties.setComponent(serviceComponent.getName());
			return serviceProperties;
		}
		
		private ServiceComponent getServiceComponent(String componentName) {
			return serviceComponents.getComponent(componentName);
		}

	}

}
