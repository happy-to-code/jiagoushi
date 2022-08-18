package com.itheima.rpc.netty.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * LengthFieldPrepender 长度字段前置器
 * 固定长度确定内容长度解码器（一次解码）
 */
public class FrameEncoder extends LengthFieldPrepender {
	public FrameEncoder() {
		super(4);
	}
}
