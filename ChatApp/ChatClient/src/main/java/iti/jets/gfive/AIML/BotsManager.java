package iti.jets.gfive.AIML;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


// todo add exception handling
public class BotsManager {
    private static final BotsManager instance = new BotsManager();
    private static final String botsPath = "src/main/resources/iti/jets/gfive";
    private List<Bot> bots;
    private List<Chat> chats;


    private BotsManager() {
    }

    public static BotsManager getInstance() {
        return instance;
    }

    public void initBotChats() {
        bots = new ArrayList<>();
        chats = new ArrayList<>();
        bots = createBots();
        bots.forEach(bot -> chats.add(new Chat(bot)));
    }

    public String askBots(String msg) {
        for (int i = 0; i < bots.size(); i++) {
            String answer = askBot(i, msg);
            if (!answer.equals("I have no answer for that.")) {
                return answer;
            }
        }
        return "I have no answer for that BS.";
    }

    public String askBot(int botId, String msg) {
        return chats.get(botId).multisentenceRespond(msg);
    }

    public void printBots() {
        System.out.printf("Found %s Bots: ", bots.size());
        bots.forEach(bot -> System.out.print(bot.getName() + " "));
        System.out.println();
    }
    public void printBotsProperties(){
        bots.forEach(bot -> bot.getProperties().forEach((s, s2) -> System.out.println(s + ": " + s2)));
    }

    public List<Bot> createBots() {
        List<Bot> bots = new ArrayList<>();
        File[] directories = new File(botsPath + "/bots")
                .listFiles(File::isDirectory);

        if (directories != null) {
            bots = Arrays.stream(directories)
                    .map(File::getName)
                    .map(name -> new Bot(name, botsPath))
                    .collect(Collectors.toList());
        }
        return bots;
    }


}
