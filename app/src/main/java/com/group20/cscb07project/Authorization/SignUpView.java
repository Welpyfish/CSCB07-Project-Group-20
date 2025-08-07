package com.group20.cscb07project.Authorization;

public interface SignUpView {
    void showProgress();
    void hideProgress();
    void showEmailError(String message);
    void showNameError(String message);
    void showPasswordError(String message);
    void clearErrors();
    void navigateToMain();

    void navigateToSetPin();

    void showToast(String message);
    String getEmail();
    String getName();
    String getPassword();
}
