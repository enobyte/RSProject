package com.rsproject.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Three Hero on 08/09/2015.
 */
public class ConnectionManager {

    public static final String URL_LOGIN = Config.makeUrlString(Config.suffix_login);
    public static final String URL_GETALL_LAPORAN = Config.makeUrlString(Config.suffix_getall_laporan);
    public static final String URL_GET_LAPORANBYNAME = Config.makeUrlString(Config.suffix_list_laporanbyname);
    public static final String URL_SAVE_LAPORAN = Config.makeUrlString(Config.suffix_save_laporan);
    public static final String URL_SAVE_RESPONSE = Config.makeUrlString(Config.suffix_post_response);
    public static final String URL_GET_RESPONSE = Config.makeUrlString(Config.suffix_getresponse);
    public static final String URL_GET_CATEGORY = Config.makeUrlString(Config.suffix_get_category);
    public static final String URL_IMAGE = Config.makeUrlString(Config.suffix_post_image);



    private static String executeHttpClientPost(String requestURL, HashMap<String, String> params, Context context) throws IOException {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String executeHttpClientGet(String requestURL, Context context) throws IOException {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(15000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static String connectHttpPost(String url, HashMap<String, String> params, Context context) throws IOException {
        return executeHttpClientPost(url, params, context);
    }


    public static String connectHttpGet(String url, Context context) throws IOException {
        return executeHttpClientGet(url, context);
    }


    public static String requestLogin(String url, String username, String password, Context context) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        return connectHttpPost(url, param, context);
    }


    public static String requestAllLaporan(String url, Context context) throws IOException {
        return connectHttpGet(url, context);
    }

    public static String requestResponse(String url, String username, String id_laporan, Context context) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("id_laporan", id_laporan);
        return connectHttpPost(url, param, context);
    }

    public static String requestSaveResponse(String url, String username, String response, String tgl_response, String id_laporan, Context context) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("response", response);
        param.put("tgl_response", tgl_response);
        param.put("id_laporan", id_laporan);
        return connectHttpPost(url, param, context);
    }

    public static String requestLaporanByName(String url, String username, Context context) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        return connectHttpPost(url, param, context);
    }

    public static String requestSaveLaporan(String url, String id_category, String judul, String keterangan,
                                            String lat, String lon, String tgl, String username, String status_urgensi,
                                            String status_laporan,String gambar, Context context) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("id_kategori_laporan", id_category);
        param.put("judul_laporan", judul);
        param.put("keterangan", keterangan);
        param.put("latitude", lat);
        param.put("longitude", lon);
        param.put("tgl_laporan", tgl);
        param.put("status_urgensi", status_urgensi);
        param.put("status_laporan", status_laporan);
        param.put("gambar", gambar);
        return connectHttpPost(url, param, context);
    }

    public static String requestCategory(String url, Context context) throws IOException {
        return connectHttpGet(url, context);
    }


}
