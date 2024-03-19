package com.learnspring;

import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var albumOne = new Album("Avatar-1", "2010");
        var albumTwo = new Album("Avatar-2", "2023");

        Transaction transaction = null;

        // insert the record album information
        transaction = createAlbumDetails(transaction, albumOne, albumTwo);

        // Fetching the details
        getAlbumDetails(transaction);

    }

    private static Transaction createAlbumDetails(Transaction transaction, Album albumOne, Album albumTwo) {
        try(var session = HibernateHelper.getSessionFactory().openSession()) {
            // start transaction
            transaction = session.beginTransaction();

            session.save(albumOne);
            session.save(albumTwo);

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return transaction;
    }

    private static void getAlbumDetails(Transaction transaction) {
        try (var session = HibernateHelper.getSessionFactory().openSession()) {
            List<Album> albums = session.createQuery("from Album", Album.class).list();
            albums.forEach(System.out::println);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}