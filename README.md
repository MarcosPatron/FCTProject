# Aplicación Android + Backend en Python 

Este proyecto está compuesto por dos partes principales:

**Backend (Python)**: Encargado de procesar las peticiones realizadas por la app móvil. Repositorio disponible aquí: [FCTPython](https://github.com/MarcosPatron/FCTPython).

**Frontend (Java - Android)**: Aplicación Android desarrollada en Java, que representa la interfaz visible para el usuario.

### Cómo ejecutar la aplicación:

Opción 1: Desde Android Studio
Puedes lanzar la aplicación directamente desde Android Studio, usando un emulador o un dispositivo físico conectado por USB.

Opción 2: Instalando la APK
También puedes generar la APK desde Android Studio (Build > Build APK(s)) e instalarla en un dispositivo Android.

Si va a lanzar la parte del backend desde otro dispositivo, debaras cambiar la URL en 'app/src/main/java/com/example/myapplication/api/ApiClient.java' linea 15(BASE_URL) a la correspondiente al dispositivo desde el que se lanza. Si se van a lanzar anbos proyectos desde local, deberas cambiar esta URL por 'http://10.0.2.2:5000', la ip del localhost de Android Studio.
