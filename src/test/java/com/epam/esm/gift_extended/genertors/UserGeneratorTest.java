package com.epam.esm.gift_extended.genertors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.User;

class UserGeneratorTest {
    private UserGenerator userGenerator=new UserGenerator();
    @Test
    void generateUserList() {
        List<User> certificateList=userGenerator.generateUserList(1000);
        assertEquals(1000,certificateList.size());
    }
}