package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс управляет структурой данных для хранения пользователей
 *
 * @author Kioresko Igor
 * @version 0.2
 */
@ThreadSafe
public class UserStorage {
    @GuardedBy("this")
    private final ConcurrentHashMap<Integer, User> storage = new ConcurrentHashMap<>();

    /**
     * Метод добавляет пользователя в хранилище если он в нем отсутствует
     *
     * @param user Пользователь - объект с которым проводится операция
     * @return true если операция прошла успешно, иначе false.
     */
    public synchronized boolean add(User user) {
        return Objects.equals(
                storage.putIfAbsent(user.getId(), user), null);
    }

    /**
     * Метод обновляет данные пользователя по ID
     *
     * @param user Пользователь - объект с которым проводится операция
     * @return true если операция прошла успешно, иначе false.
     */
    public synchronized boolean update(User user) {
        int id = user.getId();
        return storage.replace(id, storage.get(id), user);
    }

    /**
     * Метод удаляет пользователя из хранилища
     *
     * @param user Пользователь - объект с которым проводится операция
     * @return true если операция прошла успешно, иначе false.
     */
    public synchronized boolean delete(User user) {
        return Objects.equals(storage.remove(user.getId()), user);
    }

    /**
     * Метод проверяет баланс пользователя
     *
     * @param userFrom Пользователь чей баланс будет списан
     * @param amount   Сумма к списанию
     * @return true если баланс > или = сумме списания, иначе false.
     */
    private synchronized boolean balanceCheck(User userFrom, int amount) {
        return amount > 0 && userFrom.getAmount() >= amount;
    }

    /**
     * Метод совершает перевод средств от одного пользователя к другому по ID
     *
     * @param fromId ID пользователя отправителя
     * @param toId   ID пользователя получателя
     * @param amount Сумма перевода
     * @return true если операция прошла успешно, иначе false.
     */
    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        User userFrom = storage.get(fromId);
        User userTo = storage.get(toId);
        if (!Objects.equals(userFrom, null)
                && !Objects.equals(userTo, null)
                && balanceCheck(userFrom, amount)) {
            update(new User(fromId, userFrom.getAmount() - amount));
            update(new User(toId, userTo.getAmount() + amount));
            result = true;
        }
        return result;
    }

    /**
     * Метод возвращает текущий баланс пользователя
     *
     * @param id ID пользователя
     * @return Баланс пользователя
     */
    public synchronized int getBalance(int id) {
        int balance = -1;
        User user = storage.get(id);
        if (!Objects.equals(user, null)) {
            balance = user.getAmount();
        }
        return balance;
    }
}