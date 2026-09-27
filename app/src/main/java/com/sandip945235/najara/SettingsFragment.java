package com.sandip945235.najara;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        v.findViewById(R.id.privacyBtn).setOnClickListener(view ->
                startActivity(new Intent(getContext(), PrivacyActivity.class)));

        v.findViewById(R.id.shareBtn).setOnClickListener(view -> {
            Intent s = new Intent(Intent.ACTION_SEND);
            s.setType("text/plain");
            s.putExtra(Intent.EXTRA_TEXT,
                    "Download NAJARA App: https://play.google.com/store/apps/details?id=" +
                    getContext().getPackageName());
            startActivity(Intent.createChooser(s, "Share via"));
        });

        return v;
    }
                                         }
