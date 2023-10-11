package com.capitaworld.commonutil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class DatabaseConnection {

    //private static Connection connection =null;


    public static Connection getConnection(Connection connection, String environment) throws IOException
    {
        List<String> dbconDetails=CommonMethods.getPropertyValue(environment);
        if(connection==null)
        {
            System.out.println("Database Connection Created.");
            connection= getConnection(dbconDetails.get(0), dbconDetails.get(1), dbconDetails.get(2));
        }else {
            System.out.println("Connection object created.");

        }
        return connection;
    }

    private static Connection getConnection(String db_name,String user_name,String password)
    {
        Connection con=null;
        try {
            Class.forName("org.postgresql.Driver");
            //List<String>dbconDetails=getPropertyValue();
            con = DriverManager.getConnection(db_name,user_name, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

}
