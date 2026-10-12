# Taller grupal de pasajeros con colas FIFO

## Consigna

Completen tres funcionalidades del proyecto de pasajeros de una aerolínea. Trabajen en grupo y conserven el orden FIFO: el primero que llega es el primero atendido. Utilicen `Queue<Pasajero>` con `LinkedList<Pasajero>`; la categoría del asiento no cambia el turno de atención.

Cada integrante debe realizar **al menos un commit con un aporte real de código desde su propia cuenta de GitHub**. No se exigen ramas, issues, pull requests ni revisiones. Entreguen el repositorio, ejecuten las pruebas proporcionadas y expliquen cómo desarrollaron las funcionalidades.

Se evaluarán únicamente tres criterios: **GitHub, funcionamiento y defensa explicativa del código**.

## Qué incluye el proyecto base

- `Pasajero.java`: datos y getters del pasajero.
- `Cola.java`: registro básico, atención FIFO, consulta del siguiente y listado ya implementados.
- `Ventana.java`: interfaz Swing con botones para registrar, buscar, cancelar y atender.
- `ColaBaseTest.java`: cinco pruebas de las operaciones que ya funcionan.
- `ColaTallerTest.java`: quince pruebas de los tres requerimientos a completar, cinco por requerimiento.
- `pom.xml`: configuración Maven y JUnit 5 preparada.

El proyecto inicia sin pasajeros. Los pendientes se muestran en orden de llegada. La interfaz se construye directamente en Java y no necesita un archivo `.form` ni un diseñador visual para compilar.

El código está preparado para Java 17. No usa JavaFX ni `module-info.java`.

## Los tres requerimientos

### R1 Validar el registro de pasajeros

Completen las validaciones del método `Cola.agregar(Pasajero p)`.

- El pasajero no puede ser `null` y su nombre no puede ser nulo, vacío o formado solo por espacios.
- El asiento debe estar entre 1 y 30.
- No se puede repetir un asiento que ya esté ocupado en la cola de espera.
- Un error debe generar `IllegalArgumentException` con un mensaje comprensible, sin agregar al pasajero ni modificar la fila.
- Si los datos son válidos, el pasajero se agrega al final mediante `offer()`.

**Ejemplo:** si Ana ya ocupa el asiento 5 en la fila, registrar a Juan con asiento 5 debe mostrar un error y dejar a Ana en su posición.

Para simplificar, solo se controla ocupación entre pasajeros pendientes. No se exige historial, control de asientos después del check-in, validación adicional de categorías ni almacenamiento en archivos. Las categorías ya se seleccionan en el combo de la interfaz.

### R2 Buscar un pasajero y conocer su posición

Implementen estos métodos en `Cola`:

```java
public Pasajero buscarPorAsiento(int asiento)
public int posicionPorAsiento(int asiento)
```

- La búsqueda devuelve el pasajero pendiente encontrado o `null` si no existe.
- La posición comienza en 1; devuelve `-1` si el asiento no está en la fila.
- Asientos fuera de 1 a 30 generan `IllegalArgumentException`.
- Buscar no extrae pasajeros, no completa check-in y no cambia el orden o el tamaño de la cola.

**Ejemplo:** en `[Ana asiento 5, Juan asiento 8, Flor asiento 12]`, buscar el asiento 8 devuelve a Juan y su posición es 2.

El botón **Buscar y ver posición** ya llama a los métodos. No deben cambiar sus nombres ni sus tipos de retorno.

### R3 Cancelar un registro pendiente

Implementen este método en `Cola`:

```java
public Pasajero cancelarPorAsiento(int asiento)
```

- Retira y devuelve únicamente al pasajero indicado.
- Conserva el orden relativo de los demás pasajeros.
- Si el asiento está fuera de 1 a 30, lanza `IllegalArgumentException`.
- Si el asiento es válido, pero no está en la fila, lanza `NoSuchElementException`.
- Una cancelación fallida no modifica el tamaño ni el orden.
- El asiento cancelado queda disponible. Un nuevo registro para ese asiento entra al final de la fila.
- Cancelar no completa el check-in del pasajero retirado.

