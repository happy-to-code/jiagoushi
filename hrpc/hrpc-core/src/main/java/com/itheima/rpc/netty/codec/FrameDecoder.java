package com.itheima.rpc.netty.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 固定长度字段存消息长度解码器
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {
	public FrameDecoder() {
		super(Integer.MAX_VALUE, 0, 4, 0, 4);
	}
}
