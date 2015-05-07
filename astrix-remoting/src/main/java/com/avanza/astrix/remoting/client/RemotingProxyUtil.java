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
package com.avanza.astrix.remoting.client;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import com.avanza.astrix.core.AstrixRemoteResultReducer;
import com.avanza.astrix.core.util.ReflectionUtil;

final class RemotingProxyUtil {
	
	static void validateRemoteResultReducer(Method targetServiceMethod,
			Class<? extends AstrixRemoteResultReducer> reducerType) {
		validateRemoteResultReducerReturnType(targetServiceMethod, reducerType);
		validateRemoteResultReducerArgumentType(targetServiceMethod, reducerType);
	}
	
	private static void validateRemoteResultReducerReturnType(
			Method targetServiceMethod,
			Class<? extends AstrixRemoteResultReducer> reducerType) {
		Method reduceMethod = ReflectionUtil.getMethod(reducerType, "reduce", List.class);
		Class<?> returnType = targetServiceMethod.getReturnType();
		if (returnType.equals(Void.TYPE)) {
			return;
		}
		if (TypeVariable.class.isAssignableFrom(reduceMethod
				.getGenericReturnType().getClass())) {
			return;
		}
		if (!returnType.isAssignableFrom(reduceMethod.getReturnType())) {
			throw new IncompatibleRemoteResultReducerException(
					String.format(
							"Return type of AstrixRemoteResultReducer must be same as (or subtype) of the one returned by the service method. "
									+ "serviceMethod=%s reducerType=%s reducerReturnType=%s serviceMethodReturnType=%s",
							targetServiceMethod, reducerType, returnType.getName(), reduceMethod.getReturnType().getName()));
		}
	}

	
	private static void validateRemoteResultReducerArgumentType(Method m,
			Class<? extends AstrixRemoteResultReducer> reducerType) {
		// Lookup the "<T>" type parameter in:
		// "R reduce(List<AstrixRemoteResult<T>> result)";
		Method reduceMethod = ReflectionUtil.getMethod(reducerType, "reduce", List.class);
		ParameterizedType listType = (ParameterizedType) reduceMethod.getGenericParameterTypes()[0];
		ParameterizedType astrixRemoteResultType = (ParameterizedType) listType.getActualTypeArguments()[0];
		Type astrixRemoteResultTypeParameter = astrixRemoteResultType.getActualTypeArguments()[0];
		if (!(astrixRemoteResultTypeParameter instanceof Class)) {
			return;
		}
		Class<?> type = (Class<?>) astrixRemoteResultTypeParameter;
		if (!type.isAssignableFrom(m.getReturnType()) && !m.getReturnType().equals(Void.TYPE)) {
			throw new IncompatibleRemoteResultReducerException(
					String.format(
							"Generic argument type of AstrixRemoteResultReducer.reduce(List<AstrixRemoteResult<T>>) must same as of the one returned by the serivce method. "
									+ "serviceMethod=%s reducerType=%s", m,
							reducerType));
		}
	}

}
