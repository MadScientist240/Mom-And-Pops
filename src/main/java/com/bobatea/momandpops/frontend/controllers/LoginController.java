package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.frontend.SceneManager;

public class LoginPageController {

    public void backButton() {
        SceneManager.getInstance().navigateToLast();
    }
}
