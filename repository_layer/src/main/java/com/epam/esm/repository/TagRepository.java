package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO class for {@link Tag} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

    Optional<Tag> findByName(String name);

    void deleteByName(String name);

    /**
     * @return the most widely used tag of a user with the highest cost of all orders
     */
    @Query(value = "select tags.id, tags.name, tags.created_date, tags.last_modified_date " +
            "from tags " +
            "         right join gift_certificates_tags gct on tags.id = gct.tag_id " +
            "         right join orders_gift_certificates ogc on gct.gc_id = ogc.gc_id " +
            "         right join orders on ogc.order_id = orders.id " +
            "where user_id in " +
            "      (select * from (select user_id from orders group by user_id order by sum(cost) desc limit 1) as userId) " +
            "group by tag_id " +
            "order by count(tag_id) desc " +
            "limit 1", nativeQuery = true)
    Optional<Tag> complexNativeQuery();
}
