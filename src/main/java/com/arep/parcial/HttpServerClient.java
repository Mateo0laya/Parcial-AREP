package com.arep.parcial;

import java.net.*;
import java.text.Normalizer.Form;
import java.io.*;

public class HttpServerClient {

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
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;

        
        boolean isRunning = true;
            
        while(isRunning){

            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e);
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
                    System.out.println(path);
                    first = false;
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

    private static String response(String path) throws IOException{
        String outputLine;
        if(path.startsWith("/cliente" )){
            outputLine = headerOK + "<!DOCTYPE html>\r\n" + //
                "<html>\r\n" + //
                "    <head>\r\n" + //
                "        <title>Reflective ChatGPT Mateo Olaya</title>\r\n" + //
                "        <meta charset=\"UTF-8\">\r\n" + //
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    </head>\r\n" + //
                "    <body>\r\n" + //
                "        <h1>Reflective ChatGPT</h1>\r\n" + //
                "        <form action=\"/consulta\">\r\n" + //
                "            <label for=\"name\">Command</label><br>\r\n" + //
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\r\n" + //
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\r\n" + //
                "        </form> \r\n" + //
                "        <div id=\"getrespmsg\"></div>\r\n" + //
                "\r\n" + //
                "        <script>\r\n" + //
                "            function loadGetMsg() {\r\n" + //
                "                let nameVar = document.getElementById(\"name\").value;\r\n" + //
                "                const xhttp = new XMLHttpRequest();\r\n" + //
                "                xhttp.onload = function() {\r\n" + //
                "                    document.getElementById(\"getrespmsg\").innerHTML =\r\n" + //
                "                    this.responseText;\r\n" + //
                "                }\r\n" + //
                "                xhttp.open(\"GET\", \"/consulta?comando=\"+nameVar);\r\n" + //
                "                xhttp.send();\r\n" + //
                "            }\r\n" + //
                "        </script>\r\n" + //
                "\r\n" + // 
                "        <h1>Comandos Soportados:</h1>\r\n" + //
                "        <p> Class([class name]): Retorna una lista de campos declarados y metodos declarados </p>\r\n" +
                "        <p> invoke([class name],[method name]): retorna el resultado de la invocacion del metodo. </p>\r\n" +
                "        <p> unaryInvoke([class name],[method name],[paramtype],[param value]): retorna el resultado de la invocacion del metodo. </p>\r\n" +
                "        <p> binaryInvoke([class name],[method name],[paramtype 1],[param value], [paramtype 1],[param value],): retorna el resultado de la invocacion del metodo. </p>\r\n" +

                "    </body>\r\n" + //
                "</html>";
            return outputLine;
        } else if(path.startsWith("/consulta")){
            return headerOK + query(path);
        } else {
            return httpError; 
        }
        
    }

    private static String query(String path) throws IOException {
        String command = path.split("?")[1];
        System.out.println(command);
        return HttpConnection.query(command);
    }
}