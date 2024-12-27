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


    @Override
    @Transactional
    public void reorderIds() {
        try {
            entityManager.createNativeQuery("CREATE TABLE temp_users AS SELECT * FROM users ORDER BY id").executeUpdate();
            entityManager.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
            entityManager.createNativeQuery("INSERT INTO users (name, email, age, created_at) SELECT name, email, age, created_at FROM temp_users").executeUpdate();
            entityManager.createNativeQuery("DROP TABLE temp_users").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            entityManager.clear();
            System.out.println("✅ IDs успешно перенумерованы и автоинкремент сброшен.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при сбросе ID: " + e.getMessage());
            throw new RuntimeException("Ошибка при сбросе ID", e);
        }
    }
}
