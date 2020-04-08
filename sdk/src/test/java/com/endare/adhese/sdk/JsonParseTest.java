package com.endare.adhese.sdk;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class JsonParseTest {

    @Test
    public void canParseWithoutTrackerOrImpressionsUrl() throws IOException, JSONException {
        InputStream in = getClass().getResourceAsStream("trackerlessad.json");
        String json = new Scanner(in).useDelimiter("\\A").next();
        JSONObject ad = new JSONObject(json);

        Ad.fromJSON(ad);


    }

}
