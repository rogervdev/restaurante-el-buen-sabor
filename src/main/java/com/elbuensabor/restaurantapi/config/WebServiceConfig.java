package com.elbuensabor.restaurantapi.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig { // <-- Solo quitamos 'extends WsConfigurerAdapter'

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*"); // La ruta para SOAP será /ws
    }

    @Bean(name = "restaurante")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema restauranteSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("RestaurantePort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://elbuensabor.com/schema");
        wsdl11Definition.setSchema(restauranteSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema restauranteSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/restaurante.xsd"));
    }
}