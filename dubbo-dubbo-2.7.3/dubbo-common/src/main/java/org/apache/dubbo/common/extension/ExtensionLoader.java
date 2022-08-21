/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.extension;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.support.ActivateComparator;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ArrayUtils;
import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.common.utils.Holder;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import static org.apache.dubbo.common.constants.CommonConstants.COMMA_SPLIT_PATTERN;
import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.REMOVE_VALUE_PREFIX;

/**
 * Load dubbo extensions
 * <ul>
 * <li>auto inject dependency extension </li>
 * <li>auto wrap extension in wrapper </li>
 * <li>default extension is an adaptive instance</li>
 * </ul>
 *
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/guide/jar/jar.html#Service%20Provider">Service Provider in Java 5</a>
 * @see org.apache.dubbo.common.extension.SPI
 * @see org.apache.dubbo.common.extension.Adaptive
 * @see org.apache.dubbo.common.extension.Activate
 */
public class ExtensionLoader<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
	
	private static final String SERVICES_DIRECTORY = "META-INF/services/";
	
	private static final String DUBBO_DIRECTORY = "META-INF/dubbo/";
	
	private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";
	
	private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");
	// 已加载的 Extension
	private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
	// 缓存所有的实例Class及对应的实例对象
	private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
	
	// ==============================
	// 接口 类型
	private final Class<?> type;
	
	private final ExtensionFactory objectFactory;
	
	private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();
	// 缓存该接口type下的所有实例key及 实例对应的Class
	private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
	private final Map<String, Object> cachedActivates = new ConcurrentHashMap<>();
	// 缓存 所有实例key及对应的Holder
	private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
	// 缓存该接口的自适应实例
	private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
	private volatile Class<?> cachedAdaptiveClass = null;
	private String cachedDefaultName;
	private volatile Throwable createAdaptiveInstanceError;
	// 缓存WrapperClasses
	private Set<Class<?>> cachedWrapperClasses;
	
	private Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();
	
	private ExtensionLoader(Class<?> type) {
		//指定接口类型
		this.type = type;
		// 对象工厂(扩展点工厂)
		objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
	}
	
	private static <T> boolean withExtensionAnnotation(Class<T> type) {
		return type.isAnnotationPresent(SPI.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		/**
		 * 校验
		 * 1、不为空
		 * 2、是接口
		 * 3、接口上需要有@SPI注解
		 */
		if (type == null) {
			throw new IllegalArgumentException("Extension type == null");
		}
		if (!type.isInterface()) {
			throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
		}
		if (!withExtensionAnnotation(type)) {
			throw new IllegalArgumentException("Extension type (" + type + ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
		}
		/**
		 * 先从EXTENSION_LOADERS(已加载的ExtensionLoader) 中获取
		 * 每个接口type都对应一个 ExtensionLoader,该接口下会对应多个扩展点
		 */
		ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		if (loader == null) {
			EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type)); // 每个接口type都对应一个 ExtensionLoader
			loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		}
		return loader;
	}
	
	// For testing purposes only
	public static void resetExtensionLoader(Class type) {
		ExtensionLoader loader = EXTENSION_LOADERS.get(type);
		if (loader != null) {
			// Remove all instances associated with this loader as well
			Map<String, Class<?>> classes = loader.getExtensionClasses();
			for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
				EXTENSION_INSTANCES.remove(entry.getValue());
			}
			classes.clear();
			EXTENSION_LOADERS.remove(type);
		}
	}
	
	private static ClassLoader findClassLoader() {
		return ClassUtils.getClassLoader(ExtensionLoader.class);
	}
	
	public String getExtensionName(T extensionInstance) {
		return getExtensionName(extensionInstance.getClass());
	}
	
	public String getExtensionName(Class<?> extensionClass) {
		getExtensionClasses();// load class
		return cachedNames.get(extensionClass);
	}
	
	/**
	 * This is equivalent to {@code getActivateExtension(url, key, null)}
	 *
	 * @param url url
	 * @param key url parameter key which used to get extension point names
	 * @return extension list which are activated.
	 * @see #getActivateExtension(org.apache.dubbo.common.URL, String, String)
	 */
	public List<T> getActivateExtension(URL url, String key) {
		return getActivateExtension(url, key, null);
	}
	
	/**
	 * This is equivalent to {@code getActivateExtension(url, values, null)}
	 *
	 * @param url    url
	 * @param values extension point names
	 * @return extension list which are activated
	 * @see #getActivateExtension(org.apache.dubbo.common.URL, String[], String)
	 */
	public List<T> getActivateExtension(URL url, String[] values) {
		return getActivateExtension(url, values, null);
	}
	
	/**
	 * This is equivalent to {@code getActivateExtension(url, url.getParameter(key).split(","), null)}
	 *
	 * @param url   url
	 * @param key   url parameter key which used to get extension point names
	 * @param group group
	 * @return extension list which are activated.
	 * @see #getActivateExtension(org.apache.dubbo.common.URL, String[], String)
	 */
	public List<T> getActivateExtension(URL url, String key, String group) {
		String value = url.getParameter(key);
		return getActivateExtension(url, StringUtils.isEmpty(value) ? null : COMMA_SPLIT_PATTERN.split(value), group);
	}
	
	/**
	 * Get activate extensions.
	 *
	 * @param url    url
	 * @param values extension point names
	 * @param group  group
	 * @return extension list which are activated
	 * @see org.apache.dubbo.common.extension.Activate
	 */
	public List<T> getActivateExtension(URL url, String[] values, String group) {
		List<T> exts = new ArrayList<>();
		List<String> names = values == null ? new ArrayList<>(0) : Arrays.asList(values);
		if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
			getExtensionClasses();
			for (Map.Entry<String, Object> entry : cachedActivates.entrySet()) {
				String name = entry.getKey();
				Object activate = entry.getValue();
				
				String[] activateGroup, activateValue;
				
				if (activate instanceof Activate) {
					activateGroup = ((Activate) activate).group();
					activateValue = ((Activate) activate).value();
				} else if (activate instanceof com.alibaba.dubbo.common.extension.Activate) {
					activateGroup = ((com.alibaba.dubbo.common.extension.Activate) activate).group();
					activateValue = ((com.alibaba.dubbo.common.extension.Activate) activate).value();
				} else {
					continue;
				}
				if (isMatchGroup(group, activateGroup)) {
					T ext = getExtension(name);
					if (!names.contains(name) && !names.contains(REMOVE_VALUE_PREFIX + name) && isActive(activateValue, url)) {
						exts.add(ext);
					}
				}
			}
			exts.sort(ActivateComparator.COMPARATOR);
		}
		List<T> usrs = new ArrayList<>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			if (!name.startsWith(REMOVE_VALUE_PREFIX) && !names.contains(REMOVE_VALUE_PREFIX + name)) {
				if (DEFAULT_KEY.equals(name)) {
					if (!usrs.isEmpty()) {
						exts.addAll(0, usrs);
						usrs.clear();
					}
				} else {
					T ext = getExtension(name);
					usrs.add(ext);
				}
			}
		}
		if (!usrs.isEmpty()) {
			exts.addAll(usrs);
		}
		return exts;
	}
	
	private boolean isMatchGroup(String group, String[] groups) {
		if (StringUtils.isEmpty(group)) {
			return true;
		}
		if (groups != null && groups.length > 0) {
			for (String g : groups) {
				if (group.equals(g)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isActive(String[] keys, URL url) {
		if (keys.length == 0) {
			return true;
		}
		for (String key : keys) {
			for (Map.Entry<String, String> entry : url.getParameters().entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				if ((k.equals(key) || k.endsWith("." + key)) && ConfigUtils.isNotEmpty(v)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get extension's instance. Return <code>null</code> if extension is not found or is not initialized. Pls. note
	 * that this method will not trigger extension load.
	 * <p>
	 * In order to trigger extension load, call {@link #getExtension(String)} instead.
	 *
	 * @see #getExtension(String)
	 */
	@SuppressWarnings("unchecked")
	public T getLoadedExtension(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Extension name == null");
		}
		Holder<Object> holder = getOrCreateHolder(name);
		return (T) holder.get();
	}
	
	private Holder<Object> getOrCreateHolder(String name) {
		/**
		 * cachedInstances：缓存所有实例key及对应的Holder
		 */
		Holder<Object> holder = cachedInstances.get(name);
		if (holder == null) {
			cachedInstances.putIfAbsent(name, new Holder<>());
			holder = cachedInstances.get(name);
		}
		return holder;
	}
	
	/**
	 * Return the list of extensions which are already loaded.
	 * <p>
	 * Usually {@link #getSupportedExtensions()} should be called in order to get all extensions.
	 *
	 * @see #getSupportedExtensions()
	 */
	public Set<String> getLoadedExtensions() {
		return Collections.unmodifiableSet(new TreeSet<>(cachedInstances.keySet()));
	}
	
	public Object getLoadedAdaptiveExtensionInstances() {
		return cachedAdaptiveInstance.get();
	}
	
	/**
	 * Find the extension with the given name. If the specified name is not found, then {@link IllegalStateException}
	 * will be thrown.
	 */
	@SuppressWarnings("unchecked")
	public T getExtension(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Extension name == null");
		}
		if ("true".equals(name)) {
			return getDefaultExtension();
		}
		/**
		 * name为 META-INF/dubbo下配置文件中配置的key
		 *
		 * 1、获取key对应Extension实例的holder (双重锁校验)--->getOrCreateHolder内部通过cachedInstances缓存所有实例key及对应的Holder
		 * 2、创建key对应的Extension实例并存入holder
		 * 3、返回对应的Extension实例
		 */
		final Holder<Object> holder = getOrCreateHolder(name);
		Object instance = holder.get();
		if (instance == null) {
			synchronized (holder) {
				instance = holder.get();
				if (instance == null) {
					//创建实例的核心方法
					instance = createExtension(name);
					holder.set(instance);
				}
			}
		}
		return (T) instance;
	}
	
	/**
	 * Return default extension, return <code>null</code> if it's not configured.
	 */
	public T getDefaultExtension() {
		getExtensionClasses();
		if (StringUtils.isBlank(cachedDefaultName) || "true".equals(cachedDefaultName)) {
			return null;
		}
		return getExtension(cachedDefaultName);
	}
	
	public boolean hasExtension(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Extension name == null");
		}
		Class<?> c = this.getExtensionClass(name);
		return c != null;
	}
	
	public Set<String> getSupportedExtensions() {
		Map<String, Class<?>> clazzes = getExtensionClasses();
		return Collections.unmodifiableSet(new TreeSet<>(clazzes.keySet()));
	}
	
	/**
	 * Return default extension name, return <code>null</code> if not configured.
	 */
	public String getDefaultExtensionName() {
		getExtensionClasses();
		return cachedDefaultName;
	}
	
	/**
	 * Register new extension via API
	 *
	 * @param name  extension name
	 * @param clazz extension class
	 * @throws IllegalStateException when extension with the same name has already been registered.
	 */
	public void addExtension(String name, Class<?> clazz) {
		getExtensionClasses(); // load classes
		
		if (!type.isAssignableFrom(clazz)) {
			throw new IllegalStateException("Input type " + clazz + " doesn't implement the Extension " + type);
		}
		if (clazz.isInterface()) {
			throw new IllegalStateException("Input type " + clazz + " can't be interface!");
		}
		
		if (!clazz.isAnnotationPresent(Adaptive.class)) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalStateException("Extension name is blank (Extension " + type + ")!");
			}
			if (cachedClasses.get().containsKey(name)) {
				throw new IllegalStateException("Extension name " + name + " already exists (Extension " + type + ")!");
			}
			
			cachedNames.put(clazz, name);
			cachedClasses.get().put(name, clazz);
		} else {
			if (cachedAdaptiveClass != null) {
				throw new IllegalStateException("Adaptive Extension already exists (Extension " + type + ")!");
			}
			
			cachedAdaptiveClass = clazz;
		}
	}
	
	/**
	 * Replace the existing extension via API
	 *
	 * @param name  extension name
	 * @param clazz extension class
	 * @throws IllegalStateException when extension to be placed doesn't exist
	 * @deprecated not recommended any longer, and use only when test
	 */
	@Deprecated
	public void replaceExtension(String name, Class<?> clazz) {
		getExtensionClasses(); // load classes
		
		if (!type.isAssignableFrom(clazz)) {
			throw new IllegalStateException("Input type " + clazz + " doesn't implement Extension " + type);
		}
		if (clazz.isInterface()) {
			throw new IllegalStateException("Input type " + clazz + " can't be interface!");
		}
		
		if (!clazz.isAnnotationPresent(Adaptive.class)) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalStateException("Extension name is blank (Extension " + type + ")!");
			}
			if (!cachedClasses.get().containsKey(name)) {
				throw new IllegalStateException("Extension name " + name + " doesn't exist (Extension " + type + ")!");
			}
			
			cachedNames.put(clazz, name);
			cachedClasses.get().put(name, clazz);
			cachedInstances.remove(name);
		} else {
			if (cachedAdaptiveClass == null) {
				throw new IllegalStateException("Adaptive Extension doesn't exist (Extension " + type + ")!");
			}
			
			cachedAdaptiveClass = clazz;
			cachedAdaptiveInstance.set(null);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getAdaptiveExtension() {
		// Holder<Object> cachedAdaptiveInstance
		Object instance = cachedAdaptiveInstance.get();
		if (instance == null) {
			if (createAdaptiveInstanceError == null) {
				synchronized (cachedAdaptiveInstance) {
					instance = cachedAdaptiveInstance.get();
					if (instance == null) {
						try {
							// 创建接口的自适应实例
							instance = createAdaptiveExtension();
							cachedAdaptiveInstance.set(instance);
						} catch (Throwable t) {
							createAdaptiveInstanceError = t;
							throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
						}
					}
				}
			} else {
				throw new IllegalStateException("Failed to create adaptive instance: " + createAdaptiveInstanceError.toString(), createAdaptiveInstanceError);
			}
		}
		
		return (T) instance;
	}
	
	private IllegalStateException findException(String name) {
		for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
			if (entry.getKey().toLowerCase().contains(name.toLowerCase())) {
				return entry.getValue();
			}
		}
		StringBuilder buf = new StringBuilder("No such extension " + type.getName() + " by name " + name);
		
		
		int i = 1;
		for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
			if (i == 1) {
				buf.append(", possible causes: ");
			}
			
			buf.append("\r\n(");
			buf.append(i++);
			buf.append(") ");
			buf.append(entry.getKey());
			buf.append(":\r\n");
			buf.append(StringUtils.toString(entry.getValue()));
		}
		return new IllegalStateException(buf.toString());
	}
	
	/**
	 * 根据name创建对应的扩展实例
	 *
	 * @param name 实例key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T createExtension(String name) {
		// 根据 key 获取对应实例的 Class
		// 从配置文件中加载所有的拓展类，可得到“配置项名称”到“配置类”的映射关系表
		Class<?> clazz = getExtensionClasses().get(name);
		if (clazz == null) {
			throw findException(name);
		}
		try {
			/**
			 * 1、创建扩展实例
			 * EXTENSION_INSTANCES 是一个 ConcurrentMap
			 */
			T instance = (T) EXTENSION_INSTANCES.get(clazz);
			if (instance == null) { // 如果从map中未获取到  则通过反射创建实例
				EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
				instance = (T) EXTENSION_INSTANCES.get(clazz);
			}
			/**
			 * 向实例中注入其依赖的实例
			 */
			injectExtension(instance);
			
			/**
			 * 装配到Wrapper中
			 */
			Set<Class<?>> wrapperClasses = cachedWrapperClasses;
			if (CollectionUtils.isNotEmpty(wrapperClasses)) {
				// 遍历Wrapper类型的Class
				for (Class<?> wrapperClass : wrapperClasses) {
					/**
					 * 将当前实例包装到Wrappe中,通过构造注入，往Wrapper中注入依赖，
					 * 通过Wrapper包装实例，从而在Wrapper的方法中进行方法增强；是实现AOP的关键
					 *
					 * // 将当前 instance 作为参数传给 Wrapper 的构造方法，并通过反射创建 Wrapper 实例。
					 * // 然后向 Wrapper 实例中注入依赖，最后将 Wrapper 实例再次赋值给 instance 变量
					 *
					 * wrapperClass.getConstructor（type）====>通过反射获取构造方法====>创建实例
					 */
					instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
				}
			}
			return instance;
		} catch (Throwable t) {
			throw new IllegalStateException("Extension instance (name: " + name + ", class: " + type + ") couldn't be instantiated: " + t.getMessage(), t);
		}
	}
	
	private T injectExtension(T instance) {
		try {
			if (objectFactory != null) {
				for (Method method : instance.getClass().getMethods()) { // 通过反射获取实例的所有公有方法
					/**
					 * 通过set方法注入
					 */
					if (isSetter(method)) {
						/**
						 * Check {@link DisableInject} to see if we need auto injection for this property
						 */
						if (method.getAnnotation(DisableInject.class) != null) { // 如果方法上有 @DisableInject这个注解则跳过
							continue;
						}
						// set方法只能有一个参数
						// 获取注入点  即 要注入的 接口
						Class<?> pt = method.getParameterTypes()[0];
						// set方法参数的类型如果是基本数据类型则跳过,即不支持基本数据类型的注入
						// dubbo spi  此处应该是接口类型
						if (ReflectUtils.isPrimitives(pt)) {
							continue;
						}
						try {
							// 获取set方法对应的属性名称
							// for instance: setVersion, return "version"
							String property = getSetterProperty(method);
							/**
							 * pt:属性类型Class
							 * property:属性名称
							 *
							 * 根据类型和名称获取待注入的Extension实例
							 * ExtensionFactory objectFactory;
							 *   实现有很多比如：
							 *      SpiExtensionFactory
							 *      SpringExtensionFactory
							 */
							// 获取到真正的扩展点 --》要执行的方法
							Object object = objectFactory.getExtension(pt, property);
							if (object != null) {
								// 通过反射执行方法
								method.invoke(instance, object);
							}
						} catch (Exception e) {
							logger.error("Failed to inject via method " + method.getName() + " of interface " + type.getName() + ": " + e.getMessage(), e);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return instance;
	}
	
	/**
	 * get properties name for setter, for instance: setVersion, return "version"
	 * <p>
	 * return "", if setter name with length less than 3
	 */
	private String getSetterProperty(Method method) {
		return method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
	}
	
	/**
	 * return true if and only if:
	 * <p>
	 * 1, public
	 * <p>
	 * 2, name starts with "set"
	 * <p>
	 * 3, only has one parameter
	 */
	private boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getParameterTypes().length == 1 && Modifier.isPublic(method.getModifiers());
	}
	
	private Class<?> getExtensionClass(String name) {
		if (type == null) {
			throw new IllegalArgumentException("Extension type == null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Extension name == null");
		}
		return getExtensionClasses().get(name);
	}
	
	/**
	 * 获取扩展Class
	 *
	 * @return
	 */
	private Map<String, Class<?>> getExtensionClasses() {
		// Holder<Map<String, Class<?>>> cachedClasses
		Map<String, Class<?>> classes = cachedClasses.get();
		if (classes == null) {
			synchronized (cachedClasses) {
				classes = cachedClasses.get();
				if (classes == null) {
					classes = loadExtensionClasses();
					cachedClasses.set(classes);
				}
			}
		}
		return classes;
	}
	
	// synchronized in getExtensionClasses
	private Map<String, Class<?>> loadExtensionClasses() {
		cacheDefaultExtensionName();
		/**
		 * loadDirectory方法从指定位置中加载拓展类配置
		 * "META-INF/dubbo/internal/"  DubboInternalLoadingStrategy
		 * “META-INF/dubbo/”，          DubboLoadingStrategy
		 * "META-INF/services/"，       ServicesLoadingStrategy
		 */
		Map<String, Class<?>> extensionClasses = new HashMap<>();
		loadDirectory(extensionClasses, DUBBO_INTERNAL_DIRECTORY, type.getName());
		loadDirectory(extensionClasses, DUBBO_INTERNAL_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
		loadDirectory(extensionClasses, DUBBO_DIRECTORY, type.getName());
		loadDirectory(extensionClasses, DUBBO_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
		loadDirectory(extensionClasses, SERVICES_DIRECTORY, type.getName());
		loadDirectory(extensionClasses, SERVICES_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
		return extensionClasses;
	}
	
	/**
	 * extract and cache default extension name if exists
	 */
	private void cacheDefaultExtensionName() {
		// 获取 SPI 注解，这里的 type 变量是在调用 getExtensionLoader 方法时传入的
		final SPI defaultAnnotation = type.getAnnotation(SPI.class);
		if (defaultAnnotation != null) {
			String value = defaultAnnotation.value();
			if ((value = value.trim()).length() > 0) {
				// 对 SPI 注解内容进行切分
				String[] names = NAME_SEPARATOR.split(value);
				if (names.length > 1) {
					throw new IllegalStateException("More than 1 default extension name on extension " + type.getName() + ": " + Arrays.toString(names));
				}
				if (names.length == 1) {
					cachedDefaultName = names[0];
				}
			}
		}
	}
	
	private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type) {
		// dir 有三种目录  type是接口类型
		// fileName = 文件夹路径 + type全限定名
		// bumblebee=com.itheima.spi.dubbo.robot.Bumblebee
		String fileName = dir + type;
		try {
			Enumeration<java.net.URL> urls;
			ClassLoader classLoader = findClassLoader();
			// 根据文件名加载所有的同名文件
			if (classLoader != null) {
				urls = classLoader.getResources(fileName);
			} else {
				urls = ClassLoader.getSystemResources(fileName);
			}
			if (urls != null) {
				while (urls.hasMoreElements()) {
					java.net.URL resourceURL = urls.nextElement();
					// 核心方法  加载资源
					loadResource(extensionClasses, classLoader, resourceURL);
				}
			}
		} catch (Throwable t) {
			logger.error("Exception occurred when loading extension class (interface: " + type + ", description file: " + fileName + ").", t);
		}
	}
	
	private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
		try {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
				String line;
				// 按行读取配置
				while ((line = reader.readLine()) != null) {
					final int ci = line.indexOf('#'); // # 是注释符
					if (ci >= 0) {
						// 截取 bumblebee=com.itheima.spi.dubbo.robot.Bumblebee # 这是注释
						//  主要是处理#在最后面的逻辑
						line = line.substring(0, ci);
					}
					line = line.trim();
					if (line.length() > 0) {
						try {
							String name = null;
							//按=切割 得到key和lvalue
							int i = line.indexOf('=');
							if (i > 0) {
								name = line.substring(0, i).trim(); // 自定义的key
								line = line.substring(i + 1).trim(); // value 即扩展点类的全路径
							}
							if (line.length() > 0) {
								// 加载类，并通过 loadClass 方法对类进行缓存
								loadClass(extensionClasses, resourceURL, Class.forName(line, true, classLoader), name);
							}
						} catch (Throwable t) {
							IllegalStateException e = new IllegalStateException("Failed to load extension class (interface: " + type + ", class line: " + line + ") in " + resourceURL + ", cause: " + t.getMessage(), t);
							exceptions.put(line, e);
						}
					}
				}
			}
		} catch (Throwable t) {
			logger.error("Exception occurred when loading extension class (interface: " + type + ", class file: " + resourceURL + ") in " + resourceURL, t);
		}
	}
	
	/**
	 * loadResource 方法用于读取和解析配置文件，并通过反射加载类，最后调用 loadClass 方法进行其他操作。
	 * loadClass 方法用于主要用于操作缓存，该方法的逻辑如下：
	 *
	 * @param extensionClasses
	 * @param resourceURL
	 * @param clazz
	 * @param name
	 * @throws NoSuchMethodException
	 */
	private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
		if (!type.isAssignableFrom(clazz)) {
			throw new IllegalStateException("Error occurred when loading extension class (interface: " + type + ", class line: " + clazz.getName() + "), class " + clazz.getName() + " is not subtype of interface.");
		}
		if (clazz.isAnnotationPresent(Adaptive.class)) {
			// 扩展点Class上有 Adaptive注解
			cacheAdaptiveClass(clazz);
		} else if (isWrapperClass(clazz)) { // 扩展点类有接口类型的构造函数,表明是Wrapper
			// 添加到Set<Class<?>> cachedWrapperClasses 缓存起来
			cacheWrapperClass(clazz);
		} else { // 证明是普通 extensionClasses
			clazz.getConstructor();
			if (StringUtils.isEmpty(name)) {
				name = findAnnotationName(clazz);
				if (name.length() == 0) {
					throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
				}
			}
			
			String[] names = NAME_SEPARATOR.split(name);
			if (ArrayUtils.isNotEmpty(names)) {
				cacheActivateClass(clazz, names[0]);
				for (String n : names) {
					cacheName(clazz, n);
					saveInExtensionClass(extensionClasses, clazz, n);
				}
			}
		}
	}
	
	/**
	 * cache name
	 */
	private void cacheName(Class<?> clazz, String name) {
		if (!cachedNames.containsKey(clazz)) {
			cachedNames.put(clazz, name);
		}
	}
	
	/**
	 * put clazz in extensionClasses
	 */
	private void saveInExtensionClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
		Class<?> c = extensionClasses.get(name);
		if (c == null) {
			extensionClasses.put(name, clazz);
		} else if (c != clazz) {
			throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName());
		}
	}
	
	/**
	 * cache Activate class which is annotated with <code>Activate</code>
	 * <p>
	 * for compatibility, also cache class with old alibaba Activate annotation
	 */
	private void cacheActivateClass(Class<?> clazz, String name) {
		Activate activate = clazz.getAnnotation(Activate.class);
		if (activate != null) {
			// 如果类上有 Activate 注解，则使用 names 数组的第一个元素作为键，
			// 存储 name 到 Activate 注解对象的映射关系
			cachedActivates.put(name, activate);
		} else {
			// support com.alibaba.dubbo.common.extension.Activate
			com.alibaba.dubbo.common.extension.Activate oldActivate = clazz.getAnnotation(com.alibaba.dubbo.common.extension.Activate.class);
			if (oldActivate != null) {
				cachedActivates.put(name, oldActivate);
			}
		}
	}
	
	/**
	 * cache Adaptive class which is annotated with <code>Adaptive</code>
	 */
	private void cacheAdaptiveClass(Class<?> clazz) {
		if (cachedAdaptiveClass == null) {
			cachedAdaptiveClass = clazz;
		} else if (!cachedAdaptiveClass.equals(clazz)) {
			throw new IllegalStateException("More than 1 adaptive class found: " + cachedAdaptiveClass.getClass().getName() + ", " + clazz.getClass().getName());
		}
	}
	
	/**
	 * cache wrapper class
	 * <p>
	 * like: ProtocolFilterWrapper, ProtocolListenerWrapper
	 */
	private void cacheWrapperClass(Class<?> clazz) {
		if (cachedWrapperClasses == null) {
			cachedWrapperClasses = new ConcurrentHashSet<>();
		}
		cachedWrapperClasses.add(clazz);
	}
	
	/**
	 * test if clazz is a wrapper class
	 * <p>
	 * which has Constructor with given class type as its only argument
	 */
	private boolean isWrapperClass(Class<?> clazz) {
		try {
			clazz.getConstructor(type);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	private String findAnnotationName(Class<?> clazz) {
		org.apache.dubbo.common.Extension extension = clazz.getAnnotation(org.apache.dubbo.common.Extension.class);
		if (extension == null) {
			String name = clazz.getSimpleName();
			if (name.endsWith(type.getSimpleName())) {
				name = name.substring(0, name.length() - type.getSimpleName().length());
			}
			return name.toLowerCase();
		}
		return extension.value();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 创建接口的自适应实例
	 */ private T createAdaptiveExtension() {
		try {
			//  getAdaptiveExtensionClass()是核心
			return injectExtension((T) getAdaptiveExtensionClass().newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("Can't create adaptive extension " + type + ", cause: " + e.getMessage(), e);
		}
	}
	
	private Class<?> getAdaptiveExtensionClass() {
		getExtensionClasses();
		if (cachedAdaptiveClass != null) {
			return cachedAdaptiveClass;
		}
		/**
		 * 获取接口自适应实例Class
		 */
		return cachedAdaptiveClass = createAdaptiveExtensionClass();
	}
	
	private Class<?> createAdaptiveExtensionClass() {
		/**
		 * 首先会生成自适应类的Java源码，然后再将源码编译成Java的字节码，加载到JVM中
		 * 使用一个StringBuilder来构建自适应类的Java源码;
		 * 这种生成字节码的方式也挺有意思的，先生成Java源代码，然后编译，加载到jvm中。
		 * 通过这种方式，可以更好的控制生成的Java类。而且这样也不用care各个字节码生成框架的api等。
		 * 因为xxx.java文件是Java通用的，也是我们最熟悉的。只是代码的可读性不强，需要一点一点构建xx.java的内容
		 */
		String code = new AdaptiveClassCodeGenerator(type, cachedDefaultName).generate();
		ClassLoader classLoader = findClassLoader();
		/**
		 * @SPI("javassist")
		 * public interface Compiler
		 */
		org.apache.dubbo.common.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(org.apache.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
		return compiler.compile(code, classLoader);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + "[" + type.getName() + "]";
	}
	
}
