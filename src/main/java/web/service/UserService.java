package web.service;

import web.model.User;

import java.util.List;

public interface UserService {
    /**
     * Получить список всех пользователей.
     *
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Сохранить пользователя (добавить нового или обновить существующего).
     *
     * @param user пользователь для сохранения
     */
    void saveUser(User user);

    /**
     * Удалить пользователя по его ID.
     *
     * @param id ID пользователя для удаления
     */
    void deleteUser(Long id);

    /**
     * Получить пользователя по его ID.
     *
     * @param id ID пользователя
     * @return пользователь с указанным ID
     */
    User getUserById(Long id);

    /**
     * Переупорядочить ID пользователей.
     * Метод обновляет ID существующих пользователей,
     * начиная с 1 и увеличивая на 1 для каждого пользователя.
     */
    void reorderIds();
}
