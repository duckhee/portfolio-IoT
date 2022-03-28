package kr.co.won.config;

import kr.co.won.mqtt.MqttSubScript;
import kr.co.won.properties.MqttProperties;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import org.springframework.integration.support.DefaultErrorMessageStrategy;
import org.springframework.integration.support.ErrorMessageStrategy;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@RequiredArgsConstructor
public class MqttConfiguration {

    private final MqttProperties mqttProperties;
    private final MqttSubScript subScript;

    /**
     * Mqtt Client Factory Setting
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        // mqtt server url
        connectOptions.setServerURIs(new String[]{"tcp://" + mqttProperties.getHost() +":"+ mqttProperties.getPort()});
        // mqtt server user setting
        connectOptions.setUserName(mqttProperties.getUserName());
        // mqtt server user password information
        connectOptions.setPassword(mqttProperties.getUserPassword().toCharArray());
        // connection session clear
        connectOptions.setCleanSession(true);
//        connectOptions.setAutomaticReconnect(true);
        connectOptions.setConnectionTimeout(1000);

        // mqtt client factory option setting
        defaultMqttPahoClientFactory.setConnectionOptions(connectOptions);

        return defaultMqttPahoClientFactory;
    }

    /**
     * Mqtt Inbound Channel Setting
     */
    @Bean
    public MessageChannel mqttInBoundChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    @Bean
    public MessageProducer mqttInBound() {
        MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter =
                new MqttPahoMessageDrivenChannelAdapter("PORTFOLIO-Mqtt-In", mqttPahoClientFactory(), mqttProperties.getBaseTopic());
        mqttPahoMessageDrivenChannelAdapter.setCompletionTimeout(5000);
        mqttPahoMessageDrivenChannelAdapter.setConverter(new DefaultPahoMessageConverter());
        mqttPahoMessageDrivenChannelAdapter.setQos(2);
        mqttPahoMessageDrivenChannelAdapter.setOutputChannel(mqttInBoundChannel());
        return mqttPahoMessageDrivenChannelAdapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInBoundChannel")
    public MessageHandler mqttInBoundMessageHandler() {

        return subScript.subscript();
    }

    @Bean
    public IntegrationFlow mqttOutBoundFlow() {
        return flow
                -> flow.handle(
                new MqttPahoMessageHandler("tcp://" + mqttProperties.getHost() + ":" + mqttProperties.getPort(), "PORTFOLIO-Mqtt")
        );
    }

    @Bean
    public MessageChannel mqttOutBoundChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    /**
     * Service Activator 는 서비스를 메시징 시스템에 연결하기 위한 엔드포인트이다. 입력 채널이 설정되어 있어야 하고 서비스가 값을 리턴하도록 구현했다면 출력 채널도 설정해야 한다.
     */
    @Bean
    @ServiceActivator(outputChannel = "mqttOutBoundChannel", inputChannel = "mqttInBoundChannel")
    public MessageHandler mqttOutputHandler() {
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler("PORTFOLIO-Mqtt-Out", mqttPahoClientFactory());
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic("#");
        mqttPahoMessageHandler.setDefaultRetained(true);

        return mqttPahoMessageHandler;
    }

}
