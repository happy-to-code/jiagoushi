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
/*
 * Autogenerated by Dubbo Compiler (0.1.0)
 * <p>
 * Thrift (0.7.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class $__ClassNameTestDubboStub {
	
	public interface Iface {
		
		public String echo(String arg);
		
	}
	
	public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor implements org.apache.thrift.TProcessor {
		private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
		
		public Processor(I iface) {
			super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
		}
		
		protected Processor(I iface, Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			super(iface, getProcessMap(processMap));
		}
		
		private static <I extends Iface> Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			processMap.put("echo", new echo());
			return processMap;
		}
		
		private static class echo<I extends Iface> extends org.apache.thrift.ProcessFunction<I, echo_args> {
			public echo() {
				super("echo");
			}
			
			public echo_args getEmptyArgsInstance() {
				return new echo_args();
			}
			
			@Override
			protected boolean isOneway() {
				return false;
			}
			
			public echo_result getResult(I iface, echo_args args) throws org.apache.thrift.TException {
				echo_result result = new echo_result();
				result.success = iface.echo(args.arg);
				return result;
			}
		}
		
	}
	
	public static class echo_args implements org.apache.thrift.TBase<echo_args, echo_args._Fields>, java.io.Serializable, Cloneable {
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("echo_args");
		private static final org.apache.thrift.protocol.TField ARG_FIELD_DESC = new org.apache.thrift.protocol.TField("arg", org.apache.thrift.protocol.TType.STRING, (short) 1);
		
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.ARG, new org.apache.thrift.meta_data.FieldMetaData("arg", org.apache.thrift.TFieldRequirementType.REQUIRED,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(echo_args.class, metaDataMap);
		}
		
		// isset id assignments
		
		public String arg; // required
		
		public echo_args() {
		}
		
		public echo_args(
				String arg) {
			this();
			this.arg = arg;
		}
		
		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public echo_args(echo_args other) {
			if (other.isSetArg()) {
				this.arg = other.arg;
			}
		}
		
		public echo_args deepCopy() {
			return new echo_args(this);
		}
		
		public void clear() {
			this.arg = null;
		}
		
		public String getArg() {
			return this.arg;
		}
		
		public echo_args setArg(String arg) {
			this.arg = arg;
			return this;
		}
		
		public void unsetArg() {
			this.arg = null;
		}
		
		/**
		 * Returns true if field arg is set (has been assigned a value) and false otherwise
		 */
		public boolean isSetArg() {
			return this.arg != null;
		}
		
		public void setArgIsSet(boolean value) {
			if (!value) {
				this.arg = null;
			}
		}
		
		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case ARG:
					if (value == null) {
						unsetArg();
					} else {
						setArg((String) value);
					}
					break;
				
			}
		}
		
		public Object getFieldValue(_Fields field) {
			switch (field) {
				case ARG:
					return getArg();
				
			}
			throw new IllegalStateException();
		}
		
		/**
		 * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}
			
			switch (field) {
				case ARG:
					return isSetArg();
			}
			throw new IllegalStateException();
		}
		
		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof echo_args)
				return this.equals((echo_args) that);
			return false;
		}
		
		public boolean equals(echo_args that) {
			if (that == null)
				return false;
			
			boolean this_present_arg = true && this.isSetArg();
			boolean that_present_arg = true && that.isSetArg();
			if (this_present_arg || that_present_arg) {
				if (!(this_present_arg && that_present_arg))
					return false;
				if (!this.arg.equals(that.arg))
					return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			return 0;
		}
		
		public int compareTo(echo_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}
			
			int lastComparison = 0;
			echo_args typedOther = (echo_args) other;
			
			lastComparison = Boolean.valueOf(isSetArg()).compareTo(typedOther.isSetArg());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetArg()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.arg, typedOther.arg);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}
		
		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}
		
		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			org.apache.thrift.protocol.TField field;
			iprot.readStructBegin();
			while (true) {
				field = iprot.readFieldBegin();
				if (field.type == org.apache.thrift.protocol.TType.STOP) {
					break;
				}
				switch (field.id) {
					case 1: // ARG
						if (field.type == org.apache.thrift.protocol.TType.STRING) {
							this.arg = iprot.readString();
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
				}
				iprot.readFieldEnd();
			}
			iprot.readStructEnd();
			
			// check for required fields of primitive type, which can't be checked in the validate method
			validate();
		}
		
		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			validate();
			
			oprot.writeStructBegin(STRUCT_DESC);
			if (this.arg != null) {
				oprot.writeFieldBegin(ARG_FIELD_DESC);
				oprot.writeString(this.arg);
				oprot.writeFieldEnd();
			}
			oprot.writeFieldStop();
			oprot.writeStructEnd();
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("echo_args(");
			boolean first = true;
			
			sb.append("arg:");
			if (this.arg == null) {
				sb.append("null");
			} else {
				sb.append(this.arg);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}
		
		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			if (arg == null) {
				throw new org.apache.thrift.protocol.TProtocolException("Required field 'arg' was not present! Struct: " + toString());
			}
		}
		
		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}
		
		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}
		
		/**
		 * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			ARG((short) 1, "arg");
			
			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
			
			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}
			
			private final short _thriftId;
			private final String _fieldName;
			
			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}
			
			/**
			 * Find the _Fields constant that matches fieldId, or null if its not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 1: // ARG
						return ARG;
					default:
						return null;
				}
			}
			
			/**
			 * Find the _Fields constant that matches fieldId, throwing an exception
			 * if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}
			
			/**
			 * Find the _Fields constant that matches name, or null if its not found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}
			
			public short getThriftFieldId() {
				return _thriftId;
			}
			
			public String getFieldName() {
				return _fieldName;
			}
		}
		
	}
	
	public static class echo_result implements org.apache.thrift.TBase<echo_result, echo_result._Fields>, java.io.Serializable, Cloneable {
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("echo_result");
		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short) 0);
		
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(echo_result.class, metaDataMap);
		}
		
		// isset id assignments
		
		public String success; // required
		
		public echo_result() {
		}
		
		public echo_result(
				String success) {
			this();
			this.success = success;
		}
		
		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public echo_result(echo_result other) {
			if (other.isSetSuccess()) {
				this.success = other.success;
			}
		}
		
		public echo_result deepCopy() {
			return new echo_result(this);
		}
		
		public void clear() {
			this.success = null;
		}
		
		public String getSuccess() {
			return this.success;
		}
		
		public echo_result setSuccess(String success) {
			this.success = success;
			return this;
		}
		
		public void unsetSuccess() {
			this.success = null;
		}
		
		/**
		 * Returns true if field success is set (has been assigned a value) and false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}
		
		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}
		
		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case SUCCESS:
					if (value == null) {
						unsetSuccess();
					} else {
						setSuccess((String) value);
					}
					break;
				
			}
		}
		
		public Object getFieldValue(_Fields field) {
			switch (field) {
				case SUCCESS:
					return getSuccess();
				
			}
			throw new IllegalStateException();
		}
		
		/**
		 * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}
			
			switch (field) {
				case SUCCESS:
					return isSetSuccess();
			}
			throw new IllegalStateException();
		}
		
		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof echo_result)
				return this.equals((echo_result) that);
			return false;
		}
		
		public boolean equals(echo_result that) {
			if (that == null)
				return false;
			
			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			return 0;
		}
		
		public int compareTo(echo_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}
			
			int lastComparison = 0;
			echo_result typedOther = (echo_result) other;
			
			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(typedOther.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, typedOther.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}
		
		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}
		
		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			org.apache.thrift.protocol.TField field;
			iprot.readStructBegin();
			while (true) {
				field = iprot.readFieldBegin();
				if (field.type == org.apache.thrift.protocol.TType.STOP) {
					break;
				}
				switch (field.id) {
					case 0: // SUCCESS
						if (field.type == org.apache.thrift.protocol.TType.STRING) {
							this.success = iprot.readString();
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
				}
				iprot.readFieldEnd();
			}
			iprot.readStructEnd();
			
			// check for required fields of primitive type, which can't be checked in the validate method
			validate();
		}
		
		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			oprot.writeStructBegin(STRUCT_DESC);
			
			if (this.isSetSuccess()) {
				oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
				oprot.writeString(this.success);
				oprot.writeFieldEnd();
			}
			oprot.writeFieldStop();
			oprot.writeStructEnd();
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("echo_result(");
			boolean first = true;
			
			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}
		
		public void validate() throws org.apache.thrift.TException {
			// check for required fields
		}
		
		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}
		
		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}
		
		/**
		 * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");
			
			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
			
			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}
			
			private final short _thriftId;
			private final String _fieldName;
			
			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}
			
			/**
			 * Find the _Fields constant that matches fieldId, or null if its not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 0: // SUCCESS
						return SUCCESS;
					default:
						return null;
				}
			}
			
			/**
			 * Find the _Fields constant that matches fieldId, throwing an exception
			 * if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}
			
			/**
			 * Find the _Fields constant that matches name, or null if its not found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}
			
			public short getThriftFieldId() {
				return _thriftId;
			}
			
			public String getFieldName() {
				return _fieldName;
			}
		}
		
	}
	
}
