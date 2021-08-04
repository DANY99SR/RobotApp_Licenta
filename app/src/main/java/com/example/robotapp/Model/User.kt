package com.example.robotapp.Model

class User {

    private var username: String = ""
    private var fullname: String = ""
    private var email: String = ""
    private var uid: String = ""


    constructor()

    constructor(username: String, fullname: String, email: String, uid: String) {
        this.username = username
        this.fullname = fullname
        this.email = email
        this.uid = uid
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getFullname(): String {
        return fullname
    }

    fun setFullname(fullname: String) {
        this.fullname = fullname
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getUid(): String {
        return uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }
}