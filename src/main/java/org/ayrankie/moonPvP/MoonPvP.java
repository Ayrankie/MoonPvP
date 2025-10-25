package org.ayrankie.moonPvP;

import org.ayrankie.moonPvP.Listeners.AdminLoginListener;
import org.ayrankie.moonPvP.Listeners.ChatFormatterListener;
import org.ayrankie.moonPvP.Listeners.LoginListener;
import org.ayrankie.moonPvP.Punishments.*;
import org.ayrankie.moonPvP.Punishments.Chatlog.ChatLogger;
import org.ayrankie.moonPvP.Punishments.Control.KontrolCommand;
import org.ayrankie.moonPvP.Punishments.Control.KontrolListener;
import org.ayrankie.moonPvP.Punishments.GUIs.BanGUI;
import org.ayrankie.moonPvP.Punishments.GUIs.MuteGUI;
import org.ayrankie.moonPvP.PvPListeners.PvPListener;
import org.ayrankie.moonPvP.TAB.PermissionCommand;
import org.ayrankie.moonPvP.TAB.TabListFormatter;
import org.bukkit.plugin.java.JavaPlugin;
import okhttp3.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class MoonPvP extends JavaPlugin {

    private static MoonPvP instance;
    private TabListFormatter tabFormatter;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void onEnable() {
        // HikariCP havuzunu başlat!
        Database.initDBPool();
        instance = this;
        saveDefaultConfig();

        getLogger().info("MoonPvP 1.0 aktif!");
        sendServerStartedLog();
        getServer().getPluginManager().registerEvents(new PunishmentListeners(), this);
        getServer().getPluginManager().registerEvents(new AdminLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new MuteGUI(), this);
        getServer().getPluginManager().registerEvents(new ChatLogger(), this);
        getServer().getPluginManager().registerEvents(new BanGUI(), this);
        getServer().getPluginManager().registerEvents(new KontrolListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatterListener(), this);
        getServer().getPluginManager().registerEvents(new BanListener(Database.getDataSource()), this);
        getServer().getPluginManager().registerEvents(new PvPListener(), this);
        getServer().getPluginManager().registerEvents(new LoginListener(), this);
        getCommand("chatlog").setExecutor(new ChatLogger());
        getCommand("mute").setExecutor(new TempCommandMute(Database.getDataSource()));
        getCommand("kick").setExecutor(new KickCommand(Database.getDataSource()));
        getCommand("gerivites").setExecutor(new RevokeCommand());
        getCommand("kanıt").setExecutor(new Evidence(Database.getDataSource()));
        getCommand("ban").setExecutor(new BanCommand(Database.getDataSource()));
        getCommand("history").setExecutor(new HistoryCommand(Database.getDataSource()));
        getCommand("kontrol").setExecutor(new KontrolCommand());
        getCommand("moonpvpreload").setExecutor(new ReloadCommand(this));
        getCommand("yetkiver").setExecutor(new PermissionCommand());
        getCommand("yetkiver").setTabCompleter(new PermissionCommand());



        tabFormatter = new TabListFormatter(this);
        tabFormatter.startAutoUpdateTask();

        getServer().getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
                tabFormatter.updateTabForAll();
                tabFormatter.setTabHeaderFooter(e.getPlayer());
            }
        }, this);
    }

    private void sendServerStartedLog() {
        OkHttpClient client = new OkHttpClient();

        String message = "Sunucu Başlatıldı";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"message\":\"" + message + "\",\"timestamp\":\"" + timestamp + "\"}";

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://72.60.129.142:3001/api/server-log")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getLogger().warning("API isteği başarısız: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    getLogger().info("Sunucu başlatıldı logu gönderildi: " + response.code() + " " + response.message());
                    getLogger().info("İstek URL: " + call.request().url());
                } finally {
                    response.close();
                }
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("MoonPvP 1.0 deaktif!");
        // HikariCP havuzunu kapatmak ister misin?
        Database.closePool();
    }
}