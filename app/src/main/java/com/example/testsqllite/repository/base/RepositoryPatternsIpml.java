package com.example.testsqllite.repository.base;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testsqllite.config.datasource.SqlLiteConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RepositoryPatternsIpml<T>  implements Repository<T>{

    private final String tableName;
    private final SQLiteDatabase readRepo;

    private final Class<T> clazz;
    private final SQLiteDatabase writeRepo;

    public  RepositoryPatternsIpml(SqlLiteConfig sqlLiteConfig,String tableName,Class<T> tClass){
            readRepo=sqlLiteConfig.getReadableDatabase();
            writeRepo=sqlLiteConfig.getWritableDatabase();
            this.tableName=tableName;
            this.clazz=tClass;
    }

    public  void create(T data) throws NoSuchFieldException, IllegalAccessException {
        List<String> proName= Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        ContentValues values = contentValues(proName,data);
        writeRepo.insert(tableName,null,values);
    }
    public List<T> findAll() throws IOException {
        String sql="SELECT * FROM "+tableName;
        @SuppressLint("Recycle") Cursor cursor= readRepo.rawQuery(sql,null);
        return  getDataSet(cursor,clazz);
    }

    public  List<T> findByField(Map<String,String> mapSearch) throws IOException {
        String sql="SELECT * FROM "+tableName ;
        String where=" where";
        AtomicInteger count= new AtomicInteger();
        AtomicReference<String> predicate= new AtomicReference<>(" ");
        mapSearch.forEach((key, value) ->{
                count.getAndIncrement();
                predicate.set( predicate.get()+ " " + key + " =" + value);
                if(count.get() !=mapSearch.size())
                    predicate.set(predicate.get() + " AND ");
        } );
        sql = sql+where+predicate.get();
        @SuppressLint("Recycle") Cursor cursor= readRepo.rawQuery(sql,null);
        return  getDataSet(cursor,clazz);

    }
    public   void update(int id,T data,Class<T> clazz,String tableName) throws NoSuchFieldException, IllegalAccessException {
        List<String> proName= Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        ContentValues contentValues=contentValues(proName,data);
        writeRepo.update(tableName,contentValues,"id=?",new String[]{String.valueOf(id)});
    }

    public List<T> search(Map<String,String> mapSearch,Map<String,String> mapSort) throws IOException {
        String sql="SELECT * FROM "+tableName ;
        String where=" where";
        AtomicInteger count= new AtomicInteger(0);
        AtomicReference<String> predicate= new AtomicReference<>(" ");
        mapSearch.forEach((key, value) ->{
            count.getAndIncrement();
            String patternSearch= "'%"+value+"%'";
            predicate.set( predicate.get()+ " " + key + " like " + patternSearch);
            if(count.get()!=mapSearch.size())
                predicate.set(predicate.get() + " AND ");
        } );
        String oderBy=" order by ";
        count.set(0);
        AtomicReference<String> sortQuery= new AtomicReference<>(" ");
        mapSort.forEach((key, value) ->{
            count.getAndIncrement();
            sortQuery.set(String.format(" %s %s ",key,value));
            if(count.get() != mapSort.size())
                sortQuery.set(sortQuery.get() + ", ");
        } );
        sql=sql+where+predicate+oderBy+sortQuery;
        @SuppressLint("Recycle") Cursor cursor= readRepo.rawQuery(sql,null);
        return  getDataSet(cursor,clazz);
    }
    public  ContentValues contentValues(List<String> proName,T data) throws NoSuchFieldException, IllegalAccessException {
        ContentValues values = new ContentValues();
        for (String name:proName){
            Field field= data.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object o=field.get(data);
            if(o instanceof  String)
                values.put(name,(String) o);
            if(o instanceof  Long)
                values.put(name,(Long) o);
            if(o instanceof  Integer)
                values.put(name,(Integer) o);
            if(o instanceof  Boolean)
                values.put(name,(Boolean) o );
            if(o instanceof Date)
                values.put(name, String.valueOf(o));
        }
        return values;
    }
    public  List<T> getDataSet(Cursor cursor,Class<T> clazz) throws IOException {
        List<T> result=new ArrayList<>();
        cursor.moveToFirst();
        int columnNumber=cursor.getColumnCount();
        while(!cursor.isAfterLast()) {
            Map<String,Object> map=new HashMap<>();
            for(int i=0;i<columnNumber;i++){
                String columnName=cursor.getColumnName(i);
                int index=cursor.getColumnIndex(columnName);
                int type=cursor.getType(index);
                switch (type) {
                    case Cursor.FIELD_TYPE_INTEGER: {
                        map.put(columnName, cursor.getInt(index));
                        break;
                    }
                    case Cursor.FIELD_TYPE_FLOAT: {
                        map.put(columnName, cursor.getFloat(index));
                        break;
                    }
                    case Cursor.FIELD_TYPE_STRING: {
                        map.put(columnName, cursor.getString(index));
                        break;
                    }
                    case Cursor.FIELD_TYPE_BLOB:
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        break;
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json=objectMapper.writeValueAsString(map);
            T obj = objectMapper.readValue(json, clazz);
            result.add(obj);
            cursor.moveToNext();
        }
        return result;
    }
}
