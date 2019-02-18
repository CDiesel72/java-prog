package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Diesel on 17.02.2019.
 */
public class SendMessage {

    public static int send(String url, Message m) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        try {
            String json = m.toJSON();
            os.write(json.getBytes(StandardCharsets.UTF_8));

        } finally {
            os.close();
        }

        InputStream is = conn.getInputStream();
        try {
            byte[] buf = Utils.requestBodyToArray(is);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            Gson gson = new GsonBuilder().create();
            Message getM = gson.fromJson(strBuf, Message.class);
            if (getM != null) {
                System.out.println(getM);
            }
            int res = conn.getResponseCode();
            if (res == 200
                    && getM != null
                    && getM.getFrom().equals("Server")
                    && getM.getText().indexOf("#ERROR400!") >= 0) {
                res = 400;
            }
            return res;
        } finally {
            is.close();
        }
    }
}
