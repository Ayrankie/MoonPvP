package org.ayrankie.moonPvP;

import org.bukkit.plugin.java.JavaPlugin;
import okhttp3.*;

import java.io.IOException;

public final class MoonPvP extends JavaPlugin {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void onEnable() {
        getLogger().info("MoonPvP 1.0 aktif!");
        sendTestApiRequest();
    }

    private void sendTestApiRequest() {
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/posts/1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getLogger().warning("API isteği başarısız: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    getLogger().info("API yanıtı: " + responseData);
                } else {
                    getLogger().warning("API isteği başarısız: " + response.message());
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}