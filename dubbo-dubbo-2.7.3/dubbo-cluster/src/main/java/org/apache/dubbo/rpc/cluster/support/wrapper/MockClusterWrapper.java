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
package org.apache.dubbo.rpc.cluster.support.wrapper;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

/**
 * mock impl
 * 查看dubbo-cluster模块中的SPI配置
 * META-INF/dubbo/internal下关于Cluster的配置
 * 可知,failover,failfast等扩展点都会通过MockClusterWrapper进行装配
 * <p>
 * mock=org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterWrapper
 * failover=org.apache.dubbo.rpc.cluster.support.FailoverCluster
 * failfast=org.apache.dubbo.rpc.cluster.support.FailfastCluster
 * failsafe=org.apache.dubbo.rpc.cluster.support.FailsafeCluster
 * failback=org.apache.dubbo.rpc.cluster.support.FailbackCluster
 * forking=org.apache.dubbo.rpc.cluster.support.ForkingCluster
 * available=org.apache.dubbo.rpc.cluster.support.AvailableCluster
 * mergeable=org.apache.dubbo.rpc.cluster.support.MergeableCluster
 * broadcast=org.apache.dubbo.rpc.cluster.support.BroadcastCluster
 * registryaware=org.apache.dubbo.rpc.cluster.support.RegistryAwareCluster
 */
public class MockClusterWrapper implements Cluster {
	
	private Cluster cluster;
	
	public MockClusterWrapper(Cluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
		/**
		 * 在 MockClusterInvoker 的 invoke 方法进行一系列的服务降级应用
		 */
		return new MockClusterInvoker<T>(directory,
				this.cluster.join(directory));
	}
	
}
