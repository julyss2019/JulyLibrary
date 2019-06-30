package com.github.julyss2019.mcsp.julylibrary.skull;

import com.github.julyss2019.mcsp.julylibrary.skull.json.Profile;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CacheSkullManager {
    private Map<String, String> textureMap = new HashMap<>();
    private static Gson gson = new Gson();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();

    public void loadTexture(String playerName) {
        okHttpClient.newCall(new Request.Builder().url("https://api.mojang.com/users/profiles/minecraft/" + playerName).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    ResponseBody responseBody = response.body();

                    if (responseBody == null) {
                        return;
                    }

                    String responseBodyStr = responseBody.string();
                    Map uuidMap = gson.fromJson(responseBodyStr, Map.class);

                    if (uuidMap == null || !uuidMap.containsKey("id")) {
                        return;
                    }

                    {
                        okHttpClient.newCall(new Request.Builder().url("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidMap.get("id")).build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {}

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                ResponseBody responseBody = response.body();

                                if (responseBody == null) {
                                    return;
                                }

                                String responseBodyStr = responseBody.string();
                                Profile profile = gson.fromJson(responseBodyStr, Profile.class);

                                if (profile != null) {
                                    System.out.println(profile.getProperties().get(0).getValue());
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public String getTexture(String playerName) {
        return textureMap.get(playerName);
    }
}
