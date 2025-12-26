package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class LaterApplication {
    public static void main(String[] args) throws LifecycleException {
        // Tomcat Context
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector();
        Context tomcatContext = tomcat.addContext(
                "",
                null
        );

        // Application Context
        AnnotationConfigWebApplicationContext applicationContext =
                new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(
                tomcatContext.getServletContext()
        );
        applicationContext.scan("ru.practicum");
        applicationContext.refresh(); // Load Spring Context

        // Tomcat port
        int port = applicationContext.getEnvironment()
                .getProperty("server.port", Integer.class);
        tomcat.getConnector().setPort(port);


        // Dispatcher Servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        Wrapper dispatcherWrapper = tomcat.addServlet(
                tomcatContext,
                "dispatcherServlet",
                dispatcherServlet
        );
        dispatcherWrapper.addMapping("/");
        dispatcherWrapper.setLoadOnStartup(1);

        // Run
        tomcat.start();
    }
}
