package org.fenglin.bot.Config

import net.mamoe.mirai.console.data.*

object DebugSetting: ReadOnlyPluginConfig("LineUpConfig") {
    @ValueDescription("机器人所有者")
    val Admin by value(2180323481)
}