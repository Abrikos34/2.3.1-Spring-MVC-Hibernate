package web.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            System.out.println("✅ Пользователь с ID " + id + " успешно удалён из базы данных.");
        } else {
            System.err.println("❌ Пользователь с ID " + id + " не найден.");
            throw new IllegalArgumentException("Пользователь с таким ID не существует.");
        }
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }
}
