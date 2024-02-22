package com.arep.parcial;

import java.net.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerBack {

    public static String headerOK = "HTTP/1.1 200 OK\r\n"
    + "Content-Type: text/html\r\n"
    + "\r\n";

    public static String httpError = "HTTP/1.1 400 Not found\r\n"
    + "Content-Type: text/html\r\n"
    + "\r\n"
    + "<!DOCTYPE html>\r\n" + //
            "<html>\r\n" + //
            "    <head>\r\n" + //
            "        <title>Page Not Found</title>\r\n" + //
            "        <meta charset=\"UTF-8\">\r\n" + //
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
            "    </head>\r\n" + //
            "    <body>\r\n" + //
                    "Page Not Found\r\n"+
            "    </body>\r\n" + //
            "</html>";
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 45000.");
            System.exit(1);
        }

        Socket clientSocket = null;

        boolean isRunning = true;

        while (isRunning) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            String path = "";
            boolean first = true;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if(first){
                    path = inputLine;
                    first = false;
                    System.out.println(path);
                }
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = response(path);
            
            out.println(outputLine);
            out.close();
            in.close();
        }
        
        clientSocket.close();
        serverSocket.close();
    }
    private static String response(String path) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(path.startsWith("/compreflex")){
            String command = path.split("=")[1];
            return headerOK + resolve(command);
        } else{
            return httpError;
        }
    }
    private static String resolve(String command) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(command.startsWith("Class")){
            String className = command.substring(6, -1);
            System.out.println(className);
            Class c = Class.forName(className);
            Field[] fields = c.getDeclaredFields();
            Method[] methods = c.getDeclaredMethods();

        } else if(command.startsWith("invoke")){
            String parameters = command.substring(7);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1].substring(0, -1);

            System.out.println(className);
            System.out.println(methodName);

            Class c = Class.forName(className);
            Method method = c.getMethod(methodName);
            Object result = method.invoke(null);
            return result.toString();

        } else if(command.startsWith("unaryInvoke")){
            String parameters = command.substring(12);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1];
            String paramType = parameters.split(",")[2];
            String value = parameters.split(",")[3].substring(0, -1);

            System.out.println(className);
            System.out.println(methodName);
            System.out.println(paramType);
            System.out.println(value);

            Object arg = null;

            Class c = Class.forName(className);

            if(paramType.equals("int")){
                arg = Integer.parseInt(value);
            }else if(paramType.equals("String")){
                arg = value;
            } else if(paramType.equals("double")){
                arg = Double.parseDouble(value);
            }

            Method method = c.getMethod(methodName);
            Object result = method.invoke(arg);
            return result.toString();

        } else if(command.startsWith("binatyInvoke")){
            String parameters = command.substring(13);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1];
            String paramType1 = parameters.split(",")[2];
            String value1 = parameters.split(",")[3];
            String paramType2 = parameters.split(",")[4];
            String value2 = parameters.split(",")[5].substring(0, -1);

            System.out.println(className);
            System.out.println(methodName);
            System.out.println(paramType1);
            System.out.println(value1);
            System.out.println(paramType2);
            System.out.println(value2);

            Object arg1 = null;
            Object arg2 = null;

            Class c = Class.forName(className);

            if(paramType1.equals("int")){
                arg1 = Integer.parseInt(value1);
            }else if(paramType1.equals("String")){
                arg1 = value1;
            } else if(paramType1.equals("double")){
                arg1 = Double.parseDouble(value1);
            }

            if(paramType2.equals("int")){
                arg2 = Integer.parseInt(value2);
            }else if(paramType2.equals("String")){
                arg2 = value2;
            } else if(paramType2.equals("double")){
                arg2 = Double.parseDouble(value2);
            }

            Method method = c.getMethod(methodName);
            Object result = method.invoke(arg1,arg2);
            return result.toString();
        }
        return httpError;
    }
}
