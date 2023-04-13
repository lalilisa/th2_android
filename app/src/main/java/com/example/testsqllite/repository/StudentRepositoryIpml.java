package com.example.testsqllite.repository;

import com.example.testsqllite.config.datasource.SqlLiteConfig;
import com.example.testsqllite.model.Student;
import com.example.testsqllite.repository.base.RepositoryPatternsIpml;

public class StudentRepositoryIpml  extends RepositoryPatternsIpml<Student> implements SutudentRepository {

    public StudentRepositoryIpml(SqlLiteConfig sqlLiteConfig, String tableName, Class<Student> studentClass){
        super(sqlLiteConfig, tableName, studentClass);
    }
}
