package com.endare.adhese.sdk;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AdheseOptionsTest {

    @Test
    public void basic_case() {
        AdheseOptions.Builder builder = getBaseBuilder();
        assertEquals("/slloc1-slot1/tlnone", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_single() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", "abc");
        assertEquals("/slloc1-slot1/tlnone/xaabc", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_multiple() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", "abc", "def", "ghi");
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_list_single() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", Arrays.asList("abc"));
        assertEquals("/slloc1-slot1/tlnone/xaabc", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_list_multiple() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", Arrays.asList("abc", "def", "ghi"));
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_merge() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", Arrays.asList("abc", "def"));
        builder.addCustomParameter("xa", Arrays.asList("def", "ghi"));
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_key() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xab", Arrays.asList("abc", "def"));
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_key_not_null() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter(null, Arrays.asList("abc", "def"));
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_value_not_null() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xab", null, "abc");
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_value_not_empty() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameter("xa", Collections.<String>emptyList());
    }

    @Test( expected = IllegalArgumentException.class)
    public void basic_case_validate_req_params_missing() {
        AdheseOptions.Builder builder = new AdheseOptions.Builder();
        builder.build();
    }

    private AdheseOptions.Builder getBaseBuilder() {
        AdheseOptions.Builder builder = new AdheseOptions.Builder();
        builder.addSlot("slot1");
        builder.forLocation("loc1");
        return builder;
    }
}
