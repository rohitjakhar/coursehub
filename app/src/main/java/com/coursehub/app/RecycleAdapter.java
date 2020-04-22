package com.coursehub.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private List<ModelClass> modelClassList;
    private String extraOnCourse = "I saw this course at free and thought of you\n";
    private String link = "https://play.google.com/store/apps/details?id=com.coursehub.app";
    private String extraOnShare = "\nDownload Course Hub App and get Daily new 100% free courses\n" + link;

    public RecycleAdapter(List<ModelClass> modelClassList) {
        this.modelClassList = modelClassList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list, parent, false);
        view.setClickable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ModelClass modelClass = modelClassList.get(position);
        holder.courseTitle.setText(modelClass.getCourseTitle());
        holder.courseBody.setText(modelClass.getCourseBody());
        // holder.mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        holder.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        holder.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                holder.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        final String enrollLink = modelClass.getCourseLink();
        final String courseLink = extraOnCourse + modelClass.getCourseLink() + "\n" + extraOnShare;

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText(courseLink, courseLink);
                manager.setPrimaryClip(data);
                Toast.makeText(v.getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        holder.enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (holder.mInterstitialAd.isLoaded()) {
                    holder.mInterstitialAd.show();
                    holder.mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            String UrlLink = enrollLink;
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.intent.setPackage("com.android.chrome");
                            customTabsIntent.launchUrl(v.getContext(), Uri.parse(UrlLink));
                        }
                    });
                } else {
                    String UrlLink = enrollLink;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.setPackage("com.android.chrome");
                    customTabsIntent.launchUrl(v.getContext(), Uri.parse(UrlLink));

                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, courseLink);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseTitle, courseBody;
        private ImageButton copy, share;
        private Button enroll;
        private InterstitialAd mInterstitialAd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.courseHeading);
            courseBody = itemView.findViewById(R.id.courseBody);
            copy = itemView.findViewById(R.id.copy);
            share = itemView.findViewById(R.id.share);
            mInterstitialAd = new InterstitialAd(itemView.getContext());
            mInterstitialAd.setAdUnitId("ca-app-pub-7968569441691248/7888754531");
            enroll = itemView.findViewById(R.id.enroll);
        }
    }
}

