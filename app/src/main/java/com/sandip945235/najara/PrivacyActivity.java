package com.sandip945235.najara;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        TextView textView = findViewById(R.id.privacyText);
        textView.setText(
            "Privacy Policy\n\n" +
            "हम आपकी निजी जानकारी को सुरक्षित रखते हैं। " +
            "यह ऐप कोई भी व्यक्तिगत डेटा तीसरे पक्ष के साथ साझा नहीं करता है।\n\n" +
            "यहाँ अपनी पूरी प्राइवेसी पॉलिसी का टेक्स्ट डालें।"
        );
    }
}
