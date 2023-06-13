package com.epam.esm.util_repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class FillTablesUtil {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void fillTables() {

        List<Tag> tags = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            Tag tag = new Tag((long) i, String.valueOf(i));
            tags.add(i, tag);
            em.persist(tag);
        }

        for (int i = 0; i < 1000; i++) {
            em.persist(new User((long) i, i + "@gmail.com"));
        }

        List<Integer>         gcDiscountsPull  = List.of(10, 20, 30, 40, 50, 60, 70, 80, 90);
        for (int i = 0; i < 10000; i++) {
            Random    random   = new Random();
            int       discount = gcDiscountsPull.get(random.nextInt(9));
            List<Tag> gcTags   = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                gcTags.add(tags.get((i + j) % 1000));
            }

            em.persist(new GiftCertificate((long) i, discount + "% discount",
                    "Get " + discount + "% " + "discount in our shop shop.com", random.nextInt(991) + 10 + 0.99,
                    random.nextInt(8) + 7, true, gcTags));
        }

        for (int i = 0; i < 5000; i++) {
            GiftCertificate gc    = em.find(GiftCertificate.class, i);
            em.persist(new Order(em.find(User.class, i / 5), gc, gc.getPrice()));

            gc.setActive(false);
            em.merge(gc);
        }
    }
}
