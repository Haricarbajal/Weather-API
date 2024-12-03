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


                    //icono
                    JSONObject current = json.getJSONObject("current");
                    JSONObject condition = current.getJSONObject("condition");
                    String icon = condition.getString("icon");


                    //temp_f
                    String tempf = String.valueOf(current.getInt("temp_f"));



                    //time
                    String time = location.getString("localtime");
                    SwingUtilities.invokeLater(() -> {
                        // Crear el marco (ventana)
                        JFrame frame = new JFrame("Cimapp");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Cierra la app al salir

                        // Usar null layout para manejar las posiciones manualmente
                        JPanel panel = new JPanel();
                        panel.setLayout(null); // Desactivar LayoutManager

                        //Region
                        JLabel regionLabel = new JLabel(region);
                        regionLabel.setVerticalAlignment(SwingConstants.TOP);
                        regionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        regionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                        regionLabel.setBounds(100, 200, 400, 50);
                        panel.add(regionLabel);


                        // Imagen
                        ImageIcon imagen = downloadImage("https:" + icon);
                        Image imagenEscalada = imagen.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        ImageIcon nuevaImg = new ImageIcon(imagenEscalada);
                        JLabel labelImage = new JLabel(nuevaImg);
                        labelImage.setVerticalAlignment(SwingConstants.CENTER);
                        labelImage.setBounds(205, 250, 200, 100);
                        panel.add(labelImage);


                        //temperatura
                        JLabel tempfLabel = new JLabel(tempf + "ºF");
                        tempfLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        tempfLabel.setVerticalAlignment(SwingConstants.CENTER);
                        tempfLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                        tempfLabel.setBounds(150, 330, 300, 50);
                        panel.add(tempfLabel);


                        // Time
                        JLabel timeLabel = new JLabel(time);
                        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        timeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
                        timeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                        timeLabel.setBounds(150, 350, 300, 50);
                        panel.add(timeLabel);


                        // Agregar el panel al frame
                        frame.add(panel);


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