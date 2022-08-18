package by.logonuk;

import by.logonuk.domain.User;
import by.logonuk.repository.user.UserRepository;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static  final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        List<User> all = userRepository.findAll();

        for (User user : all) {
            System.out.println(user);
        }
        System.out.println();
        User user = userRepository.findById(3L);
        System.out.println(user);

        String line = SCANNER.nextLine();

        List<User> all1 = userRepository.findByLine(line);

        for (User user1 : all1) {
            System.out.println(user1);
        }
    }
}
