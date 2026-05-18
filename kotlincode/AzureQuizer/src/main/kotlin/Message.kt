package com.walcron

import kotlinx.serialization.Serializable


@Serializable
data class Message(val error: String)