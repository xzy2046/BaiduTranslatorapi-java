/*
 *  This program provider how to use baidu translator api
 *  Copyright (C) 2012  Xu Zhengyang
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package xzy.android.baidutranslatorapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * (C) 2012 zhengyang.xu
 *
 * @author zhengyang.xu
 * @version 0.1
 * @since 2:51:57 PM Jun 13, 2012
 */
public class Translator {

    private static final String BaseUri = "http://openapi.baidu.com/public/2.0/bmt/translate?";

    private static final String PARAM_FROM = "from=";

    private static final String PARAM_TO = "to=";

    private static final String PARAM_CLIEND_ID = "client_id=";

    private static final String PARAM_TEXT = "q=";

    private static final String ENTER = "%0A"; // enter

    public static final String JP = "jp";

    public static final String EN = "en";

    public static final String ZH = "zh";

    public static final String AUTO = "auto";

    private static String YourApiKey = "YourApiKey";

    private static String From = AUTO;

    private static String To = AUTO;

    public Translator() {
    }

    public static void setApiKey(String key) {
        YourApiKey = key;
    }

    public static void setFrom(String from) {
        From = from;
    }

    public static void setTo(String to) {
        To = to;
    }

    public static String execute(String sourceString) {
        // String uri = BaseUri;
        StringBuilder sb = new StringBuilder();
        sb.append(BaseUri);
        sb.append(PARAM_CLIEND_ID);
        sb.append(YourApiKey);
        sb.append("&");
        sb.append(PARAM_TEXT);
        sb.append(sourceString);
        sb.append("&");
        sb.append(PARAM_FROM);
        sb.append(From);
        sb.append("&");
        sb.append(PARAM_TO);
        sb.append(To);
        String data = "";
        String result = null;
        try {
            URL url = new URL(sb.toString());
            InputStreamReader resultBuffer = new InputStreamReader(url.openStream(), "UTF-8");

            BufferedReader br = new BufferedReader(resultBuffer, 16);
            String temp;
            while ((temp = br.readLine()) != null) {
                data = data + temp;
            }
            JSONObject full = null;
            try {
                full = new JSONObject(data);
                JSONArray tran = full.getJSONArray("trans_result");
                result = getResult(tran);
            } catch (JSONException e) {
                if (full != null) {
                    try {
                        result = "error code" + full.getString("error_code") + "error_msg"
                                + full.getString("error_msg");
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            return null;
        }

        return result;
    }

    public static String[] execute(String[] sourceStrings) {
        // String uri = BaseUri;
        StringBuilder sb = new StringBuilder();
        sb.append(BaseUri);
        sb.append(PARAM_CLIEND_ID);
        sb.append(YourApiKey);
        sb.append("&");
        sb.append(PARAM_TEXT);
        for (int i = 0; i < sourceStrings.length; i++) {
            if (i > 0)
                sb.append(ENTER);
            sb.append(sourceStrings[i]);
        }
        sb.append("&");
        sb.append(PARAM_FROM);
        sb.append(From);
        sb.append("&");
        sb.append(PARAM_TO);
        sb.append(To);
        String data = "";
        String result[] = null;
        try {
            URL url = new URL(sb.toString());
            InputStreamReader resultBuffer = new InputStreamReader(url.openStream(), "UTF-8");

            BufferedReader br = new BufferedReader(resultBuffer, 16);
            String temp;
            while ((temp = br.readLine()) != null) {
                data = data + temp;
            }
            JSONObject full = new JSONObject(data);
            JSONArray tran = full.getJSONArray("trans_result");
            result = getResults(tran);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public static String getResult(JSONArray json) {
        String result = null;
        try {
            result = json.getJSONObject(0).getString("dst");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String[] getResults(JSONArray json) {

        String[] result = new String[json.length()];
        try {
            for (int i = 0; i < json.length(); i++) {
                result[i] = json.getJSONObject(i).getString("dst");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
