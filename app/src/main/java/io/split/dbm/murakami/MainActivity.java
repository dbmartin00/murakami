package io.split.dbm.murakami;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.split.android.client.SplitClient;
import io.split.android.client.SplitClientConfig;
import io.split.android.client.SplitFactory;
import io.split.android.client.SplitFactoryBuilder;
import io.split.android.client.SplitResult;
import io.split.android.client.api.Key;
import io.split.android.client.events.SplitEvent;
import io.split.android.client.events.SplitEventTask;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MURAKAMI";

    public static SplitClient split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MParticleOptions options = MParticleOptions.builder(this)
                .credentials("us1-7a65afd42079f549948419d409792db2", "5ZE3YQJ3m-a9FF3u81vT3l8kvjiJrJtn6dxLTqDT8zxBQYZCMor0rHTTgE7aPaE_")
                .build();
        MParticle.start(options);

        String apiKey = "69haia9b9pf3b0rs951bj8kuufr4ke2987op";

        SplitClientConfig config = SplitClientConfig.builder()
                .enableDebug()
                .build();

        String matchingKey = "dmartin";
        Key k = new Key(matchingKey);

        try {
            SplitFactory splitFactory = SplitFactoryBuilder.build(apiKey, k, config, getApplicationContext());
            split = splitFactory.client();
            Log.i(TAG, "split client created");
            split.on(SplitEvent.SDK_READY, new SplitEventTask() {
                @Override
                public void onPostExecution(SplitClient client) {
                    Log.i(TAG, "onPostExecution");
                    String treatment = split.getTreatment("murakami");
                    Log.i(TAG, treatment);
                }

                @Override
                public void onPostExecutionView(SplitClient client) {
                    Log.i(TAG, "onPostExecutionView");
                }
            });

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        setContentView(R.layout.activity_main);

        new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view)).execute("http://www.cortazar-split.com/dog_on_the_couch.jpeg");

        Button nextButton = ((Button)findViewById(R.id.button));
        nextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "next");
                        SplitResult result = split.getTreatmentWithConfig("murakami", new HashMap());
                        String url = "";
                        try {
                            JSONObject configs = new JSONObject(result.config());
                            JSONArray urls = configs.getJSONArray("images");
                            url = urls.getString(count++ % urls.length());
                            new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view)).execute(url);
                            Log.i(TAG, "found " + urls.length() + " images");
                            Log.i(TAG, "urls[0]: " + urls.getString(0));
                        } catch (JSONException e) {
                            Log.e(TAG, "error with dynamic config: " + e.getMessage());
                        }

                        Map<String, String> customAttributes = new HashMap<String, String>();
                        customAttributes.put("category", "murakami");
                        customAttributes.put("title", "pets");
                        customAttributes.put("url", url);

                        MPEvent event = new MPEvent.Builder(result.treatment() + " Watched", MParticle.EventType.Navigation)
                                .customAttributes(customAttributes)
                                .build();

                        MParticle.getInstance().logEvent(event);
                    }
                });
    }
    public static int count = 0;

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            Log.i(TAG, "constructor DownloadImageFromInternet");
            this.imageView=imageView;
//            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            Log.i(TAG, "returning downloaded image");
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            Log.i(TAG, "setting image bitmap");
            imageView.setImageBitmap(result);
        }
    }
}