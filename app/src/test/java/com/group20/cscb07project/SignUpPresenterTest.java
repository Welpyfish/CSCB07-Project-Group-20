package com.group20.cscb07project;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.group20.cscb07project.auth.SignUpPresenter;
import com.group20.cscb07project.auth.SignUpView;
import com.group20.cscb07project.firebase.FirebaseAuthService;
import com.group20.cscb07project.firebase.FirebaseResultCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class SignUpPresenterTest {

    private SignUpView view;
    private FirebaseAuthService model;
    private SignUpPresenter presenter;

    @Before
    public void setUp() {
        view = mock(SignUpView.class);
        model = mock(FirebaseAuthService.class);
    }

    @Test
    public void testEmptyEmailShowsError() {
        when(view.getEmail()).thenReturn("");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        verify(view).clearErrors();
        verify(view).showEmailError("Email is required");
        verifyNoInteractions(model);
    }

    @Test
    public void testEmptyNameShowsError() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        verify(view).clearErrors();
        verify(view).showNameError("Name is required");
        verifyNoInteractions(model);
    }

    @Test
    public void testEmptyPasswordShowsError() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        verify(view).clearErrors();
        verify(view).showPasswordError("Password is required");
        verifyNoInteractions(model);
    }

    @Test
    public void testWeakPasswordShowsError() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        verify(view).clearErrors();
        verify(view).showPasswordError("Password must be at least 6 characters");
        verifyNoInteractions(model);
    }

    @Test
    public void testSignUpSuccessNavigatesBasedOnPinExistsTrue() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, true);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(eq("john@example.com"), eq("password123"), captor.capture());

        FirebaseResultCallback callback = captor.getValue();
        callback.onSuccess();

        verify(view).clearErrors();
        verify(view).showProgress();
        verify(view).logSuccess();
        verify(view).hideProgress();
        verify(view).showToast("Account created successfully!");
        verify(view).navigateToMain();
    }

    @Test
    public void testSignUpSuccessNavigatesBasedOnPinExistsFalse() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(eq("john@example.com"), eq("password123"), captor.capture());

        FirebaseResultCallback callback = captor.getValue();
        callback.onSuccess();

        verify(view).clearErrors();
        verify(view).showProgress();
        verify(view).logSuccess();
        verify(view).hideProgress();
        verify(view).showToast("Account created successfully!");
        verify(view).navigateToSetPin();
    }

    @Test
    public void testSignUpFailure_WeakPassword() {
        when(view.getEmail()).thenReturn("john@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(anyString(), anyString(), captor.capture());

        Exception e = mock(FirebaseAuthWeakPasswordException.class);
        captor.getValue().onFailure(e);

        verify(view).logFailure(e);
        verify(view).hideProgress();
        verify(view).showPasswordError("Password is too weak. Please choose a stronger one.");
        verify(view).showToast("Password is too weak. Please choose a stronger one.");
    }

    @Test
    public void testSignUpFailure_InvalidEmail() {
        when(view.getEmail()).thenReturn("bademail");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(anyString(), anyString(), captor.capture());

        Exception e = mock(FirebaseAuthInvalidCredentialsException.class);
        captor.getValue().onFailure(e);

        verify(view).logFailure(e);
        verify(view).hideProgress();
        verify(view).showEmailError("The email address is malformed or invalid.");
        verify(view).showToast("The email address is malformed or invalid.");
    }

    @Test
    public void testSignUpFailure_EmailCollision() {
        when(view.getEmail()).thenReturn("duplicate@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(anyString(), anyString(), captor.capture());

        Exception e = mock(FirebaseAuthUserCollisionException.class);
        captor.getValue().onFailure(e);

        verify(view).logFailure(e);
        verify(view).hideProgress();
        verify(view).showEmailError("An account with this email address already exists.");
        verify(view).showToast("An account with this email address already exists.");
    }

    @Test
    public void testSignUpFailure_UknownError() {
        when(view.getEmail()).thenReturn("duplicate@example.com");
        when(view.getName()).thenReturn("John");
        when(view.getPassword()).thenReturn("password123");

        presenter = new SignUpPresenter(view, model, false);
        presenter.signUp();

        ArgumentCaptor<FirebaseResultCallback> captor = ArgumentCaptor.forClass(FirebaseResultCallback.class);
        verify(model).signUp(anyString(), anyString(), captor.capture());

        Exception e = mock(Exception.class);
        when(e.getMessage()).thenReturn("Something unexpected went wrong");
        captor.getValue().onFailure(e);

        verify(view).logFailure(e);
        verify(view).hideProgress();
        verify(view).showToast(contains("Something unexpected went wrong"));
    }
}