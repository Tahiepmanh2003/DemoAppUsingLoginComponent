package com.example.demoappusinglogincomponent.model

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isTyping: Boolean = false
) {
    companion object {
        fun createUserMessage(text: String): Message {
            return Message(text, true)
        }

        fun createBotMessage(text: String): Message {
            return Message(text, false)
        }
        fun typing(): Message {
            return Message("", false, isTyping = true)
        }
    }
}