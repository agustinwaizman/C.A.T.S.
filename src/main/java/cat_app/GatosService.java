package cat_app;

import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GatosService {
    public static void verGatos() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();

        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1);

        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);

        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(http.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage);

            if (fondoGato.getIconWidth() > 800){
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            String menu = "Opciones: \n" +
                    "1. Ver otra imagen\n" +
                    "2. Favorito\n" +
                    "3. Volver\n";
            String[] botones = {
                    "Ver otra imagen",
                    "Favorito",
                    "Volver"};
            String id_gato = String.valueOf(gatos.getId());
            String opcion = (String) JOptionPane.showInputDialog(
                    null,
                    menu,
                    id_gato,
                    JOptionPane.INFORMATION_MESSAGE,
                    fondoGato,
                    botones,
                    botones[0]);

            int seleccion = -1;

            for (int i = 0; i < botones.length; i++) {
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }

            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public static void favoritoGato(Gatos gato){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\""+gato.getId()+"\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public static void verFavoritos(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();

        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        //creamos el objeto gson
        Gson gson = new Gson();

        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);

        if(gatosArray.length > 0){
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max-min) + 1)) + min;
            int indice = aleatorio - 1;

            GatosFav gatofav = gatosArray[indice];

                    Image image = null;
                    try{
                        URL url = new URL(gatofav.image.getUrl());
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.addRequestProperty("User-Agent", "");
                        BufferedImage bufferedImage = ImageIO.read(http.getInputStream());
                        ImageIcon fondoGato = new ImageIcon(bufferedImage);

                        if (fondoGato.getIconWidth() > 800){
                            Image fondo = fondoGato.getImage();
                            Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                            fondoGato = new ImageIcon(modificada);
                        }
                        String menu = "Opciones: \n" +
                                "1. Ver otra imagen\n" +
                                "2. Eliminar favorito\n" +
                                "3. Volver\n";
                        String[] botones = {
                                "Ver otra imagen",
                                "Eliminar favorito",
                                "Volver"};
                        String id_gato = String.valueOf(gatofav.getId());
                        String opcion = (String) JOptionPane.showInputDialog(
                                null,
                                menu,
                                id_gato,
                                JOptionPane.INFORMATION_MESSAGE,
                                fondoGato,
                                botones,
                                botones[0]);

                        int seleccion = -1;

                        for (int i = 0; i < botones.length; i++) {
                            if(opcion.equals(botones[i])){
                                seleccion = i;
                            }
                        }

                        switch (seleccion){
                            case 0:
                                verFavoritos(apiKey);
                                break;
                            case 1:
                                borrarFavorito(gatofav);
                                break;
                            default:
                                break;
                        }
                    }catch (IOException e){
                        System.out.println(e);
                    }

        }
    }

    public static void borrarFavorito(GatosFav gatofav){

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatofav.getId() + "")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatofav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
