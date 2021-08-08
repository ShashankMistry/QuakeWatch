package com.shashank.quakewatch.ActivitiesAndFragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.shashank.quakewatch.R;
import com.shashank.quakewatch.customAdapterAndLoader.Constant;
import com.shashank.quakewatch.customAdapterAndLoader.NewsAdapter;
import com.shashank.quakewatch.customAdapterAndLoader.news;

import java.util.Objects;

public class newsData extends Fragment {


    public newsData() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }
//    private static final int NEWS_LOADER_ID = 2;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_data, container, false);
        //
        ListView newsView = rootView.findViewById(R.id.newsView);
        ProgressBar progressBar = rootView.findViewById(R.id.loading);
        SwipeRefreshLayout refresh = rootView.findViewById(R.id.refresh);
        progressBar.setVisibility(View.VISIBLE);
        newsView.animate().alpha(0.0f);
//        ArrayList<news> newsArrayList = new ArrayList<>();
        NewsApiClient newsApiClient = new NewsApiClient("46ef00c8abd44573afd74929552f32f5");
        //
        int nightModeFlags = requireContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        //  earthquakeListView.setAdapter(mAdapter);
        Configuration configuration = getResources().getConfiguration();
        int screenhtDp = configuration.screenHeightDp;
        //Toast.makeText(MainActivity.this,screenhtDp+" W: "+ScreenWidth,Toast.LENGTH_LONG).show();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT && screenhtDp < 550) {
            Toast.makeText(getContext()
                    , "this app is designed for BIG screen phones may not work properly."
                    , Toast.LENGTH_LONG).show();
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int ScreenWidth = configuration.screenWidthDp;
            if (ScreenWidth < 550) {
                Toast.makeText(getContext()
                        , "this app is designed for BIG screen phones may not work properly."
                        , Toast.LENGTH_LONG).show();
            }
        }

// /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Earthquake")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
//                        int total = response.getTotalResults();
                        int total = response.getArticles().size();
                        new Thread(() -> {
                            for (int  i = 0 ; i < total ; i++){
                                Article res = response.getArticles().get(i);
                                Constant.news.add(new news(res.getTitle(), res.getDescription(), res.getUrlToImage(), res.getUrl()));
                            }
                            requireActivity().runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                NewsAdapter adapter = new NewsAdapter(requireContext(),Constant.news);
                                newsView.setAdapter(adapter);
                                newsView.animate().alpha(1.0f).setDuration(1500);
                            });
                        }).start();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        refresh.setOnRefreshListener(() -> {
            Constant.news.clear();
            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q("Earthquake")
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
//                        int total = response.getTotalResults();
                            int total = response.getArticles().size();
                            new Thread(() -> {
                                for (int  i = 0 ; i < total ; i++){
                                    Article res = response.getArticles().get(i);
                                    Constant.news.add(new news(res.getTitle(), res.getDescription(), res.getUrlToImage(), res.getUrl()));
                                }
                                requireActivity().runOnUiThread(() -> {
                                    progressBar.setVisibility(View.GONE);
                                    NewsAdapter adapter = new NewsAdapter(requireContext(),Constant.news);
                                    newsView.setAdapter(adapter);
                                    newsView.animate().alpha(1.0f).setDuration(1500);
                                    refresh.setRefreshing(false);
                                });
                            }).start();

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    }
            );
        });
        // Set an item click listener on the ListView, which starts new activity with detailed data
        newsView.setOnItemClickListener((adapterView, view, position, l) -> {
            news News = (news) newsView.getAdapter().getItem(position);
            String url = News.getURL();
            Intent intent = new Intent(getContext(),WebView.class);
            intent.putExtra("url",url);
            intent.putExtra("request","NEWS");
            startActivity(intent);
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.

        return rootView;
    }



}