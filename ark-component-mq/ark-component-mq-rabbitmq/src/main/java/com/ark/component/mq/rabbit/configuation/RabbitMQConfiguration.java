package com.ark.component.mq.rabbit.configuation;

import com.ark.component.mq.configuation.MQConfiguration;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * MQ配置
 * @author jc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "ark.component.mq.rabbitmq")
public class RabbitMQConfiguration extends MQConfiguration {

    /**
     * Login user to authenticate to the broker.
     */
    private String username = "guest";

    /**
     * Login to authenticate against the broker.
     */
    private String password = "guest";

    /**
     * Virtual host to use when connecting to the broker.
     */
    private String virtualHost;

    /**
     * 交换机配置，通过提前配置好使用的交换和类型。
     * 在使用Rabbit发送消息的时候就可以直接使用统一API，交换机类型根据交换机名称获取
     */
    private List<Exchange> exchanges = Collections.emptyList();


    private static final int DEFAULT_PORT = 5672;

    private static final int DEFAULT_PORT_SECURE = 5671;

    /**
     * SSL configuration.
     */
    private final RabbitMQConfiguration.Ssl ssl = new RabbitMQConfiguration.Ssl();

    /**
     * Requested heartbeat timeout; zero for none. If a duration suffix is not specified,
     * seconds will be used.
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration requestedHeartbeat;

    /**
     * Number of channels per connection requested by the client. Use 0 for unlimited.
     */
    private int requestedChannelMax = 2047;

    /**
     * Whether to enable publisher returns.
     */
    private boolean publisherReturns;

    /**
     * Type of publisher confirms to use.
     */
    private CachingConnectionFactory.ConfirmType publisherConfirmType = CachingConnectionFactory.ConfirmType.CORRELATED;

    /**
     * Connection timeout. Set it to zero to wait forever.
     */
    private Duration connectionTimeout;

    /**
     * Cache configuration.
     */
    private final RabbitMQConfiguration.Cache cache = new RabbitMQConfiguration.Cache();

    private List<RabbitMQConfiguration.Address> parsedAddresses;

    /**
     * Returns the comma-separated addresses or a single address ({@code host:port})
     * created from the configured host and port if no addresses have been set.
     * @return the addresses
     */
    public String determineAddresses() {
        List<String> addressStrings = new ArrayList<>();
        List<Address> addresses = parseAddresses(this.getServer());
        for (Address parsedAddress : addresses) {
            addressStrings.add(parsedAddress.host + ":" + parsedAddress.port);
        }
        return StringUtils.collectionToCommaDelimitedString(addressStrings);
    }

