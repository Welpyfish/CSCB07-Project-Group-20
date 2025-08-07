package com.group20.cscb07project;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

import com.group20.cscb07project.Authorization.FirebaseAuthPresenter;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FirebaseAuthTest {
    @Mock
    FirebaseAuthPresenter presenter;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testEmailPasswordCreation(){

    }
}