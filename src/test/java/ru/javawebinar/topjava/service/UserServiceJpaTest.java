package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.POSTGRES, Profiles.JPA})
public class UserServiceJpaTest extends AbstractUserServiceTest {
    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testFindWithMeals() throws Exception {
        super.testFindWithMeals();
    }
}
