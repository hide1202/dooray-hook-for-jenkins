package io.viewpoint.dooray.hook

data class Hook(var botName: String, var botIconImage: String?, var text: String, var attachments: List<Attachment>?)
data class Attachment(var title: String, var titleLink: String?, var text: String, var color: String?, var image: String?)
