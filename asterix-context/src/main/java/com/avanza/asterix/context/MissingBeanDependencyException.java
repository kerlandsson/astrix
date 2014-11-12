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
package com.avanza.asterix.context;

public class MissingBeanDependencyException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MissingBeanDependencyException(AsterixBeanAware beanDependenciesAware, Class<?> beanType) {
		super(createErrorMessage(beanDependenciesAware, beanType));
	}

	private static String createErrorMessage(AsterixBeanAware beanDependenciesAware, Class<?> beanType) {
		String requiredBy = beanDependenciesAware.getClass().getName();
		if (beanDependenciesAware instanceof AsterixFactoryBeanPlugin<?>) {
			AsterixFactoryBeanPlugin<?> factory = (AsterixFactoryBeanPlugin<?>) beanDependenciesAware;
			requiredBy = requiredBy + "["+ factory.getBeanType().getName() + "]";
		}
		return "Missing bean provider. requiredBeanType=" + beanType.getName() + " requiredBy=" + requiredBy;
	}

}
