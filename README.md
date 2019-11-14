# Adhese SDK
## Introduction
This is the Adhese SDK for native Android. The SDK enables you to load ad data from the Adhese API
and to display them with a native view.

## Code example
Initialise the SDK once for the application. This can be called in your Application class or MainActivity.

        Adhese.initialise(this);

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

        // Build the options
        AdheseOptions options = new AdheseOptions.Builder()
                .withAccount("_demo_ster_a_")
                .forLocation("demo")
                .addSlot("billboard")
                .addSlot("halfpage")
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

## Available listeners
The `AdView` has a few listeners available that can be implemented to watch for communication to the Adhese API.

        AdView.OnAdLoadedListener                   // Triggers when the ad is loaded inside the view
        AdView.OnViewImpressionNotifiedListener     // Triggers when the ad has become visible in the viewport
        AdView.OnTrackerNotifiedListener            // Triggers when the tracker URL has been called successfully.
        AdView.OnErrorListener                      // Triggers when any error occurs within the AdView (for example failing to call the tracker URL)
        AdView.OnAdClickListener                    // Triggers when the advertisement was clicked.

## Extra

Call `AdView.setShouldOpenAd()` to enable/disable automatic opening of the ad in the browser. The default value is true, so it will open automatically.
However, when setting it to false and implementing the `OnAdClickListener` you can implement custom behaviour

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
