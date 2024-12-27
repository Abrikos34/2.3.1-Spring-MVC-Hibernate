package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDAO;

    @Autowired
    public UserServiceImpl(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        try {
            userDAO.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                org.hibernate.exception.ConstraintViolationException constraintEx =
                        (org.hibernate.exception.ConstraintViolationException) e.getCause();

                if ("users.email".equals(constraintEx.getConstraintName())) {
                    throw new RuntimeException("Ошибка: Пользователь с таким email уже существует!");
                }
            }
            throw new RuntimeException("Ошибка целостности данных при сохранении пользователя", e);
        } catch (Exception e) {
            throw new RuntimeException("Неизвестная ошибка при сохранении пользователя", e);
        }
    }



    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null.");
        }
        try {
            userDAO.deleteUser(id);
            System.out.println("✅ Пользователь с ID " + id + " успешно удалён.");
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Некорректный ID пользователя: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении пользователя: " + e.getMessage());
            throw new RuntimeException("Ошибка при удалении пользователя с ID: " + id, e);
        }
    }


    @Override
    public User getUserById(Long id) {
        try {
            return userDAO.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователя с ID: " + id, e);
        }
    }

    @Override
    @Transactional
    public void reorderIds() {
        try {
            userDAO.reorderIds();
            System.out.println("✅ IDs успешно перенумерованы через сервис.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при перенумерации ID в сервисе: " + e.getMessage());
            throw new RuntimeException("Ошибка при перенумерации ID в сервисе", e);
        }
    }
}
