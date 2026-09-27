package com.sandip945235.najara;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String CSV_URL =
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vR7mJZgRFUfXAdW7HWEK4RCbmKlHrOZLhFueYvvlzq9IVWSBwAITF2VJaNresbGnH4RiAKKS0GYFcHt/pub?output=csv";

    RecyclerView recyclerView;
    TabLayout tabLayout;
    ProgressBar progressBar;
    VideoAdapter adapter;
    List<Video> allVideos = new ArrayList<>();
    List<Video> filteredVideos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        tabLayout = v.findViewById(R.id.tabLayout);
        progressBar = v.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new VideoAdapter(getContext(), filteredVideos);
        recyclerView.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                filterData(tab.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadVideos();
        return v;
    }

    private void loadVideos() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue q = Volley.newRequestQueue(getContext());

        StringRequest req = new StringRequest(Request.Method.GET, CSV_URL,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    allVideos.clear();
                    allVideos.addAll(CSVHelper.parse(response));

                    if (allVideos.isEmpty()) {
                        Toast.makeText(getContext(), "कोई वीडियो नहीं मिला",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    tabLayout.removeAllTabs();
                    tabLayout.addTab(tabLayout.newTab().setText("All"));
                    List<String> cats = new ArrayList<>();
                    for (Video v : allVideos) {
                        if (!cats.contains(v.category) && !v.category.isEmpty()) {
                            cats.add(v.category);
                            tabLayout.addTab(tabLayout.newTab().setText(v.category));
                        }
                    }
                    filterData("All");
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),
                            "Load failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
        q.add(req);
    }

    private void filterData(String category) {
        filteredVideos.clear();
        for (Video v : allVideos) {
            if (category.equalsIgnoreCase("All") ||
                v.category.equalsIgnoreCase(category)) {
                filteredVideos.add(v);
            }
        }
        adapter.notifyDataSetChanged();
    }
                                              }
