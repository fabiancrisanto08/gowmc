# God of War: Todas las Sagas — Minecraft 1.20.1 (Fabric & Forge + GeckoLib)

Proyecto multi-loader generado con Architectury API para Minecraft **1.20.1**.

## Contenido incluido
- **Entidades / Jefes / Titanes / Romanos / Criaturas:** 43
- **Bloques y Altares:** 6
- **Poderes, Magias e Ítems:** 11

## Requisitos para compilar los archivos .jar
1. Instalar **Java 17 (JDK 17)** (Eclipse Temurin recomendado).
2. Instalar **Gradle 8.6** (o abrir la carpeta directamente con **IntelliJ IDEA**, que descarga Gradle 8.6 automáticamente usando `gradle/wrapper/gradle-wrapper.properties`).
   > Nota: Si usas Gradle 8.11+, también puedes cambiar `dev.architectury.loom` en `build.gradle` a `1.7-SNAPSHOT`. Con Gradle 8.1–8.10 usa `1.5-SNAPSHOT` (configurado por defecto).
3. En una terminal dentro de esta carpeta:
   - Si deseas generar el wrapper local primero:
     ```bash
     gradle wrapper --gradle-version 8.6
     ```
   - Compilar ambos loaders (Windows: `gradlew.bat build` o `gradle build`):
     ```bash
     gradle build
     ```
   - Solo Fabric 1.20.1:
     ```bash
     gradle :fabric:build
     ```
   - Solo Forge 1.20.1:
     ```bash
     gradle :forge:build
     ```
4. Los archivos `.jar` compilados aparecerán en `fabric/build/libs/` y `forge/build/libs/`.

## Personalizar modelos 3D en Blockbench
- Los modelos base de GeckoLib están en `common/src/main/resources/assets/godofwar/geo/`.
- Las animaciones están en `common/src/main/resources/assets/godofwar/animations/`.
- Las texturas PNG base están en `common/src/main/resources/assets/godofwar/textures/`.
