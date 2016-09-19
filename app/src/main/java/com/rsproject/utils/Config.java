package com.rsproject.utils;

/**
 * Created by Three Hero on 19/08/2015.
 */
public class Config {
    public static final String URL_HTTP_STRING = "http://";
    public static final String URL_HTTPS_STRING = "https://";

    /*
   |-----------------------------------------------------------------------------------------------
   | URL Suffix.
   |-----------------------------------------------------------------------------------------------
    */
    public static final String url_Server = "bantensoftware.com/dinkes/api/v1";
    public static final String suffix_getall_laporan = "data_laporan";
    public static final String suffix_getresponse = "data_response";
    public static final String suffix_post_response = "response/entry";
    public static final String suffix_list_laporanbyname = "data_laporan_user";
    public static final String suffix_login = "user/login";
    public static final String suffix_save_laporan = "laporan/entry";
    public static final String suffix_get_category = "report_category";
    public static final String suffix_post_image = "image/upload";

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as API
    |-----------------------------------------------------------------------------------------------
    */
    public static String makeUrlString(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTP_STRING);
        url.append(url_Server);
        url.append("/");
        url.append(uri);
        return url.toString();
    }

}