**Ejemplo:** cancelar a Juan en `[Ana, Juan, Flor]` deja `[Ana, Flor]`. Si luego Pepe usa el asiento de Juan, la fila queda `[Ana, Flor, Pepe]`.

El botón **Cancelar pasajero** ya está conectado. La eliminación no debe ordenar la colección ni cambiar la atención a prioridades.

## Cómo comenzar

1. Descompriman el ZIP y abran la carpeta `pryColasFIFO` en IntelliJ IDEA.
2. Seleccionen JDK 17 y carguen el proyecto Maven desde `pom.xml`. La primera carga descarga las dependencias y requiere internet.
3. Ejecuten `Ventana.main()` con el botón verde del IDE. El registro y la atención básica ya funcionan.
4. Abran `Cola.java` y busquen los comentarios `TODO R1`, `TODO R2` y `TODO R3`.
5. Distribuyan el trabajo y completen los métodos. Conserven los nombres y las pruebas suministradas.

La validación debe estar en `Cola`, aunque los controles visuales limiten algunos valores. Las pruebas llaman directamente al modelo y comprueban los casos que podrían llegar desde otra interfaz.

## Pruebas unitarias

El proyecto contiene **20 métodos de prueba**: cinco de la base y quince del taller. Cada prueba crea una cola nueva para evitar que dependa de otra.

| Grupo | Qué comprueba | Pruebas |
|---|---|---|
| Base | Cola vacía, FIFO entre categorías, consulta sin extracción, errores en vacío y pasajero nulo. | 5 |
| R1 | Registros válidos, nombres inválidos, rango de asientos, duplicados y conservación de la fila ante errores. | 5 |
| R2 | Búsqueda, ausencia, posiciones, consultas sin cambios y rango de asientos. | 5 |
| R3 | Cancelación intermedia, primero/último, ausencia, reutilización al final y rango de asientos. | 5 |

**Estado inicial verificado:** el proyecto compila. Si ejecutan todas las pruebas, pasan 6 y fallan 14: las cinco de base y el caso de registro válido ya están correctos. Las funciones nuevas están intencionalmente incompletas. Los métodos R2 y R3 lanzan `UnsupportedOperationException` hasta que los implementen; esa excepción debe reemplazarse por el comportamiento solicitado.

Las pruebas rojas iniciales muestran el trabajo pendiente. No las borren, deshabiliten ni modifiquen para ocultar fallos. Pueden agregar pruebas adicionales, pero no es obligatorio.

En IntelliJ pueden ejecutar las clases `ColaBaseTest` y `ColaTallerTest` con el botón verde. Para ejecutar todas, seleccionen `src/test/java` y ejecuten las pruebas de la carpeta.

Si tienen Maven disponible en la terminal, ejecuten los comandos desde la carpeta que contiene `pom.xml`:

```bash
# Verificar solo la parte ya implementada.
mvn test -Dgroups=base

# Revisar un requerimiento mientras lo desarrollan.
mvn test -Dgroups=r1
mvn test -Dgroups=r2
mvn test -Dgroups=r3

# Verificar el taller completo antes de entregar.
mvn test

# Iniciar la interfaz.
mvn compile exec:java
```

En IntelliJ también pueden ejecutar esos objetivos desde la ventana Maven, sin instalar Maven por separado. El taller está completo cuando pasan las 20 pruebas y los botones producen el mismo comportamiento.

## Demostración breve de funcionamiento

Realicen esta secuencia en la interfaz:

1. Registrar Ana, asiento 5, económica; Juan, asiento 8, primera clase; Flor, asiento 12, ejecutiva.
2. Intentar registrar otro pasajero con asiento 8: debe rechazarse.
3. Buscar el asiento 8: debe mostrar a Juan en posición 2, sin cambiar la fila.
4. Cancelar el asiento 8: deben quedar Ana y Flor, en ese orden.
5. Registrar Pepe con asiento 8: debe quedar al final.
6. Atender: deben salir Ana, Flor y Pepe, en ese orden.

Las pruebas verifican el modelo; esta demostración comprueba que los botones muestran el resultado correcto. No se exige automatizar la interfaz Swing.

