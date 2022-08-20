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

package org.apache.dubbo.serialize.hessian.serializer.java8;


import com.caucho.hessian.io.HessianHandle;

import java.io.Serializable;
import java.time.YearMonth;

public class YearMonthHandle implements HessianHandle, Serializable {
	private static final long serialVersionUID = -4150786187896925314L;
	
	private int year;
	private int month;
	
	public YearMonthHandle() {
	}
	
	public YearMonthHandle(Object o) {
		try {
			YearMonth yearMonth = (YearMonth) o;
			this.year = yearMonth.getYear();
			this.month = yearMonth.getMonthValue();
		} catch (Throwable t) {
			// ignore
		}
	}
	
	private Object readResolve() {
		try {
			return YearMonth.of(year, month);
		} catch (Throwable t) {
			// ignore
		}
		return null;
	}
}
