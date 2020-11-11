package com.epam.esm.gift_extended.genertors;

import java.util.List;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.genertors.util.RandomUtil;

public class RelationGenerator {

    private static final Integer MAX_TAG_AMOUNT = 50;
    private static final Integer CERT_HOLDER_CHANCE = 90;

    public void MakeRelation(List<User> users, List<Certificate> certificates, List<Tag> tags) {
        certificates.forEach(cert -> {
            if (RandomUtil.getBooleanWithTrueProbability(CERT_HOLDER_CHANCE)) {
                User user = RandomUtil.getRandomElementFromList(users);
                cert.setHolder(user);
                cert.setDescription("cert along to " + user.getName());
            } else {
                cert.setDescription("cert without holder");
            }

        });
        certificates.forEach(cert -> {
            int tagsAmount = RandomUtil.getRandInt(MAX_TAG_AMOUNT)+1;
            cert.setTags(RandomUtil.getRandomSubListWithSize(tagsAmount, tags));
        });

    }

}
