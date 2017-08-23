package org.wss.weixin.common.jedis;

public class RedisConfig {
	//最大连接数
	private static int MAX_TOTAL=200;
	//最大空闲链接数
	private static int MAX_IDLE=50;
	//最大等待时间
	private static int MAX_WAIT_MILLIS =300;
	private static int TIMEOUT=2000;
	private static int RETRY_COUNT=5;
	
	public static int getMaxTotal() {
		return MAX_TOTAL;
	}

	public static int getMaxIdle() {
		return MAX_IDLE;
	}

	public static long getMaxWaitMillis() {
		return MAX_WAIT_MILLIS;
	}

	public static int getTimeout() {
		return TIMEOUT;
	}

	public static int getRetryNum() {
		return RETRY_COUNT;
	}

}
