package org.example;


import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://api.weatherapi.com/v1/current.json?key=a4a6626b1d5d48dc943194523240312&q=Madrid&aqi=no");
            try {
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("GET");
                int response;
                if((response = conection.getResponseCode()) == 200){
                    InputStream inputStream = conection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }

                    System.out.println("Esta es la respuesta de la api: ");

                    JSONObject json = new JSONObject(sb.toString());
                    String respuestaJson = json.toString(4);
                    System.out.println(respuestaJson);


                    JSONObject location = json.getJSONObject("location");
                    String region = location.getString("region");
                    System.out.println("Esta es la region: " + region);

                    JSONObject current = json.getJSONObject("current");
                    JSONObject condition = current.getJSONObject("condition");
                    String icon = condition.getString("icon");


                    SwingUtilities.invokeLater(() -> {
                        // Crear el marco (ventana)
                        JFrame frame = new JFrame("Cimapp");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cierra la app al salir


                        ImageIcon imagen = downloadImage( "https:"+ icon);


                        // Crear una etiqueta con el icono de la api
                        JLabel labelImage = new JLabel(imagen, SwingConstants.CENTER);
                        labelImage.setVerticalAlignment(SwingConstants.TOP);
                        // Agregar la etiqueta al marco
                        frame.add(labelImage);

                        JLabel regionLabel = new JLabel(region);
                        regionLabel.setBounds(50,50, 640, 640);
                        frame.add(labelImage);

                        frame.add(regionLabel, BorderLayout.NORTH);

                        // Ajustar el tamaño de la ventana para que se acomode al contenido
                        frame.setSize(600, 800);  // Ancho x Alto
                        // Mostrar la ventana
                        frame.setVisible(true);
                    });

                    br.close();
                    conection.disconnect();
                }else{
                    System.out.println("Error: " + response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ImageIcon downloadImage(String imagenUrl){
        try {
            URL url = new URL(imagenUrl);
            InputStream is = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int byteRead;
            while((byteRead = is.read(buffer)) != -1){
                baos.write(buffer, 0, byteRead);
            }

            is.close();
            byte[] byteImage = baos.toByteArray();

            return new ImageIcon(byteImage);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}