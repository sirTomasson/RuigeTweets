package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;

/**
 * Created by Eric on 10-5-2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private FrameLayout profileBackground;
    private ImageView profileImg;
    private TextView name;
    private TextView screenName;
    private TextView description;
    private TextView followers;
    private TextView retweets;
    private TextView location;
    private TwitterModel model = TwitterModel.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.PROFILE_ID)){
            int id = intent.getIntExtra(MainActivity.PROFILE_ID, -1);
            if (id != -1) {
                User u = model.getUser(id);

                initXmlElements();

                Picasso.with(this).
                        load(u.getProfileImageUrl())
                        .into(profileImg);

                URL profileBackgroundUrl = null;
                try {
                    profileBackgroundUrl = new URL(u.getProfileBackgroundUrl());
                    DownloadFilesTask task = (DownloadFilesTask) new DownloadFilesTask().execute(profileBackgroundUrl);
                    Bitmap background = task.get();
                    profileBackground = (FrameLayout) findViewById(R.id.rl_profileBackground);
                    BitmapDrawable bmpBackground = new BitmapDrawable(getResources(), background);
                    profileBackground.setBackground(bmpBackground);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                name.setText(u.getName());
                screenName.setText(u.getScreenName());
                description.setText(u.getDescription());
                followers.setText(u.getFollowers());
                retweets.setText(u.getRetweets());
                location.setText(u.getLocation());
            }
        }
    }
    private class DownloadFilesTask extends AsyncTask<URL,Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }


    private void initXmlElements() {
        profileImg = (ImageView) findViewById(R.id.iv_profileImg);
        name = (TextView) findViewById(R.id.tv_name);
        screenName = (TextView) findViewById(R.id.tv_sceenName);
        description = (TextView) findViewById(R.id.tv_description);
        followers = (TextView) findViewById(R.id.tv_followers);
        retweets = (TextView) findViewById(R.id.tv_retweets);
        location = (TextView) findViewById(R.id.tv_location);
        profileBackground = (FrameLayout) findViewById(R.id.rl_profileBackground);

    }

    private void setContent(User u)
            throws ExecutionException, InterruptedException,IOException {
        //TODO : in ontwikkeling nog niet klaar voor gebruik.
        URL profileImgUrl = new URL(u.getProfileUrl());
        URL profileBackgroundImgUrl = new URL (u.getProfileBackgroundUrl());

        Bitmap bmpProfile = new DownloadFilesTask()
                .execute(profileImgUrl).get();

        Bitmap bmpBackground = new DownloadFilesTask()
                .execute(profileBackgroundImgUrl).get();

        BitmapDrawable drawableBackground = new BitmapDrawable(getResources(), bmpBackground);

        profileImg.setImageBitmap(bmpProfile);
        profileBackground.setBackground(drawableBackground);
        name.setText(u.getName());
        screenName.setText(u.getScreenName());
        description.setText(u.getDescription());
        followers.setText(u.getFollowers());
        retweets.setText(u.getRetweets());
        location.setText(u.getLocation());
    }
}
