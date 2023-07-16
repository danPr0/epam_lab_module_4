package com.epam.esm.util_service;

import com.epam.esm.entity.*;
import com.epam.esm.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class FillTablesUtil {

    private final static String USER_DEFAULT_PASSWORD = "password";

    private final GiftCertificateRepository gcRepository;
    private final TagRepository             tagRepository;
    private final UserRepository            userRepository;
    private final OrderRepository           orderRepository;
    private final RoleRepository            roleRepository;
    private final ProviderRepository        providerRepository;
    private final PasswordEncoder           passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(FillTablesUtil.class);

    @Autowired
    public FillTablesUtil(
            GiftCertificateRepository gcRepository, TagRepository tagRepository, UserRepository userRepository,
            OrderRepository orderRepository, RoleRepository roleRepository, ProviderRepository providerRepository,
            @Lazy PasswordEncoder passwordEncoder) {

        this.gcRepository       = gcRepository;
        this.tagRepository      = tagRepository;
        this.userRepository     = userRepository;
        this.orderRepository    = orderRepository;
        this.roleRepository     = roleRepository;
        this.providerRepository = providerRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    @Transactional
    public void fillTables() {

        List<Tag> tags = insertTags();
        logger.info("Tags were added.");

        List<GiftCertificate> gcList = insertGiftCertificates(tags);
        logger.info("Gift certificates were added.");

        List<User> users = insertUsers();
        logger.info("Users were added.");

        insertOrders(gcList, users);
        logger.info("Orders were added.");
    }

    private List<Tag> insertTags() {

        List<Tag> tags = new ArrayList<>(1000);
        for (int i = 1; i <= 1000; i++) {
            Tag tag = Tag.builder().name(String.valueOf(i)).build();
            tags.add(i - 1, tagRepository.save(tag));
        }

        return tags;
    }

    private List<User> insertUsers() {

        Role       role     = roleRepository.findByName(RoleName.ROLE_USER.name());
        Provider   provider = providerRepository.findByName(ProviderName.LOCAL.name());
        List<User> users    = new ArrayList<>(1000);
        for (int i = 1; i <= 1000; i++) {
            users.add(i - 1, userRepository.save(
                    User.builder().id((long) i).email(i + "@gmail.com")
                        .password(passwordEncoder.encode(USER_DEFAULT_PASSWORD)).username(String.valueOf(i))
                        .firstName(String.valueOf(i)).lastName(String.valueOf(i)).enabled(true).role(role)
                        .provider(provider).build()));
        }

        return users;
    }

    private List<GiftCertificate> insertGiftCertificates(List<Tag> tags) {

        List<GiftCertificate> gcList          = new ArrayList<>(10000);
        List<Integer>         gcDiscountsPull = List.of(10, 20, 30, 40, 50, 60, 70, 80, 90);
        for (int i = 1; i <= 10000; i++) {
            SecureRandom random = new SecureRandom();

            int       discount = gcDiscountsPull.get(random.nextInt(9));
            List<Tag> gcTags   = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                gcTags.add(tags.get((i + j - 1) % 1000));
            }

            gcList.add(i - 1, gcRepository.save(
                    GiftCertificate.builder().name(discount + "% discount")
                                   .description("Get " + discount + "% " + "discount in our shop shop.com")
                                   .price(random.nextInt(990) + 10 + 0.99).duration(random.nextInt(8) + 7).tags(gcTags)
                                   .build()));
        }

        return gcList;
    }

    private void insertOrders(List<GiftCertificate> gcList, List<User> users) {

        for (int i = 1; i <= 5000; i++) {
            GiftCertificate gc1 = gcList.get((i - 1) * 2);
            GiftCertificate gc2 = gcList.get(i * 2 - 1);

            orderRepository.save(Order.builder().user(users.get((i - 1) / 5)).giftCertificates(List.of(gc1, gc2))
                                      .cost(gc1.getPrice() + gc2.getPrice()).build());
        }
    }
}
