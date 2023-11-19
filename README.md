[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/2k2-pIk4)
# Práctica 3 - Black Jack

## Desarrollo de un Juego de Blackjack con Modos de Juego y Clases Adicionales

### Objetivo:

El objetivo de esta práctica es aplicar los conceptos de Jetpack Compose, Kotlin y programación orientada a objetos para desarrollar un juego de Blackjack en Android Studio. En esta versión, se introducirán dos modos de juego (2 jugadores y 1 jugador contra la máquina) y se implementarán clases adicionales para gestionar la baraja, las cartas y los jugadores.

### Requisitos Básicos:
1. Interfaz Gráfica:

   ```
   Diseñar la interfaz del juego utilizando Jetpack Compose, considerando la opción de seleccionar el modo de juego al inicio.
   Mostrar las cartas de los jugadores y del crupier de manera clara y atractiva (imágenes).
   ```

2. Lógica del Juego:

   ```
   Implementar la lógica del juego de Blackjack para ambos modos de juego.
   Esto incluye la distribución inicial de cartas, el cálculo de la puntuación de la mano y las decisiones del jugador (pedir carta, plantarse, etc.).
   ```

3. Manejo de Estado:

   ```
   Utilizar ViewModels para gestionar el estado del juego de manera eficiente.
   Actualizar la interfaz de usuario de acuerdo con el estado actual del juego.
   ```

4. Clases Enumeradas:

   ```
   Crear dos clases enumeradas: una para definir los palos de la baraja y otra para los nombres de las cartas de cada palo.
   ```
   
5. Clase Baraja:

   ```
   Crear una clase Baraja con un companion object que contenga:
   - Una lista de objetos Carta que representan las cartas de la baraja.
   - Métodos para crear una nueva baraja y barajar o desordenar las cartas.
   ```
  
6. Modos de Juego:

   ```
   Implementar dos modos de juego:
   - Modo 1 Jugador contra la Máquina: El jugador juega contra la máquina (crupier).
   - Modo 2 Jugadores: Dos jugadores compiten entre sí.

### Clases Adicionales:

1. Clase Carta:

   ```
   Crear una clase Carta que represente una carta de la baraja con propiedades como palo, nombre, valor...
   ```

2. Clase Jugador:

   ```
   Crear una clase Jugador que tenga propiedades como nombre, mano (conjunto de cartas), fichas (en el caso del modo 2 Jugadores), etc.
   ```
   
### Recursos:

   * Documentación oficial de Jetpack Compose: [Compose Overview](https://developer.android.com/jetpack/compose?hl=es-419)
   * Documentación oficial de Kotlin: [Kotlin Docs](https://kotlinlang.org/docs/home.html)
   * Recuerda fomentar la modularidad y la reutilización del código mediante la implementación de clases y objetos bien estructurados.
