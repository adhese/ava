package com.endare.adhese.sdk;

import com.endare.adhese.sdk.parameters.Device;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AdheseOptionsTest {

    @Test
    public void basic_case() {
        AdheseOptions.Builder builder = getBaseBuilder();
        assertEquals("/slloc1-slot1/tlnone", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_single() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", "abc");
        assertEquals("/slloc1-slot1/tlnone/xaabc", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_map() {
        AdheseOptions.Builder builder = getBaseBuilder();
        Map map = new HashMap<String, List<String>>();
        map.put("xa", Arrays.asList("abc", "def", "ghi"));
        builder.addCustomParametersRaw(map);
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_map_merges() {
        AdheseOptions.Builder builder = getBaseBuilder();
        Map map = new HashMap<String, List<String>>();
        map.put("xa", Arrays.asList("abc", "def", "ghi"));
        builder.addCustomParametersRaw(map);
        Map map2 = new HashMap<String, List<String>>();
        map2.put("xa", Arrays.asList("def", "ghi", "jkl"));
        map2.put("xb", Arrays.asList("123"));
        builder.addCustomParametersRaw(map2);
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi;jkl/xb123", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_list_single() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Arrays.asList("abc"));
        assertEquals("/slloc1-slot1/tlnone/xaabc", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_list_multiple() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Arrays.asList("abc", "def", "ghi"));
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_case_merge() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Arrays.asList("abc", "def"));
        builder.addCustomParameterRaw("xa", Arrays.asList("def", "ghi"));
        assertEquals("/slloc1-slot1/tlnone/xaabc;def;ghi", builder.build().getAsURL());
    }

    @Test
    public void custom_parameter_are_sorted() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("zz", Arrays.asList("abc"));
        builder.addCustomParameterRaw("aa", Arrays.asList("abc"));
        builder.addCustomParameterRaw("oo", Arrays.asList("abc"));
        assertEquals("/slloc1-slot1/tlnone/aaabc/ooabc/zzabc", builder.build().getAsURL());
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_key() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xab", Arrays.asList("abc", "def"));
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_key_not_null() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw(null, Arrays.asList("abc", "def"));
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_value_not_null() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xab", Arrays.asList("abc", null));
    }

    @Test( expected = IllegalArgumentException.class)
    public void custom_parameter_validate_value_not_empty() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Collections.<String>emptyList());
    }

    @Test( expected = IllegalArgumentException.class)
    public void basic_case_validate_req_params_missing() {
        AdheseOptions.Builder builder = new AdheseOptions.Builder();
        builder.build();
    }

    @Test
    public void can_clone() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Arrays.asList("abc", "def"));
        AdheseOptions original = builder.build();
        AdheseOptions duplicate = original.clone().build();

        assertEquals(original.getAsURL(), duplicate.getAsURL());
    }

    @Test
    public void clones_are_unique() {
        AdheseOptions.Builder builder = getBaseBuilder();
        builder.addCustomParameterRaw("xa", Arrays.asList("abc", "def"));
        AdheseOptions original = builder.build();
        AdheseOptions.Builder duplicateBuilder = original.clone();
        duplicateBuilder.addCustomParameterRaw("xb", Arrays.asList("abc", "def"));
        AdheseOptions duplicate = duplicateBuilder.build();

        assertNotEquals(original.getAsURL(), duplicate.getAsURL());
    }


    private AdheseOptions.Builder getBaseBuilder() {
        AdheseOptions.Builder builder = new AdheseOptions.Builder();
        builder.addSlot("slot1");
        builder.forLocation("loc1");
        return builder;
    }
}
