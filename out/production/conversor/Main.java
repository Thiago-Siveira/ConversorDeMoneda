import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int opcion;
        double valor;
        double tasa = 0;
        double valorFinal = 0;
        ArrayList<String> historial = new ArrayList<>();
        Scanner ingreso = new Scanner(System.in);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String url = "https://v6.exchangerate-api.com/v6/a4fcef2aac0ac2e2d5ee2b72/latest/USD";

        while (true){
            System.out.println("*************************************");
            System.out.println("Sea bienvenido al Conversor de Moneda\n");
            System.out.println("1) Dolar => Peso argentino");
            System.out.println("2) Peso argentino => Dolar");
            System.out.println("3) Dolar => Real brasileño");
            System.out.println("4) Real brasileño => Dolar");
            System.out.println("5) Peso colombiano => Dolar");
            System.out.println("6) Dolar => Peso colombiano");
            System.out.println("7) Historial");
            System.out.println("8) Convertir otras monedas");
            System.out.println("9) Salir");
            System.out.println("Elija una opción válida:");
            System.out.println("*************************************\n");
            opcion = ingreso.nextInt();
            if (opcion == 9){
                System.out.println("Fin del programa");
                break;
            }
            if (opcion == 7) {
                System.out.println("Historial de conversiones:");
                for (String registro : historial) {
                    System.out.println(registro);
                }
                continue;
            }

            System.out.println("Ingrese el valor que deseas convertir: ");
            valor = ingreso.nextDouble();

            switch (opcion) {
                case 1: case 2:
                    tasa = obtenerTasa(url,"ARS");
                    break;
                case 3: case 4:
                    tasa = obtenerTasa(url,"BRL");
                    break;
                case 5: case 6:
                    tasa = obtenerTasa(url,"COP");
                    break;
                case 8:
                    System.out.println("Ingrese el código de la moneda de origen (Ejemplo: USD, EUR, ARS, BRL): ");
                    String monedaOrigen = ingreso.next().toUpperCase();

                    System.out.println("Ingrese el código de la moneda de destino (Ejemplo: USD, EUR, ARS, BRL): ");
                    String monedaDestino = ingreso.next().toUpperCase();
                    double tasaOrigen = obtenerTasa(url, monedaOrigen);
                    double tasaDestino = obtenerTasa(url, monedaDestino);
                    valorFinal = valor / tasaOrigen * tasaDestino;
                    String fechaHora = LocalDateTime.now().format(formatoFecha);
                    System.out.printf("[%s] El valor %.2f [%s] equivale a %.4f [%s]\n",fechaHora ,valor, monedaOrigen, valorFinal, monedaDestino);
                    tasa = -1;

                default:
                    System.out.println("Error. Elija una opción válida:");
            }
            if (tasa != -1) {
                valorFinal = (opcion % 2 == 0) ? valor / tasa : valor * tasa;


                String fechaHora = LocalDateTime.now().format(formatoFecha);
                    String resultado = String.format("[%s] El valor %.2f [%s] corresponde al valor final de => %.4f [%s]", fechaHora, valor, opcion % 2 == 0 ? "Destino" : "USD", valorFinal, opcion % 2 == 0 ? "USD" : "Destino");
                historial.add(resultado);
                System.out.println(resultado);
            }
        }
    }

    public static double obtenerTasa(String url, String moneda) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            JsonElement elemento = JsonParser.parseString(respuesta.body());
            JsonObject objectRoot = elemento.getAsJsonObject();
            JsonObject tasasConversion = objectRoot.getAsJsonObject("conversion_rates");
            return tasasConversion.get(moneda).getAsDouble();
        } catch (Exception e) {
            System.out.println("Error al obtener la tasa: " + e.getMessage());
            return -1;
        }
    }

}
