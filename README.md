#Parcial AREP
Mateo Olaya Garzón 

##Instrucciones para ejecutar la aplicación

1. Clone el repositorio
2. En el repositorio encontrará dos clases en la ruta: src/main/java/com/arep/parcial, las cuales son HttpServerClient.java y HttpServerBack.java
3. Debe ejecutar ambas clases, asegurese de ejecutar ambas clases o la aplicación no correra correctamente
4. En su navegador dirijase a http://localhost:35000/cliente
5. Alli encontrara un campo de texto para enviar los comandos correspondientes del tipo:
- Class(java.lang.Math)
- invoke(java.lang.System, getenv)
- unaryInvoke(java.lang.Math, abs, int, 3)
- binaryInvoke(java.lang.Math, max, double, 4.5, double, -3.7)

### Test de la aplicación
- unaryInvoke(java.lang.Math, abs, int, 3)
  
![image](https://github.com/Mateo0laya/Parcial-AREP/assets/89365336/8a3ddb21-00d9-4999-90bc-fac9b8e44b64)

- binaryInvoke(java.lang.Math, max, double, 4.5, double, -3.7)

![image](https://github.com/Mateo0laya/Parcial-AREP/assets/89365336/6cd92b5b-e24f-464d-a468-4947aaa9c5c6)

- invoke(java.lang.System, getenv)

![image](https://github.com/Mateo0laya/Parcial-AREP/assets/89365336/cac530b4-abf5-472d-a676-f2122ec89c6e)
