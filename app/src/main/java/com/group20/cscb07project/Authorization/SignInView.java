package com.group20.cscb07project.Authorization;

public interface SignInView {
    void showProgress();

    void hideProgress();

    void showPasswordError(String message);

    void clearPasswordError();

    void navigateToMain();

    void navigateToSetPin();

    void showAuthFailed(String message);

    String getPassword();
}