package by.logonuk;

import by.logonuk.domain.User;
import by.logonuk.repository.UserRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        List<User> all = userRepository.findAll();

        for (User user : all) {
            System.out.println(user);
        }
    }
}
