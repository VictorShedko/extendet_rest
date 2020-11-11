package com.epam.esm.gift_extended.genertors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;

class TagGeneratorTest {
    private TagGenerator generator=new TagGenerator();
    @Test
    void generateTagList() {
        List<Tag> certificateList=generator.generateTagList(1000);
        assertEquals(1000,certificateList.size());

    }
}