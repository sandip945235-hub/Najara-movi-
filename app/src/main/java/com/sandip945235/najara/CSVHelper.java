package com.sandip945235.najara;

import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static List<Video> parse(String csv) {
        List<Video> list = new ArrayList<>();
        String[] lines = csv.split("\n");
        if (lines.length < 2) return list;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = splitCSV(line);
            if (parts.length < 11) continue;

            String title = parts[0].trim();
            if (title.isEmpty()) continue;

            list.add(new Video(
                    title,
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim(),
                    parts[7].trim(),
                    parts[8].trim(),
                    parts[9].trim(),
                    parts[10].trim()
            ));
        }
        return list;
    }

    private static String[] splitCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
  }
