package com.group20.cscb07project;

import com.group20.cscb07project.auth.SignInPresenter;
import com.group20.cscb07project.auth.SignInView;
import com.group20.cscb07project.firebase.FirebaseAuthService;
import com.group20.cscb07project.firebase.FirebaseResultCallback;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SignInPresenterTest {

    private SignInView mockView;
    private FirebaseAuthService mockModel;
    private SignInPresenter presenter;

    private final String testEmail = "test@email.com";

    @Before
    public void setUp() {
        mockView = mock(SignInView.class);
        mockModel = mock(FirebaseAuthService.class);
    }

    @Test
    public void testSignIn_emptyPassword_showsError() {
        when(mockView.getPassword()).thenReturn("");

        presenter = new SignInPresenter(mockView, mockModel, testEmail, false);
        presenter.signIn();

        verify(mockView).showPasswordError("Password is required");
        verify(mockView, never()).showProgress();
        verifyNoMoreInteractions(mockModel);
    }

    @Test
    public void testSignIn_success_pinExists() {
        when(mockView.getPassword()).thenReturn("validPassword");

        presenter = new SignInPresenter(mockView, mockModel, testEmail, true);

        doAnswer(invocation -> {
            FirebaseResultCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockModel).signIn(eq(testEmail), eq("validPassword"), any());

        presenter.signIn();

        verify(mockView).clearPasswordError();
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockView).navigateToMain();
        verify(mockView, never()).navigateToSetPin();
    }

    @Test
    public void testSignIn_success_pinDoesNotExist() {
        when(mockView.getPassword()).thenReturn("validPassword");

        presenter = new SignInPresenter(mockView, mockModel, testEmail, false);

        doAnswer(invocation -> {
            FirebaseResultCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockModel).signIn(eq(testEmail), eq("validPassword"), any());

        presenter.signIn();

        verify(mockView).clearPasswordError();
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockView).navigateToSetPin();
        verify(mockView, never()).navigateToMain();
    }

    @Test
    public void testSignIn_failure() {
        when(mockView.getPassword()).thenReturn("validPassword");

        presenter = new SignInPresenter(mockView, mockModel, testEmail, true);

        doAnswer(invocation -> {
            FirebaseResultCallback callback = invocation.getArgument(2);
            callback.onFailure(new Exception("Auth failed"));
            return null;
        }).when(mockModel).signIn(eq(testEmail), eq("validPassword"), any());

        presenter.signIn();

        verify(mockView).clearPasswordError();
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockView).showAuthFailed("Authentication failed");
        verify(mockView, never()).navigateToMain();
        verify(mockView, never()).navigateToSetPin();
    }
}