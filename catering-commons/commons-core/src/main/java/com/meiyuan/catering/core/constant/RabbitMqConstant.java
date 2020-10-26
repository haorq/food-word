package com.meiyuan.catering.core.constant;

/**
 * RabbitMQ常量定义
 *
 * @author gz
 * @version v1.0.0
 * @date 2020/03/23 15:26
 */
public class RabbitMqConstant {

    /**
     * 延迟消息队列最大的最大延迟值：(1L<<32) - 1L = 4294967295
     */
    public static Long MAX_X_DELAY_TTL = (1L << 32) - 1L;

    public static class PluginExchangeTypes {
        public static String DELAY = "x-delayed-message";
    }

    public static class MessageHeaders {
        /**
         * 向延迟交换机推送消息时使用，延迟多少毫秒
         */
        public static String X_DELAY = "x-delay";
    }

    public static class MessageProperties {
        public static String CONTENT_TYPE = "content_type";
        public static String CONTENT_ENCODING = "content_encoding";
        public static String PRIORITY = "priority";
        public static String CORRELATION_ID = "correlation_id";
        public static String REPLY_TO = "reply_to";
        public static String EXPIRATION = "expiration";
        public static String MESSAGE_ID = "message_id";
        public static String TIMESTAMP = "timestamp";
        public static String TYPE = "type";
        public static String USER_ID = "user_id";
        public static String APP_ID = "app_id";
        public static String CLUSTER_ID = "cluster_id";
    }

    public static class ExchangeArguments {
        public static String ALTERNATE_EXCHANGE = "alternate-exchange";
    }

    public static class PluginExchangeArguments {
        public static String X_DELAYED_TYPE = "x-delayed-type";
    }

    public static class QueueArguments {
        public static String MESSAGE_TTL = "x-message-ttl";
        public static String AUTO_EXPIRE = "x-expires";
        public static String MAX_LENGTH = "x-max-length";
        public static String MAX_LENGTH_BYTES = "x-max-length-bytes";
        public static String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
        public static String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
        public static String PRIORITY_QUEUE_SUPPORT = "x-max-priority";
    }
}
