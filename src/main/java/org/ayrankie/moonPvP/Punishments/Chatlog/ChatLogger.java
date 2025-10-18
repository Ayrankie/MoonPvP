package org.ayrankie.moonPvP.Punishments.Chatlog;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import java.time.Instant;
import java.time.Duration;
import java.util.*;

public class ChatLogger implements Listener, CommandExecutor {
    // {oyuncuAdı: List<ChatMessage>}
    private static final Map<String, LinkedList<ChatMessage>> chatLogs = new HashMap<>();

    // ChatMessage: mesaj ve zaman damgası
    private static class ChatMessage {
        final String text;
        final Instant time;

        ChatMessage(String text, Instant time) {
            this.text = text;
            this.time = time;
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        LinkedList<ChatMessage> list = chatLogs.computeIfAbsent(name, k -> new LinkedList<>());
        list.addLast(new ChatMessage(event.getMessage(), Instant.now()));

        // Temizlik: 20 dakika öncesini sil
        Instant threshold = Instant.now().minus(Duration.ofMinutes(20));
        while (!list.isEmpty() && list.getFirst().time.isBefore(threshold)) {
            list.removeFirst();
        }
    }

    // Komut: /chatlog oyuncu
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Kullanım: /chatlog <oyuncu>");
            return true;
        }
        String target = args[0].toLowerCase();
        List<ChatMessage> messages = chatLogs.get(target);
        if (messages == null || messages.isEmpty()) {
            sender.sendMessage("Oyuncunun son 20 dakikada mesajı yok.");
            return true;
        }

        // Log id: isim + tarih
        String logId = target + "-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        saveChatLog(logId, target, messages);

        sender.sendMessage(ChatColor.GOLD + "-------------------------------");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + target + ChatColor.AQUA + " Adlı Oyuncunun Chatlogu Başarıyla Kaydedildi!");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.YELLOW + "Web'den görüntülemek için: https://staff.moonpvp.com/chatlogs/" + logId);
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GOLD +"-------------------------------");

        return true;
    }

  /*  public static void sendChatLog(String playerName) {
        List<ChatMessage> messages = chatLogs.get(playerName.toLowerCase());
        if (messages == null || messages.isEmpty()) return;

        // Son 20 dakikayı filtrele
        Instant threshold = Instant.now().minus(Duration.ofMinutes(20));
        List<ChatMessage> recentMessages = new LinkedList<>();
        for (ChatMessage msg : messages) {
            if (msg.time.isAfter(threshold)) recentMessages.add(msg);
        }
        if (recentMessages.isEmpty()) return;

        String logId = logIdFor(playerName.toLowerCase());
        saveChatLog(logId, playerName, recentMessages);
    } */

    public static String logIdFor(String playerName) {
        return playerName + "-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }

    private static void saveChatLog(String logId, String player, List<ChatMessage> messages) {
        try {
            // JSON array
            StringBuilder arr = new StringBuilder("[");
            for (int i = 0; i < messages.size(); i++) {
                ChatMessage msg = messages.get(i);
                arr.append("{\"text\":\"").append(msg.text.replace("\"", "\\\"")).append("\",")
                        .append("\"time\":").append(msg.time.getEpochSecond()).append("}");
                if (i < messages.size() - 1) arr.append(",");
            }
            arr.append("]");

            String json = "{\"id\":\"" + logId + "\",\"player\":\"" + player + "\",\"messages\":" + arr + "}";
            // POST isteği gönder
            java.net.URL url = new java.net.URL("http://72.60.129.142:3001/api/chatlogs");
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(json.getBytes("UTF-8"));
            con.getOutputStream().close();
            con.getInputStream().close();
        } catch (Exception e) { e.printStackTrace(); }
    }


}