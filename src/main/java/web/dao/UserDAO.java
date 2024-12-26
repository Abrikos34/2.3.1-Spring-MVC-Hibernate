package web.dao;

import web.model.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();
    void saveUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    void reorderIds(); // Новый метод для обновления ID
}