    private List<RabbitMQConfiguration.Address> parseAddresses(String addresses) {
        List<RabbitMQConfiguration.Address> parsedAddresses = new ArrayList<>();
        for (String address : StringUtils.commaDelimitedListToStringArray(addresses)) {
            parsedAddresses.add(new RabbitMQConfiguration.Address(address, Optional.ofNullable(getSsl().getEnabled()).orElse(false)));
        }
        return parsedAddresses;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * If addresses have been set and the first address has a username it is returned.
     * Otherwise returns the result of calling {@code getUsername()}.
     * @return the username
     * @see #getUsername()
     */
    public String determineUsername() {
        RabbitMQConfiguration.Address address = this.parsedAddresses.get(0);
        return (address.username != null) ? address.username : this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * If addresses have been set and the first address has a password it is returned.
     * Otherwise returns the result of calling {@code getPassword()}.
     * @return the password or {@code null}
     * @see #getPassword()
     */
    public String determinePassword() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getPassword();
        }
        RabbitMQConfiguration.Address address = this.parsedAddresses.get(0);
        return (address.password != null) ? address.password : getPassword();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RabbitMQConfiguration.Ssl getSsl() {
        return this.ssl;
    }

    public String getVirtualHost() {
        return this.virtualHost;
    }

    /**
     * If addresses have been set and the first address has a virtual host it is returned.
     * Otherwise returns the result of calling {@code getVirtualHost()}.
     * @return the virtual host or {@code null}
     * @see #getVirtualHost()
     */
    public String determineVirtualHost() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getVirtualHost();
        }
        RabbitMQConfiguration.Address address = this.parsedAddresses.get(0);
        return (address.virtualHost != null) ? address.virtualHost : getVirtualHost();
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = "".equals(virtualHost) ? "/" : virtualHost;
    }

    public Duration getRequestedHeartbeat() {
        return this.requestedHeartbeat;
    }

    public void setRequestedHeartbeat(Duration requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public int getRequestedChannelMax() {
        return this.requestedChannelMax;
    }

    public void setRequestedChannelMax(int requestedChannelMax) {
        this.requestedChannelMax = requestedChannelMax;
    }

    public boolean isPublisherReturns() {
        return this.publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }

    public Duration getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setPublisherConfirmType(CachingConnectionFactory.ConfirmType publisherConfirmType) {
        this.publisherConfirmType = publisherConfirmType;
    }

    public CachingConnectionFactory.ConfirmType getPublisherConfirmType() {
        return this.publisherConfirmType;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public RabbitMQConfiguration.Cache getCache() {
        return this.cache;
    }
    public class Ssl {

        /**
         * Whether to enable SSL support. Determined automatically if an address is
         * provided with the protocol (amqp:// vs. amqps://).
         */
        private Boolean enabled;

        /**
         * Path to the key store that holds the SSL certificate.
         */
        private String keyStore;

        /**
         * Key store type.
         */
        private String keyStoreType = "PKCS12";

        /**
         * Password used to access the key store.
         */
        private String keyStorePassword;

        /**
         * Trust store that holds SSL certificates.
         */
        private String trustStore;

        /**
         * Trust store type.
         */
        private String trustStoreType = "JKS";

        /**
         * Password used to access the trust store.
         */
        private String trustStorePassword;

        /**
         * SSL algorithm to use. By default, configured by the Rabbit client library.
         */
        private String algorithm;

        /**
         * Whether to enable server side certificate validation.
         */
        private boolean validateServerCertificate = true;

        /**
         * Whether to enable hostname verification.
         */
        private boolean verifyHostname = true;

        public Boolean getEnabled() {
            return this.enabled;
        }

        /**
         * Returns whether SSL is enabled from the first address, or the configured ssl
         * enabled flag if no addresses have been set.
         * @return whether ssl is enabled
         * @see #getEnabled() ()
         */
        public boolean determineEnabled() {
            boolean defaultEnabled = Optional.ofNullable(getEnabled()).orElse(false);
            if (CollectionUtils.isEmpty(RabbitMQConfiguration.this.parsedAddresses)) {
                return defaultEnabled;
            }
            RabbitMQConfiguration.Address address = RabbitMQConfiguration.this.parsedAddresses.get(0);
            return address.determineSslEnabled(defaultEnabled);
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getKeyStore() {
            return this.keyStore;
        }

        public void setKeyStore(String keyStore) {
            this.keyStore = keyStore;
        }

        public String getKeyStoreType() {
            return this.keyStoreType;
        }

        public void setKeyStoreType(String keyStoreType) {
            this.keyStoreType = keyStoreType;
        }

        public String getKeyStorePassword() {
            return this.keyStorePassword;
        }

        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }

        public String getTrustStore() {
            return this.trustStore;
        }

        public void setTrustStore(String trustStore) {
            this.trustStore = trustStore;
        }

        public String getTrustStoreType() {
            return this.trustStoreType;
        }

        public void setTrustStoreType(String trustStoreType) {
            this.trustStoreType = trustStoreType;
        }

        public String getTrustStorePassword() {
            return this.trustStorePassword;
        }

        public void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }

        public String getAlgorithm() {
            return this.algorithm;
        }

        public void setAlgorithm(String sslAlgorithm) {
            this.algorithm = sslAlgorithm;
        }

        public boolean isValidateServerCertificate() {
            return this.validateServerCertificate;
        }

        public void setValidateServerCertificate(boolean validateServerCertificate) {
            this.validateServerCertificate = validateServerCertificate;
        }

        public boolean getVerifyHostname() {
            return this.verifyHostname;
        }

        public void setVerifyHostname(boolean verifyHostname) {
            this.verifyHostname = verifyHostname;
        }

    }

    public static class Cache {

        private final RabbitMQConfiguration.Cache.Channel channel = new RabbitMQConfiguration.Cache.Channel();

        private final RabbitMQConfiguration.Cache.Connection connection = new RabbitMQConfiguration.Cache.Connection();

        public RabbitMQConfiguration.Cache.Channel getChannel() {
            return this.channel;
        }

        public RabbitMQConfiguration.Cache.Connection getConnection() {
            return this.connection;
        }

        public static class Channel {

            /**
             * Number of channels to retain in the cache. When "check-timeout" > 0, max
             * channels per connection.
             */
            private Integer size;

            /**
             * Duration to wait to obtain a channel if the cache size has been reached. If
             * 0, always create a new channel.
             */
            private Duration checkoutTimeout;

            public Integer getSize() {
                return this.size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Duration getCheckoutTimeout() {
                return this.checkoutTimeout;
            }

            public void setCheckoutTimeout(Duration checkoutTimeout) {
                this.checkoutTimeout = checkoutTimeout;
            }

        }

        public static class Connection {

            /**
             * Connection factory cache mode.
             */
            private CachingConnectionFactory.CacheMode mode = CachingConnectionFactory.CacheMode.CHANNEL;

            /**
             * Number of connections to cache. Only applies when mode is CONNECTION.
             */
            private Integer size;

            public CachingConnectionFactory.CacheMode getMode() {
                return this.mode;
            }

            public void setMode(CachingConnectionFactory.CacheMode mode) {
                this.mode = mode;
            }

            public Integer getSize() {
                return this.size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

        }

    }

    public enum ContainerType {

        /**
         * Container where the RabbitMQ consumer dispatches messages to an invoker thread.
         */
        SIMPLE,

        /**
         * Container where the listener is invoked directly on the RabbitMQ consumer
         * thread.
         */
        DIRECT

    }

    public static class Listener {

        /**
         * Listener container type.
         */
        private RabbitMQConfiguration.ContainerType type = RabbitMQConfiguration.ContainerType.SIMPLE;

        private final RabbitMQConfiguration.SimpleContainer simple = new RabbitMQConfiguration.SimpleContainer();

        private final RabbitMQConfiguration.DirectContainer direct = new RabbitMQConfiguration.DirectContainer();

        public RabbitMQConfiguration.ContainerType getType() {
            return this.type;
        }

        public void setType(RabbitMQConfiguration.ContainerType containerType) {
            this.type = containerType;
        }

        public RabbitMQConfiguration.SimpleContainer getSimple() {
            return this.simple;
        }

        public RabbitMQConfiguration.DirectContainer getDirect() {
            return this.direct;
        }

    }

    public abstract static class AmqpContainer {

        /**
         * Whether to start the container automatically on startup.
         */
        private boolean autoStartup = true;

        /**
         * Acknowledge mode of container.
         */
        private AcknowledgeMode acknowledgeMode;

        /**
         * Maximum number of unacknowledged messages that can be outstanding at each
         * consumer.
         */
        private Integer prefetch;

        /**
         * Whether rejected deliveries are re-queued by default.
         */
        private Boolean defaultRequeueRejected;

        /**
         * How often idle container events should be published.
         */
        private Duration idleEventInterval;

        /**
         * Optional properties for a retry interceptor.
         */
        private final RabbitMQConfiguration.ListenerRetry retry = new RabbitMQConfiguration.ListenerRetry();

        public boolean isAutoStartup() {
            return this.autoStartup;
        }

        public void setAutoStartup(boolean autoStartup) {
            this.autoStartup = autoStartup;
        }

        public AcknowledgeMode getAcknowledgeMode() {
            return this.acknowledgeMode;
        }

        public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
            this.acknowledgeMode = acknowledgeMode;
        }

        public Integer getPrefetch() {
            return this.prefetch;
        }

        public void setPrefetch(Integer prefetch) {
            this.prefetch = prefetch;
        }

        public Boolean getDefaultRequeueRejected() {
            return this.defaultRequeueRejected;
        }

        public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
            this.defaultRequeueRejected = defaultRequeueRejected;
        }

        public Duration getIdleEventInterval() {
            return this.idleEventInterval;
        }

        public void setIdleEventInterval(Duration idleEventInterval) {
            this.idleEventInterval = idleEventInterval;
        }

        public abstract boolean isMissingQueuesFatal();

        public RabbitMQConfiguration.ListenerRetry getRetry() {
            return this.retry;
        }

    }

    /**
     * Configuration properties for {@code SimpleMessageListenerContainer}.
     */
    public static class SimpleContainer extends RabbitMQConfiguration.AmqpContainer {

        /**
         * Minimum number of listener invoker threads.
         */
        private Integer concurrency;

        /**
         * Maximum number of listener invoker threads.
         */
        private Integer maxConcurrency;

        /**
         * Batch size, expressed as the number of physical messages, to be used by the
         * container.
         */
        private Integer batchSize;

        /**
         * Whether to fail if the queues declared by the container are not available on
         * the broker and/or whether to stop the container if one or more queues are
         * deleted at runtime.
         */
        private boolean missingQueuesFatal = true;

        public Integer getConcurrency() {
            return this.concurrency;
        }

        public void setConcurrency(Integer concurrency) {
            this.concurrency = concurrency;
        }

        public Integer getMaxConcurrency() {
            return this.maxConcurrency;
        }

        public void setMaxConcurrency(Integer maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }

        public Integer getBatchSize() {
            return this.batchSize;
        }

        public void setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
        }

        @Override
        public boolean isMissingQueuesFatal() {
            return this.missingQueuesFatal;
        }

        public void setMissingQueuesFatal(boolean missingQueuesFatal) {
            this.missingQueuesFatal = missingQueuesFatal;
        }

    }

    /**
     * Configuration properties for {@code DirectMessageListenerContainer}.
     */
    public static class DirectContainer extends RabbitMQConfiguration.AmqpContainer {

        /**
         * Number of consumers per queue.
         */
        private Integer consumersPerQueue;

        /**
         * Whether to fail if the queues declared by the container are not available on
         * the broker.
         */
        private boolean missingQueuesFatal = false;

        public Integer getConsumersPerQueue() {
            return this.consumersPerQueue;
        }

        public void setConsumersPerQueue(Integer consumersPerQueue) {
            this.consumersPerQueue = consumersPerQueue;
        }

        @Override
        public boolean isMissingQueuesFatal() {
            return this.missingQueuesFatal;
        }

        public void setMissingQueuesFatal(boolean missingQueuesFatal) {
            this.missingQueuesFatal = missingQueuesFatal;
        }

    }

    public static class Template {

        private final RabbitMQConfiguration.Retry retry = new RabbitMQConfiguration.Retry();

        /**
         * Whether to enable mandatory messages.
         */
        private Boolean mandatory;

        /**
         * Timeout for `receive()` operations.
         */
        private Duration receiveTimeout;

        /**
         * Timeout for `sendAndReceive()` operations.
         */
        private Duration replyTimeout;

        /**
         * Name of the default exchange to use for send operations.
         */
        private String exchange = "";

        /**
         * Value of a default routing key to use for send operations.
         */
        private String routingKey = "";

        /**
         * Name of the default queue to receive messages from when none is specified
         * explicitly.
         */
        private String defaultReceiveQueue;

        public RabbitMQConfiguration.Retry getRetry() {
            return this.retry;
        }

        public Boolean getMandatory() {
            return this.mandatory;
        }

        public void setMandatory(Boolean mandatory) {
            this.mandatory = mandatory;
        }

        public Duration getReceiveTimeout() {
            return this.receiveTimeout;
        }

        public void setReceiveTimeout(Duration receiveTimeout) {
            this.receiveTimeout = receiveTimeout;
        }

        public Duration getReplyTimeout() {
            return this.replyTimeout;
        }

        public void setReplyTimeout(Duration replyTimeout) {
            this.replyTimeout = replyTimeout;
        }

        public String getExchange() {
            return this.exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getRoutingKey() {
            return this.routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }

        public String getDefaultReceiveQueue() {
            return this.defaultReceiveQueue;
        }

        public void setDefaultReceiveQueue(String defaultReceiveQueue) {
            this.defaultReceiveQueue = defaultReceiveQueue;
        }

    }

    public static class Retry {

        /**
         * Whether publishing retries are enabled.
         */
        private boolean enabled;

        /**
         * Maximum number of attempts to deliver a message.
         */
        private int maxAttempts = 3;

        /**
         * Duration between the first and second attempt to deliver a message.
         */
        private Duration initialInterval = Duration.ofMillis(1000);

        /**
         * Multiplier to apply to the previous retry interval.
         */
        private double multiplier = 1.0;

        /**
         * Maximum duration between attempts.
         */
        private Duration maxInterval = Duration.ofMillis(10000);

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxAttempts() {
            return this.maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public Duration getInitialInterval() {
            return this.initialInterval;
        }

        public void setInitialInterval(Duration initialInterval) {
            this.initialInterval = initialInterval;
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        public Duration getMaxInterval() {
            return this.maxInterval;
        }

        public void setMaxInterval(Duration maxInterval) {
            this.maxInterval = maxInterval;
        }

    }

    public static class ListenerRetry extends RabbitMQConfiguration.Retry {

        /**
         * Whether retries are stateless or stateful.
         */
        private boolean stateless = true;

        public boolean isStateless() {
            return this.stateless;
        }

        public void setStateless(boolean stateless) {
            this.stateless = stateless;
        }

    }

    private static final class Address {

        private static final String PREFIX_AMQP = "amqp://";

        private static final String PREFIX_AMQP_SECURE = "amqps://";

        private String host;

        private int port;

        private String username;

        private String password;

        private String virtualHost;

        private Boolean secureConnection;

        private Address(String input, boolean sslEnabled) {
            input = input.trim();
            input = trimPrefix(input);
            input = parseUsernameAndPassword(input);
            input = parseVirtualHost(input);
            parseHostAndPort(input, sslEnabled);
        }

        private String trimPrefix(String input) {
            if (input.startsWith(PREFIX_AMQP_SECURE)) {
                this.secureConnection = true;
                return input.substring(PREFIX_AMQP_SECURE.length());
            }
            if (input.startsWith(PREFIX_AMQP)) {
                this.secureConnection = false;
                return input.substring(PREFIX_AMQP.length());
            }
            return input;
        }

        private String parseUsernameAndPassword(String input) {
            if (input.contains("@")) {
                String[] split = StringUtils.split(input, "@");
                String creds = split[0];
                input = split[1];
                split = StringUtils.split(creds, ":");
                this.username = split[0];
                if (split.length > 0) {
                    this.password = split[1];
                }
            }
            return input;
        }

        private String parseVirtualHost(String input) {
            int hostIndex = input.indexOf('/');
            if (hostIndex >= 0) {
                this.virtualHost = input.substring(hostIndex + 1);
                if (this.virtualHost.isEmpty()) {
                    this.virtualHost = "/";
                }
                input = input.substring(0, hostIndex);
            }
            return input;
        }

        private void parseHostAndPort(String input, boolean sslEnabled) {
            int portIndex = input.indexOf(':');
            if (portIndex == -1) {
                this.host = input;
                this.port = (determineSslEnabled(sslEnabled)) ? DEFAULT_PORT_SECURE : DEFAULT_PORT;
            }
            else {
                this.host = input.substring(0, portIndex);
                this.port = Integer.parseInt(input.substring(portIndex + 1));
            }
        }

        private boolean determineSslEnabled(boolean sslEnabled) {
            return (this.secureConnection != null) ? this.secureConnection : sslEnabled;
        }

    }

    @Data
    public static class Exchange {

        /**
         * 交换机名字
         */
        private String name;

        /**
         * 交换机类型
         */
        private BuiltinExchangeType type;
    }

}