## Entrega en GitHub

- Un repositorio del grupo con el proyecto completo y las tres funcionalidades terminadas.
- Al menos un commit significativo de código por integrante, atribuible a su cuenta. El commit inicial del proyecto realizado por una sola persona no reemplaza los aportes de los demás.
- Una captura de la ejecución final con las 20 pruebas aprobadas.
- Esta tabla completada en el README y una explicación oral de los aportes.

| Integrante | Usuario de GitHub | Qué desarrolló | Enlace a su commit |
|---|---|---|---|
| Por completar | Por completar | Por completar | Por completar |

Cada estudiante debe subir su aporte con su cuenta y comprobar que GitHub lo muestre como autor. Si utilizan Git local, el correo configurado debe estar asociado a esa cuenta. No compartir cuentas. No es obligatorio usar ramas o pull requests: pueden trabajar por turnos e integrar sus cambios, conservando los aportes individuales.

## Rúbrica con tres criterios sobre 100 puntos

| Criterio | Máximo | Cumplimiento completo | Cumplimiento parcial | Sin evidencia |
|---|---|---|---|---|
| GitHub | 20 | **20:** repositorio accesible y al menos un commit de código propio, identificado y relacionado con el aporte explicado. | **10:** hay un aporte identificable, pero falta el enlace o la atribución a la cuenta no está clara. | **0:** no se evidencia un aporte propio. |
| Funcionamiento | 60 | **60:** funcionan los tres requerimientos, se conserva FIFO, pasan las 20 pruebas y la demostración visual es correcta. | **40:** dos requerimientos completos; **20:** uno completo. Se verifica con sus pruebas y la demostración. | **0:** ninguno cumple o el proyecto no ejecuta. |
| Defensa explicativa del código | 20 | **20:** explica su aporte, los métodos usados, una prueba y qué ocurre ante un error o caso límite. | **10:** explica el caso normal, pero requiere ayuda para justificar FIFO, errores o pruebas. | **0:** no participa o no puede explicar el código atribuido. |

El funcionamiento recibe una nota común para el grupo. GitHub y defensa se califican individualmente. La nota de cada estudiante es la suma de los tres criterios. Cada requerimiento representa 20 de los 60 puntos de funcionamiento; para considerarlo completo, sus cinco pruebas deben pasar, su botón debe funcionar y debe mantenerse la base FIFO.

## Preguntas para defender el código

El docente seleccionará dos preguntas por integrante. Se sugiere una demostración grupal de cinco minutos y dos minutos de explicación por estudiante.

1. Muestre el método y el commit de su aporte. ¿Qué cambió y por qué?
2. ¿Qué diferencia hay entre `Queue` y `LinkedList` en este proyecto?
3. ¿Qué hacen `offer()`, `poll()` y `peek()`? ¿Cuál retira un pasajero?
4. ¿Cómo impiden registrar un asiento repetido? ¿Qué excepción utilizan?
5. ¿Cómo buscan una posición sin modificar la fila?
6. ¿Cómo eliminan un pasajero intermedio sin cambiar el orden de los demás?
7. Si cancelamos a Juan y registramos a Pepe en su asiento, ¿por qué Pepe queda al final?
8. Muestre una prueba proporcionada y explique qué resultado verifica y qué cambio incorrecto haría que falle.

## Revisión final

- [ ] R1, R2 y R3 implementados en `Cola.java`.
- [ ] Atención FIFO conservada; ninguna cola con prioridad.
- [ ] Las 20 pruebas pasan y la interfaz funciona.
- [ ] Cada integrante tiene al menos un commit de código propio.
- [ ] Tabla de integrantes completada y evidencia de pruebas incluida.
- [ ] Todos pueden explicar su aporte y una prueba.

## Referencias

- [Queue en Java 17](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Queue.html).
- [JUnit 5 versión 5.11.0](https://docs.junit.org/5.11.0/user-guide/).
- [GitHub configuración del correo de commits](https://docs.github.com/en/account-and-profile/how-tos/email-preferences/setting-your-commit-email-address).
