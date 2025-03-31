package com.university.config;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.cdi.SessionFactoryProvider;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@ApplicationScoped
public class MyBatisProducer {

    @Resource(lookup = "java:jboss/datasources/UniversityFileDS")
    private DataSource dataSource;

    @Produces
    @ApplicationScoped
    @SessionFactoryProvider
    public SqlSessionFactory produceFactory() {
        TransactionFactory transactionFactory = new ManagedTransactionFactory();

        Environment environment = new Environment("development", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);

        configuration.getTypeAliasRegistry().registerAlias("Student", com.university.entity.Student.class);
        configuration.getTypeAliasRegistry().registerAlias("Club", com.university.entity.Club.class);
        configuration.getTypeAliasRegistry().registerAlias("Course", com.university.entity.Course.class);

        configuration.addMappers("com.university.mybatis");
        
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
