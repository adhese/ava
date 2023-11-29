# Adhese SDK
## Introduction
This is the Adhese SDK for native Android. The SDK enables you to load ad data from the Adhese API
and to display them with a native view.

## Code example
Initialise the SDK once for the application. This can be called in your Application class or MainActivity.
The first parameter should be an Android Context and the second parameter is the account name.

        Adhese.initialise(this, "demo");

Create your view and add the AdView

        ...

        <com.endare.adhese.sdk.views.AdView
            android:id="@+id/billboardAd"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="16dp">
        </com.endare.adhese.sdk.views.AdView>

        ...

The SDK is now ready to fetch ad data. Here's an example on how to fetch ad data:
        
        // custom parameters: example for contextual targeting
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("Sports");
        topics.add("Basketball");
        
        ArrayList<String> personalities = new ArrayList<String>();
        personalities.add("Kobe Bryant");
        personalities.add("LA Lakers");
        
        // Build the options
        AdheseOptions options = new AdheseOptions.Builder()
                .forLocation("_demo_ster_a_")
                .addSlot("billboard")
                .addSlot("halfpage")
                .addCustomerParameterRaw("ct", topics) // custom parameters can be added as a single String or a collection of Strings
                .addCustomerParameterRaw("rl", personalities)
                .withCookieMode(CookieMode.ALL) // This is the Adhese parameter "tl"
                .build();

        // Load the ad data and assign on of the fetched ads to the AdView    
        Adhese.loadAds(options, new APICallback<List<Ad>>() {
            @Override
            public void onResponse(List<Ad> ads, APIError error) {
                if (error != null) {
                    return;
                }

                Ad billboard = ads[0];
                billboardAdView.setAd(billboard);
            }
        });

That's it, your ad should now appear in the view.
## About Custom Parameters
The addCustomerParameterRaw method allows you to register data that will be sent to the Adhese endpoint. This data is typically used to target campaigns, generate reporting or create deals and segments for programmatic partners.

Each custom parameter consists of a key and a list of values. The key is a String with a maximum length of 2 chars. In your Adhese account, you need to configure the keys so they can get picked up when processing requests.

[Here is a list of often used targets](https://confluence.adhese.org/display/AD/Request+target+parameters), consult your Adhese support contact for more details.

There are three versions of the addCustomerParameterRaw method. When you register a key multiple times, the values will be merged. Duplicate values will be ignored.

        addCustomParameterRaw(String key, String value); // key has a maximum length of 2 chars
        addCustomParameterRaw(String key, Collection<String> values); // key has a maximum length of 2 chars
        addCustomParametersRaw(Map<String, Collection<String>> map); // key has a maximum length of 2 chars

You can also remove targets, useful when you want to reuse an options instance but change some of the custom parameters.

        removeCustomParameters();
        removeCustomParameter(String key);

## Available listeners
The `AdView` has a few listeners available that can be implemented to watch for communication to the Adhese API.

        AdView.OnAdLoadedListener                   // Triggers when the ad is loaded inside the view
        AdView.OnViewImpressionNotifiedListener     // Triggers when the ad has become visible in the viewport
        AdView.OnTrackerNotifiedListener            // Triggers when the tracker URL has been called successfully.
        AdView.OnErrorListener                      // Triggers when any error occurs within the AdView (for example failing to call the tracker URL)
        AdView.OnAdClickListener                    // Triggers when the advertisement was clicked.
        AdView.AdvancedOnAdClickListener            // Triggers when the advertisement was clicked, but passes the resulting url to the listener instead of following it in browser.

## Extra

If you wish to handle navigation after a click yourself, use an AdvancedOnAdClickListener.
It disables opening up the target url in browser, and passes the the url to the listener.

~~Call `AdView.setShouldOpenAd()` to enable/disable automatic opening of the ad in the browser. The default value is true, so it will open automatically.
However, when setting it to false and implementing the `OnAdClickListener` you can implement custom behaviour~~

## Publishing
The Gradle file has been set-up to enable publishing to JCenter (Bintray).

Make sure the following details have been added to the `local.properties` file:

    bintray.pkg.userOrg=xxxxxx
    bintray.user=xxxxxxxx
    bintray.apikey=xxxxxxxxxxxxxxxx

Follow these steps to publish the SDK:

1. Change the version number in the `build.gradle` file.
2. Run the following command: `./gradlew bintrayUpload`
3. When successful you should see your new version on `https://bintray.com/tsturtew/ava/com.endare.adhese.sdk`
4. A pop-up should be shown on the website stating that you have unpublished items. Click on publish.
5. Done!
