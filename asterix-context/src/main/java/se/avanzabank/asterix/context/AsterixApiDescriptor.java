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
package se.avanzabank.asterix.context;

import java.lang.annotation.Annotation;
/**
 * 
 * @author Elias Lindholm (elilin)
 *
 */
public class AsterixApiDescriptor {
	
	/*
	 *TODO: is an api-descriptor the same as a service-descriptor?
	 *
	 *Every api-provider has an corresponding api-descriptor, but only
	 *services has a descriptor that's used on the server side. Does the difference require separate representations?
	 * 
	 */
	
	private final Class<?> descriptorHolder;

	public AsterixApiDescriptor(Class<?> descriptorHolder) {
		this.descriptorHolder = descriptorHolder;
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return descriptorHolder.isAnnotationPresent(annotationClass);
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return descriptorHolder.getAnnotation(annotationClass);
	}
	
}