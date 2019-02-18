package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            boolean first = true;
            String menu = "";
            do {
                if (!first) {
                    System.out.println("Не корректный ввод, попробуйте еще раз");
                    System.out.println("--------");
                }
                first = false;
                System.out.println("Выберите действие:");
                System.out.println("Войти под своим логином - 1");
                System.out.println("Зарегистрироваться - 2");
                System.out.println("Выход - 0");
                menu = scanner.nextLine();
                System.out.println("--------");
            } while (!("1".equals(menu) || "2".equals(menu) || "0".equals(menu) || "".equals(menu)));


            int res = 200;
            if ("1".equals(menu)) {
                res = User.checkIn();
            } else if ("2".equals(menu)) {
                res = User.create();
            } else {
                return;
            }

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }

            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();

            System.out.println("Вводите сообщения и команды: ");
            while (true) {
                String text = scanner.nextLine();
                if (text.isEmpty()) break;


                Message m = new Message(User.getLogin(), null, text);
                res = SendMessage.send(Utils.getURL() + "/add", m);

                if (res != 200) { // 200 OK
                    System.out.println("HTTP error occured: " + res);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
