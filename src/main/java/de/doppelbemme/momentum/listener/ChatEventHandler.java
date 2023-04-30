package de.doppelbemme.momentum.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ChatEventHandler {
    boolean booleanunscramble = false;
    boolean placeholder = false;
    char[] unscramble;                                                              //Liste einzelner Buchstaben die unscrambled werden müssen
    String unscramble_word;                                                         //Das Wort was gescrambled werden muss
    List<String> allscrambledwords;                                                 //Liste der Worter welche in Frage kommen
    boolean equal = false;
    boolean notinlist = false;
    String newword = " ";
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) throws IOException, InterruptedException {
        notinlist = true;
        String message = event.getRawText().replace(".","").toLowerCase();
        if(message.contains("unscramble the word")){
            booleanunscramble = true;
            String[] line = message.split(" ");
            for(String word : line){
                if(placeholder){
                    unscramble_word = word;
                    placeholder = false;
                    break;
                }
                if(word.equals("word")) {
                    placeholder = true;
                }
            }
            JsonReader(unscramble_word.length());
            unscramble = unscramble_word.toCharArray();
            for(String wordscramble : allscrambledwords){
                for(char character : unscramble){
                    if(wordscramble.contains(String.valueOf(character))){
                        equal = false;
                        break;
                    }else{
                        equal = true;
                    }
                }
                if(equal){
                    notinlist = false;
                    if(wordscramble.length() <= 5){
                        Thread.sleep(700);
                    }else{
                        if(wordscramble.length() < 9){
                            Thread.sleep(1000);
                        }else{
                            Thread.sleep(1500);
                        }
                    }
                    Minecraft.getInstance().getConnection().sendChat(wordscramble);

                    equal = false;
                    break;
                }
            }
        }
        if(message.contains("got the answer")){
            String[] line = message.split(" ");
            for(String word : line){
                if(placeholder){
                    newword = word;
                    placeholder = false;
                    break;
                }
                if(word.equals("answer")){
                    placeholder = true;
                }
            }
            //JsonUpdate(newword);
        }
    }

    public void JsonReader(int length){
        String fileName = "C:\\Users\\ahost\\Documents\\Java\\momentum\\src\\main\\java\\de\\doppelbemme\\momentum\\listener\\ScrambleWords.json";
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
            System.out.println(jsonString);
            JSONObject obj = new JSONObject(jsonString);
            JSONArray words =  obj.getJSONObject("words").getJSONArray("4");
            String s = words.getString(1);
            Minecraft.getInstance().getConnection().sendChat(s);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
