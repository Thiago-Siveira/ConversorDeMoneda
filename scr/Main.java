import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int opcion;
        double valor;
        double tasa = 0;
        double valorFinal = 0;
        Scanner ingreso = new Scanner(System.in);

        String url = "https://v6.exchangerate-api.com/v6/a4fcef2aac0ac2e2d5ee2b72/latest/USD";

        do {
            System.out.println("*************************************");
            System.out.println("Sea bienvenido al Conversor de Moneda\n");
            System.out.println("1) Dolar => Peso argentino");
            System.out.println("2) Peso argentino => Dolar");
            System.out.println("3) Dolar => Real brasileño");
            System.out.println("4) Real brasileño => Dolar");
            System.out.println("5) Peso colombiano => Dolar");
            System.out.println("6) Dolar => Peso colombiano");
            System.out.println("7) Salir");
            System.out.println("Elija una opción válida:");
            System.out.println("*************************************\n");
            opcion = ingreso.nextInt();
            if (opcion == 7){
                System.out.println("Fin del programa");
                break;
            }
            System.out.println("Ingrese el valor que deseas convertir: ");
            valor = ingreso.nextDouble();

            switch (opcion) {
                case 1:
                    tasa = obtenerTasa(url,"ARS");
                    if(tasa != -1) {
                        valorFinal = valor * tasa;
                        System.out.println("El valor " + valor + " [USD] corresponde al valor final de => " + valorFinal + " [ARS]");
                    }
                    break;
                case 2:
                    tasa = obtenerTasa(url,"ARS");
                    if(tasa != -1) {
                        valorFinal = valor / tasa;
                        System.out.println(String.format("El valor %.2f [ARS] corresponde al valor final de => %.4f [USD]", valor, valorFinal));
                    }
                    break;
                case 3:
                    tasa = obtenerTasa(url,"BRL");
                    if(tasa != -1) {
                        valorFinal = valor * tasa;
                        System.out.println("El valor " + valor + " [USD] corresponde al valor final de => " + valorFinal + " [BRL]");
                    }
                    break;
                case 4:
                    tasa = obtenerTasa(url,"BRL");
                    if(tasa != -1){
                        valorFinal = valor / tasa;
                        System.out.println(String.format("El valor %.2f [BRL] corresponde al valor final de => %.2f [USD]", valor, valorFinal));
                    }
                    break;
                case 5:
                    tasa = obtenerTasa(url,"COP");
                    if(tasa != -1) {
                        valorFinal = valor * tasa;
                        System.out.println("El valor " + valor + " [USD] corresponde al valor final de => " + valorFinal + " [COP]");
                    }
                    break;
                case 6:
                    tasa = obtenerTasa(url,"COP");
                    if(tasa != -1){
                        valorFinal = valor / tasa;
                        System.out.println(String.format("El valor %.2f [COP] corresponde al valor final de => %.4f [USD]", valor, valorFinal));
                    }
                    break;
                default:
                    System.out.println("Error. Elija una opción válida:");
            }
            System.out.println("");
        }while (opcion != 7);
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
