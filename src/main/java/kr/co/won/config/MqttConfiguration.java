package kr.co.won.config;

import kr.co.won.mqtt.MqttSubScript;
import kr.co.won.properties.MqttProperties;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dispatcher.LoadBalancingStrategy;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@RequiredArgsConstructor
public class MqttConfiguration {

    private final MqttProperties mqttProperties;

    /**
     * Mqtt Client Factory Setting
     */
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        // mqtt server url
        connectOptions.setServerURIs(new String[]{"tcp://" + mqttProperties.getHost() + mqttProperties.getPort()});
        // mqtt server user setting
        connectOptions.setUserName(mqttProperties.getUserName());
        connectOptions.setPassword(mqttProperties.getUserPassword().toCharArray());
        // connection session clear
        connectOptions.setCleanSession(true);
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setConnectionTimeout(1000);
        // mqtt client factory option setting
        defaultMqttPahoClientFactory.setConnectionOptions(connectOptions);
        return defaultMqttPahoClientFactory;
    }

    /**
     * Mqtt Inbound Channel Setting
     */
    public MessageChannel mqttInBoundChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    public MessageProducer mqttInBound() {
        MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter =
                new MqttPahoMessageDrivenChannelAdapter("PORTFOLIO-Mqtt-In", mqttPahoClientFactory(), mqttProperties.getBaseTopic());
        mqttPahoMessageDrivenChannelAdapter.setCompletionTimeout(5000);
        mqttPahoMessageDrivenChannelAdapter.setConverter(new DefaultPahoMessageConverter());
        mqttPahoMessageDrivenChannelAdapter.setQos(2);
        return mqttPahoMessageDrivenChannelAdapter;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInBoundMessageHandler() {
        MqttSubScript subScript = null;
        return subScript.subscript();
    }

    public IntegrationFlow mqttOutBoundFlow() {
        return flow
                -> flow.handle(
                new MqttPahoMessageHandler("tcp://" + mqttProperties.getHost() + ":" + mqttProperties.getPort(), "PORTFOLIO-Mqtt")
        );
    }

    public MessageChannel mqttOutBoundChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    @ServiceActivator(outputChannel = "mqttOutputChannel", inputChannel = "mqttInputChannel")
    public MessageHandler mqttOutputHandler() {
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler("PORTFOLIO-Mqtt-Out", mqttPahoClientFactory());
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic("#");
        mqttPahoMessageHandler.setDefaultRetained(true);
        return mqttPahoMessageHandler;
    }

}
