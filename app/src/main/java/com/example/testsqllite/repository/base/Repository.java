package com.example.testsqllite.repository.base;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Repository<T> {
     void create(T data) throws NoSuchFieldException, IllegalAccessException;
     List<T> findAll() throws IOException;
     List<T> findByField( Map<String,String> mapSearch) throws IOException ;
     void update(int id,T data,Class<T> clazz,String tableName)  throws NoSuchFieldException, IllegalAccessException;
     List<T> search(Map<String,String> mapSearch,Map<String,String> mapSort) throws IOException;
     ContentValues contentValues(List<String> proName, T data) throws NoSuchFieldException, IllegalAccessException;
     List<T> getDataSet(Cursor cursor, Class<T> clazz) throws IOException;
}
