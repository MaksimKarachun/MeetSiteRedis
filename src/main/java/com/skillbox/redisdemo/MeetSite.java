package com.skillbox.redisdemo;

public class MeetSite {

    private static int user_id = 1;

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();

        for (int i=1; i <= 20; i++) {
            redis.registerNewUser(user_id);
            user_id++;
        }

        int i = 1;

        try {
              while (true){
                  if (i == 2){
                      redis.userDonate();
                      i = 1;
                  }

                  redis.listUsers();
                  i++;
              }
        }
        catch (Exception e){
            redis.shutdown();
        }
        finally {
            redis.shutdown();
        }
    }
}
