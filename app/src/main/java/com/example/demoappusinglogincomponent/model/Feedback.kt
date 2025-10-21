package com.example.demoappusinglogincomponent.model

class Feedback(
    private var name: String? = null,
    private var phone: String? = null,
    private var email: String? = null,
    private var comment: String? = null,
    private var accountId: String? = null,      // id tài khoản gửi
    private var createdAt: Long = System.currentTimeMillis() // thời gian gửi
) {

    fun getName(): String? = name
    fun setName(name: String?) { this.name = name }

    fun getPhone(): String? = phone
    fun setPhone(phone: String?) { this.phone = phone }

    fun getEmail(): String? = email
    fun setEmail(email: String?) { this.email = email }

    fun getComment(): String? = comment
    fun setComment(comment: String?) { this.comment = comment }

    fun getAccountId(): String? = accountId
    fun setAccountId(accountId: String?) { this.accountId = accountId }

    fun getCreatedAt(): Long = createdAt
    fun setCreatedAt(createdAt: Long) { this.createdAt = createdAt }
}
