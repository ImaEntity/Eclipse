package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class UpdateManager {
    public record Update(String version, String gameVersion, String jarFileName) {}

    public static Update getLatest() {
        try {
            URL url = URI.create("https://raw.githubusercontent.com/ImaEntity/Eclipse/latest/gradle.properties").toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            if (status < 200 || status > 299)
                return new Update(Eclipse.VERSION, Eclipse.MC_VERSION, null);

            InputStream stream = con.getInputStream();
            String properties = new String(stream.readAllBytes());

            stream.close();
            con.disconnect();

            String mod_version = properties
                    .split("mod_version=")[1]
                    .split("\n")[0];

            String archives_base_name = properties
                    .split("archives_base_name=")[1]
                    .split("\n")[0];

            return new Update(
                    "v" + mod_version.split("\\+")[0],
                    mod_version.split("\\+")[1].split("-")[0],
                    String.format(
                            "%s-%s.jar",
                            archives_base_name,
                            mod_version
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new Update(Eclipse.VERSION, Eclipse.MC_VERSION, null);
        }
    }
}
