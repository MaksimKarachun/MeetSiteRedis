package com.skillbox.redisdemo;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import static java.lang.System.out;

public class RedisStorage {

    // Объект для работы с Redis
    private RedissonClient redisson;

    // Объект для работы с ключами
    private RKeys rKeys;

    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> users;

    private final static String KEY = "ONLINE_USERS";


    void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        users = redisson.getScoredSortedSet(KEY, StringCodec.INSTANCE);
        rKeys.delete(KEY);
    }

    void shutdown() {
        redisson.shutdown();
    }

    void registerNewUser(int user_id)
    {
        //ZADD ONLINE_USERS
        users.add(user_id, String.valueOf(user_id));
    }

    void listUsers() throws InterruptedException {
        for(String user : users){
            out.println("- На главной странице показываем пользователя " + user);
            Thread.sleep(500);
        }
    }

    void userDonate() throws InterruptedException {
        int userNumber =  (int) (1 + Math.random() * 20);
        out.println("> Пользователь " + userNumber + " оплатил платную услугу " + users.getScore(String.valueOf(userNumber)));
        users.addScore(String.valueOf(userNumber), users.firstScore() - users.getScore(String.valueOf(userNumber)) - 1);
        Thread.sleep(1000);
    }

}
