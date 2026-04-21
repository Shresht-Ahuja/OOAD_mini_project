package com.musicapp.factory;

import com.musicapp.model.Admin;
import com.musicapp.model.User;

/**
 * Factory Method (Creational) Design Pattern.
 *
 * Encapsulates the instantiation logic for User and Admin.
 * Callers never use `new User()` or `new Admin()` directly —
 * they always go through this factory, making it easy to add
 * new roles (e.g. Moderator) without changing client code.
 *
 * Design Principle — Open/Closed:
 *   Add new roles by extending this factory, not modifying it.
 */
public class UserFactory {

    private UserFactory() {}   // utility class — no instantiation

    /**
     * Creates a User or Admin based on the given role string.
     *
     * @param userId   unique identifier
     * @param name     display name
     * @param email    login email
     * @param password raw password (hash in production)
     * @param role     "ADMIN" → Admin, anything else → User
     * @return         newly created User (or Admin) instance
     */
    public static User createUser(int userId, String name, String email,
                                  String password, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            System.out.println("[FACTORY] Creating Admin: " + name);
            return new Admin(userId, name, email, password, "ADMIN");
        } else if ("ARTIST".equalsIgnoreCase(role)) {
            System.out.println("[FACTORY] Creating Artist: " + name);
            return new User(userId, name, email, password, "ARTIST");
        }
        System.out.println("[FACTORY] Creating User: " + name);
        return new User(userId, name, email, password, "USER");
    }
}
