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
                    path = inputLine.split(" ")[1];
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
        System.out.println("Resolviendo");
        if(command.startsWith("Class")){
            String className = command.substring(6);

            int length = className.length();
            className = className.substring(0, length-1);

            System.out.println(className);
            Class c = Class.forName(className);
            Field[] fields = c.getDeclaredFields();
            String fieldsString = "["; 
            for(Field f : fields){
                System.out.println(f.toString());
                fieldsString = fieldsString + f.toString() + ", ";
            }
            fieldsString = fieldsString + "]";
            Method[] methods = c.getDeclaredMethods();
            String methodsString = "[";
            for(Method m : methods){
                System.out.println(m.toString());
                methodsString = methodsString + m.toString() + ", ";
            }
            methodsString = methodsString + "]";
            String response = "{Fields: " + fieldsString + ", " + "Methods: " + methodsString + "}";
            return response;

        } else if(command.startsWith("invoke")){
            String parameters = command.substring(7);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1].substring(3);
            int length = methodName.length();
            methodName = methodName.substring(0, length-1);

            System.out.println(className);
            System.out.println(methodName);

            Class c = Class.forName(className);
            Method method = c.getMethod(methodName);
            Object result = method.invoke(null);
            return result.toString();

        } else if(command.startsWith("unaryInvoke")){
            String parameters = command.substring(12);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1].substring(3);
            String paramType = parameters.split(",")[2].substring(3);
            String value = parameters.split(",")[3].substring(3);
            int length = value.length();
            value = value.substring(0, length-1);

            System.out.println(className);
            System.out.println(methodName);
            System.out.println(paramType);
            System.out.println(value);

            Object arg = null;
            Class<?> typeClass = null;

            Class c = Class.forName(className);

            if(paramType.equals("int")){
                arg = Integer.parseInt(value);
                typeClass = int.class;
            }else if(paramType.equals("String")){
                arg = value.substring(3,4);
                typeClass = String.class;
            } else if(paramType.equals("double")){
                arg = Double.parseDouble(value);
                typeClass = Double.TYPE;
            }

            Method method = c.getMethod(methodName, typeClass);
            Object result = method.invoke(null, arg);
            return result.toString();

        } else if(command.startsWith("binaryInvoke")){
            String parameters = command.substring(13);
            String className = parameters.split(",")[0];
            String methodName = parameters.split(",")[1].substring(3);
            String paramType1 = parameters.split(",")[2].substring(3);
            String value1 = parameters.split(",")[3].substring(3);
            String paramType2 = parameters.split(",")[4].substring(3);
            String value2 = parameters.split(",")[5].substring(3);
            int length = value2.length();
            value2 = value2.substring(0, length-1);

            System.out.println(className);
            System.out.println(methodName);
            System.out.println(paramType1);
            System.out.println(value1);
            System.out.println(paramType2);
            System.out.println(value2);

            Object arg1 = null;
            Class<?> typeClass1 = null;
            Object arg2 = null;
            Class<?> typeClass2 = null;

            Class c = Class.forName(className);

            if(paramType1.equals("int")){
                arg1 = Integer.parseInt(value1);
                typeClass1 = int.class;
            }else if(paramType1.equals("String")){
                arg1 = value1.substring(3,4);
                typeClass1 = String.class;
            } else if(paramType1.equals("double")){
                arg1 = Double.parseDouble(value1);
                typeClass1 = Double.TYPE;
            }

            if(paramType2.equals("int")){
                arg2 = Integer.parseInt(value2);
                typeClass2 = int.class;
            }else if(paramType2.equals("String")){
                arg2 = value2.substring(3,4);
                typeClass2 = String.class;
            } else if(paramType2.equals("double")){
                arg2 = Double.parseDouble(value2);
                typeClass2 = Double.TYPE;
            }

            Method method = c.getMethod(methodName, typeClass1, typeClass2);
            Object result = method.invoke(null, arg1,arg2);
            return result.toString();
        }
        return httpError;
    }
}
