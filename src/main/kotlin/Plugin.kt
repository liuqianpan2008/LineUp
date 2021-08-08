package org.fenglin.bot

import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import org.fenglin.bot.Config.DebugSetting

object Plugin : KotlinPlugin(
    JvmPluginDescription(
        id = "org.fenglin.bot.plugin",
        name = "LineUp",
        version = "1.0-SNAPSHOT",
    ) {
        author("枫叶秋林")
    }
) {

    private lateinit var list: String
    private var IsLineUp: Boolean = false
    private lateinit var send: String
    private lateinit var QQ: String
    private val Admin by DebugSetting::Admin
    val numbers = mutableListOf<String>()
    private fun <T : PluginConfig> T.save() = loader.configStorage.store(this@Plugin, this)
    override fun onEnable() {
        logger.info { "排队插件已已启动" }

        DebugSetting.reload()
        DebugSetting.save()
        logger.info { Admin.toString() }
        this.globalEventChannel().subscribeAlways<MessageEvent> { event ->
            if (event.message.get(1).toString().equals("排队")&&IsLineUp){
                QQ = event.senderName+"("+event.sender.id.toString()+")"
                if (!numbers.any{it.endsWith(QQ)}){
                    numbers.add(QQ)
                    event.subject.sendMessage("排队成功！")
                    send="";
                    send="当前排队人数："+ numbers.size+"\n"
                    send+="当前排队情况："+"\n"+numbers
                    event.subject.sendMessage(send)
                }else{
                    event.subject.sendMessage("你已经排上了")
                }
            }

            if (event.message.get(1).toString().equals("排队情况")){
                send="";
                send="当前排队人数："+ numbers.size+"\n"
                send+="当前排队情况："+"\n"+numbers
                event.subject.sendMessage(send)
            }

            if (event.message.get(1).toString().equals("开启排队")){
                if (event.sender.id.toString().equals(Admin.toString())){
                    IsLineUp=true;
                    event.subject.sendMessage("开启成功")
                }else{
                    event.subject.sendMessage("你没有管理员权限,无法开启")
                }
            }

            if (event.message.get(1).toString().equals("停止排队")){
                if (event.sender.id.toString().equals(Admin.toString())){
                    IsLineUp=false;
                    event.subject.sendMessage("已停止排队")
                }else{
                    event.subject.sendMessage("你没有管理员权限,无法开启")
                }
            }

            if (event.message.get(1).toString().equals("重置排队")){
                if (event.sender.id.toString().equals(Admin.toString())){
                    for (index in numbers.indices){
                        numbers.removeAt(index);
                    }
                    event.subject.sendMessage("已重置排队")
                }else{
                    event.subject.sendMessage("你没有管理员权限,无法开启")
                }
            }

            if (event.message.get(1).toString().equals("已完成")){
                if (event.sender.id.toString().equals(Admin.toString())){
                    numbers.removeAt(0);
                    event.subject.sendMessage("已完成当前排队")
                }else{
                    event.subject.sendMessage("你没有管理员权限,无法执行")
                }
            }

        }
    }
}


