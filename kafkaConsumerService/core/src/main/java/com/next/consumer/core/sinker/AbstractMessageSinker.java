package com.next.consumer.core.sinker;

import com.next.optimus.common.messagebean.Message;

/**
 * KAFKA消息实现处理实现类
 * @author QDR
 *
 */
public abstract class AbstractMessageSinker{
	/**
	 * 监听分组分组
	 */
	public String consumerId;

	/**
	 * 执行处理消息中间件消息处理
	 * @param message
	 * @return
	 */
    public abstract boolean sink(Message message);

	public String getConsumerId() {
		return consumerId;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	
	
	
	/**
	 * 初始化调用方法
	 * @return
	 */
	public abstract void initSink();
	
	
	/**
	 * 关闭sink
	 * @return
	 */
	public abstract void shutdownSink();
}
