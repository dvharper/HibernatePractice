package org.test.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.test.model.User;
import org.test.util.HibernateUtil;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void create(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User existing = session.createQuery(
                            "from User where email = :email", User.class)
                    .setParameter("email", user.getEmail())
                    .uniqueResult();

            if (existing != null) {
                logger.warn("User with email={} already exists!", user.getEmail());
                return;
            }

            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("User created: {}", user);

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            logger.error("Creating error", e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                logger.info("Found user: {}", user);
            } else {
                logger.warn("User with id={} not found", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error while finding user with id={}", id, e);
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            logger.info("Found {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Error while getting all users", e);
            return List.of();
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("User updated: {}", user);
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            logger.error("Error while updating. User: {}", user, e);
        }
    }

    @Override
    public void delete(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(user);
            tx.commit();
            logger.info("User deleted: {}", user);
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            logger.error("Error while deleting user: {}", user, e);
        }
    }
}
