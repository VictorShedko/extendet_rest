package com.epam.esm.gift_extended.genertors;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.genertors.util.RandomUtil;

public class CertificateGenerator {

    private List<String> CertNames = List.of("Perfume by ", "Auto by", "Jewellery by", "Clothe by ", "Massage by ",
            "Diving by ", "Spa by ");
    private List<String> CertCompany = List.of("Dolce", "Gucci", "Swarovski", "Tesl", "BMW", "Lamborghini", "Mercedes");

    private Function<Integer, String> indexedCertNameSupplier = (i) -> {
        return RandomUtil.getRandomElementFromList(CertNames) + " " + RandomUtil.getRandomElementFromList(CertCompany)
                + " " + i;
    };

    public List<Certificate> generateCertificateList(int amount) {
        return IntStream.range(0, amount).mapToObj(t -> {
            Certificate certificate = new Certificate();
            String name = (String) indexedCertNameSupplier.apply(t);
            certificate.setName(name);
            certificate.setPrice(RandomUtil.getRandFloat() * 100);
            certificate.setDuration(RandomUtil.getRandInt(1000));
            Date now = new Date();
            int shift = RandomUtil.getRandInt(100000);
            long create = now.getTime() - shift;
            long update = create + RandomUtil.getRandInt(shift);
            Date creationTime = new Date(create);
            Date updateTime = new Date(update);
            certificate.setCreationTime(creationTime);
            certificate.setUpdateTime(updateTime);
            return certificate;
        }).collect(Collectors.toList());
    }

}
