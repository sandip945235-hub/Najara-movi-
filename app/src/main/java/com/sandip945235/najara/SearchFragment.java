package com.sandip945235.najara;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String CSV_URL =
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vR7mJZgRFUfXAdW7HWEK4RCbmKlHrOZLhFueYvvlzq9IVWSBwAITF2VJaNresbGnH4RiAKKS0GYFcHt/pub?output=csv";

    RecyclerView recyclerView;
    EditText searchBox;
    VideoAdapter adapter;
    List<Video> allVideos = new ArrayList<>();
    List<Video> filtered = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = v.findViewById(R.id.searchRecycler);
        searchBox = v.findViewById(R.id.searchBox);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getContext(), filtered);
        recyclerView.setAdapter(adapter);

        loadData();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return v;
    }

    private void loadData() {
        RequestQueue q = Volley.newRequestQueue(getContext());
        StringRequest req = new StringRequest(Request.Method.GET, CSV_URL,
                response -> {
                    allVideos.clear();
                    allVideos.addAll(CSVHelper.parse(response));
                    filtered.clear();
                    filtered.addAll(allVideos);
                    adapter.notifyDataSetChanged();
                },
                error -> { });
        q.add(req);
    }

    private void filter(String text) {
        filtered.clear();
        if (text.isEmpty()) {
            filtered.addAll(allVideos);
        } else {
            for (Video v : allVideos) {
                if (v.title.toLowerCase().contains(text.toLowerCase()) ||
                    v.category.toLowerCase().contains(text.toLowerCase())) {
                    filtered.add(v);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
                                         }
