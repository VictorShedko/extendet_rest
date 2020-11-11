package com.epam.esm.gift_extended.appconfig;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class AppConfigurator {

    private static SessionFactory sessionFactory ;

    public AppConfigurator() {
        buildSessionFactory();
    }

    private void  buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            this.sessionFactory = configuration.buildSessionFactory();

        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }



    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static EntityManager getEntityManager(){
        return sessionFactory.createEntityManager();
    }

    public static void shutdown() {
        // Чистит кеш и закрывает соединение с БД
        sessionFactory.close();
    }

}
