# murakami

A screen with an image and a button make up the app.  Pressing the button cycles through images.  The images are defined in a JSON dynamic config with the on/off "murakami" split.

Highlights dynamic content and feature flag controls.

Shows integration with mParticle and Quantum Metric.  You need to get keys for Split, mParticle, and Quantum Metric to install in the code appropriately.

To finish Quantum Metric integration, you must also install an AAR you can obtain from them directly:

```
app/libs/quantum-1.0.17.aar
```

Or similar version.  The app/build.gradle has references to Split, Quantum Metric, and mParticle:

```
    // For Split
    implementation 'io.split.client:android-client:2.10.0'
    implementation 'org.json:json:20090211'

    // For Quantum Metric
    implementation fileTree(dir: 'libs', include: ['quantum*.aar'])

    // For mParticle
    implementation 'com.mparticle:android-core:5+'
    implementation 'com.google.android.gms:play-services-ads-identifier:16.0.0'
    implementation 'com.android.installreferrer:installreferrer:1.0'
 ```

Quantum Metric also needs to create a custom event, e.g. "Split Android Test", to aggregate the events sent by the impression listener.  In the example code, this event has value 3.

```
QuantumMetric.sendEvent(3, impressionSummary, EventType.Encrypted);
```

You may need to update the code to reflect you integer event code.

You can recreate the "murakami" split easily with two treatments.

"tea" has JSON config:

```
{ 
  "images" : [
    "https://images.unsplash.com/photo-1594631252845-29fc4cc8cde9?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3387&q=80",
    "https://images.unsplash.com/photo-1531969179221-3946e6b5a5e7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3264&q=80",
    "https://images.unsplash.com/photo-1504382103100-db7e92322d39?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2686&q=80"
    ]
}
```

"coffee" has JSON config:

```
{ 
  "images" : [
    "https://images.unsplash.com/photo-1509042239860-f550ce710b93?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3387&q=80",
    "https://images.unsplash.com/photo-1512568400610-62da28bc8a13?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3387&q=80",
    "https://images.unsplash.com/photo-1522992319-0365e5f11656?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3387&q=80"
    ]
}
```

These images are borrowed from a public site, so there's a chance the URLs will break.  In that case, substitute your own image URLs.


David.Martin@split.io
